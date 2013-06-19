#include "BLInflectionTP.h"
#include "Debug.h"

int BLInflectionTP::function_on_square(double *foncub, int i, int j) {
    double f_aux[4];

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            double sw    = 1.0 - gv->grid(i + l, j + k).component(0);
            double Theta = gv->grid(i + l, j + k).component(1);
            JetMatrix m(2);

            fh->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 2, m);

            f_aux[l * 2 + k] = m(0, 0, 0);
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]
    foncub[2] = f_aux[3]; // Was: foncub[0][2]

    return 1;
}

int BLInflectionTP::curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, std::vector<RealVector> &BLinflection_curve) {


    Flux2Comp2PhasesAdimensionalized * fluxAdimensional = (Flux2Comp2PhasesAdimensionalized *)f;
    fh = fluxAdimensional->getHorizontalFlux();

 double phi= a->accumulationParams().component(0);
    if ( Debug::get_debug_level() == 5 ) {
        cout << "Valor de phi em bl: " << phi << endl;
    }

    g.fill_functions_on_grid(f, a);
    gv = &g;

    BLinflection_curve.clear();

    int info = ContourMethod::contour2d(this, BLinflection_curve);

    return info;
}

  BLInflectionTP::~BLInflectionTP(){}

