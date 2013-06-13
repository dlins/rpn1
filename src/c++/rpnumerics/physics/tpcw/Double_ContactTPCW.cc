#include "Double_ContactTPCW.h"

Double_ContactTPCW::Double_ContactTPCW(const RealVector &lpmin, const RealVector &lpmax, const int *l_number_of_grid_pnts,
        const FluxFunction *lff, const AccumulationFunction *laa,
        int lf,
        const RealVector &rpmin, const RealVector &rpmax, const int *r_number_of_grid_pnts,
        const FluxFunction *rff, const AccumulationFunction *raa,
        int rf) {

    // ======================== Left  domain ======================== //
    int lrows = l_number_of_grid_pnts[0];
    int lcols = l_number_of_grid_pnts[1];

    // Left flux and accumulation functions.

    leftff = (Flux2Comp2PhasesAdimensionalized*) lff;
    leftaa = (Accum2Comp2PhasesAdimensionalized*) laa;


    // Reserve space and/or copy the input parameters to their inner counterparts.
    left_number_of_grid_pnts = new int[lpmin.size()];
    leftpmin.resize(lpmin.size());
    leftpmax.resize(lpmin.size());
    for (int i = 0; i < lpmin.size(); i++) {
        left_number_of_grid_pnts[i] = l_number_of_grid_pnts[i];
        leftpmin.component(i) = lpmin.component(i);
        leftpmax.component(i) = lpmax.component(i);
    }

    // Allocate space for the grid, etc., and fill those values.
    // TODO: The case where the domain is a triangle is to be treated differently.
    leftgrid.resize(lrows, lcols);
    leftffv.resize(lrows, lcols);
    leftaav.resize(lrows, lcols);
    lefte.resize(lrows, lcols);
    left_eig_is_real.resize(lrows, lcols);

    create_grid(leftpmin, leftpmax, left_number_of_grid_pnts, leftgrid);

    IF_DEBUG
        printf("After create_grid()\n");
    END_DEBUG

    fill_values_on_grid(leftpmin, leftpmax,
            leftff, leftaa, 
            left_number_of_grid_pnts,
            leftgrid,
            leftffv, leftaav,
            lefte, left_eig_is_real);

    set_left_family(lf);
    //create_cells(left_number_of_grid_pnts, left_family, left_cells, &left_eig_is_real);

    nul = left_number_of_grid_pnts[0] - 1; // Number of cells, not number of points in the grid
    nvl = left_number_of_grid_pnts[1] - 1; // Number of cells, not number of points in the grid
    ul0 = leftpmin.component(0);
    ul1 = leftpmax.component(0);
    vl0 = leftpmin.component(1);
    vl1 = leftpmax.component(1);
    dul = (ul1 - ul0) / nul;
    dvl = (vl1 - vl0) / nvl;
    // ======================== Left  domain ======================== //

    // ======================== Right domain ======================== //
    int rrows = r_number_of_grid_pnts[0];
    int rcols = r_number_of_grid_pnts[1];

    // Right flux and accumulation functions.

    rightff = (Flux2Comp2PhasesAdimensionalized*) rff;
    rightaa = (Accum2Comp2PhasesAdimensionalized*) raa;

   
    // Reserve space and/or copy the input parameters to their inner counterparts.
    right_number_of_grid_pnts = new int[rpmin.size()];
    rightpmin.resize(rpmin.size());
    rightpmax.resize(rpmin.size());
    for (int i = 0; i < rpmin.size(); i++) {
        right_number_of_grid_pnts[i] = r_number_of_grid_pnts[i];
        rightpmin.component(i) = rpmin.component(i);
        rightpmax.component(i) = rpmax.component(i);
    }

    // Allocate space for the grid, etc., and fill those values.
    // TODO: The case where the domain is a triangle is to be treated differently.
    rightgrid.resize(rrows, rcols);
    rightffv.resize(rrows, rcols);
    rightaav.resize(rrows, rcols);
    righte.resize(rrows, rcols);
    right_eig_is_real.resize(rrows, rcols);

    create_grid(rightpmin, rightpmax, right_number_of_grid_pnts, rightgrid);


    fill_values_on_grid(rightpmin, rightpmax, rightff, rightaa,
            right_number_of_grid_pnts,
            rightgrid,
            rightffv, rightaav,
            righte, right_eig_is_real);

    set_right_family(rf);
    //    create_cells(right_number_of_cells, right_cells, &right_eig_is_real);

    nur = right_number_of_grid_pnts[0] - 1; // Number of cells, not number of points in the grid
    nvr = right_number_of_grid_pnts[1] - 1; // Number of cells, not number of points in the grid
    ur0 = rightpmin.component(0);
    ur1 = rightpmax.component(0);
    vr0 = rightpmin.component(1);
    vr1 = rightpmax.component(1);
    dur = (ur1 - ur0) / nur;
    dvr = (vr1 - vr0) / nvr;
    // ======================== Right domain ======================== //

    dumax = 2.0 * max(dur, dul);
    dvmax = 2.0 * max(dvr, dvl);
}

