#include "Double_Contact.h"

template <typename T> void Double_Contact::initialize_matrix(int n, int m, T *matrix, T value){
    for (int i = 0; i < n*m; i++) matrix[i] = value;
    return;
}

// TODO: Move this method upwards to Bifurcation_Curve.
//
void Double_Contact::initialize_projection(const FluxFunction *ff, const AccumulationFunction *aa,
                                           const RealVector &pmin, const RealVector &pmax, const int *number_of_grid_pnts, 
                                           Matrix<RealVector> &grid,
                                           Matrix<RealVector> &ffv, Matrix<RealVector> &aav,
                                           Matrix< std::vector<double> > &e, Matrix< std::vector<bool> > &eig_is_real,
                                           int family, int &inner_family,
                                           int &nu, int &nv, double &u0, double &u1, double &v0, double &v1, double &du, double &dv){
    
    int rows = number_of_grid_pnts[0];
    int cols = number_of_grid_pnts[1];

    // Reserve space and/or copy the input parameters to their inner counterparts.
//    left_number_of_grid_pnts = new int[lpmin.size()];
//    leftpmin.resize(lpmin.size());
//    leftpmax.resize(lpmin.size());
//    for (int i = 0; i < lpmin.size(); i++){
//        left_number_of_grid_pnts[i] = l_number_of_grid_pnts[i];
//        leftpmin.component(i) = lpmin.component(i);
//        leftpmax.component(i) = lpmax.component(i);
//    }

    // Allocate space for the grid, etc., and fill those values.
    // TODO: The case where the domain is a triangle is to be treated differently.
    grid.resize(rows, cols);
    ffv.resize(rows, cols);
    aav.resize(rows, cols);
    e.resize(rows, cols);
    eig_is_real.resize(rows, cols);

    create_grid(pmin, pmax, number_of_grid_pnts, grid);

    fill_values_on_grid(pmin, pmax, (FluxFunction*)ff, (AccumulationFunction*)aa, 
                        number_of_grid_pnts,
                        grid,
                        ffv, aav, 
                        e, eig_is_real);

    inner_family = family;

    nu = number_of_grid_pnts[0] - 1; // Number of cells, not number of points in the grid
    nv = number_of_grid_pnts[1] - 1; // Number of cells, not number of points in the grid
    u0 = pmin.component(0);
    u1 = pmax.component(0);
    v0 = pmin.component(1);
    v1 = pmax.component(1);

    du = ( u1 - u0 ) / nu;
    dv = ( v1 - v0 ) / nv;

    return;
}

