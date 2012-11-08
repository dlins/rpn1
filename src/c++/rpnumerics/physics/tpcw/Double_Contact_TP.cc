#include "Double_Contact_TP.h"

bool Double_Contact_TP::function_on_cell(double *val, int ir, int jr, int kl, int kr) {
    int domain_i, domain_j;

    if (kr == 0) {
        domain_i = ir;
        domain_j = jr;
    } else if (kr == 1) {
        domain_i = ir + 1;
        domain_j = jr;
    } else if (kr == 2) {
        domain_i = ir + 1;
        domain_j = jr + 1;
    } else if (kr == 3) {
        domain_i = ir;
        domain_j = jr + 1;
    }

    if (!gv_right->eig_is_real(domain_i, domain_j)[right_family]) return false;

    double lr = gv_right->e(domain_i, domain_j)[right_family].r;

    double Fr[3], Gr[3];
    for (int k = 0; k < 3; k++) {
        Fr[k] = gv_right->F_on_grid(domain_i, domain_j).component(k);
        Gr[k] = gv_right->G_on_grid(domain_i, domain_j).component(k);
    }


    /* * */

    // LAMBDA ON THE RIGHT
    //    double lr;


    //    double Gr[3], Fr[3];

    //    if (kr == 0) {
    //        lr = righte(ir, jr)[right_family];
    //        for (int k = 0; k < 3; k++) {
    //            Gr[k] = rightaav(ir, jr).component(k); // hur = hua(ir,jr);
    //            Fr[k] = rightffv(ir, jr).component(k); // fr  = fa(ir,jr);
    //        }
    //    } else if (kr == 1) {
    //        lr = righte(ir + 1, jr)[right_family];
    //        for (int k = 0; k < 3; k++) {
    //            Gr[k] = rightaav(ir + 1, jr).component(k); // hur = hua(ir + 1,jr);
    //            Fr[k] = rightffv(ir + 1, jr).component(k); // fr  = fa(ir + 1,jr);
    //        }
    //    } else if (kr == 2) {
    //        lr = righte(ir + 1, jr + 1)[right_family];
    //        for (int k = 0; k < 3; k++) {
    //            Gr[k] = rightaav(ir + 1, jr + 1).component(k); // hur = hua(ir + 1, jr + 1);
    //            Fr[k] = rightffv(ir + 1, jr + 1).component(k); // fr  = fa(ir + 1, jr + 1);
    //        }
    //    } else if (kr == 3) {
    //        lr = righte(ir, jr + 1)[right_family];
    //        for (int k = 0; k < 3; k++) {
    //            Gr[k] = rightaav(ir, jr + 1).component(k); // hur = hua(ir, jr + 1);
    //            Fr[k] = rightffv(ir, jr + 1).component(k); // fr  = fa(ir, jr + 1);
    //        }
    //    }

    double Hmatrix[3][3];
    double Gq[3];

    for (int i = 0; i < 3; i++) {
        Gq[i] = Gr[i] - accum_left(i, kl);
        Hmatrix[i][0] = Gq[i]; // bJetMatrix(i) - bref_G[i]; // b=G
        Hmatrix[i][1] = -Fr[i]; // a=F
        Hmatrix[i][2] = flux_left(i, kl);
    }


    //det(3, &Hmatrix[0][0]); // TODO: PRECISAMOS DO METODO DETERMINANTE.


    double X12 = Fr[0] * Gq[1] - Fr[1] * Gq[0];
    double X31 = Fr[2] * Gq[0] - Fr[0] * Gq[2];
    double X23 = Fr[1] * Gq[2] - Fr[2] * Gq[1];

    double X12_0 = flux_left(0, kl) * Gq[1] - flux_left(1, kl) * Gq[0];
    double X31_0 = flux_left(2, kl) * Gq[0] - flux_left(0, kl) * Gq[2];
    double X23_0 = flux_left(1, kl) * Gq[2] - flux_left(2, kl) * Gq[1];

    double Y21 = Fr[1] * flux_left(0, kl) - Fr[0] * flux_left(1, kl);
    double Y13 = Fr[0] * flux_left(2, kl) - Fr[2] * flux_left(0, kl);
    double Y32 = Fr[2] * flux_left(1, kl) - Fr[1] * flux_left(2, kl);

    double den = X12 * X12 + X31 * X31 + X23 * X23;

    if (fabs(den) < 1e-8) {


        return false;
    }


    double scaling_factor = (X12_0 * X12 + X31_0 * X31 + X23_0 * X23) / den;

    double red_shock_speed = (Y21 * X12 + Y13 * X31 + Y32 * X23) / den;



        double lambda_right = scaling_factor * lr;


//    double lambda_right = lr;



    //    cout<<"den: "<<den<<endl;

    // Output
    val[0] = Hmatrix[0][0] * (Hmatrix[1][1] * Hmatrix[2][2] - Hmatrix[1][2] * Hmatrix[2][1])
            + Hmatrix[0][1] * (Hmatrix[1][2] * Hmatrix[2][0] - Hmatrix[1][0] * Hmatrix[2][2])
            + Hmatrix[0][2] * (Hmatrix[1][0] * Hmatrix[2][1] - Hmatrix[1][1] * Hmatrix[2][0]);
    val[1] = (red_shock_speed - lambda_left[kl]);
    val[2] = (red_shock_speed - lambda_right);

    //    cout<<"val: "<<val[0]<<" "<<val[1]<<" "<<val[2]<<endl;
    /* * */

    return true;
}

