#include "Extension_CurveTPCW.h"
#include "Debug.h"

Extension_CurveTPCW::Extension_CurveTPCW(const RealVector &dpmin, const RealVector &dpmax, const int *domain_number_of_grid_pnts_input,
                                 const Flux2Comp2PhasesAdimensionalized *dff, const Accum2Comp2PhasesAdimensionalized *daa){
//                                 const FluxFunction *drff, const AccumulationFunction *draa){

    // ======================== Right domain ======================== //
    int domain_rows = domain_number_of_grid_pnts_input[0];
    int domain_cols = domain_number_of_grid_pnts_input[1];

    // Right flux and accumulation functions.
    domain_ff = dff;
    domain_aa = daa;
    domain_reduced_ff = dff->getReducedFlux();
    domain_reduced_aa = daa->getReducedAccumulation();

    // Reserve space and/or copy the input parameters to their inner counterparts.
    domain_number_of_grid_pnts = new int[dpmin.size()];
    domain_pmin.resize(dpmin.size());
    domain_pmax.resize(dpmin.size());
    for (int i = 0; i < dpmin.size(); i++){
        domain_number_of_grid_pnts[i] = domain_number_of_grid_pnts_input[i];
        domain_pmin.component(i) = dpmin.component(i);
        domain_pmax.component(i) = dpmax.component(i);
    }

    // Allocate space for the grid, etc., and fill those values.
    // TODO: The case where the domain is a triangle is to be treated differently.
    domain_grid.resize(domain_rows, domain_cols);
    domain_reduced_ffv.resize(domain_rows, domain_cols);
    domain_reduced_aav.resize(domain_rows, domain_cols);
    domain_reduced_e.resize(domain_rows, domain_cols);
    domain_reduced_eig_is_real.resize(domain_rows, domain_cols);

    create_grid(domain_pmin, domain_pmax, domain_number_of_grid_pnts, domain_grid);

    fill_values_on_grid(domain_pmin, domain_pmax,
                        domain_ff, domain_aa,
//                        domain_reduced_ff, domain_reduced_aa,
                        domain_number_of_grid_pnts,
                        domain_grid,
                        domain_reduced_ffv, domain_reduced_aav,
                        domain_reduced_e, domain_reduced_eig_is_real);

    //set_domain_family(domain_f);

    nur = domain_number_of_grid_pnts[0] - 1; // Number of cells, not number of points in the grid
    nvr = domain_number_of_grid_pnts[1] - 1; // Number of cells, not number of points in the grid
    ur0 = domain_pmin.component(0);
    ur1 = domain_pmax.component(0);
    vr0 = domain_pmin.component(1);
    vr1 = domain_pmax.component(1);
    dur = ( ur1 - ur0 ) / nur;
    dvr = ( vr1 - vr0 ) / nvr;
    // ======================== Right domain ======================== //

    // Invoke the combinatorial stuff
    hn    = 3; //N
    hm    = 2; //M
    dncv  =  8; // dncv SEEMS to be DNCV, as can be inferred from: hcmarc.inc:4.
                // Value confirmed in: hc32c.inc:5.

    dims_ = 18; // dims_ SEEMS to be DIMS, as can be inferred from: hc32s.inc:7.

    dime_ = 36; // dime_ SEEMS to be DIME, as can be inferred from: hc32e.inc:7.
                // See, however the accompanying warning in the original file:
                //
                //     "to do:  why are too many solutions created?"
                //
                // Apparently theory suggests that DIME = 6.

    dimf  = 18; // dimf SEEMS to be DNFACE, as can be inferred from: hccube.inc:7.

    ncvert_ = 8; //16; // N^2 ncvert_ SEEMS to be DNCV, as can be inferred from: hcsoln.inc:3.
    nsimp_  =  6; // N!  nsimp_ SEEMS to be DNSIMP, as can be inferred from: hccube.inc:5.

//    dnsimp = 6;
//    dnface = 18;

//    int DNCV = 8;  // Found in: rp/inc/hc32c.inc
//    int DIMS = 18; // hc32s.inc
//    int DIME = 36; // hc32e.inc
//    int dimf_ = 18;

    int numberOfCombinations = 4; // = hc.combination(hn + 1, hm + 1) = hc.combination(3 + 1, 2 + 1);
                                  // numberOfCombinations SEEMS to be DNSF, as can be inferred from: hccube.inc:7.

    // TODO: Copy what is needed from these lines below in compute_extension_curve();  
    storn_ = new int[hn + 1]; // int storn_[hn + 1]; 
    int storm_[hm + 1];

    // double cvert_[ncvert_][hn];
//    cvert_ = new double*[ncvert_];
//    for (int i = 0; i < ncvert_; i++) cvert_[i] = new double[hn];
    cvert_.resize(ncvert_, hn); // Was transposed: hccube.inc:4

//    double vert[ncvert_][hn];
    vert.resize(ncvert_, hn);

//    int bsvert_[hn + 1][hn];
    bsvert_.resize(hn + 1, hn); // Was transposed: hccube.inc:5

//    int perm_[hn][nsimp_];
    perm_.resize(hn, nsimp_);   // Was NOT transposed: hccube.inc:5

    //int comb_[numberOfCombinations][hm + 1]; 
    comb_.resize(numberOfCombinations, hm + 1); // Was transposed: hccube.inc:7

//    double foncub[hm][ncvert_]; // First defined here.
    foncub.resize(hm, ncvert_); // NOT transposed, as seen in hcsoln.inc:3.

    //inicializing arrays dimensions
    nsface_ = hc.mkcomb(comb_.data(), hn + 1, hm + 1);

//    int fnbr_[nsface_][nsface_];
    fnbr_.resize(nsface_, nsface_);

    //int dimf_ = 84;

    nsoln_ = -1;
    //double sol_[hn][dims_];
    cpp_sol.resize(hn, dims_); // NOT transposed, as seen in hcsoln.inc:3.

//    int solptr_[nsimp_][nsface_];
    solptr_.resize(nsimp_, nsface_); // Transposed, as seen in hcsoln.inc:4.
                                     // nsface_ SEEMS to be DNSF.

//    initialize_matrix(nsimp_, nsface_, &solptr_[0][0], 0);//TODO: Revisar como "solptr" eh modificada, os numero sao muito estranhos

    // int edges_[2][dime_];
    cpp_edges_.resize(2, dime_); // NOT transposed as seen in hcedge.inc:3.
//    initialize_matrix(2, dime_, &edges_[0][0], -6);//TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

//    int smpedg_[nsimp_][2];
    smpedg_.resize(nsimp_, 2);   // Transposed, as seen in hcedge.inc:3.
    initialize_matrix(nsimp_, 2, smpedg_.data(), 0);//TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    //inicializing another arrays, it were globally defined in java
//    int facptr_[nsimp_][nsface_];
    facptr_.resize(nsimp_, nsface_);

//    int face_[hm + 1][dimf_];
    face_.resize(hm + 1, dimf);  // Was NOT transposed: hccube.inc:7
                                 // Confirmed in: hcmarc.inc:4.

    //initialize_matrix(hn, nsimp_, perm_.data(), -7);
    hc.mkcube(cvert_.data(), bsvert_.data(), perm_.data(), ncvert_, nsimp_, hn);


    nface_ = hc.mkface(face_.data(), facptr_.data(), fnbr_.data(), dimf, nsimp_, hn, hm, nsface_,
                       bsvert_.data(), comb_.data(), perm_.data(), &storn_[0], &storm_[0]);

//    int exstfc[nface_];
    exstfc = new int[nface_]; // Verify that nface_ is DNFACE.

    // Cell-related:
    index = new int[4];
    index[0] = 0;
    index[1] = 2;
    index[2] = 3;
    index[3] = 1;

}