Double_ContactTPCW::~Double_ContactTPCW() {
    delete [] right_number_of_grid_pnts;
    delete [] left_number_of_grid_pnts;
}

// TODO: Dan believes this function can return void, since memory will not be overused.

void Double_ContactTPCW::filedg4(Matrix<double> &sol_, int dims, Matrix<int> &edges_,
        int dime, int nedges_,
        int il, int jl, int ir, int jr,
        std::vector<RealVector> &left_vrs, std::vector<RealVector> &right_vrs) {

    // TODO: Verify if il, jl, ir & jr are the same as are computed by the Fortran code. (There they are used as indices and pass as references and are modified by some function.) Panters.

    // The variables below are defined in COMMON blocks (lrectdat and rrectdat).
    //input:
    // double ul0, vl0, ul1, vl1, dul, dvl.
    // double ur0, vr0, ur1, vr1, dur, dvr.

    // Store all pairs of edges that were found
    RealVector p1(2), p2(2), p3(2), p4(2);

    for (int nedg = 0; nedg < nedges_; nedg++) {
        // 2011-09-28: Eliminated "-1" from il, jl, ir and jr below. Marchesin, Morante.

        p1.component(0) = ul0 + dul * (il + sol_(0, edges_(0, nedg))); // LX1 Was  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
        p1.component(1) = vl0 + dvl * (jl + sol_(1, edges_(0, nedg))); // LY1 Was  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];

        p2.component(0) = ul0 + dul * (il + sol_(0, edges_(1, nedg))); // LX2 Was  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
        p2.component(1) = vl0 + dvl * (jl + sol_(1, edges_(1, nedg))); // LY2 Was  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];

        left_vrs.push_back(p1);
        left_vrs.push_back(p2);

        p3.component(0) = ur0 + dur * (ir + sol_(2, edges_(0, nedg))); // RX1 Was:  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
        p3.component(1) = vr0 + dvr * (jr + sol_(3, edges_(0, nedg))); // RY1 Was:  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];

        p4.component(0) = ur0 + dur * (ir + sol_(2, edges_(1, nedg))); // RX2 Was:  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
        p4.component(1) = vr0 + dvr * (jr + sol_(3, edges_(1, nedg))); // RY2 Was:  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];

        right_vrs.push_back(p3);
        right_vrs.push_back(p4);

    }

    return;
}

