#include "ContourMethod.h"
#include "Inflection_Curve.h"

// TODO: Move or blend fill_with_jet as a method or methods of JetMatrix.
//

void Inflection_Curve::fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];

    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);

    flux_object->jet(state_c, c_jet, degree);

    // Fill F
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet(i);

    // Fill J
    if (J != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i * n + j] = c_jet(i, j);
            }
        }
    }

    // Fill H
    if (H != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    H[(i * n + j) * n + k] = c_jet(i, j, k);
                }
            }
        }
    }

    return;
}

Inflection_Curve::Inflection_Curve(const FluxFunction *f, const AccumulationFunction *a,
        const Boundary *b,
        const RealVector &min, const RealVector &max,
        const int *cells) {

    ff = f;
    aa = a;

    boundary = b;
    
    cout << "Min" << boundary->minimums() << endl;
    cout << "Max" << boundary->maximums() << endl;
    cout << "Type" << boundary->boundaryType() << endl;



    // Create the grid...
    //

    pmin.resize(min.size());
    pmax.resize(max.size());
    number_of_cells = new int[2];
    for (int i = 0; i < 2; i++) {
        number_of_cells[i] = cells[i];
        pmin.component(i) = min.component(i);
        pmax.component(i) = max.component(i);
    }


    int dim = 2;

    double delta[2];
    for (int i = 0; i < pmin.size(); i++) delta[i] = (fabs(pmax.component(i) - pmin.component(i))) / (double) (number_of_cells[i]);

    printf("delta[0] = %f, delta[1] = %f\n", delta[0], delta[1]);

    grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    dd.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    e.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    eig_is_real.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);

    for (int i = 0; i <= number_of_cells[0]; i++) {
        for (int j = 0; j <= number_of_cells[1]; j++) {
            grid(i, j).resize(dim);

            grid(i, j).component(0) = pmin.component(0) + (double) i * delta[0];
            grid(i, j).component(1) = pmin.component(1) + (double) j * delta[1];
        }
    }

    // ...and fill several values on the grid.
    //
    fill_values_on_grid();


    // TODO TODO Qual o lugar certo para colocar isso tudo... CONTOUR et al. versus BIFURCACOES TODO TODO
    // Combinatorial

    /* From: rp/inc/hc21c.inc 

      integer  N, DNCV, DNSIMP
      parameter (N = 2, DNCV = 4, DNSIMP = 2)

      integer  M, DNSF, DNFACE
      parameter (M = 1, DNSF = 3, DNFACE = 5)

     */

    /*    hn = 2; // N
        hm = 1; // M

        ncvert_ = 4; // N^2
        nsimp_  = 2; // N!

        hc.mkcube(&cvert_[0][0], &bsvert_[0][0], &perm_[0][0], ncvert_, nsimp_, hn);

        nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
                           &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

        index = new int[4];
        index[0] = 0;
        index[1] = 2;
        index[2] = 3;
        index[3] = 1;

        // Set the rectangle sizes and resolutions
        u0 = rect(1);
        u1 = rect(2);
        v0 = rect(3);
        v1 = rect(4);
        nu = res(1);
        nv = res(2);
        du = ( u1 - u0 ) / nu;
        dv = ( v1 - v0 ) / nv;
     */

    // Fill the usual values, AND left- and right-eigenvectors TOO.
}

Inflection_Curve::~Inflection_Curve() {
    delete [] number_of_cells;
    //    delete [] index;
}