Extension_CurveTPCW::~Extension_CurveTPCW(){
    delete [] index;

    delete [] exstfc;
    delete [] storn_;

    delete [] domain_number_of_grid_pnts;
}

void Extension_CurveTPCW::filedg3(Matrix<double> &sol_, int dims, Matrix<int> &edges_, 
                              int dime, int nedges_,
                              const std::vector<RealVector> &segment, 
                              RealVector &uv, 
                              std::vector<RealVector> &curve_segments, std::vector<RealVector> &domain_segments){

    // Set base points, increments, and midpoint
    // (From contour3.F).
    //
    double ub  = segment[0].component(0);
    double vb  = segment[0].component(1);
    double dub = segment[1].component(0) - ub;
    double dvb = segment[1].component(1) - vb;

    double u = uv.component(0); // ur0 + dur * (ir-1)
    double v = uv.component(1); // vr0 + dvr * (jr-1)

    // Store all pairs of edges that were found
    RealVector p1(2), p2(2);
    for (int nedg = 0; nedg < nedges_; nedg++) {
        // For the curve
        p1.component(0) = ub + dub*sol_(2, edges_(0, nedg)); // LX1 Was  = segend(1,1,sn) = ub + dub * sol(3,edges(1,nedg))
        p1.component(1) = vb + dvb*sol_(2, edges_(0, nedg)); // LY1 Was  = segend(2,1,sn) = vb + dvb * sol(3,edges(1,nedg))

        p2.component(0) = ub + dub*sol_(2, edges_(1, nedg)); // LX2 Was  = segend(1,2,sn) = ub + dub * sol(3,edges(2,nedg))
        p2.component(1) = vb + dvb*sol_(2, edges_(1, nedg)); // LY2 Was  = segend(2,2,sn) = vb + dvb * sol(3,edges(2,nedg))
 
        curve_segments.push_back(p1);
        curve_segments.push_back(p2);

        // For the domain;
        p1.component(0) = u + dur*sol_(0, edges_(0, nedg)); // RX1 Was:  = segend(1,1,sn) = u + dur * sol(1,edges(1,nedg))
        p1.component(1) = v + dvr*sol_(1, edges_(0, nedg)); // RY1 Was:  = segend(2,1,sn) = v + dvr * sol(2,edges(1,nedg))

        p2.component(0) = u + dur*sol_(0, edges_(1, nedg)); // RX2 Was:  = segend(1,2,sn) = u + dur * sol(1,edges(2,nedg))
        p2.component(1) = v + dvr*sol_(1, edges_(1, nedg)); // RY2 Was:  = segend(2,2,sn) = v + dvr * sol(2,edges(2,nedg))

        domain_segments.push_back(p1);
        domain_segments.push_back(p2);
    }

    return;
}