// Originally this function was: preplftc, and is defined in: locimp.F.
//
// ALWAYS: flux and accum are 2x4 matrices.
//         lambda is a vector with 4 elements.
//
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

bool Double_Contact_TP::prepare_cell(int i, int j) {
    int domain_i, domain_j;

    for (int kr = 0; kr < 4; kr++) {
        if (kr == 0) {
            domain_i = i;
            domain_j = j;
        } else if (kr == 1) {
            domain_i = i + 1;
            domain_j = j;
        } else if (kr == 2) {
            domain_i = i + 1;
            domain_j = j + 1;
        } else if (kr == 3) {
            domain_i = i;
            domain_j = j + 1;
        }

        if (!gv_left->eig_is_real(domain_i, domain_j)[left_family]) return false;
        lambda_left[kr] = gv_left->e(domain_i, domain_j)[left_family].r;

        for (int k = 0; k < 3; k++) {
            flux_left(k, kr) = gv_left->F_on_grid(domain_i, domain_j).component(k);
            accum_left(k, kr) = gv_left->G_on_grid(domain_i, domain_j).component(k);
        }
    }

    /* * */
    //    // HERE WE ARE FILLING THE LEFT ACCUMULATION AND FLUX.
    //    double Gl[3], Fl[3];

    //    for (int k = 0; k < 3; k++) {
    //        Gl[k] = accum_left_input(k, kl);
    //        Fl[k] = flux_left_input(k, kl);
    //    }
    /* * */

    return true;
}

void Double_Contact_TP::curve(const FluxFunction *lf, const AccumulationFunction *la, GridValues *lg, int lfam,
        const FluxFunction *rf, const AccumulationFunction *ra, GridValues *rg, int rfam,
        std::vector<RealVector> &left_curve, std::vector<RealVector> &right_curve) {



    cout << "Flux TP: " << lf << endl;
    cout << "Accum TP: " << la << endl;



    cout << "Flux TP: " << rf << endl;
    cout << "Accum TP: " << ra << endl;



    cout << "Grid r" << rg << endl;
    cout << "Grid l" << lg << endl;

    cout << "Fam l" << lfam << endl;
    cout << "Fam r" << rfam << endl;


    lff = lf;
    laa = la;
    gv_left = lg;
    left_family = lfam;

    rff = rf;
    raa = ra;
    gv_right = rg;
    right_family = rfam;

    // It is assumed that the grid_value must be the same
    //    singular = ( (left_family == right_family) && (lg == rg) );
    //    singular = ( (left_family == right_family) );
    singular = true;

    gv_left->fill_eigenpairs_on_grid(lff, laa);
    gv_right->fill_eigenpairs_on_grid(rff, raa);

    left_curve.clear();
    right_curve.clear();

    Contour2x2_Method::curve2x2(this, left_curve, right_curve);


    return;
}