void Double_ContactTPCW::compute_double_contactTPCW(std::vector<RealVector> &left_vrs,
        std::vector<RealVector> &right_vrs) {
    //int sn, int seglim, double f, double rect[4], int res[2], int ifirst;

    bool singular = (left_family == right_family);

    int status;
    //    int nul, nvl, nur, nvr;

    //  int ncubes, first, last, k;

    int hn = 4; //N
    int hm = 3; //M     // TODO: Que son hn y hm??? preguntarle a Dan si esto que tiene ver con la dimensión.
    int DNCV = 16;
    int DNSIMP = 24;
    int DNSF = 5;
    int DNFACE = 125;
    int nsface_, nface_, nsoln_, nedges_;
    int dims_ = 125;
    int dime_ = 200;

    double refval;
    int i, j;

    int ncvert_ = 16; //N^2
    int nsimp_ = 24; //N!

    int numberOfCombinations = 5; // = hc.combination(hn + 1, hm + 1) = hc.combination(4 + 1, 3 + 1);

    // Allocating arrays
    int storn_[hn + 1];
    int storm_[hm + 1];
    double cvert_[ncvert_][hn];
    double vert[ncvert_][hn];
    int bsvert_[hn + 1][hn];
    int perm_[hn][nsimp_];
    int comb_[numberOfCombinations][hm + 1];
    double foncub[hm][ncvert_]; // First defined here.

    //inicializing arrays dimensions
    nsface_ = hc.mkcomb(&comb_[0][0], hn + 1, hm + 1);

    int fnbr_[nsface_][nsface_];

    int dimf_ = 84;

    nsoln_ = -1;
    //double sol_[hn][dims_]; 
    Matrix<double> cpp_sol(hn, dims_);
    int solptr_[nsimp_][nsface_];
    //    initialize_matrix(nsimp_, nsface_, &solptr_[0][0], 0);//TODO: Revisar como "solptr" eh modificada, os numero sao muito estranhos

    // int edges_[2][dime_]; 
    Matrix<int> cpp_edges_(2, dime_);
    //    initialize_matrix(2, dime_, &edges_[0][0], -6);//TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    int smpedg_[nsimp_][2];
    //    initialize_matrix(nsimp_, 2, &smpedg_[0][0], 0);//TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    //inicializing another arrays, it were globally defined in java
    int facptr_[nsimp_][nsface_];
    int face_[hm + 1][dimf_];

    hc.mkcube(&cvert_[0][0], &bsvert_[0][0], &perm_[0][0], ncvert_, nsimp_, hn);

    //    Matrix<double> cpp_cvert;
    //    Matrix<int>    cpp_bsvert;
    //    Matrix<int>    cpp_perm;

    //    hc.cpp_mkcube(cpp_cvert, cpp_bsvert, cpp_perm, ncvert_, nsimp_, hn);

    //    nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
    //                       &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

    nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
            &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

    int exstfc[nface_];
    for (i = 0; i < nface_; i++) exstfc[i] = 1; // This is a MUST!!!
    initialize_matrix(1, nface_, exstfc, 1);
    //    initialize_matrix(1, nface_, exstfc, 1);
    int sptr_[nface_];

    //setrect(nul, nvl, nur, nvr); 
    // TODO: We arrived here and went no further.
    // Marchesi & Morante, Thu Jan 20 18:47:51 BRST 2011 

    //    sn = 0;
    left_vrs.clear();
    right_vrs.clear();

    // Reserve some space for prepare_cell().
    //
    double lambda_left[4];
    Matrix<double> flux_left(3, 4);
    Matrix<double> accum_left(3, 4);

    int index[4] = {0, 2, 3, 1}; // In Fortran: index = {1, 3, 4, 2}. From the common block "hcindex". TODO: To be checked.

    // Some workspace variables for HyperCube::cubsol(). Originally documented in: hcube.F.
    double u[hn][hm + 1];
    double g[hm][hm + 1];
    double stormd[hm];

    for (int il = 0; il < nul; il++) {
        for (int jl = 0; jl < nvl; jl++) {
            // if (insided("left", il, jl) == 0) continue;
            // This is valid only when the domain is a rectangle.
            // When the time comes, insided() must be written.
            if (left_is_complex(il, jl)) continue; // Perhaps left_cells can be turned into a Matrix<bool> containing the same info (rendering RealEigenvalueCell useless).

            prepare_cell(il, jl, left_family, lefte, leftffv, leftaav, lambda_left, flux_left, accum_left); // Before: preplft.
            for (int ir = 0; ir < nur; ir++) {
                for (int jr = 0; jr < nvr; jr++) {
                    // if (insided("right", ir, jr) == 0) continue;
                    if (singular && left_right_ordering(il, jl, ir, jr)) continue;
                    // TODO: See how much time these if's take up and think if they can or should be
                    //       made in C instead of C++.
                    if (right_is_complex(ir, jr)) continue;
                    if (filhcub4(ir, jr, index, &foncub[0][0], hm, ncvert_, lambda_left, flux_left, accum_left) != 0) {

                        //              if (filhcub4(ir, jr, &index[0], &foncub[0][0], hm, fun1, 0) != 0){
                        //              if (filhcub4(ir, jr, &index[0], &foncub[0][0], hm, fun2, 1) != 0){
                        //              if (filhcub4(ir, jr, &index[0], &foncub[0][0], hm, fun3, 2) != 0){
                        //                  nsoln_ = hc.cubsol(&solptr_[0][0], &sol_[0][0], dims_,
                        //                                     &sptr_[0], nsoln_, &foncub[0][0], &exstfc[0],
                        //                                     &face_[0][0], &facptr_[0][0], dimf_, &cvert_[0][0],
                        //                                     ncvert_, hn, hm, nsimp_, nsface_, nface_, &u[0][0],
                        //                                     &g[0][0], &stormd[0], &storm_[0]);

                        nsoln_ = hc.cpp_cubsol(&solptr_[0][0], cpp_sol, dims_,
                                &sptr_[0], nsoln_, &foncub[0][0], &exstfc[0],
                                &face_[0][0], &facptr_[0][0], dimf_, &cvert_[0][0],
                                ncvert_, hn, hm, nsimp_, nsface_, nface_, &u[0][0],
                                &g[0][0], &stormd[0], &storm_[0]);

                        //                  nedges_ = hc.mkedge(&edges_[0][0], dime_, nedges_, &smpedg_[0][0],
                        //                                      &solptr_[0][0], &fnbr_[0][0], nsimp_, nsface_);

                        hc.cpp_mkedge(cpp_edges_, dime_, nedges_, &smpedg_[0][0],
                                &solptr_[0][0], &fnbr_[0][0], nsimp_, nsface_);

                        filedg4(cpp_sol, dims_, cpp_edges_, dime_, nedges_,
                                il, jl, ir, jr, left_vrs, right_vrs);
                    }
                }
            }
        }
    }
    return;
}

