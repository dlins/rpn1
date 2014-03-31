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
#include "Debug.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



StoneHugoniot::StoneHugoniot(const FluxFunction * flux, const AccumulationFunction * accum):Hugoniot_Curve(flux,accum){
    
}


 int StoneHugoniot::classified_curve(GridValues & g, ReferencePoint & r,std::vector<HugoniotPolyLine> & hugoniot_curve,std::vector<RealVector> & transitionList){


    StoneExplicitHugoniot *q2eh = new StoneExplicitHugoniot((StoneFluxFunction*) ff);
    q2eh->set_reference_point(r.point);

    RealVector polar_domain(2);
    polar_domain.component(0) = 0.0;
    polar_domain.component(1) = 3.14159256;

    double delta_theta = 0.001;

    int gridRows = g.grid.rows();
    int gridCols = g.grid.cols();

    RealVector pmin = g.grid.operator ()(0, 0);
    RealVector pmax = g.grid.operator ()(gridRows - 1, gridCols - 1);

    if ( Debug::get_debug_level() == 5 ) {
        cout << "Pmin: " << pmin << endl;
        cout << "Pmax: " << pmax << endl;
    }

    Three_Phase_Boundary boundary(pmin, pmax);

    std::vector<std::deque<RealVector> > out;

    SimplePolarPlot::curve((void*) q2eh, &boundary,
            &StoneExplicitHugoniot::PolarHugoniot,
            polar_domain, 1000,
            out);
    delete q2eh;

    ColorCurve colorCurve(*ff, *aa);

    for (int i = 0; i < out.size(); i++) {

        HugoniotPolyLine polyLine;
        colorCurve.classify_continuous_curve(out.at(i), r, polyLine, transitionList);

        hugoniot_curve.push_back(polyLine);

    }


}