// Was in locimp.F as ecnimp.
//
// This routine finds shocks that are characteristic on one side.
// The shocks are based on the original segments.
//
// For type == segmnt, the shock is characteristic relative
// to the locus.
//
// For type == intror, the shock is characteristic relative
// to the opposite state.
//
// characteristic_where is one of: EXTENSION_CHARACTERISTIC_ON_CURVE or EXTENSION_CHARACTERISTIC_ON_DOMAIN.
//
void Extension_CurveTPCW::compute_extension_curve(int characteristic_where, int singular,
                                                  const std::vector<RealVector> &original_segments, int curve_family,
                                                  const Flux2Comp2PhasesAdimensionalized *curve_ff, const Accum2Comp2PhasesAdimensionalized *curve_aa, // For the curve.
//                                                  FluxFunction *curve_reduced_ff, AccumulationFunction *curve_reduced_aa, // For the curve.
                                                  int domain_family, 
                                                  std::vector<RealVector> &curve_segments,
                                                  std::vector<RealVector> &domain_segments){

    if ( Debug::get_debug_level() == 5 ) {
        printf("compute_extension_curve\n");
    }

    // Clear the output.
    curve_segments.clear();
    domain_segments.clear();

    // Truncate for square and triangle.
    initialize_matrix(1, nface_, exstfc, 1);
      
    // Set the domain family and validate the cells.
    set_domain_family(domain_family);

    // Prepare the curve-related information. This was in the ctor(), but has been moved here.
    // BEGIN
//    int dim = original_segments[0].size();
    
//    curve_number_of_segments = original_segments.size()/2;
//    curve_segments.resize(2*curve_number_of_segments);
//    
//    for (int i = 0; i < 2*curve_number_of_segments; i++){
//        curve_segments[i].resize(dim);
//        for (int j = 0; j < dim; j++) curve_segments[i].component(j) = original_segments[i].component(j);
//    }
    
//    //Flux and accumulation functions on the original curve.
//    curve_ff = (FluxFunction*)cff;
//    curve_aa = (AccumulationFunction*)caa;

    // Fill the left stuff
    fill_values_on_segments(curve_ff, curve_aa, 
//                            curve_reduced_ff, curve_reduced_aa,
                            original_segments,
                            curve_reduced_ffv, curve_reduced_aav, 
                            curve_reduced_e, curve_reduced_eig_is_real);

    // END
    // Prepare the curve-related information. This was in the ctor(), but has been moved here.

//    int type = characteristic_where;

//    bool cond = (characteristic_where == CHARACTERISTIC_ON_DOMAIN);

    double segment_reduced_lambda[2];
    Matrix<double> segment_reduced_flux(3, 2);
    Matrix<double> segment_reduced_accum(3, 2);

    for (int i = 0; i < original_segments.size()/2; i++){
        // prepare_segment is defined in Bifurcation_Curve.
        //
        if(!prepare_segment(2*i, curve_family, characteristic_where,
                            curve_reduced_e, curve_reduced_ffv, curve_reduced_aav, curve_reduced_eig_is_real,
                            segment_reduced_lambda, 
                            segment_reduced_flux, segment_reduced_accum)) continue;

        // TODO: These lines below Should go into prepare_segment()
        std::vector<RealVector> current_segment;
        current_segment.resize(2);
        current_segment[0] = original_segments[2*i];
        current_segment[1] = original_segments[2*i + 1];

        curve2p5(characteristic_where,
                 current_segment, segment_reduced_lambda, segment_reduced_flux, segment_reduced_accum, 
                 singular,
                 curve_segments, domain_segments);
    }

    return;
}