// Given the extreme points of a rectangular domain
// and the number of grid points along each dimension,
// compute the vertices of the grid thus defined,
// and in said vertices a series of values.
//
//          pmin, pmax: Extremes of the domain. Ideally, 
//
//                          pmin[i] <= pmax[i] for 0 <= i < dimension of space.
//
//                      In practice, this will be checked out within the body of the function.
//                  ff, aa: Flux and accumulation functions that apply from R^n to R^n.
//     number_of_grid_pnts: The number of cells in each dimension (array defined externally).
//                    grid: The spatial grid created. Space to hold it must be reserved outside the function.
//                          The i-th dimension will have number_of_cells[i] cells. Thus, for 2D, which is the case
//                          that is being implemented, each vertex (i, j) will be of
//                          the form:
//
//                              grid[i*number_of_grid_pnts[1] + j].component(k) = pmin[k] + j*(pmax[k] - pmin[k])/(number_of_grid_pnts[k])
//
//                          where:
//
//                              0 <= i < number_of_grid_pnts[0],
//                              0 <= j < number_of_grid_pnts[1],
//                              0 <= k < 2.
//
//                          Thus, grid needs to be of size 
//
//                              number_of_grid_pnts[0]*...*number_of_grid_pnts[pmax.size() - 1].
//
//                      dd: Array of vectors that will hold the value of the directional derivatives
//                          at each vertex of the grid. This array, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//                      
//                       e: Array of vectors of eigenpairs that will hold all the eigenpairs at
//                          each vertex of the grid. These arrays, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//
//             eig_is_real: Array of vectors of booleans that state if each eigenvalue at a given grid vertex is
//                          real (true) or complex (false). These arrays, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//

// TODO: Change indices i, j to k, l. i & j are reserved for grid- or cell-like uses.

void Inflection_Curve::fill_values_on_grid(void) {

    // Dimension of space
    int dim = pmin.size();
    //    printf("dim = %d\n", dim);

    //    // Create the grid proper
    //    create_grid(pmin, pmax, number_of_grid_pnts, grid);

    // Number of elements in the grid.
    int n = 1;

    // The right and left eigenvector (entry)
    double l[dim], r[dim];

    // This submatrix is for use ahead (for each family): SubHF[][] = HF[][][n]*r[family][n]
    double SubHF[dim][dim];
    //    for (int k = 0; k < dim; k++) {
    //        for (int m = 0; m < dim; m++) {
    //            SubHF[m][k] = 0.0;
    //        }
    //    }

    double norm, dirdrv;

    for (int i = 0; i < dim; i++) n *= number_of_cells[i] + 1;

    double epsilon = 1e-10;

    // Fill the arrays with the value of the flux and accumulation functions at every point in the grid.
    // The eigenpairs must also be stored.
    for (int i = 0; i < n; i++) {

        double point[dim];
        // We only compute the value of the function on points that are inside the physical domain
        // given by "boundary".
        for (int j = 0; j < dim; j++) point[j] = grid(i).component(j);

        if (boundary->inside(point)) {
            double JF[dim][dim], JG[dim][dim];
            double HF[dim][dim][dim], HG[dim][dim][dim];
            fill_with_jet((RpFunction*) ff, dim, point, 2, 0, &JF[0][0], &HF[0][0][0]);
            fill_with_jet((RpFunction*) aa, dim, point, 2, 0, &JG[0][0], &HG[0][0][0]);

            // TODO: Isso aqui eh para o caso de acumulacao trivial!

            //            // Fill the values of the functions
            //            for (int j = 0; j < dim; j++){
            //                ffv(i).component(j) = F[j];
            //                aav(i).component(j) = G[j];
            //            }

            // Find the eigenpairs
            std::vector<eigenpair> etemp;
            Eigen::eig(dim, &JF[0][0], &JG[0][0], etemp);

            e(i).clear();
            e(i).resize(etemp.size());
            for (int j = 0; j < etemp.size(); j++) e(i)[j] = etemp[j];

            // Decide if the eigenvalues are real or complex
            eig_is_real(i).clear();
            eig_is_real(i).resize(etemp.size());
            for (int j = 0; j < etemp.size(); j++) {
                if (fabs(etemp[j].i) < epsilon) eig_is_real(i)[j] = true; // TODO: Comparacoes devem ser feitas com valores relativos, nao absolutos
                else eig_is_real(i)[j] = false;
            }

            // Find the directional derivatives
            dd(i).resize(dim);

            //                if (eig_is_real(i)[0] == true) {
            //                    for (int fam = 0; fam < dim; fam++){
            //                        norm   = 0.0;
            //                        dirdrv = 0.0;

            //                        for (int entry = 0; entry < dim; entry++){
            //                            l[entry] = e(i)[fam].vlr[entry];
            //                            r[entry] = e(i)[fam].vrr[entry];
            //                        }

            //                        // Reset
            //                        for (int k = 0; k < dim; k++) {
            //                            for (int m = 0; m < dim; m++) {
            //                                SubHF[m][k] = 0.0;
            //                            }
            //                        }

            //                        for (int k = 0; k < dim; k++) {
            //                            for (int m = 0; m < dim; m++) {
            //                                for (int n = 0; n < dim; n++) {
            //                                    SubHF[k][m] += HF[k][m][n]*r[n];
            ////                                    printf("r[%d][%d] = %f\n", fam, n, r[n]);
            //                                }
            //                            }
            //                        }

            //                        for (int n = 0; n < dim; n++) {
            //                            norm += l[n]*r[n];
            ////                            if (norm != 1.0) printf("FAILED!\n");
            //                        }

            //                        for (int k = 0; k < dim; k++) {
            //                            for (int m = 0; m < dim; m++) {
            //                                dirdrv += l[k]*SubHF[k][m]*r[m]; // TODO: Substituir para o caso de acumulacao nao trivial
            //                            }
            //                        }

            //                        // The directional derivative is

            //                        dd(i)[fam] = dirdrv / norm;

            ////                        printf("dd(%d)[%d] = %f\n", i, fam, dd(i)[fam]);
            //                    }
            //
            //                }

            for (int fam = 0; fam < dim; fam++) {

                dd(i)[fam] = this->dirdrv(2, &point[0], fam);
            }
        }
    }

    return;
}