Double_Contact::Double_Contact(const RealVector &lpmin, const RealVector &lpmax, const int *l_number_of_grid_pnts,
                               const FluxFunction *lff, const AccumulationFunction *laa, int lf,
                               const RealVector &rpmin, const RealVector &rpmax, const int *r_number_of_grid_pnts,
                               const FluxFunction *rff, const AccumulationFunction *raa, int rf){

    // Domain-related.
    //
    // TODO: Try to consolidate set_left_family & set_right_family
    clock_t begin;

    begin = clock();
    initialize_projection(lff, laa,
                          lpmin, lpmax, l_number_of_grid_pnts, 
                          leftgrid, 
                          leftffv, leftaav,
                          lefte, left_eig_is_real,
                          lf, left_family,
                          nul, nvl, ul0, ul1, vl0, vl1, dul, dvl);
    set_left_family(lf);

    initialize_projection(rff, raa,
                          rpmin, rpmax, r_number_of_grid_pnts, 
                          rightgrid, 
                          rightffv, rightaav,
                          righte, right_eig_is_real,
                          rf, right_family,
                          nur, nvr, ur0, ur1, vr0, vr1, dur, dvr);
    set_right_family(rf);

    printf("Double_Contact. Filling the grid took %f seconds. ***\n", (double)(clock() - begin)/(double)CLOCKS_PER_SEC);

    dumax = 2.0 * max ( dur, dul );
    dvmax = 2.0 * max ( dvr, dvl );

    // Combinatorial. TODO: Convert all this stuff in a function initialize_combinatorics_double_contact().
    //
    hn = 4; //N
    hm = 3; //M
    DNCV = 16;
    DNSIMP = 24;
    DNSF = 5;
    DNFACE = 125;
    dims_ = 125;
    dime_ = 200;

    ncvert_ = 16; //N^2
    nsimp_ = 24; //N!

    numberOfCombinations = 5; // = hc.combination(hn + 1, hm + 1) = hc.combination(4 + 1, 3 + 1);

    //storn_ = new int[hn + 1];
    //storm_ = new int[hm + 1]; // Careful here
    cvert_ = new double[ncvert_*hn];
//    vert = new double[ncvert_*hn]; // TODO This line can be removed at the end.
    bsvert_ = new int[(hn + 1)*hn];
    perm_ = new int[hn*nsimp_];
    comb_ = new int[numberOfCombinations*(hm + 1)];

    foncub = new double[hm*ncvert_];

    nsface_ = hc.mkcomb(comb_, hn + 1, hm + 1);

    fnbr_ = new int[nsface_*nsface_];

    dimf_ = 84;

    cpp_sol.resize(hn, dims_);

    solptr_ = new int [nsimp_*nsface_];

    cpp_edges_.resize(2, dime_);

    smpedg_ = new int[nsimp_*2];
    facptr_ = new int[nsimp_*nsface_];
    face_ = new int[(hm + 1)*dimf_];

    hc.mkcube(cvert_, bsvert_, perm_, ncvert_, nsimp_, hn);

    nface_ = hc.mkface(face_, facptr_, fnbr_, dimf_, nsimp_, hn, hm, nsface_,
                       bsvert_, comb_, perm_, storn_, storm_);

    exstfc = new int[nface_];
    sptr_ = new int[nface_];

    // Reserve some space for prepare_cell().
    //
    lambda_left = new double[4];
    flux_left.resize(2, 4);
    accum_left.resize(2, 4);

    // In Fortran: index = {1, 3, 4, 2}. From the common block "hcindex". TODO: To be checked.
    index = new int[4];
    index[0] = 0;
    index[1] = 2;
    index[2] = 3;
    index[3] = 1;

    // Some workspace variables for HyperCube::cubsol(). Originally documented in: hcube.F.
//    u = new double[hn*(hm + 1)];
//    g = new double[hm*(hm + 1)];
//    stormd = new double[hm];

}

Double_Contact::~Double_Contact(){
//    delete [] stormd;
//    delete [] g;
//    delete [] u;
    delete [] index;
    delete [] lambda_left;
    delete [] sptr_;
    delete [] exstfc;
    delete [] face_;
    delete [] facptr_;
    delete [] smpedg_;
    delete [] solptr_;
    delete [] fnbr_;
    delete [] foncub;
    delete [] comb_;
    delete [] perm_;
    delete [] bsvert_;
//    delete [] vert;
    delete [] cvert_;
    //delete [] storm_;
    //delete [] storn_;

//    delete [] right_number_of_grid_pnts;
//    delete [] left_number_of_grid_pnts;
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
//        p1.component(0) = ul0 + dul * (il-1 + sol_(0, edges_(0, nedg) ) ); // LX1 Was  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
//        p1.component(1) = vl0 + dvl * (jl-1 + sol_(1, edges_(0, nedg) ) ); // LY1 Was  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];
//    
//        p2.component(0) = ul0 + dul * (il-1 + sol_(0, edges_(1, nedg) ) ); // LX2 Was  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
//        p2.component(1) = vl0 + dvl * (jl-1 + sol_(1, edges_(1, nedg) ) ); // LY2 Was  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];

        // This was approved by Marchesin

        p1.component(0) = ul0 + dul * (il + sol_(0, edges_(0, nedg) ) ); // LX1 Was  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
        p1.component(1) = vl0 + dvl * (jl + sol_(1, edges_(0, nedg) ) ); // LY1 Was  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];
    
        p2.component(0) = ul0 + dul * (il + sol_(0, edges_(1, nedg) ) ); // LX2 Was  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
        p2.component(1) = vl0 + dvl * (jl + sol_(1, edges_(1, nedg) ) ); // LY2 Was  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];

 
        left_vrs.push_back(p1);
        left_vrs.push_back(p2);

//        p3.component(0) = ur0 + dur * (ir-1 + sol_(2, edges_(0, nedg) ) ); // RX1 Was:  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
//        p3.component(1) = vr0 + dvr * (jr-1 + sol_(3, edges_(0, nedg) ) ); // RY1 Was:  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];

//        p4.component(0) = ur0 + dur * (ir-1 + sol_(2, edges_(1, nedg) ) ); // RX2 Was:  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
//        p4.component(1) = vr0 + dvr * (jr-1 + sol_(3, edges_(1, nedg) ) ); // RY2 Was:  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];
        p3.component(0) = ur0 + dur * (ir + sol_(2, edges_(0, nedg) ) ); // RX1 Was:  = segend[sn - 1][0][0];//sol_[0][edges_[0][nedg ]];
        p3.component(1) = vr0 + dvr * (jr + sol_(3, edges_(0, nedg) ) ); // RY1 Was:  = segend[sn - 1][0][1];//sol_[1][edges_[0][nedg ]];

        p4.component(0) = ur0 + dur * (ir + sol_(2, edges_(1, nedg) ) ); // RX2 Was:  = segend[sn - 1][1][0];//sol_[0][edges_[1][nedg ]];
        p4.component(1) = vr0 + dvr * (jr + sol_(3, edges_(1, nedg) ) ); // RY2 Was:  = segend[sn - 1][1][1];//sol_[1][edges_[1][nedg ]];


        right_vrs.push_back(p3);
        right_vrs.push_back(p4);

    }

    return;
}

