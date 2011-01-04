/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HugoniotContourMethod.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ContourMethod.h"
//#include "TPCW.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



ContourMethod::ContourMethod(HugoniotFunctionClass *h) {
    hugoniot = h;
    //    double const_gravity = 9.8;
    //    double abs_perm = 3e-12;
    //    double phi = 0.38;
    //    bool has_gravity = false;
    //    double Tref_rock = 273.15;
    //    double Tref_water = 274.3775;
    //    double pressure = 100.9;
    //    double Cr = 2.029e6;
    //    double Cw = 4297.;
    //    double rhoW_init = 998.2;
    //    double T_typical = 304.63;
    //    double Rho_typical = 998.2; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
    //    double U_typical = 4.22e-6;
    //    double h_typical = Cw * (T_typical - Tref_water);
    //
    //    Thermodynamics_SuperCO2_WaterAdimensionalized TD(Tref_rock, Tref_water, pressure,
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmac_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmaw_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoac_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoaw_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoW_spline.txt",
    //            "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
    //            rhoW_init,
    //            Cr,
    //            Cw,
    //            T_typical,
    //            Rho_typical,
    //            h_typical,
    //            U_typical);
    //
    //    double cnw = 0., cng = 0., expw = 2., expg = 2.;
    //    FracFlow2PhasesHorizontalAdimensionalized fh(cnw, cng, expw, expg, &TD);
    //
    //    //    ReducedTPCWHugoniotFunctionClass tpcwhc(Uref, abs_perm, phi, const_gravity, &TD, &fh);
    //
    //    hugoniot = new ReducedTPCWHugoniotFunctionClass(Uref, abs_perm, phi, const_gravity, &TD, &fh);
    //
    //    int zero;
    //    int nedg;

    //    int tsimp, tface, ssimp, sface;

    //    int ncubes, first, last, k;
    //    double u, v;
    //    double p[2];
    //    int type;
    //    int half = 1;
    //    int whole = 2;

    //    int hn = 2;
    //    int hm = 1;
    //    int nsface_, nface_, nsoln_, nedges_;
    //    int dims_ = 50;
    //    int dime_ = 60;

    //    double refval;

    //    int ncvert_ = 4;
    //    int nsimp_ = 2;

    //    int numberOfCombinations = hc.combination(hn + 1, hm + 1);

    //    // Reserve arrays
    //    storn_  = new int[hn + 1];
    //    storm_  = new int[hm + 1];
    //    cvert_  = new double[ncvert_][hn];
    //    vert    = new double[ncvert_][hn];
    //    bsvert_ = new int[hn + 1][hn];
    //    perm_   = new int[hn][nsimp_];
    //    comb_   = new int[numberOfCombinations][hm + 1];

    //    // Initialize arrays dimensions
    //    nsface_ = hc.mkcomb(&comb_[0][0], hn + 1, hm + 1);

    //    fnbr_   = new int[nsface_][nsface_];

    //    int dimf_ = 5;

    //    nsoln_ = -1;

    //    sol_    = new double[hn][dims_];
    //    solptr_ = new int[nsimp_][nsface_];

    //    for (i = 0; i < nsimp_; i++) {
    //        for (j = 0; j < nsface_; j++) solptr_[i][j] = 0;
    //    } //TODO: Revisar como "solptr" eh modificada, os numero sao muito estranhos

    //    edges_ = new int[2][dime_];
    //    for (int i = 0; i < 2; i++) {
    //        for (int j = 0; j < dime_; j++) edges_[i][j] = -6;
    //    } //TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    //    smpedg_   = new int[nsimp_][2];
    //    for (int i = 0; i < nsimp_; i++) {
    //        for (int j = 0; j < 2; j++) smpedg_[i][j] = 0;
    //    } //TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    //    //inicializing another arrays, it were globally defined in java
    //    facptr_ = new int[nsimp_][nsface_];
    //    face_   = new int[hm + 1][dimf_];

    //    int dblev = 3;
    //    hc.mkcube(&cvert_[0][0], &bsvert_[0][0], &perm_[0][0], ncvert_, nsimp_, hn);
    //    nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
    //                           &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

    //    exstfc = new int[nface_];
    //    for (i = 0; i < nface_; i++) exstfc[i] = 1;
    //    sptr_ = new int[nface_];

    //    tsimp = 1;
    //    tface = 3;

    // End of initialization
}