//void Inflection_Curve::compute_inflection_curve(int inflection_family, std::vector<RealVector> &inflection_curve_segments){

//    // Clear the output.
//    inflection_curve_segments.clear();

//    // Truncate for square and triangle.
//    initialize_matrix(1, nface_, exstfc, 1);
//
//    // Set the domain family and validate the cells.
//    set_family(inflection_family);

//    // Invoke the core (inner cycle)
//    int DNCV = 4; // Found in: rp/inc/hc21c.inc (N = 2, DNCV = 4, DNSIMP = 2)
//    int DIMS = 6; // hc21s.inc
//    int DIME = 6; // hc21e.inc
//    // int dimf_ = 18; // TODO: Is this value used in this problem???

//    tsimp = 1;
//    tface = 3;

//    initialize(1, nface, existface, 1); // DNCV, usevrt???

//    double work1[hn][hm + 1]; //  Equivalent to double u[hn][hm + 1];
//    double work2[hm][hm + 1]; //  Equivalent to double g[hm][hm + 1];
//    double stormd[hm];        //  Equivalent to double stormd[hm];
//    int storm_[hm + 1];

//    // Loop over the cells
//    double u, v;

//    for (int i = 0; i < nu; i++){
//        u = ur0 + i*dr;

//        for (int j = 0; j < nv; j++){
//            v = vr0 + j*dvr;

///* This is related to the triangles: will be commented for the time being.
//c           check whether all, half, or none of the square is inside
//c           --------------------------------------------------------
//c           lower right point
//              p(1) = u + dur
//              p(2) = v
//              if ( .not. inpdom ( p ) )  go to 200
//c           upper left point
//              p(1) = u
//              p(2) = v + dvr
//              if ( .not. inpdom ( p ) )  go to 200

//c           upper right point
//              p(1) = u + dur
//              p(2) = v + dvr
//              if ( inpdom ( p ) )  then
//                  dohalf = .false.
//                  usevrt(7)=1
//                  usevrt(8)=1
//              else
//                  dohalf = .true.
//                  usevrt(7)=0
//                  usevrt(8)=0
//              endif
//*/