void Double_Contact::compute_double_contact_engine(int il_min, int il_max, 
                                                   std::vector<RealVector> &left_vrs, 
                                                   std::vector<RealVector> &right_vrs){

    bool singular = (left_family == right_family);

    int status;

    double refval;
    int i, j;

    nsoln_ = -1;

    for (i = 0; i < nface_; i++) exstfc[i] = 1; // This is a MUST!!!
    initialize_matrix(1, nface_, exstfc, 1);

    //for (int il = 0; il < nul; il++){
    for (int il = il_min; il < il_max; il++){
    for (int jl = 0; jl < nvl; jl++){
        // if (insided("left", il, jl) == 0) continue;
        // This is valid only when the domain is a rectangle.
        // When the time comes, insided() must be written.
        if (  left_is_complex(il, jl)  ) continue; // Perhaps left_cells can be turned into a Matrix<bool> containing the same info (rendering RealEigenvalueCell useless).
    
        prepare_cell(il, jl, left_family, lefte, leftffv, leftaav, lambda_left, flux_left, accum_left); // Before: preplft.
        for(int ir = 0; ir < nur; ir++){
        for(int jr = 0; jr < nvr; jr++){
            // if (insided("right", ir, jr) == 0) continue;
            if (singular && left_right_ordering(il, jl, ir, jr)) continue;                
                // TODO: See how much time these if's take up and think if they can or should be
                //       made in C instead of C++.         
                if (  right_is_complex(ir, jr)  ) continue;
                if (filhcub4(ir, jr, index, foncub, hm, ncvert_, lambda_left, flux_left, accum_left) != 0){
                    nsoln_ = hc.cpp_cubsol(solptr_, cpp_sol, dims_, 
                                           sptr_, nsoln_, foncub, exstfc, 
                                           face_, facptr_, dimf_, cvert_, 
                                           ncvert_, hn, hm, nsimp_, nsface_, nface_, u, 
                                           g, stormd, storm_);
                        
                    // TODO: Add an error message as needed.
                    //
                    status = hc.cpp_mkedge(cpp_edges_, dime_, nedges_, smpedg_, 
                                           solptr_, fnbr_, nsimp_, nsface_);

                    filedg4 (cpp_sol, dims_, cpp_edges_, dime_, nedges_, 
                             il, jl, ir, jr, left_vrs, right_vrs);
                }
        }
        }
    }
    }

}

void Double_Contact::compute_double_contact(std::vector<RealVector> &left_vrs, 
                                            std::vector<RealVector> &right_vrs) {
   
    left_vrs.clear();
    right_vrs.clear();
    
    int nop = 8; // Number of processors, will be obtained via OpenMP's functions.
    int range = nul/nop; // Verify this ratio is integer.

    for (int l = 0; l < nop; l++){
        clock_t begin = clock();
        compute_double_contact_engine(l*range, (l + 1)*range, left_vrs, right_vrs);
        printf("Double_Contact. Cycle %d took %f seconds. ***\n", l, (double)(clock() - begin)/(double)CLOCKS_PER_SEC);
    }

    return;    
}