// This method sets the family and validates the cells for the left domain.
//

void Double_ContactTPCW::set_left_family(int nlf) {
    left_family = nlf;

    validate_cells(left_family, left_cell_type, left_eig_is_real, left_is_complex);

    return;
}

// This method sets the family and validates the cells for the right domain.
//

void Double_ContactTPCW::set_right_family(int nrf) {
    right_family = nrf;

    validate_cells(right_family, right_cell_type, right_eig_is_real, right_is_complex);

    return;
}

////     3     2
////     o-----o
////     |     |
////     |     |
////     o-----o
////     0     1 
////
////     0 = (i, j),
////     1 = (i + 1, j),
////     2 = (i + 1, j + 1),
////     3 = (i, j + 1).
////
//void Double_ContactTPCW::func(int index, double &val, int ir, int jr, int kl, int kr, 
//                          double *lambda_left_input, const Matrix<double> &flux_left_input, const Matrix<double> &accum_left_input){
//    if (index == 0){
//        // TODO: Why are we accessing the second eigenvalue??? (Why is it that family == 1?)
//        double lr;
//        
//        if (kr == 0 )    lr = righte(ir,jr)[1];
//        else if(kr == 1) lr = righte(ir + 1, jr)[1];
//        else if(kr == 2) lr = righte(ir + 1, jr + 1)[1];
//        else if(kr == 3) lr  = righte(ir, jr + 1)[1];
//    
//        val = lambda_left_input[kl] - lr;
//    }
//    else if (index == 1){
//        double fr, hur;
//        
//        if (kr == 0){
//            hur = rightaav(ir, jr).component(0); // hur = hua(ir,jr);
//            fr  = rightffv(ir, jr).component(0); // fr  = fa(ir,jr);
//        }
//        else if (kr == 1){
//            hur = rightaav(ir + 1, jr).component(0); // hur = hua(ir + 1,jr);
//            fr  = rightffv(ir + 1, jr).component(0); // fr  = fa(ir + 1,jr);
//        }
//        else if (kr == 2){
//            hur = rightaav(ir + 1, jr + 1).component(0); // hur = hua(ir + 1, jr + 1);
//            fr  = rightffv(ir + 1, jr + 1).component(0); // fr  = fa(ir + 1, jr + 1);
//        }
//        else if (kr == 3){
//            hur = rightaav(ir, jr + 1).component(0); // hur = hua(ir, jr + 1);
//            fr  = rightffv(ir, jr + 1).component(0); // fr  = fa(ir, jr + 1);
//        }
//      
//        //val = lambda_left_input[kl]*(accum_left_input(0, kl) - hur) - (fl(kl) - fr);
//        val = lambda_left_input[kl]*(accum_left_input(0, kl) - hur) - (flux_left_input(0, kl) - fr);
//    }
//    else if (index == 2){
//        double lr, gr, hvr;
//        
//        if (kr == 0){
//            lr = righte(ir,jr)[1];
//            hvr = rightaav(ir, jr).component(1); // hvr = hva(ir,jr);
//            gr  = rightffv(ir, jr).component(1); // gr  = ga(ir,jr);
//        }
//        else if (kr == 1){
//            lr = righte(ir + 1, jr)[1];
//            hvr = rightaav(ir + 1, jr).component(1); // hvr = hva(ir + 1,jr);
//            gr  = rightffv(ir + 1, jr).component(1); // gr  = ga(ir + 1,jr);
//        }
//        else if (kr == 2){
//            lr = righte(ir + 1, jr + 1)[1];
//            hvr = rightaav(ir + 1, jr + 1).component(1); // hvr = hva(ir + 1, jr + 1);
//            gr  = rightffv(ir + 1, jr + 1).component(1); // gr  = ga(ir + 1, jr + 1);
//        }
//        else if (kr == 3){
//            lr  = righte(ir, jr + 1)[1];
//            hvr = rightaav(ir, jr + 1).component(1); // hvr = hva(ir, jr + 1);
//            gr  = rightffv(ir, jr + 1).component(1); // gr  = ga(ir, jr + 1);
//        }