void Extension_CurveTPCW::extension_curve_func(double *val, int ir, int jr, int kl, int kr, int characteristic_where, // Equivalent to bctype's type in sqfun.F
                                           double *segment_reduced_lambda, Matrix<double> &segment_reduced_flux, Matrix<double> &segment_reduced_accum){
    
    int domain_i, domain_j;

    if      (kr == 0) {domain_i = ir;     domain_j = jr;}
    else if (kr == 1) {domain_i = ir + 1; domain_j = jr;}
    else if (kr == 2) {domain_i = ir + 1; domain_j = jr + 1;}
    else if (kr == 3) {domain_i = ir;     domain_j = jr + 1;}
        
    // HERE WE ARE FILLING THE CURVE REDUCED ACCUMULATION AND FLUX.
    double Gc[3], Fc[3];

    for (int k = 0; k < 3; k++){
        Gc[k] = segment_reduced_accum(k, kl);
        Fc[k] = segment_reduced_flux(k, kl);
    }

    double ld = domain_reduced_e(domain_i, domain_j)[domain_family];

    double Gd[3], Fd[3];
    for (int k = 0; k < 3; k++){
        Gd[k] = domain_reduced_aav(domain_i, domain_j).component(k); // hur = hua(ir,jr);
        Fd[k] = domain_reduced_ffv(domain_i, domain_j).component(k); // fr  = fa(ir,jr);
    }

    double Hmatrix[3][3];
    double Gq[3];

    for(int i = 0; i < 3; i++){
        Gq[i]         =  Gd[i] - Gc[i]  ;   
        Hmatrix[i][0] =  Gq[i]          ;    // bJetMatrix(i) - bref_G[i]; // b=G   
   	Hmatrix[i][1] = -Fd[i]          ;    // a=F 
        Hmatrix[i][2] =  Fc[i]          ;
    }

    val[0] = det(3, &Hmatrix[0][0]); // TODO: PRECISAMOS DO METODO DETERMINANTE.

    double X12 = Fd[0]*Gq[1] - Fd[1]*Gq[0] ;
    double X31 = Fd[2]*Gq[0] - Fd[0]*Gq[2] ;
    double X23 = Fd[1]*Gq[2] - Fd[2]*Gq[1] ;

    double X12_0 = Fc[0]*Gq[1] - Fc[1]*Gq[0] ;
    double X31_0 = Fc[2]*Gq[0] - Fc[0]*Gq[2] ;
    double X23_0 = Fc[1]*Gq[2] - Fc[2]*Gq[1] ;

    double Y21 = Fd[1]*Fc[0] - Fd[0]*Fc[1] ; 
    double Y13 = Fd[0]*Fc[2] - Fd[2]*Fc[0] ;
    double Y32 = Fd[2]*Fc[1] - Fd[1]*Fc[2] ;

    double den      = X12*X12 + X31*X31 + X23*X23 ; 

    double scaling_factor =  (X12_0*X12 + X31_0*X31 + X23_0*X23)/den ;    
        
    double red_shock_speed = (Y21*X12 + Y13*X31 + Y32*X23)/den ;

    double lambda;
    if (characteristic_where == CHARACTERISTIC_ON_CURVE){
        lambda = segment_reduced_lambda[kl];
    }
    else {
        lambda = scaling_factor*domain_reduced_e(domain_i, domain_j)[domain_family];
    }

    val[1] = red_shock_speed - lambda; // SECOND EQUATION

    return;
}

