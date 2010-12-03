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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




double ContourMethod::f(double x, double y) {
//    return 3.0*x-y-1.2;//pow(x - 0.3, 2.0) + pow(y - 0.7, 2.0) - 0.01;

  return pow(x - 0.313, 2.0) + pow(y - 0.724, 2.0) - 0.017;
    //    return 1 * (x - 0.1) + 2 * (y - 0.2);
}

int ContourMethod::inpdom(double *u) { // double u[2]//Replace by Boundary::inside
    //    if (u[0] >= 0 && u[1] >= 0 && u[0] + u[1] <= 1) return 1;
    //    else return 0;

    return 1;
}

int ContourMethod::curv2d(/*double *segend,*/ int sn, int seglim, double fdummy, double *rect, int *res, int ifirst) {
    double segend[seglim][2][2]; //int sn, int seglim, double f, double rect[4], int res[2], int ifirst;

    int n_ = 2;
    int m_ = 1;
    int nsface_, nface_, nsoln_, nedges_;
    int dims_ = 50;
    int dime_ = 60;

    double refval;
    int i, j;


    // initialize


    //inicializing variables

    int ncvert_ = 4;
    int nsimp_ = 2;

    //    int dimf_ = dimFACE(n_, m_); // dimFACE(n_); // TODO Falta configurar dimFACE
    int numberOfCombinations = hc.combination(n_ + 1, m_ + 1);

    //inicializing arrays
    int storn_[n_ + 1];
    int storm_[m_ + 1];
    double cvert_[ncvert_][n_];
    double vert[ncvert_][n_];
    int bsvert_[n_ + 1][n_];
    int perm_[n_][nsimp_];
    int comb_[numberOfCombinations][m_ + 1];

    //inicializing arrays dimensions
    nsface_ = hc.mkcomb(&comb_[0][0], n_ + 1, m_ + 1);


    int fnbr_[nsface_][nsface_];

    int dimf_ = 5;

    //inicializing another arrays, it were globally defined in java
    int facptr_[nsimp_][nsface_];
    int face_[m_ + 1][dimf_];

    hc.mkcube(&cvert_[0][0], &bsvert_[0][0], &perm_[0][0], ncvert_, nsimp_, n_);

//    hc.putmf("CVERT", &cvert_[0][0], ncvert_, n_);
//    hc.putmi("BSVERT", &bsvert_[0][0], n_ + 1, n_);
//    hc.putmi("PERM", &perm_[0][0], n_, nsimp_);


    nface_ = hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, n_, m_, nsface_,
            &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);

//    cout << "Valor de nface " << nface_ << endl;



//    hc.putmi("COMB", &comb_[0][0], numberOfCombinations, m_ + 1);
//    hc.putmi("FACE", &face_[0][0], m_ + 1, dimf_);
//    hc.putmi("FACPTR", &facptr_[0][0], nsimp_, nsface_);
//    hc.putmi("FNBR", &fnbr_[0][0], nsface_, nsface_);

    int ptrf_[nface_][2];
    int ptfatt_[nface_][2];

    int fdim_, faceptInd_;
    hc.findInds(&faceptInd_, &fdim_, n_, m_, &cvert_[0][0], &face_[0][0], dimf_, nface_, nsimp_, nsface_, &facptr_[0][0]);
    int facatt_[fdim_][2];
    int facept_[faceptInd_][2];

    hc.mkflst(&facept_[0][0], &ptrf_[0][0], &facatt_[0][0], &ptfatt_[0][0],
            dimf_, fdim_, n_, m_, nface_, nsface_, nsimp_, ncvert_, faceptInd_,
            &cvert_[0][0], &face_[0][0], &facptr_[0][0], &storm_[0]);

    nsoln_ = -1;
    double sol_[n_][dims_];
    int sptr_[nface_];
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

    //    double u[n_][m_ + 1];
    double g[m_][m_ + 1];
    double gx[m_];
    double x[m_];
    int wrki[m_];
    int exstfc[nface_];
    for (i = 0; i < nface_; i++) exstfc[i] = 1;
    double foncub[ncvert_][m_];