//        //val = lr*(hvl(kl) - hvr) - (gl(kl) - gr)
//        val = lr*(accum_left_input(1, kl) - hvr) - (flux_left_input(1, kl) - gr);
//    }
//    
//    return;
//}

//     3     2
//     o-----o
//     |     |
//     |     |
//     o-----o
//     0     1 
//
//     0 = (i, j),
//     1 = (i + 1, j),
//     2 = (i + 1, j + 1),
//     3 = (i, j + 1).
//

void Double_ContactTPCW::func(double *val, int ir, int jr, int kl, int kr,
        double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input) {



    // HERE WE ARE FILLING THE LEFT ACCUMULATION AND FLUX.
    double Gl[3], Fl[3];

    for (int k = 0; k < 3; k++) {
        Gl[k] = accum_left_input(k, kl);
        Fl[k] = flux_left_input(k, kl);
    }


    // LAMBDA ON THE RIGHT
    double lr;

    /*  if (kr == 0 )    lr = righte(ir,jr)[right_family];
      else if(kr == 1) lr = righte(ir + 1, jr)[right_family];
      else if(kr == 2) lr = righte(ir + 1, jr + 1)[right_family];
      else if(kr == 3) lr = righte(ir, jr + 1)[right_family]; */

    // val[0] = lambda_left_input[kl] - lr;


    double Gr[3], Fr[3];

    if (kr == 0) {
        lr = righte(ir, jr)[right_family];
        for (int k = 0; k < 3; k++) {
            Gr[k] = rightaav(ir, jr).component(k); // hur = hua(ir,jr);
            Fr[k] = rightffv(ir, jr).component(k); // fr  = fa(ir,jr);
        }
    } else if (kr == 1) {
        lr = righte(ir + 1, jr)[right_family];
        for (int k = 0; k < 3; k++) {
            Gr[k] = rightaav(ir + 1, jr).component(k); // hur = hua(ir + 1,jr);
            Fr[k] = rightffv(ir + 1, jr).component(k); // fr  = fa(ir + 1,jr);
        }
    } else if (kr == 2) {
        lr = righte(ir + 1, jr + 1)[right_family];
        for (int k = 0; k < 3; k++) {
            Gr[k] = rightaav(ir + 1, jr + 1).component(k); // hur = hua(ir + 1, jr + 1);
            Fr[k] = rightffv(ir + 1, jr + 1).component(k); // fr  = fa(ir + 1, jr + 1);
        }
    } else if (kr == 3) {
        lr = righte(ir, jr + 1)[right_family];
        for (int k = 0; k < 3; k++) {
            Gr[k] = rightaav(ir, jr + 1).component(k); // hur = hua(ir, jr + 1);
            Fr[k] = rightffv(ir, jr + 1).component(k); // fr  = fa(ir, jr + 1);
        }
    }

    double Hmatrix[3][3];
    double Gq[3];

    for (int i = 0; i < 3; i++) {
        Gq[i] = Gr[i] - Gl[i];
        Hmatrix[i][0] = Gq[i]; // bJetMatrix(i) - bref_G[i]; // b=G
        Hmatrix[i][1] = -Fr[i]; // a=F
        Hmatrix[i][2] = Fl[i];
    }

    val[0] = det(3, &Hmatrix[0][0]); // TODO: PRECISAMOS DO METODO DETERMINANTE.
    

    double X12 = Fr[0] * Gq[1] - Fr[1] * Gq[0];
    double X31 = Fr[2] * Gq[0] - Fr[0] * Gq[2];
    double X23 = Fr[1] * Gq[2] - Fr[2] * Gq[1];

    double X12_0 = Fl[0] * Gq[1] - Fl[1] * Gq[0];
    double X31_0 = Fl[2] * Gq[0] - Fl[0] * Gq[2];
    double X23_0 = Fl[1] * Gq[2] - Fl[2] * Gq[1];

    double Y21 = Fr[1] * Fl[0] - Fr[0] * Fl[1];
    double Y13 = Fr[0] * Fl[2] - Fr[2] * Fl[0];
    double Y32 = Fr[2] * Fl[1] - Fr[1] * Fl[2];

    double den = X12 * X12 + X31 * X31 + X23*X23;

    double scaling_factor = (X12_0 * X12 + X31_0 * X31 + X23_0 * X23) / den;

    double red_shock_speed = (Y21 * X12 + Y13 * X31 + Y32 * X23) / den;

    double lambda_right = scaling_factor*lr;

    val[1] = (red_shock_speed - lambda_left_input[kl]); // SECOND EQUATION
    val[2] = (red_shock_speed - lambda_right); // THIRD  EQUATION

    /*
    double Hmatrix[3][3];
    double Gq[3];

    for(int i = 0; i < 3; i++){
        Gq[i]         =  Gr[i] - Gl[i]  ;
        Hmatrix[i][0] =  Gq[i]          ;    // bJetMatrix(i) - bref_G[i]; // b=G
        Hmatrix[i][1] = -Fr[i]          ;    // a=F
        Hmatrix[i][2] =  Fl[i]          ;
        }

    val[0] = det(3, &Hmatrix[0][0]); // TODO: PRECISAMOS DO METODO DETERMINANTE.


    double X12 = Fr[0]*Gq[1] - Fr[1]*Gq[0] ;
    double X13 = Fr[2]*Gq[0] - Fr[0]*Gq[2] ;
    double X23 = Fr[1]*Gq[2] - Fr[2]*Gq[1] ;

    double Y12     = Fl[0]*Fr[1] - Fr[0]*Fl[1] ;
    double Y13     = Fr[0]*Fl[2] - Fl[0]*Fr[2] ;
    double Y23     = Fl[1]*Fr[2] - Fr[1]*Fl[2] ;

=======

    double scaling_factor = (X12_0 * X12 + X31_0 * X31 + X23_0 * X23) / den;

    double red_shock_speed = (Y21 * X12 + Y13 * X31 + Y32 * X23) / den;

    double lambda_right = scaling_factor*lr;

    val[1] = (red_shock_speed - lambda_left_input[kl]); // SECOND EQUATION
    val[2] = (red_shock_speed - lambda_right); // THIRD  EQUATION

    /*
    double Hmatrix[3][3];
    double Gq[3];

    for(int i = 0; i < 3; i++){
        Gq[i]         =  Gr[i] - Gl[i]  ;
        Hmatrix[i][0] =  Gq[i]          ;    // bJetMatrix(i) - bref_G[i]; // b=G
        Hmatrix[i][1] = -Fr[i]          ;    // a=F
        Hmatrix[i][2] =  Fl[i]          ;
        }

    val[0] = det(3, &Hmatrix[0][0]); // TODO: PRECISAMOS DO METODO DETERMINANTE.


    double X12 = Fr[0]*Gq[1] - Fr[1]*Gq[0] ;
    double X13 = Fr[2]*Gq[0] - Fr[0]*Gq[2] ;
    double X23 = Fr[1]*Gq[2] - Fr[2]*Gq[1] ;

    double Y12     = Fl[0]*Fr[1] - Fr[0]*Fl[1] ;
    double Y13     = Fr[0]*Fl[2] - Fl[0]*Fr[2] ;
    double Y23     = Fl[1]*Fr[2] - Fr[1]*Fl[2] ;

>>>>>>> refatorandoTPCW2
    double den      = X12*X12 + X13*X13 + X23*X23 ;
     
        
    double red_shock_speed = (Y12*X12 + Y13*X13 + Y23*X23)/den ;

    val[1] = red_shock_speed - lambda_left_input[kl]; // SECOND EQUATION
    val[2] = red_shock_speed - lr;                    // THIRD  EQUATION
     */

    /*
         val[1] = lambda_left_input[kl]*(accum_left_input(0, kl) - hur) - (flux_left_input(0, kl) - fr);
    
     // index == 2
         double gr, hvr;
        
         if (kr == 0){
             //lr = righte(ir,jr)[1];
             hvr = rightaav(ir, jr).component(1); // hvr = hva(ir,jr);
             gr  = rightffv(ir, jr).component(1); // gr  = ga(ir,jr);
         }
         else if (kr == 1){
             //lr = righte(ir + 1, jr)[1];
             hvr = rightaav(ir + 1, jr).component(1); // hvr = hva(ir + 1,jr);
             gr  = rightffv(ir + 1, jr).component(1); // gr  = ga(ir + 1,jr);
         }
         else if (kr == 2){
             //lr = righte(ir + 1, jr + 1)[1];
             hvr = rightaav(ir + 1, jr + 1).component(1); // hvr = hva(ir + 1, jr + 1);
             gr  = rightffv(ir + 1, jr + 1).component(1); // gr  = ga(ir + 1, jr + 1);
         }
         else if (kr == 3){
             //lr  = righte(ir, jr + 1)[1];
             hvr = rightaav(ir, jr + 1).component(1); // hvr = hva(ir, jr + 1);
             gr  = rightffv(ir, jr + 1).component(1); // gr  = ga(ir, jr + 1);
         }

         //val = lr*(hvl(kl) - hvr) - (gl(kl) - gr)
         val[2] = lr*(accum_left_input(1, kl) - hvr) - (flux_left_input(1, kl) - gr); */

    return;
}