//        // Skip elliptic cells
//        if (  domain_is_complex(i, j)  ) continue;
//i, j,
//        // TODO: Take care of the triangular case
//
//        // Skipped Setcomponent1 & 2.
//        if (filinf(i, j, &foncub[0][0], hm, ncvert_) != 0){  // TODO: Finish this function
///*
//              status = cubsol ( solptr, sol, DIMS, sptr, nsoln, foncub,
//     1                          cvert, exstfc, tface, facptr, hn, hm,
//     2                          nsimp, nsface, nface,
//     3                          wrkf1, wrkf2, wrki1 )
//*/

//            nsoln_ = hc.cpp_cubsol(&solptr_[0][0], cpp_sol, DIMS,
//                                   &sptr_[0], nsoln_, &foncub[0][0], &exstfc[0],
//                                   &face_[0][0], &facptr_[0][0], dimf_, &cvert_[0][0],
//                                   ncvert_, hn, hm, nsimp_, nsface_, nface_, &work1[0][0],
//                                   &work2[0][0], &stormd[0], &storm_[0]);

//            // Make the list of edge pointers.
//            nedges_ = hc.cpp_mkedge(cpp_edges_, DIME, nedges_, &smpedg_[0][0],
//                                    &solptr_[0][0], &fnbr_[0][0], nsimp_, nsface_);

//            // Store all pairs of edges that were found
//            filedg2(sol, edges, nedges, inflection_curve);

//        } // For j

//    }     // For i

//    return;
//}

//void Inflection_Curve::filedg2(Matrix<double> &sol, Matrix<int> &edges, int nedges, std::vector<RealVector> &inflection_curve){
//    RealVector p1(2), p2(2);

//    for (int nedg = 0; nedg < nedges; nedg++){
//        p1.component(0) = sol(0, edges(0, nedg));
//        p1.component(1) = sol(1, edges(0, nedg));

//        p2.component(0) = sol(0, edges(1, nedg));
//        p2.component(1) = sol(1, edges(1, nedg));

//        inflection_curve.push_back(p1);
//        inflection_curve.push_back(p2);
//    }

//    return;
//}


/* DE AQUI EN ADELANTE ES NUEVO... */

/***********************************************************************
c
c      this routine computes the directional derivative of the eigen-
c      value in the direction of the right eigenvector at the vertices
c      of the bottom triangle or the top triangle of a rectangle with
c      lower left corner  (x0,y0)  and side lengths  dx,dy.  the
c      choice is made by  sqrtyp  (1-lower left triangle only, 2-both
c      triangles).  output values  (forig, fside, ftop  for sqrtyp=1,
c      fopps  also for sqrtyp = 2)  are used in routine  vcontu  to
c      find the curve where there is loss of genuine nonlinearity.
c
c      ignore the diagrams below
c
c       corner coords     diag slope -1      diag slope +1
c      ---------------    -------------      -------------
c      (x0,y1) (x1,y1)    top      opps      opps     top
c
c
c
c      (x0,y0) (x1,y0)    orig     side      side     orig
c
c
c      call setfam ( family )  before using this routine.
c
 ***********************************************************************/