//
//********************************************************************
//
//    this routine finds level curves for the system of equations
//
//         f(u,v,x,y) = 0
//         g(u,v,x,y) = 0
//         h(x,y)     = 0
//
//    by the following method.  first solve the last equation to
//    obtain a curve  x = x(w), y = y(w).  then substitute this
//    curve into the system to reduce it to
//
//         f(u,v,w) = f(u,v,x(w),y(w)) = 0
//         g(u,v,w) = g(u,v,x(w),y(w)) = 0
//
//    this system can then be solved using the cube package.
//
//to do:  this comment is no longer true.
//    this code is the same as  curv3d  except that this code
//    requires the functions  f,g  to take the arguments  i,j,k.
//
//*******************************************************************
void Extension_CurveTPCW::curve2p5(int characteristic_where,
                              std::vector<RealVector> current_segment, 
                              double *segment_lambda, Matrix<double> &segment_flux, Matrix<double> &segment_accum,
                              int singular,
                              std::vector<RealVector> &curve_segments, std::vector<RealVector> &domain_segments){

//    int DNCV = 8;  // Found in: rp/inc/hc32c.inc
//    int DIMS = 18; // hc32s.inc
//    int DIME = 36; // hc32e.inc
//    int dimf_ = 18;
    
    int usevrt[dncv]; // Defined in: contour3.F.
    initialize_matrix(1, dncv, usevrt, 1); // DNCV, usevrt???

    //c to do: import the left sizes for testing closeness
    double dumax = dur * 2.0;
    double dvmax = dvr * 2.0;

    // c   set base points, increments, and midpoint
    // c   -----------------------------------------
    double ub  = current_segment[0].component(0);
    double vb  = current_segment[0].component(1);
    double dub = current_segment[1].component(0) - ub;
    double dvb = current_segment[1].component(1) - vb;

    double bmid[2];
    bmid[0] = ub + .5*dub;
    bmid[1] = vb + .5*dvb;

    double work1[hn][hm + 1]; //  Equivalent to double u[hn][hm + 1]; Was NOT transposed, as seen in hccube.inc:10.
    double work2[hm][hm + 1]; //  Equivalent to double g[hm][hm + 1]; Was 
    double stormd[hm];        //  Equivalent to double stormd[hm];
    int storm_[hm + 1];

    // Loop over the cubes
    double u, v;

    int sptr_[nface_]; // sptr SEEMS to be of size DNFACE = 18, as estated in hc32c.inc:8.

    for (int i = 0; i < nur; i++){
        u = ur0 + i*dur;

        for (int j = 0; j < nvr; j++){
            v = vr0 + j*dvr;

/* This is related to the triangles: will be commented for the time being.
c           check whether all, half, or none of the square is inside
c           --------------------------------------------------------
c           lower right point
              p(1) = u + dur
              p(2) = v
              if ( .not. inpdom ( p ) )  go to 200
c           upper left point
              p(1) = u
              p(2) = v + dvr
              if ( .not. inpdom ( p ) )  go to 200

c           upper right point
              p(1) = u + dur
              p(2) = v + dvr
              if ( inpdom ( p ) )  then
                  dohalf = .false.
                  usevrt(7)=1
                  usevrt(8)=1
              else
                  dohalf = .true.
                  usevrt(7)=0
                  usevrt(8)=0
              endif
*/

        // Check for duplicate squares in singular case
        if (singular  &&
            (fabs( u + .5*dur - bmid[0]) <= dumax) && // Equivalent to Double_Contact::left_right_adjacency().
            (fabs( v + .5*dvr - bmid[1]) <= dvmax) 
           ) continue;

        // Skip elliptic rectangles
        if (  domain_is_complex(i, j)  ) continue;
        
        // TODO: Take care of the triangular case

            // Skipped Setcomponent1 & 2.
            if (filhcub3(i, j, index, foncub.data(), hm, ncvert_, characteristic_where, segment_lambda, segment_flux, segment_accum) != 0){

/*              status = cubsol ( solptr, sol, DIMS, sptr, nsoln, foncub,
     1                          cvert, exstfc, tface, facptr, hn, hm,
     2                          nsimp, nsface, nface,
     3                          wrkf1, wrkf2, wrki1 )
*/

                nsoln_ = hc.cpp_cubsol(solptr_.data(), cpp_sol, dims_,
                                       &sptr_[0], nsoln_, foncub.data(), &exstfc[0],
                                       face_.data(), facptr_.data(), dimf, cvert_.data(),
                                       ncvert_, hn, hm, nsimp_, nsface_, nface_, &work1[0][0],
                                       &work2[0][0], &stormd[0], &storm_[0]);

//ccc           if ( dohalf )  call putmi ( 'SPTR',   sptr, 1, nface, 1 )
//cdebug
//              if ( (dblev .eq. 3)  .and.  (nsoln .gt. 0) )  then
//                  call putmf ( 'FONCUB', foncub, hm, ncvert, hm )
//                  call putmf ( 'SOL',    sol, hn, nsoln, hn )
//                 call putmi ( 'SPTR',   sptr, 1, nface, 1 )
//                  call putmi ( 'SOLPTR', solptr, nsface, nsimp, nsface )
//              endif
//cdebug

           //   status = mkedge ( edges, DIME, nedges, smpedg, solptr,
           //                     fnbr, nsimp, nsface )

            // Make the list of edge pointers.
                hc.cpp_mkedge(cpp_edges_, dime_, nedges_, smpedg_.data(),
                                        solptr_.data(), fnbr_.data(), nsimp_, nsface_);




//cdebug
//              if ( (dblev .eq. 3)  .and.  (nsoln .gt. 0) )  then
//                  call putmi ( 'EDGES',  edges, 2, nedges, 2 )
//                  call putmi ( 'SMPEDG', smpedg, 2, nsimp, 2 )
//              endif
//cdebug
/*
c           store all pairs of edges that were found
c           ----------------------------------------
              if ( nedges .gt. 0 )  then
                  do 80 nedg = 1, nedges
                      sn = sn + 1

                      segend(1,1,sn) = ub + dub * sol(3,edges(1,nedg))
                      segend(2,1,sn) = vb + dvb * sol(3,edges(1,nedg))

                      segend(1,2,sn) = ub + dub * sol(3,edges(2,nedg))
                      segend(2,2,sn) = vb + dvb * sol(3,edges(2,nedg))

                      if ( sn .ge. seglim )  then
                          crv2p5 = -1
                          return
                      endif

                      sn = sn + 1

                      segend(1,1,sn) = u + dur * sol(1,edges(1,nedg))
                      segend(2,1,sn) = v + dvr * sol(2,edges(1,nedg))

                      segend(1,2,sn) = u + dur * sol(1,edges(2,nedg))
                      segend(2,2,sn) = v + dvr * sol(2,edges(2,nedg))

                      if ( sn .ge. seglim )  then
                          crv2p5 = -1
                          return
                      endif
  80              continue
              endif

 100        continue
*/
            // Invoke fildeg3
                RealVector uv(2); uv.component(0) = u; uv.component(1) = v;

                filedg3(cpp_sol, dims_, cpp_edges_,
                        dime_, nedges_,
                        current_segment,
                        uv,
                        curve_segments, domain_segments);

            }
        } // For j

    }     // For i

    return;
}