// In Fortran:
//     index = {1, 3, 4, 2}.
//
// In C:
//     index = {0, 2, 3, 1}.
//
// TODO: See what index means, we got here and went no further today (7-fev-2011).

int Double_ContactTPCW::filhcub4(int ir, int jr, int *index, double *foncub, int hm, int ncvert_,
        double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input) { // real    foncub(hm,*)
    bool zero[3] = {false, false, false};

    double val[3]; // To be filled by Double_ContactTPCW::func();
    double refval[3]; // To be filled by Double_ContactTPCW::func();

    func(refval, ir, jr, 0, 0, lambda_left_input, flux_left_input, accum_left_input);
    //if ( funcomp ( refval, ir, jr, 1, 1 ) .eq. 0 ) return

    for (int kl = 0; kl < 4; kl++) {
        for (int kr = 0; kr < 4; kr++) {
            //if ( funcomp ( val, ir, jr, kl, kr ) .eq. 0 ) return
            func(val, ir, jr, kl, kr, lambda_left_input, flux_left_input, accum_left_input);

            for (int comp = 0; comp < 3; comp++) {
                foncub[comp * ncvert_ + 4 * (index[kl] - 0) + index[kr]] = val[comp]; // ??? O q e esse index?????     // TODO: Dan esto tiene que ver con la dimesion del espacio???
                //                foncub[comp*ncvert_ + 4*(index[kl] - 1) + index[kr]] = val[comp]; // ??? O q e esse index?????
                // foncub(comp,4*(index(kl)-1)+index(kr)) = val // ??? O q e esse index?????
                if (refval[comp] * val[comp] <= 0.0) zero[comp] = true;
            }
        }
    }

    //    if (!zero[0] || !zero[1] && !zero[2]) return 0;

    return 1;

}

