#include "Hugoniot_Curve.h"

// This is the classified Hugoniot given by segments
//
// TODO : Implementar com esta assinatura (Pablo/Leandro 18FEV)
//int Hugoniot_Curve::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
//                                     GridValues &g, const RealVector &r,
//                                     std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &testeTransitionalList) {

int Hugoniot_Curve::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
                                     GridValues &g, const RealVector &r,
                                     std::vector<HugoniotPolyLine> &hugoniot_curve,const Viscosity_Matrix * vm) {

    // The segments by a search algorithm are stored here, to be used in the ColorCurve
    std::vector<RealVector> vrs;
    std::vector<RealVector> testeTransitionalList;

    // This is auxiliary stuff (not filled, not used)
    std::vector< std::deque <RealVector> > curves;
    std::vector < bool > circular;
    int method = SEGMENTATION_METHOD;

    // Compute the Hugoniot curve as usual
    //
    int info = curve(f, a, g, r, vrs, curves, circular, method);

    ColorCurve colorCurve(*f, *a,vm);
    colorCurve.classify_segmented_curve(vrs,r,hugoniot_curve,testeTransitionalList);
    
    return info;
}

        

// This is the classified Hugoniot given by continuous curves
//
// TODO : Implementar com esta assinatura (Pablo/Leandro 18FEV)
int Hugoniot_Curve::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
                                     GridValues &g, const RealVector &r,
                                     std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
                                     std::vector<bool> &circular,const Viscosity_Matrix * vm) {

//int Hugoniot_Curve::classified_curve(const FluxFunction *f, const AccumulationFunction *a, 
//                                     GridValues &g, const RealVector &r, 
//                                     std::vector<HugoniotPolyLine> &hugoniot_curve,
//                                     std::vector<bool> &circular) {

    // --- Pablo/Leandro 18FEV
//    std::vector<RealVector> transitionList;
    transitionList.clear();
    // ---

    IF_DEBUG
        cout << "Hugoniot: classified_curve por continuacao" << endl;
    END_DEBUG

    // The continuous curve is stored by Contour2D
    std::vector< std::deque <RealVector> > curves;

    // This is auxiliary stuff (not filled, not used)
    std::vector<RealVector> vrs;
    int method = CONTINUATION_METHOD;

    // Compute the Hugoniot curve by continuation
    //
    int info = curve(f, a, g, r, vrs, curves, circular, method);
    int no_of_curves = curves.size();

    // There is a single list of Transitional points instead as one list for curves.
    std::vector<RealVector> testeTransitionalList;
    testeTransitionalList.clear();
    hugoniot_curve.clear();

    ColorCurve colorCurve(*f, *a,vm);

    for (int i = 0; i < no_of_curves; i++) {
        HugoniotPolyLine hugoniot;
        colorCurve.classify_continuous_curve(curves[i],r,hugoniot,testeTransitionalList);
        hugoniot_curve.push_back(hugoniot);
        IF_DEBUG
            cout << "Size da lista de transicao " << i  <<  "  " << testeTransitionalList.size() << endl;
        END_DEBUG

        // --- Pablo/Leandro 18FEV
        for (int j=0; j< testeTransitionalList.size(); j++) {
            transitionList.push_back(testeTransitionalList[j]);
        }
        // ---
    }

    IF_DEBUG
        cout << "Size da lista total " << transitionList.size() << endl;
    END_DEBUG

    
    
    return info;
}

 int Hugoniot_Curve::curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<RealVector> &hugoniot_curve){
     return 0;
     
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
            } 
            else {
                // First-order expansion of F in terms of G.
                //

                double inv_det = 1.0 / (JGref(0) * JGref(3) - JGref(1) * JGref(2) );

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

int Hugoniot_Curve::curve(const FluxFunction *f, const AccumulationFunction *a, 
                          GridValues &g, const RealVector &r,
                          std::vector<RealVector> &hugoniot_curve,
                          std::vector< std::deque <RealVector> > &curves, std::vector <bool> &is_circular,
                          const int method) {

    ff = f;
    aa = a;

    g.fill_functions_on_grid(f, a);
    gv = &g;

    int n = r.size();

    Fref.resize(n);
    Gref.resize(n);

    JFref.resize(n, n); 
    JGref.resize(n, n);

    RealVector p(r);

    ff->fill_with_jet(n, p.components(), 1, Fref.components(), JFref.data(), 0);
    aa->fill_with_jet(n, p.components(), 1, Gref.components(), JGref.data(), 0);

    hugoniot_curve.clear();
    curves.clear();
    is_circular.clear();

    // Notice that method splitts which curve is filled
    int info = ContourMethod::contour2d(this, hugoniot_curve, curves, is_circular, method);

    return info;
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

    f = dg2*df1 - dg1*df2;

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

bool Hugoniot_Curve::improvable(void) {
    return true;
}
