#include "Extension_Curve.h"

int Extension_Curve::function_on_vertices(double *foncub, int domain_i, int domain_j, int kl) {
    if (gv == 0 || oc == 0) return INVALID_FUNCTION_ON_VERTICES;

    if (!gv->eig_is_real(domain_i, domain_j)[family]) return INVALID_FUNCTION_ON_VERTICES;

    double lambda;


    if (gv->grid(0, 0).size() == 2) {
        if (characteristic_where == CHARACTERISTIC_ON_CURVE) {
            lambda = segment_lambda[kl];
        } else {
            lambda = gv->e(domain_i, domain_j)[family].r;
        }

        foncub[0] = lambda * (gv->G_on_grid(domain_i, domain_j).component(0) - segment_accum(0, kl))
                - (gv->F_on_grid(domain_i, domain_j).component(0) - segment_flux(0, kl));
        foncub[1] = lambda * (gv->G_on_grid(domain_i, domain_j).component(1) - segment_accum(1, kl))
                - (gv->F_on_grid(domain_i, domain_j).component(1) - segment_flux(1, kl));
    }

    else {
        double F10 = segment_flux(0, kl);
        double F20 = segment_flux(1, kl);
        double F30 = segment_flux(2, kl);

        double dG1 = gv->G_on_grid(domain_i, domain_j).component(0) - segment_accum(0, kl);
        double dG2 = gv->G_on_grid(domain_i, domain_j).component(1) - segment_accum(1, kl);
        double dG3 = gv->G_on_grid(domain_i, domain_j).component(2) - segment_accum(2, kl);

        double F1 = gv->F_on_grid(domain_i, domain_j).component(0);
        double F2 = gv->F_on_grid(domain_i, domain_j).component(1);
        double F3 = gv->F_on_grid(domain_i, domain_j).component(2);

        double X12 = F1 * dG2 - F2 * dG1;
        double X31 = F3 * dG1 - F1 * dG3;
        double X23 = F2 * dG3 - F3 * dG2;

        double X12_0 = F10 * dG2 - F20 * dG1;
        double X31_0 = F30 * dG1 - F10 * dG3;
        double X23_0 = F20 * dG3 - F30 * dG2;

        double Y21 = F2 * F10 - F1*F20;
        double Y13 = F1 * F30 - F3*F10;
        double Y32 = F3 * F20 - F2*F30;

        double red_shock_speed;
        double den = X12 * X12 + X31 * X31 + X23*X23;
        if ( fabs(den) < 1.0e-10 ) {
            den = X12_0 * X12_0 + X31_0 * X31_0 + X23_0 * X23_0;
           red_shock_speed = (Y21 * X12_0 + Y13 * X31_0 + Y32 * X23_0) / den;
          if ( fabs(den) < 1.0e-10) return INVALID_FUNCTION_ON_VERTICES;
        } else {

//        cout << "den: " << den << endl;
////
//        cout << "x12_0: " << X12_0 << "x31_0:" << X31_0 << "x23_0: " << X23_0 << endl;
        
//        cout << "x12: " << X12 << " x31_0:" << X31 << " x23: " << X23 << endl;
////
////
//        cout << "F10: " << F10<< " F20:" << F20 << " F30: " << F30 << endl;

        double scaling_factor = (X12_0 * X12 + X31_0 * X31 + X23_0 * X23) / den;

        red_shock_speed = (Y21 * X12 + Y13 * X31 + Y32 * X23) / den;
        }
        
//        cout<<"red : "<<red_shock_speed<<endl;


        if (characteristic_where == CHARACTERISTIC_ON_CURVE) {
            lambda = segment_lambda[kl];
//            cout << "Valor do lambda: " << lambda << endl;

        } else {
//                    lambda = scaling_factor * gv->e(domain_i, domain_j)[family].r;
            lambda = gv->e(domain_i, domain_j)[family].r;
//            cout << "Valor do lambda no else: " << lambda << " scaling factor: " << scaling_factor << endl;
        }
        
//        cout<<"Lambda: "<<lambda<<endl;

        foncub[0] = dG1 * (F2 * F30 - F3 * F20) - dG2 * (F1 * F30 - F3 * F10) + dG3 * (F1 * F20 - F2 * F10);
        foncub[1] = red_shock_speed - lambda;


    }

    return VALID_FUNCTION_ON_VERTICES;
}

bool Extension_Curve::valid_segment(int i) {
    if (oc == 0) return false;

    double epsilon = 1e-7;

    int dim = oc->at(i).size();


//    cout<<"Oc: "<<oc->at(i)<<endl;

    double F[dim], G[dim], JF[dim][dim], JG[dim][dim];

    std::vector<eigenpair> e;

    for (int j = 0; j < 2; j++) {
        ff->fill_with_jet(dim, oc->at(i + j).components(), 1, F, &JF[0][0], 0);
        aa->fill_with_jet(dim, oc->at(i + j).components(), 1, G, &JG[0][0], 0);

        e.clear();
        Eigen::eig(dim, &JF[0][0], &JG[0][0], e);


        if (characteristic_where == CHARACTERISTIC_ON_CURVE) {
            if (fabs(e[family].i) > epsilon) return false;
            segment_lambda.component(j) = e[family].r;
        }
        //        else {
        //
        //        }
        for (int k = 0; k < dim; k++) {
            segment_flux(k, j) = F[k];
            segment_accum(k, j) = G[k];
        }

    }

    return true;
}

void Extension_Curve::curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, int where_is_characteristic,
        bool is_singular, int fam,
        std::vector<RealVector> &original_curve,
        std::vector<RealVector> &extension_on_curve,
        std::vector<RealVector> &extension_on_domain) {

    ff = f;
    aa = a;

    family = fam;


    characteristic_where = where_is_characteristic;
    singular = is_singular;

    gv = &g;
    oc = &original_curve;

    gv->fill_eigenpairs_on_grid(ff, aa);

    extension_on_curve.clear();
    extension_on_domain.clear();

    Contour2p5_Method::contour2p5(this, extension_on_curve, extension_on_domain);

    return;
}