int Inflection_Curve::function_on_square(double *foncub, int i, int j, int is_square) { // ver como esta en el ContourMethod.cc
    //      integer  consis, rlvect
    //      real     dirdrv

    //      integer  family
    //      common  /  sqfaml  /  family


    // Estes devem ser preeenchidos com o [fill_values_on_grid] e preenchemos as derivadas direccionais (Implica que as entradas devem ser outras, basta o tipo e as coordenadas (i, j).)
    double rvorig[2], rvside[2], rvtop[2], rvopps[2];
    double forig, fside, ftop, fopps;
    //    double lvorig[2], lvside[2], lvtop[2], lvopps[2];
    //    double xorig[2],  xside[2],  xtop[2],  xopps[2];

    //    double x1, y1;
    int orient;

    //   set useful coordinates
    //    x1 = x0 + dx;
    //    y1 = y0 + dy;

    //   set the corner coordinates
    //    xorig[1] = x0;
    //    xorig[2] = y0;
    //    xside[1] = x1;
    //    xside[2] = y0;
    //    xtop[1]  = x0;
    //    xtop[2]  = y1;
    //    xopps[1] = x1;
    //    xopps[2] = y1;

    /* No [create_grid] o pontos da malha estao em &p, entao eh alguma coisa do jeito:
        xorig = p(i  , j  );
        xside = p(i+1, j  );
        xtop  = p(i  , j+1);
        xopps = p(i+1, j+1);
     */

    //   compute the vectors at the corners (bottom triangle)
    //    if ( rlvect ( rvorig, lvorig, xorig, family ) .eq. 0 ) return 0;
    //    if ( rlvect ( rvside, lvside, xside, family ) .eq. 0 ) return 0;
    //    if ( rlvect ( rvtop , lvtop , xtop , family ) .eq. 0 ) return 0;
    /* No [fill_values_on_grid] os autovetores estao dentro de &e, TODO entender a ordem, para preencher alguma coisa do tipo
            rvorig = e(i,j)[famivalueFunctionly].vrr;

            rvorig = e(i  , j  );
            rvside = e(i+1, j  );
            rvtop  = e(i  , j+1);

            rlorig = e(i  , j  );
            rlside = e(i+1, j  );
            rltop  = e(i  , j+1);
    mas acho que rvCOSO = e(familia)[valor]; mesmo assim, antes disso temos que avaliar se o autovalor eh ou nao real:
        if ( e(familia)[valor] == false ) return 0;
    deve estar num loop, pois deve testar a "realidade" de todos os autovalores
     */
    //    if ( e(family)[i][j]   == false ) return 0;
    if (!eig_is_real(i, j)[family]) return 0;
    if (!eig_is_real(i + 1, j)[family]) return 0;
    if (!eig_is_real(i, j + 1)[family]) return 0;
    //    if ( e(family)[i+1][j] == false ) return 0;
    //    if ( e(family)[i][j+1] == false ) return 0;

    for (int k = 0; k < 2; k++) {
        rvorig[k] = e(i, j)[family].vrr[k];
        rvside[k] = e(i + 1, j)[family].vrr[k];
        rvtop[k] = e(i, j + 1)[family].vrr[k];
    }


    //   make vectors consistent at pairs of corners (if possible)
    //    if ( consis ( rvtop , rvorig, xtop , xorig, orient ) .le. 0 ) return 0;
    //    if ( consis ( rvorig, rvside, xorig, xside, orient ) .le. 0 ) return 0;
    //    if ( consis ( rvside, rvtop , xside, xtop , orient ) .le. 0 ) return 0;

    //   vectors are consistent.  compute directional derivatives
    //    forig = dirdrv ( lvorig, rvorig, rvorig, xorig )
    //    fside = dirdrv ( lvside, rvside, rvside, xside )
    //    ftop  = dirdrv ( lvtop , rvtop , rvtop , xtop  )

    /* As duas coisas anteriores podem ir juntas... consistency devolve um entero, 0 eh erro
       (pensar em que seja booleano)
        if ( consistency(rvtop , rvorig, forig) == 0 ) return 0;
        if ( consistency(rvorig, rvside, fside) == 0 ) return 0;
        if ( consistency(rvside, rvtop , ftop ) == 0 ) return 0;
    Temos alguma coisa como:
        int consistency(v1, v2, &f2) { }
    onde int eh igual a orientacao.
     */
    if (consistency(rvtop, rvorig, orient) == 0) return 0;
    forig = ((double) orient) * dd(i, j)[family];

    if (consistency(rvorig, rvside, orient) == 0) return 0;
    fside = ((double) orient) * dd(i + 1, j)[family];

    if (consistency(rvside, rvtop, orient) == 0) return 0;
    ftop = ((double) orient) * dd(i, j + 1)[family];

    if (orient == -1) return 0;

    foncub[1] = forig; // Was: foncub[0][1]
    foncub[0] = fside; // Was: foncub[0][0]
    foncub[3] = ftop; // Was: foncub[0][3]
    //    printf("Valores(%d,%d) = %f, %f, %f\n", i, j, forig, fside, ftop);

    //   lower half (type = 1) of square (type = 2) is complete
    if (is_square == 1) return 1;

    //   compute the vectors at the opposite corner (top triangle)
    //    if ( e(family)[i+1][j+1] == false ) return 0;
    if (!eig_is_real(i + 1, j + 1)[family]) return 0;
    rvopps[0] = e(i + 1, j + 1)[family].vrr[0];
    rvopps[1] = e(i + 1, j + 1)[family].vrr[1];

    //    if ( rlvect ( rvopps,  lvopps,  xopps,  family ) .eq. 0 ) return 0;

    //   make vectors consistent at pairs of corners (if possible)
    //    if ( consis ( rvtop,  rvopps, xtop,  xopps, orient ) .le. 0 ) return 0;
    //    if ( consis ( rvopps, rvside, xopps, xside, orient ) .le. 0 ) return 0;

    if (consistency(rvtop, rvopps, orient) == 0) return 0;
    fopps = ((double) orient) * dd(i + 1, j + 1)[family];
    if (consistency(rvopps, rvside, orient) == 0) return 0;

    if (orient == -1) return 0;

    //    if ( orient .eq. -1 ) return 0;

    //   vectors are consistent.  compute directional derivative
    //    fopps = dirdrv ( lvopps, rvopps, rvopps, xopps )

    // Ja aqui no final faz falta alguma coisa como
    foncub[2] = fopps; // Was: foncub[0][2]
    //    printf("Valores(%d,%d) = %f, %f, %f, %f\n", i, j, forig, fside, ftop, fopps);

    return 1;
}