// This method sets the family and validates the cells for the left domain.
//
void Double_Contact::set_left_family(int nlf){
    left_family = nlf;

    cout <<"left family: "<<left_family<<endl;

    cout << "left cell type: " << left_cell_type << endl;

    validate_cells(left_family, left_cell_type, left_eig_is_real, left_is_complex);

    return;
}

// This method sets the family and validates the cells for the right domain.
//
void Double_Contact::set_right_family(int nrf){
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
    
    // index == 0
        // TODO: Why are we accessing the second eigenvalue??? (Why is it that family == 1?)
        double lr;
        
        if (kr == 0 )    lr = righte(ir,jr)[right_family];
        else if(kr == 1) lr = righte(ir + 1, jr)[right_family];
        else if(kr == 2) lr = righte(ir + 1, jr + 1)[right_family];
        else if(kr == 3) lr = righte(ir, jr + 1)[right_family];
    
        val[0] = lambda_left_input[kl] - lr;
    
    // index == 1
        double fr, hur;
        
        if (kr == 0){
            hur = rightaav(ir, jr).component(0); // hur = hua(ir,jr);
            fr  = rightffv(ir, jr).component(0); // fr  = fa(ir,jr);
        }
        else if (kr == 1){
            hur = rightaav(ir + 1, jr).component(0); // hur = hua(ir + 1,jr);
            fr  = rightffv(ir + 1, jr).component(0); // fr  = fa(ir + 1,jr);
        }
        else if (kr == 2){
            hur = rightaav(ir + 1, jr + 1).component(0); // hur = hua(ir + 1, jr + 1);
            fr  = rightffv(ir + 1, jr + 1).component(0); // fr  = fa(ir + 1, jr + 1);
        }
        else if (kr == 3){
            hur = rightaav(ir, jr + 1).component(0); // hur = hua(ir, jr + 1);
            fr  = rightffv(ir, jr + 1).component(0); // fr  = fa(ir, jr + 1);
        }
      
        //val = lambda_left_input[kl]*(accum_left_input(0, kl) - hur) - (fl(kl) - fr);
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
                foncub[comp*ncvert_ + 4*(index[kl] - 0) + index[kr]] = val[comp]; // ??? O q e esse index?????
//                foncub[comp*ncvert_ + 4*(index[kl] - 1) + index[kr]] = val[comp]; // ??? O q e esse index?????
                // foncub(comp,4*(index(kl)-1)+index(kr)) = val // ??? O q e esse index?????
                if (refval[comp]*val[comp] < 0.0) zero[comp] = true;
            }
        }
    }
          
    if (!zero[0] && !zero[1] && !zero[2]) return 0;
    
    return 1;

}

// From: integer function adjrect ( il, jl, ir, jr ) at contour4.F.
//
// This function returns true if the cell (il, jl) is adjacent to the cell (ir, jr),
// and false otherwise.
//
bool Double_Contact::left_right_ordering(int il, int jl, int ir, int jr){
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
     
//      return ( (fabs( (ur0+(ir+.5)*dur) - (ul0+(il+.5)*dul) ) <= dumax) &&
//               (fabs( (vr0+(jr+.5)*dvr) - (vl0+(jl+.5)*dvl) ) <= dvmax)
//             );

//      return ( (fabs( (ur0+(ir-.5)*dur) - (ul0+(il-.5)*dul) ) <= 2.0*dumax) &&
//               (fabs( (vr0+(jr-.5)*dvr) - (vl0+(jl-.5)*dvl) ) <= 2.0*dvmax)
//             );


      // Modified: 2011-09-28. Marchesin, Morante. Twice-computing suppressed.
      return ( (( (ur0+(ir-.5)*dur) - (ul0+(il-.5)*dul) ) <= 2.0*dumax) &&
               (( (vr0+(jr-.5)*dvr) - (vl0+(jl-.5)*dvl) ) <= 2.0*dvmax)
             );

}
