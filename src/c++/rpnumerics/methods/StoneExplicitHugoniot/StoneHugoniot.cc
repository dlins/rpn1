/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneHugoniot.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StoneHugoniot.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


int StoneHugoniot::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, const RealVector &r,
        std::vector<HugoniotPolyLine> &hugoniot_curve) {



}

int StoneHugoniot::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, const RealVector &r,
        std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
        std::vector<bool> &circular,const Viscosity_Matrix * vm) {





    StoneExplicitHugoniot *q2eh = new StoneExplicitHugoniot((StoneFluxFunction*) f);
    q2eh->set_reference_point(r);

    RealVector polar_domain(2);
    polar_domain.component(0) = 0.0;
    polar_domain.component(1) = 3.14159256;

    double delta_theta = 0.001;

    int gridRows = g.grid.rows();
    int gridCols = g.grid.cols();

    RealVector pmin = g.grid.operator ()(0, 0);
    RealVector pmax = g.grid.operator ()(gridRows - 1, gridCols - 1);

    cout << "Pmin: " << pmin << endl;
    cout << "Pmax: " << pmax << endl;

    Three_Phase_Boundary boundary(pmin, pmax);

    std::vector<std::deque<RealVector> > out;

    SimplePolarPlot::curve((void*) q2eh, &boundary,
            &StoneExplicitHugoniot::PolarHugoniot,
            polar_domain, 1000,
            out);
    delete q2eh;

    ColorCurve colorCurve(*f, *a,vm);

    for (int i = 0; i < out.size(); i++) {

        HugoniotPolyLine polyLine;
        colorCurve.classify_continuous_curve(out.at(i), r, polyLine, transitionList);

        hugoniot_curve.push_back(polyLine);

    }


}

int StoneHugoniot::curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, const RealVector &r,
        std::vector<RealVector> &hugoniot_curve) {

}