//    printf("HERE\n");




    int zero;
    //double foncub[ncvert_][m_], int ncvert_, int exstfc[nface],
    //bool zero; //TODO: Ver se o analogo do "logical" de Fortran eh este
    int nedg;

    // DEBUG



    int tsimp, tface, ssimp, sface;

    int ncubes, first, last, k;
    double u, v;
    double p[2];
    int type;
    int half = 1;
    int whole = 2;
    // TODO: ver half, whole linha 59 de fortran

    // TODO: No fortran isto eh feito para debugar, depois vemos os valores certos
    int hn = 2;
    int hm = 1;
    int dblev = 3;
    if (ifirst != 0) {
        hc.mkcube(&cvert_[0][0], &bsvert_[0][0], &perm_[0][0], ncvert_, nsimp_, hn);

//        if (dblev == 3) {
//            hc.putmf("CVERT", &cvert_[0][0], hn, ncvert_);
//            hc.putmi("BSVERT", &bsvert_[0][0], hn, hn + 1);
//            hc.putmi("PERM", &perm_[0][0], hn, nsimp_);
//        }

        hc.mkface(&face_[0][0], &facptr_[0][0], &fnbr_[0][0], dimf_, nsimp_, hn, hm, nsface_,
                &bsvert_[0][0], &comb_[0][0], &perm_[0][0], &storn_[0], &storm_[0]);
//
//        if (dblev == 3) {
//            hc.putmi("COMB", &comb_[0][0], hm + 1, nsface_);
//            hc.putmi("FACE", &face_[0][0], hm + 1, nface_);
//            hc.putmi("FACPTR", &facptr_[0][0], nsface_, nsimp_);
//            hc.putmi("FNBR", &fnbr_[0][0], nsface_, nsface_);
//        }
    }

    tsimp = 1;
    tface = 3;

    // initialize all faces so that they are considered by cubsol

    for (int l = 0; l < nface_; l++) {
        exstfc[l] = 1;
    }

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
        if (inpdom(&p[0]) == 0) goto lab200;
        // upper left point
        p[0] = u;
        p[1] = v + dv;
        if (inpdom(&p[0]) == 0) goto lab200;

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

        foncub[0][0] = f(vert[0][0], vert[0][1]);
        refval = foncub[0][0];
        for (int l = 1; l < 4; l++) {
            if (l == 2 && type == half) goto lab90;
//            foncub[l][0] = f(vert[l][0], vert [l][1]);
            foncub[0][l] = f(vert[l][0], vert [l][1]);
            if (refval * foncub[l][0] < 0.0) zero = 1;
lab90:
            ;
        }

//        hc.putmf("FONCUB", &foncub[0][0], 4, 1);
        if (zero == 0) goto lab200;

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
        double g[hm][hm+1];
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

//        cout << "Valor de inteiros " << dime_ << " " << nedges_ << " " << nsimp_ << "  " << nsface_ << endl;



        nedges_ = hc.mkedge(&edges_[0][0], dime_, nedges_, &smpedg_[0][0], &solptr_[0][0], &fnbr_[0][0], nsimp_, nsface_);

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
            for (nedg = 0; nedg < nedges_; nedg++) {
                sn += 1;
                // TODO: tal vez seja necesario trocar a ordem dos indices.
//                segend[sn - 1][0][0] = sol_[0][edges_[nedg ][0]]; // falta passar para forma vetorial
//                segend[sn - 1][0][1] = sol_[1][edges_[nedg ][0]]; // falta passar para forma vetorial
//                segend[sn - 1][1][0] = sol_[0][edges_[nedg ][1]]; // falta passar para forma vetorial
//                segend[sn - 1][1][1] = sol_[1][edges_[nedg ][1]]; // falta passar para forma vetorial
//



                segend[sn - 1][0][0] = sol_[0][edges_[0][nedg ]]; // X1
                segend[sn - 1][0][1] = sol_[1][edges_[0][nedg ]]; // Y1
                segend[sn - 1][1][0] = sol_[0][edges_[1][nedg ]]; // X2
                segend[sn - 1][1][1] = sol_[1][edges_[1][nedg ]]; // Y2












//                printf("Agora aqui");
                cout << segend[sn - 1][0][0] << "    " << segend[sn - 1][0][1]<<";" << endl;

                cout << segend[sn - 1][1][0] << "     " << segend[sn - 1][1][1] <<";"<< endl;

                cout <<endl;

                if (sn >= seglim) {
                    return -1;
                }
            }

//            cout << "Valor de sn: " << sn << endl;
        }

lab200:
        ;

    }
    return 0;
}