// In Fortran:
//     index = {1, 3, 4, 2}.
//
// In C:
//     index = {0, 2, 3, 1}.
//
// The triangular case is not dealt with yet.
int Extension_CurveTPCW::filhcub3(int ir, int jr, int *index, double *foncub, int hm, int ncvert_, int characteristic_where,
                              double *segment_lambda, Matrix<double> &segment_flux, Matrix<double> &segment_accum){ // real    foncub(hm,*)
    bool zero[2] = {false, false};

    double val[2];    // To be filled by Extension_Curve::extension_curve_func();
    double refval[2]; // To be filled by Extension_Curve::extension_curve_func();
    
    // TODO. So far this function is incomplete. 15-02-2011.
    extension_curve_func(refval, ir, jr, 0, 0, characteristic_where, segment_lambda, segment_flux, segment_accum);

    for (int kl = 0; kl < 2; kl++){
        for (int kr = 0; kr < 4; kr++){
            //if ( funcomp ( val, ir, jr, kl, kr ) .eq. 0 ) return
            extension_curve_func(val, ir, jr, kl, kr, characteristic_where, segment_lambda, segment_flux, segment_accum);
            
            for (int comp = 0; comp < 2; comp++){
                //foncub[comp*ncvert_ + 2*index[kl] + index[kr]] = val[comp]; // ??? O q e esse index?????
                foncub[comp*ncvert_ + kl + 2*index[kr]] = val[comp]; // Changed on 2011/06/06 by R. Morante, following Pablo's advice.
//                foncub[comp*ncvert_ + 4*(index[kl] - 1) + index[kr]] = val[comp]; // ??? O q e esse index?????
                // foncub(comp,4*(index(kl)-1)+index(kr)) = val // ??? O q e esse index?????
                if (refval[comp]*val[comp] < 0.0) zero[comp] = true;
            }
        }
    }
          
    // if (!zero[0] && !zero[1]) return 0;
    if (!zero[0]) return 0;
    if (!zero[1]) return 0;
    
    return 1;

}

// This method sets the family and validates the cells for the domain.
//
void Extension_CurveTPCW::set_domain_family(int family){
    domain_family = family;

    validate_cells(domain_family, domain_cell_type, domain_reduced_eig_is_real, domain_is_complex);

    return;
}


double Extension_CurveTPCW::det(int nn, double *A){ 
    double determinant ;

    switch (nn) {
        case 1:
            determinant = A[0];
            return determinant;
        break;

        case 2:
            determinant = A[0]*A[3] - A[1]*A[2];
            return determinant;
        break;

        case 3:
            determinant = A[0]*( A[4]*A[8] - A[5]*A[7] )
                          - A[3]*( A[1]*A[8] - A[7]*A[2] )
                          + A[6]*( A[1]*A[5] - A[4]*A[2] );

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