ContourMethod::~ContourMethod() {
    //    delete [] sptr_;
    //    delete [] exstfc;
    //    delete [] face_;
    //    delete [] facptr_;
    //    delete [] smpedg_;
    //    delete [] edges_;
    //    delete [] solptr_;
    //    delete [] sol_;
    //    delete [] fnbr_;
    //    delete [] comb_;
    //    delete [] perm_;
    //    delete [] bsvert_;
    //    delete [] vert;
    //    delete [] cvert_;
    //    delete [] storm_;
    //    delete [] storn_;
}

//double ContourMethod::f(double x, double y) {
////    return 3.0*x-y-1.2;//pow(x - 0.3, 2.0) + pow(y - 0.7, 2.0) - 0.01;

//    // return pow(x - 0.313, 2.0) + pow(y - 0.724, 2.0) - 0.017;
//    return pow(x*x + y*y, 2.0) - 2.0*(x*x - y*y);
//    //    return 1 * (x - 0.1) + 2 * (y - 0.2);
//}

int ContourMethod::inpdom(double *u) { // double u[2]//Replace by Boundary::inside
    //    if (u[0] >= 0 && u[1] >= 0 && u[0] + u[1] <= 1) return 1;
    //    else return 0;

    return 1;
}

//int ContourMethod::curv2d(/*double *segend,*/ int sn, int seglim, double fdummy, double *rect, int *res, int ifirst) {

 int ContourMethod::curv2d(/*double *segend,*/ int sn, int seglim, double fdummy, double *rect, int *res, int ifirst,
        std::vector<RealVector> &vrs) {
    vrs.clear();
      for (unsigned int i = 0; i < 2; i++) {
        cout << "Valor de res:" << res[i] << endl;
      }

    cout<<"---------------"<<endl;


    for (unsigned int i = 0; i < 4; i++) {
        cout <<"Valor de rect:"<<rect[i]<<endl;
    }


    double segend[seglim][2][2]; //int sn, int seglim, double f, double rect[4], int res[2], int ifirst;

    int zero;
    int nedg;

    int tsimp, tface, ssimp, sface;

    int ncubes, first, last, k;
    double u, v;
    double p[2];
    int type;
    int half = 1;
    int whole = 2;

    int hn = 2;
    int hm = 1;
    int nsface_, nface_, nsoln_, nedges_=0;
    int dims_ = 50;
    int dime_ = 600;

    double refval;
    int i, j;

    int ncvert_ = 4;
    int nsimp_ = 2;

    int numberOfCombinations = hc.combination(hn + 1, hm + 1);

    //inicializing arrays
    int storn_[hn + 1];
    int storm_[hm + 1];
    double cvert_[ncvert_][hn];
    double vert[ncvert_][hn];
    int bsvert_[hn + 1][hn];
    int perm_[hn][nsimp_];
    int comb_[numberOfCombinations][hm + 1];

    //inicializing arrays dimensions
    nsface_ = hc.mkcomb(&comb_[0][0], hn + 1, hm + 1);

    int fnbr_[nsface_][nsface_];

    int dimf_ = 5;

    nsoln_ = -1;
    double sol_[hn][dims_];
    int solptr_[nsimp_][nsface_];
    for (i = 0; i < nsimp_; i++) {
        for (j = 0; j < nsface_; j++) solptr_[i][j] = 0;
    } //TODO: Revisar como "solptr" eh modificada, os numero sao muito estranhos

    int edges_[2][dime_];
    for (i = 0; i < 2; i++) {
        for (j = 0; j < dime_; j++) edges_[i][j] = -6;
    } //TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    int smpedg_[nsimp_][2];
    for (i = 0; i < nsimp_; i++) {
        for (j = 0; j < 2; j++) smpedg_[i][j] = 0;
    } //TODO: Ver o que acontece, pois se nao sao inicializadas coloca valores estranhos

    //inicializing another arrays, it were globally defined in java
    int facptr_[nsimp_][nsface_];
    int face_[hm + 1][dimf_];

    int dblev = 3;
    if (ifirst != 0) {
        hc.mkcube(&cvert_[0][0], &bsvert_[0][0], &perm_[0][0], ncvert_, nsimp_, hn);
        nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
                &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

    }

    int exstfc[nface_];
    for (i = 0; i < nface_; i++) exstfc[i] = 1;
    int sptr_[nface_];

    tsimp = 1;
    tface = 3;

    // End of initialization

    // Curve2D proper
    double foncub[hm][ncvert_];

    // Work area
    double g[hm][hm + 1];
    double gx[hm];
    double x[hm];
    int wrki[hm];

    // set the rectangle sizes and resolutions
    double u0 = rect[0];
    double u1 = rect[1];
    double v0 = rect[2];
    double v1 = rect[3];
    int nu = res[0];
    int nv = res[1];
    double du = (u1 - u0) / nu;
    double dv = (v1 - v0) / nv;

    ncubes = nu * nv;

    first = 1;
    last = ncubes;

    // initialize number of segments found
    sn = 0;

    //int i, j, k;

    // loop over the rectangles
    for (k = first; k <= last; k++) {
        j = k % nv;
        if (j == 0) {
            j = nv;
            i = k / nv;
        } else {
            i = (k / nv) + 1;
        }
        u = u0 + (i - 1) * du;
        vert[0][0] = u + du;
        vert[1][0] = u;
        vert[2][0] = u + du;
        vert[3][0] = u;

        v = v0 + (j - 1) * dv;

        vert[0][1] = v;
        vert[1][1] = v;
        vert[2][1] = v + dv;
        vert[3][1] = v + dv;

        // check whether all, half, or none of the square is inside

        //lower right point
        p[0] = u + du;
        p[1] = v;

        // a funcao inpdom foi criada a partir do arquivo bndry.F (localizada em phys/stone) do fortran!
        //if (inpdom(&p[0]) == 0) goto lab200;
        if (inpdom(&p[0]) != 0) {
            // upper left point
            p[0] = u;
            p[1] = v + dv;
            //if (inpdom(&p[0]) == 0) goto lab200;
            if (inpdom(&p[0]) != 0) {

                /* TODO: this works provided that the lower left corner is inside
                   when both the upper left and lower right corners are inside.
                   (e.g., rectangles oriented with the axes.)
                   upper right point */
                p[0] = u + du;
                p[1] = v + dv;

                if (inpdom(&p[0]) == 1) {
                    type = whole;


                    ssimp = nsimp_;
                    sface = nface_;
                } else {

                    type = half;
                    ssimp = tsimp;
                    sface = tface;
                }

                //set component 1
                //        hc.putmf("VERT", &vert[0][0], ncvert_, n_);
                zero = 0; // isso vai substituir o usso de uma variavel logica (false)

                //TODO: lembrar descometar

                //foncub[0][0] = f(vert[0][0], vert[0][1]);
                RealVector u(2);
                u(0) = vert[0][0];
                u(1) = vert[0][1];

                foncub[0][0] = hugoniot->HugoniotFunction(u);
                //foncub[0][0] = f(vert[0][0], vert [0][1]);

                refval = foncub[0][0];
                for (int l = 1; l < 4; l++) {
                    if (l == 2 && type == half) goto lab90;
                    u(0) = vert[l][0];
                    u(1) = vert[l][1];

                    foncub[0][l] = hugoniot->HugoniotFunction(u);
                    //foncub[0][l] = f(vert[l][0], vert [l][1]);


                    if (refval * foncub[0][l] < 0.0) zero = 1;
lab90:
                    ;
                }

                //        hc.putmf("FONCUB", &foncub[0][0], 4, 1);
                //if (zero == 0) goto lab200;
                if (zero != 0) {

                    //  solve for the current cube

                    // debug

                    //        if (dblev == 3) {
                    //            printf("CALLING CUBSOL WITH I, J=%d\n");
                    //        }
                    //DEBUG
                    //        status = hc.cubsol(&solptr_[0][0], &sol_[0][0], dims_, &sptr_[0], nsoln_,
                    //                &foncub[0][0], &vert[0][0], &exstfc[0], &face_[0][0], &facptr_[0][0],
                    //                hn, hm, ssimp, nsface_, sface, &storm_[0],
                    //                &storm_[0], &storm_[0]);
                    //

                    double u[hn][hm + 1]; //TODO Remove
                    double g[hm][hm + 1];
                    double stormd[hm];
                    //        cout << "Valor de exstfc" << endl;
                    //        for (i = 0; i < nface_; i++) cout << exstfc[i] << " " << endl;

                    nsoln_ = hc.cubsol(&solptr_[0][0], &sol_[0][0], dims_, &sptr_[0], nsoln_, &foncub[0][0], &exstfc[0],
                            &face_[0][0], &facptr_[0][0], dimf_, &vert[0][0], ncvert_, hn, hm, ssimp, nsface_, sface, &u[0][0],
                            &g[0][0], &stormd[0], &storm_[0]);

                    //        hc.putmi("SOLPTR", &solptr_[0][0], nsimp_, nsface_);


                    // TODO: ver o numero certo de variavei de entrada e os tracinhos.

                    // debug
                    //        if ((dblev == 3) && (nsoln_ > 0)) {
                    //        hc.putmf("FONCUB", &foncub[0][0], ncvert_, m_);
                    //        hc.putmf("SOL", &sol_[0][0], n_, dims_);
                    //        hc.putmi("SPTR", &sptr_[0], nface_, 1);
                    //        hc.putmi("SOLPTR", &solptr_[0][0], nsimp_, nsface_);
                    //        //        }

                    // IMPROVE THE SOLUTION USING A ZERO-FINDER.

                    //        imprv(f, &sol_[0][0], dims_, sface, &sptr_[0], &face_[0][0], &vert[0][0]);
                    // (TODO: VER AS ENTRADAS CERTAS)

                    //MAKE THE LIST OF EDGE POINTERS

                    nedges_ = hc.mkedge(&edges_[0][0], dime_, &smpedg_[0][0], &solptr_[0][0], &fnbr_[0][0], nsimp_, nsface_);

                    //        printf("Estamos aqui\n");
                    //        cout << "Valor de nedges " << nedges_ << endl;

                    // debug
                    //        if ((dblev == 3) && (nsoln_ > 0)) {
                    //            hc.putmi("EDGES", &edges_[0][0], 2, nedges_);
                    //            hc.putmi("SMPEDG", &smpedg_[0][0], nsimp_, 2);
                    //        }

                    //STORE ALL PAIRS OF EDGES THAT WERE FOUND
                    //        cout << "Aqui sn= "<<sn<<endl;

                    if (nedges_ > 0) {
                        cout << "Depois  nedges_"<< nedges_ << endl;
//                        if (nedges_ >2 ) return 0;

                        for (nedg = 0; nedg < nedges_; nedg++) {
                            sn++;
                            cout << "Depois  nedg" << nedg << endl;
                            segend[sn - 1][0][0] = sol_[0][edges_[0][nedg ]]; // X1
                            segend[sn - 1][0][1] = sol_[1][edges_[0][nedg ]]; // Y1
                            segend[sn - 1][1][0] = sol_[0][edges_[1][nedg ]]; // X2
                            segend[sn - 1][1][1] = sol_[1][edges_[1][nedg ]]; // Y2

                            //                cout << segend[sn - 1][0][0] << "    " << segend[sn - 1][0][1]<<";" << endl;

                            //                cout << segend[sn - 1][1][0] << "     " << segend[sn - 1][1][1] <<";"<< endl;

                            //                cout <<endl;

                            // Store the segments
                            RealVector p1(2), p2(2);
                            p1.component(0) = segend[sn - 1][0][0]; //sol_[0][edges_[0][nedg ]];
                            p1.component(1) = segend[sn - 1][0][1]; //sol_[1][edges_[0][nedg ]];

                            p2.component(0) = segend[sn - 1][1][0]; //sol_[0][edges_[1][nedg ]];
                            p2.component(1) = segend[sn - 1][1][1]; //sol_[1][edges_[1][nedg ]];

                            vrs.push_back(p1);
                            vrs.push_back(p2);



                            if (sn >= seglim) {
                                return -1;
                            }
                        }

                        //            cout << "Valor de sn: " << sn << endl;
                    }


                    //lab200:;
                }
            }
        }


    }
    return 0;
}


