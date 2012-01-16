#include "Double_Contact.h"

//template <typename T> void Double_Contact::initialize_matrix(int n, int m, T *matrix, T value){
//    for (int i = 0; i < n*m; i++) matrix[i] = value;
//    return;
//}

Double_Contact::Double_Contact(const RealVector &lpmin, const RealVector &lpmax, const int *l_number_of_grid_pnts,
                               const FluxFunction *lff, const AccumulationFunction *laa, int lf,
                               const Boundary *lboundary,
                               const RealVector &rpmin, const RealVector &rpmax, const int *r_number_of_grid_pnts,
                               const FluxFunction *rff, const AccumulationFunction *raa, int rf,
                               const Boundary *rboundary){

    // ======================== Left  domain ======================== //
    int lrows = l_number_of_grid_pnts[0];
    int lcols = l_number_of_grid_pnts[1];

    // Left flux and accumulation functions.
    leftff = (FluxFunction*)lff;
    leftaa = (AccumulationFunction*)laa;

    // Left boundary.
    leftb = (Boundary*)lboundary;

    // Reserve space and/or copy the input parameters to their inner counterparts.
    left_number_of_grid_pnts = new int[lpmin.size()];
    leftpmin.resize(lpmin.size());
    leftpmax.resize(lpmin.size());
    for (int i = 0; i < lpmin.size(); i++){
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
    left_is_inside.resize(lrows, lcols);

    create_grid(leftpmin, leftpmax, left_number_of_grid_pnts, leftgrid);

    fill_values_on_grid(leftpmin, leftpmax, leftff, leftaa, 
                        left_number_of_grid_pnts,
                        leftgrid,
                        leftffv, leftaav, 
                        lefte, left_eig_is_real,
                        leftb, left_is_inside);

//    printf("After create_grid()\n");
//    printf("TESTE create_grid. Line = %u\n", __LINE__);

    set_left_family(lf);
    //create_cells(left_number_of_grid_pnts, left_family, left_cells, &left_eig_is_real);

 //   printf("TESTE create_grid. Line = %u\n", __LINE__);

    nul = left_number_of_grid_pnts[0] - 1; // Number of cells, not number of points in the grid
    nvl = left_number_of_grid_pnts[1] - 1; // Number of cells, not number of points in the grid
    ul0 = leftpmin.component(0);
    ul1 = leftpmax.component(0);
    vl0 = leftpmin.component(1);
    vl1 = leftpmax.component(1);
    dul = ( ul1 - ul0 ) / nul;
    dvl = ( vl1 - vl0 ) / nvl;
    // ======================== Left  domain ======================== //

    // ======================== Right domain ======================== //
    int rrows = r_number_of_grid_pnts[0];
    int rcols = r_number_of_grid_pnts[1];

    // Right flux and accumulation functions.
    rightff = (FluxFunction*)lff;
    rightaa = (AccumulationFunction*)laa;

    // Right boundary.
    rightb = (Boundary*)rboundary;

    // Reserve space and/or copy the input parameters to their inner counterparts.
    right_number_of_grid_pnts = new int[rpmin.size()];
    rightpmin.resize(rpmin.size());
    rightpmax.resize(rpmin.size());
    for (int i = 0; i < rpmin.size(); i++){
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
    right_is_inside.resize(rrows, rcols);

    create_grid(rightpmin, rightpmax, right_number_of_grid_pnts, rightgrid);

    fill_values_on_grid(rightpmin, rightpmax, rightff, rightaa, 
                        right_number_of_grid_pnts,
                        rightgrid,
                        rightffv, rightaav, 
                        righte, right_eig_is_real,
                        rightb, right_is_inside);

    set_right_family(rf);
//    create_cells(right_number_of_cells, right_cells, &right_eig_is_real);

    nur = right_number_of_grid_pnts[0] - 1; // Number of cells, not number of points in the grid
    nvr = right_number_of_grid_pnts[1] - 1; // Number of cells, not number of points in the grid
    ur0 = rightpmin.component(0);
    ur1 = rightpmax.component(0);
    vr0 = rightpmin.component(1);
    vr1 = rightpmax.component(1);
    dur = ( ur1 - ur0 ) / nur;
    dvr = ( vr1 - vr0 ) / nvr;
    // ======================== Right domain ======================== //

    dumax = 2.0 * max ( dur, dul );
    dvmax = 2.0 * max ( dvr, dvl );
}

Double_Contact::~Double_Contact(){
    delete [] right_number_of_grid_pnts;
    delete [] left_number_of_grid_pnts;
}

// TODO: Dan believes this function can return void, since memory will not be overused.
void Double_Contact::filedg4(Matrix<double> &sol_, int dims, Matrix<int> &edges_, 
                            int dime, int nedges_, 
                            int il, int jl, int ir, int jr, 
                            std::vector<RealVector> &left_vrs, std::vector<RealVector> &right_vrs){
// TODO: Verify if il, jl, ir & jr are the same as are computed by the Fortran code. (There they are used as indices and pass as references and are modified by some function.) Panters.

    // The variables below are defined in COMMON blocks (lrectdat and rrectdat).
    //input:
    // double ul0, vl0, ul1, vl1, dul, dvl.
    // double ur0, vr0, ur1, vr1, dur, dvr.

    // Store all pairs of edges that were found
    RealVector p1(2), p2(2), p3(2), p4(2);
    for (int nedg = 0; nedg < nedges_; nedg++) {
        p1.component(0) = ul0 + dul * (il-0 + sol_(0, edges_(0, nedg) ) ); // LX1 Was  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
        p1.component(1) = vl0 + dvl * (jl-0 + sol_(1, edges_(0, nedg) ) ); // LY1 Was  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];
    
        p2.component(0) = ul0 + dul * (il-0 + sol_(0, edges_(1, nedg) ) ); // LX2 Was  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
        p2.component(1) = vl0 + dvl * (jl-0 + sol_(1, edges_(1, nedg) ) ); // LY2 Was  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];
 
        left_vrs.push_back(p1);
        left_vrs.push_back(p2);

//        p3.component(0) = ur0 + dur * (ir-1 + sol_(0, edges_(0, nedg) ) ); // RX1 Was:  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
//        p3.component(1) = vr0 + dvr * (jr-1 + sol_(1, edges_(0, nedg) ) ); // RY1 Was:  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];

//        p4.component(0) = ur0 + dur * (ir-1 + sol_(0, edges_(1, nedg) ) ); // RX2 Was:  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
//        p4.component(1) = vr0 + dvr * (jr-1 + sol_(1, edges_(1, nedg) ) ); // RY2 Was:  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];

        p3.component(0) = ur0 + dur * (ir-0 + sol_(2, edges_(0, nedg) ) ); // RX1 Was:  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]]; // Modified by Morante on 21-06-2011 by advice from Castaneda.
        p3.component(1) = vr0 + dvr * (jr-0 + sol_(3, edges_(0, nedg) ) ); // RY1 Was:  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];

        p4.component(0) = ur0 + dur * (ir-0 + sol_(2, edges_(1, nedg) ) ); // RX2 Was:  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
        p4.component(1) = vr0 + dvr * (jr-0 + sol_(3, edges_(1, nedg) ) ); // RY2 Was:  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];

        right_vrs.push_back(p3);
        right_vrs.push_back(p4);

    }

    return;
}

void Double_Contact::compute_double_contact(std::vector<RealVector> &left_vrs, 
                                            std::vector<RealVector> &right_vrs) {
    //int sn, int seglim, double f, double rect[4], int res[2], int ifirst;

    bool singular = (left_family == right_family);
    // TODO: Left_family & right_family must be input parameters. Validation will occur here as well.

//    int nul, nvl, nur, nvr;

    //  int ncubes, first, last, k;
   
    int hn = 4; //N
    int hm = 3; //M
    //int DNCV = 16;
    //int DNSIMP = 24;
    //int DNSF = 5;
    //int DNFACE = 125;
    int nsface_, nface_, nsoln_, nedges_;
    int dims_ = 125;
    int dime_ = 200;

    double refval;
    int i, j;

    int ncvert_ = 16; //N^2
    int nsimp_  = 24; //N!

    int numberOfCombinations = 5; // = hc.combination(hn + 1, hm + 1) = hc.combination(4 + 1, 3 + 1);

    // Allocating arrays
    int storn_[hn + 1];
    int storm_[hm + 1];
    double cvert_[ncvert_][hn];
    // double vert[ncvert_][hn];
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

// TODO: nface_ sempre eh 84! (Pablo 13/Jan/12)

    nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
                       &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

    int exstfc[nface_];
//    for (i = 0; i < nface_; i++) exstfc[i] = 1; // This is a MUST!!!
    initialize_matrix(1, nface_, exstfc, 1);
//    initialize_matrix(1, nface_, exstfc, 1);
    int sptr_[nface_];
    
    left_vrs.clear();
    right_vrs.clear();
    
    // Reserve some space for prepare_cell().
    //
    double lambda_left[4];
    Matrix<double> flux_left(2, 4);
    Matrix<double> accum_left(2, 4);

    int index[4] = {0, 2, 3, 1};  // In Fortran: index = {1, 3, 4, 2}. From the common block "hcindex". TODO: To be checked.

    // Some workspace variables for HyperCube::cubsol(). Originally documented in: hcube.F.
    double u[hn][hm + 1];
    double g[hm][hm + 1];
    double stormd[hm];

    for (int il = 0; il < nul; il++){
    for (int jl = 0; jl < nvl; jl++){
    if  (left_is_inside(il+1, jl+1)){
        // if (insided("left", il, jl) == 0) continue;
        // This is valid only when the domain is a rectangle.
        // When the time comes, insided() must be written.
        if (  left_is_complex(il, jl)  ) continue; // Perhaps left_cells can be turned into a Matrix<bool> containing the same info (rendering RealEigenvalueCell useless).
    
        prepare_cell(il, jl, left_family, lefte, leftffv, leftaav, lambda_left, flux_left, accum_left); // Before: preplft.
        for (int ir = 0; ir < nur; ir++){
        for (int jr = 0; jr < nvr; jr++){
        if  (right_is_inside(ir+1,jr+1)){
            // if (insided("right", ir, jr) == 0) continue;
            if (singular && left_right_adjacency(il, jl, ir, jr)) continue;                
                // TODO: See how much time these if's take up and think if they can or should be
                //       made in C instead of C++.         
                if (  right_is_complex(ir, jr)  ) continue;
                if (filhcub4(ir, jr, index, &foncub[0][0], hm, ncvert_, lambda_left, flux_left, accum_left) != 0){
                
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

                      nedges_ = hc.cpp_mkedge(cpp_edges_, dime_, nedges_, &smpedg_[0][0], 
                                              &solptr_[0][0], &fnbr_[0][0], nsimp_, nsface_);
                           
                      filedg4 (cpp_sol, dims_, cpp_edges_, dime_, nedges_, 
                               il, jl, ir, jr, left_vrs, right_vrs);
                }
        }
        }
        }
    }
    }
    }
    return;    
}

// This method sets the family and validates the cells for the left domain.
//
void Double_Contact::set_left_family(int nlf){
    left_family = nlf; 

    validate_cells(left_family, left_cell_type, left_eig_is_real, left_is_complex, left_is_inside);

    return;
}

// This method sets the family and validates the cells for the right domain.
//
void Double_Contact::set_right_family(int nrf){
    right_family = nrf; 

    validate_cells(right_family, right_cell_type, right_eig_is_real, right_is_complex, right_is_inside);

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
//void Double_Contact::func(int index, double &val, int ir, int jr, int kl, int kr, 
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
void Double_Contact::func(double *val, int ir, int jr, int kl, int kr, 
                          double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input){

        // Some auxiliary stuff.
        int domain_i, domain_j;

        if      (kr == 0) {domain_i = ir;     domain_j = jr;}
        else if (kr == 1) {domain_i = ir + 1; domain_j = jr;}
        else if (kr == 2) {domain_i = ir + 1; domain_j = jr + 1;}
        else if (kr == 3) {domain_i = ir;     domain_j = jr + 1;}

        double lr  = righte(domain_i, domain_j)[right_family];

        double fr  = rightffv(domain_i, domain_j).component(0);
        double hur = rightaav(domain_i, domain_j).component(0);

        double gr  = rightffv(domain_i, domain_j).component(1);
        double hvr = rightaav(domain_i, domain_j).component(1);;

        // Output        
        val[0] = lambda_left_input[kl] - lr;
        val[1] = lambda_left_input[kl]*(accum_left_input(0, kl) - hur) - (flux_left_input(0, kl) - fr);
        val[2] = lr*(accum_left_input(1, kl) - hvr) - (flux_left_input(1, kl) - gr);
    
    return;
}

// In Fortran:
//     index = {1, 3, 4, 2}.
//
// In C:
//     index = {0, 2, 3, 1}.
//
// TODO: See what index means, we got here and went no further today (7-fev-2011).
int Double_Contact::filhcub4 (int ir, int jr, int *index, double *foncub, int hm, int ncvert_,
                              double *lambda_left_input, Matrix<double> &flux_left_input, Matrix<double> &accum_left_input){ // real    foncub(hm,*)
    bool zero[3] = {false, false, false};

    double val[3];    // To be filled by Double_Contact::func();
    double refval[3]; // To be filled by Double_Contact::func();
    
    func(refval, ir, jr, 0, 0, lambda_left_input, flux_left_input, accum_left_input);
    //if ( funcomp ( refval, ir, jr, 1, 1 ) .eq. 0 ) return
    
    for (int kl = 0; kl < 4; kl++){
        for (int kr = 0; kr < 4; kr++){
            //if ( funcomp ( val, ir, jr, kl, kr ) .eq. 0 ) return
            func(val, ir, jr, kl, kr, lambda_left_input, flux_left_input, accum_left_input);
            
            for (int comp = 0; comp < 3; comp++){
                foncub[comp*ncvert_ + 4*index[kl] + index[kr]] = val[comp]; // ??? O q e esse index?????
//                foncub[comp*ncvert_ + 4*(index[kl] - 1) + index[kr]] = val[comp]; // ??? O q e esse index?????
                // foncub(comp,4*(index(kl)-1)+index(kr)) = val // ??? O q e esse index?????
                // if (refval[comp]*val[comp] < 0.0) zero[comp] = true;
                if (refval[comp]*val[comp] <= 0.0) zero[comp] = true; // Modified by Morante on 21-06-2011 by advice from Castaneda.
            }
        }
    }
          
    // if (!zero[0] && !zero[1] && !zero[2]) return 0;
    if (!zero[0] || !zero[1] || !zero[2]) return 0; // Modified by Morante on 21-06-2011 by advice from Castaneda.
    
    return 1;

}

// From: integer function adjrect ( il, jl, ir, jr ) at contour4.F.
//
// This function returns true if the cell (il, jl) is adjacent to the cell (ir, jr),
// and false otherwise.
//
bool Double_Contact::left_right_adjacency(int il, int jl, int ir, int jr){
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
     
      return ( (fabs( (ur0+(ir+.5)*dur) - (ul0+(il+.5)*dul) ) <= dumax) &&
               (fabs( (vr0+(jr+.5)*dvr) - (vl0+(jl+.5)*dvl) ) <= dvmax)
             );
}