// From: integer function adjrect ( il, jl, ir, jr ) at contour4.F.
//
// This function returns true if the cell (il, jl) is adjacent to the cell (ir, jr),
// and false otherwise.
//

bool Double_ContactTPCW::left_right_ordering(int il, int jl, int ir, int jr) {
    //c     input:
    //      real     ul0, vl0, ul1, vl1, dul, dvl, dvmax
    //      common  /  lrectdat / ul0, vl0, ul1, vl1, dul, dvl, dvmax
    //
    //c     input:
    //      real     ur0, vr0, ur1, vr1, dur, dvr, dumax
    //      common  /  rrectdat / ur0, vr0, ur1, vr1, dur, dvr, dumax

    //      adjrect = 1
    //      if ( (abs( (ur0+(ir+.5)*dur) - (ul0+(il+.5)*dul) ) .le. dumax)    .and.
    //     2     .and.
    //     3     (abs( (vr0+(jr+.5)*dvr) - (vl0+(jl+.5)*dvl) ) .le. dvmax)
    //     4   ) adjrect = 0

//    return ( (fabs((ur0 + (ir + .5) * dur) - (ul0 + (il + .5) * dul)) <= dumax) &&
//            (fabs((vr0 + (jr + .5) * dvr) - (vl0 + (jl + .5) * dvl)) <= dvmax)
//            );

//      return ( (fabs( (ur0+(ir-.5)*dur) - (ul0+(il-.5)*dul) ) <= 2.0*dumax) &&
//               (fabs( (vr0+(jr-.5)*dvr) - (vl0+(jl-.5)*dvl) ) <= 2.0*dvmax)
//             );


      // Modified: 2011-09-28. Marchesin, Morante. Twice-computing suppressed.
      return ( (( (ur0+(ir-.5)*dur) - (ul0+(il-.5)*dul) ) <= 2.0*dumax) &&
               (( (vr0+(jr-.5)*dvr) - (vl0+(jl-.5)*dvl) ) <= 2.0*dvmax)
             );


}