int Inflection_Curve::consistency(double *v1, double *v2, int &orient) {

    // When v2 is in opposite direction, must be changed. Orientation is negative
    if ((v1[0] * v2[0] + v1[1] * v2[1]) < 0.0) {
        v2[0] = -v2[0];
        v2[1] = -v2[1];
        orient = -1;
    } else {
        orient = 1;
    }

    // The angle between vectors must be lower than 45degrees ( cos45 = 0.7071... )
    // We are assuming normal vectors.
    // TODO: O angulo de 45graus eh arbitrario

    // TODO: If commented, a point that goes to inf appears.

    if ((v1[0] * v2[0] + v1[1] * v2[1]) < 0.7071) return 0;

    return 1;
}

int Inflection_Curve::curve(int fam, std::vector<RealVector> &inflection_curve) {
    inflection_curve.clear();

    // family MUST be a member of Inflection_Curve
    family = fam;

    double rect[4];
    rect[0] = pmin.component(0);
    rect[1] = pmax.component(0);
    rect[2] = pmin.component(1);
    rect[3] = pmax.component(1);
   

    int info = ContourMethod::contour2d(this,(Boundary *) boundary, rect, number_of_cells, inflection_curve);



    return info;
}


// /////////////////////////////////////////////////////////////////////////////////////////
// Isto eh novo
// /////////////////////////////////////////////////////////////////////////////////////////


