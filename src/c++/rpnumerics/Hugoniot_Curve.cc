#include "Hugoniot_Curve.h"

Hugoniot_Curve::Hugoniot_Curve(const FluxFunction * flux, const AccumulationFunction * accum)
:  ff(flux), aa(accum) {


}



// This is the classified Hugoniot given by segments
//

int Hugoniot_Curve::classified_curve(GridValues & grid, ReferencePoint & refPoint,std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList) {

    grid.fill_functions_on_grid(ff, aa);
    
    gv=&grid;
    Fref = refPoint.F;
    Gref = refPoint.G;

    JFref = refPoint.JF;
    JGref = refPoint.JG;

    hugoniot_curve.clear();

    std::vector<RealVector> vrs;

    // Notice that method splitts which curve is filled
    int info = ContourMethod::contour2d(this, vrs);

    ColorCurve colorCurve(*ff, *aa);

    colorCurve.classify_segmented_curve(vrs, refPoint,
            hugoniot_curve, transitionList);

    return info;
}

Hugoniot_Curve::~Hugoniot_Curve() {
}

/**
 *  Inside the calculus of the RH condition, when the differences of the accumulation are small, it is
 *  better to approximate the flux differences by the accumulation. Notice that epsilon must be small,
 *  however large enough!
 *     The approximation is done in order to change [ df1 = f1(i,j) - f1(Uref) ] with the aid of the 
 *  approximation :
 *                     dF         dF       dU         dF      [ dG ]^(-1)
 *                    ----   =   ----  *  ----   =   ----  *  [----]
 *                     dG         dU       dG         dU      [ dU ]
 *  Here the inverse matrix [dG/dU]^(-1) is calculated in the standard form insde the [else] part.
 **/

int Hugoniot_Curve::function_on_square(double *foncub, int i, int j) {
    int is_square = gv->cell_type(i, j);
    double dg1, dg2;
    double f_aux[4];
    double epsilon = 1.0e-5;

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            dg1 = gv->G_on_grid(i + l, j + k).component(0) - Gref.component(0);
            dg2 = gv->G_on_grid(i + l, j + k).component(1) - Gref.component(1);

            if (fabs(dg1) + fabs(dg2) >= epsilon) {
                double df1 = gv->F_on_grid(i + l, j + k).component(0) - Fref.component(0);
                double df2 = gv->F_on_grid(i + l, j + k).component(1) - Fref.component(1);

                f_aux[l * 2 + k] = dg2 * df1 - dg1*df2;
            } else {
                // First-order expansion of F in terms of G.
                //

                double inv_det = 1.0 / (JGref(0) * JGref(3) - JGref(1) * JGref(2));

                f_aux[l * 2 + k] = ((JFref(0) * JGref(3) - JFref(2) * JGref(1) + JFref(1) * JGref(2) - JFref(3) * JGref(0)) * dg1 * dg2 +
                        (JFref(1) * JGref(3) - JFref(3) * JGref(1)) * dg2 * dg2 +
                        (JFref(0) * JGref(2) - JFref(2) * JGref(0)) * dg1 * dg1) * inv_det;
            }
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]

    // Only useful if the cell is a square.
    //
    if (is_square == CELL_IS_SQUARE) foncub[2] = f_aux[3]; // Was: foncub[0][2]

    return 1;
}

void Hugoniot_Curve::map(const RealVector &r, double &f, RealVector &map_Jacobian) {
    int n = r.size();
    map_Jacobian.resize(n);

    double F[n], JF[n][n], G[n], JG[n][n];

    RealVector p(r);

    ff->fill_with_jet(n, p.components(), 1, F, &JF[0][0], 0);
    aa->fill_with_jet(n, p.components(), 1, G, &JG[0][0], 0);

    double dg1 = G[0] - Gref[0];
    double dg2 = G[1] - Gref[1];

    // TODO: The following commented lines can be uncommented in case of the Newton_Improvement do
    //       a division by zero!
    //    double epsilon = 1.0e-3;
    //    if (fabs(dg1) + fabs(dg2) >= epsilon){
    double df1 = F[0] - Fref[0];
    double df2 = F[1] - Fref[1];

    f = dg2 * df1 - dg1*df2;

    map_Jacobian.component(0) = JF[0][0] * dg2 + JG[1][0] * df1 - JF[1][0] * dg1 - JG[0][0] * df2;
    map_Jacobian.component(1) = JF[0][1] * dg2 + JG[1][1] * df1 - JF[1][1] * dg1 - JG[0][1] * df2;
    //    }
    //    else {
    //        // First-order expansion of F in terms of G.
    //        //

    //        double det1 = (JFref[0]*JGref[3] - JFref[2]*JGref[1] + JFref[1]*JGref[2] - JFref[3]*JGref[0]);
    //        double det2 = (JFref[1]*JGref[3] - JFref[3]*JGref[1]);
    //        double det3 = (JFref[0]*JGref[2] - JFref[2]*JGref[0]);

    //        f = det1*dg1*dg2 + det2*dg2*dg2 + det3*dg1*dg1;

    //        map_Jacobian.component(0) = det1*(JG[0][0]*dg2 + JG[1][0]*dg1) + 2*det2*JG[0][0]*dg1 + 2*det3*JG[1][0]*dg2;
    //        map_Jacobian.component(1) = det1*(JG[0][1]*dg2 + JG[1][1]*dg1) + 2*det2*JG[0][1]*dg1 + 2*det3*JG[1][1]*dg2;
    //    }

    return;
}

int Hugoniot_Curve::complete(const RealVector &p0, const RealVector &p1, const RealVector &p_init, RealVector &p_completed) {
    Newton_Improvement newton_improver(this);
    int newton_info = newton_improver.newton(p0, p1, p_init, p_completed);

    return IMPROVABLE_OK;
}

bool Hugoniot_Curve::improvable(void) {
    return true;
}