// TODO: PLACE THIS METHOD IN A NUMERICAL LIBRARY !!!

double Double_ContactTPCW::det(int nn, double *A) {
    double determinant;

    switch (nn) {
        case 1:
            determinant = A[0];
            return determinant;
            break;

        case 2:
            determinant = A[0] * A[3] - A[1] * A[2];
            return determinant;
            break;


        case 3:
            determinant = A[0]*(A[4] * A[8] - A[5] * A[7])
                    - A[3]*(A[1] * A[8] - A[7] * A[2])
                    + A[6]*(A[1] * A[5] - A[4] * A[2]);

            return determinant;
            break;

        default: // 4 or greater
            //TODO:     COMO SE ACHA O DETERMINANTE COM LAPACK PARA DIMENSAO 4:  FATORIZACOES?

            /*
                        int nrhs = 1;
                        int lda = nn;
                        int ipiv[nn];
                        int ldb = nn;
                        int info;
             */

            /*
                        // Create a transposed copy of A to be used by LAPACK's dgesv:
                        double B[nn][nn];
                        for (i = 0; i < nn; i++){
                            for (j = 0; j < nn; j++) B[j][i] = A[i*nn + j];
                        }
    
                        // Create a copy of b to be used by LAPACK's dgesv:
                        double bb[nn];
                        for (i = 0; i < nn; i++) bb[i] = b[i];
    
                        dgesv_(&dim, &nrhs, &B[0][0], &lda, &ipiv[0], &bb[0], &ldb, &info);
    
                        if (info == 0){
                            for (i = 0; i < nn; i++) x[i] = bb[i];
                            return SUCCESSFUL_PROCEDURE;
                        }
                        else return ABORTED_PROCEDURE;
             */
            return 0.0; // TODO: FIX THIS
            break;
    }

}