// Adapted from the FORTRAN original (at eigen.F, code provided by Dan).
//
// Adapted to the GENERALIZED case by Helmut.
//
// This function computes the directional derivatives for
// generalized problem.
//
//          n: Dimension of the space.
//          p: Point where the directional derivative is computed.
//
// The function returns the directional derivative at p, for
// a given flux and accumulation.
//
// Let l and r be the left- and right-eigenvectors of the GENERALIZED
// SYSTEM OF CONSERVATION LAWS. (notice the difference between this and
// the Stone case), at point p. Let lambda be the associated eigenvalue
// (All of them corresponding to the same family).
//
// Let B the Jacobian of the (NON-TRIVIAL) accumulation.
// Let A the Jacobian of the (NON-TRIVIAL) flux.
//
// Let H be the Hessian of said flux, and M the Hessian of the accumulation.
// Then the (GENERALIZED) directional derivative is (see Chapter
// Numerical Methods thesis Helmut):
//
//     dirdrv = ddot( l, H(r,r) - lambda M(r,r) )/ddot(l, Br).
//
// In particular, D = r^T*H is computed thus:
//
//     D[k] = r^T*H[k],     k = 0,..., (n - 1),
//
// where D[k] is the k-th row of n*n matrix D and H[k] is the
// k-th "matrix" of the Hessian.  (Where there are n Hessians.)
// i.e. it is the Hessian of the k-th component function.
// r^T denotes the transpose of r.
//
// In particular, E = r^T*M is computed thus:
//
//     E[k] = r^T*M[k],     k = 0,..., (n - 1),
//
// where E[k] is the k-th row of n*n matrix E and M[k] is the
// i-th "matrix" of the Hessian.  (Where there are n Hessians.)
//

  double Inflection_Curve::dirdrv(int n, const RealVector &p, int fam) {
    //    RealVector pp(n);
    //    for (int i = 0; i < n; i++) pp.component(i) = p[i];
    double point[n];
    for (int i = 0; i < n; i++) point[i] = p.component(i);

    return dirdrv(n, &point[0], fam);
}

double Inflection_Curve::dirdrv(int n, double *point, int fam) {
    double A[n][n];
    double B[n][n];

    double H[n][n][n];
    double M[n][n][n];
    fill_with_jet((RpFunction*) aa, n, point, 2, 0, &B[0][0], &M[0][0][0]);
    fill_with_jet((RpFunction*) ff, n, point, 2, 0, &A[0][0], &H[0][0][0]);

    // Extract the left and right eigenvalues of the generalized system.
    std::vector<eigenpair> e;
    int info = Eigen::eig(n, &A[0][0], &B[0][0], e);

    // Extract the indx-th left and right-eigenvector of the GENERALIZED
    // PROBLEM (A - lambda B)r=0  and  l(A - lambda B)=0

    double l[n], r[n];
    double norm = 0.0;
    double dirdrv = 0.0;

    for (int i = 0; i < n; i++) {
        l[i] = e[fam].vlr[i];
        r[i] = e[fam].vrr[i];
    }

    // Extract lambda.
    // The i-th eigenvalue must be real.
    // The eigenvalues must be chosen carefully in the n-dimensional case.
    // ALL eigenvalues must be real. Extend this by using a for cycle.
    //
    double lambda;

    if (e[fam].i != 0) {
        printf("Inside dirdrv(): Init step, eigenvalue %d is complex: % f %+f.\n", fam, e[fam].r, e[fam].i);
        return ABORTED_PROCEDURE;
    } else lambda = e[fam].r;

    double SubH[n][n];
    double SubM[n][n];

    // Nested loops, constructing SubH, SubM as H, M times r and the norm.
    for (int k = 0; k < n; k++) {
        for (int m = 0; m < n; m++) {
            SubH[k][m] = 0.0;
            SubM[k][m] = 0.0;
            norm += l[k] * B[k][m] * r[m];
            for (int i = 0; i < n; i++) {
                SubH[k][m] += H[k][m][i] * r[i];
                SubM[k][m] += M[k][m][n] * r[n];
            }
            // For trivial accumulation, the directional derivative may be simplified as
            //     dirdrv += l[k]*SubH[k][m]*r[m];
            dirdrv += l[k]*(SubH[k][m] * r[m] - lambda * SubM[k][m] * r[m]);
        }
    }

    return dirdrv / norm;
}

double Inflection_Curve::ddot(int n, double *x, double *y) {
    double p = 0.0;

    for (int i = 0; i < n; i++) p += x[i] * y[i];

    return p;
}

// C = A*B
// A = m times p
// B = p times n
// C = m times n

void Inflection_Curve::matrixmult(int m, int p, int n, double *A, double *B, double *C) {
    double sum;

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            sum = 0.0;
            for (int k = 0; k < p; k++) sum += A[i * p + k] * B[k * n + j];
            C[i * n + j] = sum;
        }
    }

    return;
}
