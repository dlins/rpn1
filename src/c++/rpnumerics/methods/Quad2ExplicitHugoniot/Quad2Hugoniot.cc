/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2Hugoniot.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Quad2Hugoniot.h"
#include "Debug.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


int Quad2Hugoniot::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, const RealVector &r,
        std::vector<HugoniotPolyLine> &hugoniot_curve ) {

}

int Quad2Hugoniot::classified_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, const RealVector &r,
        std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
        std::vector<bool> &circular,const Viscosity_Matrix * vm) {
    
    if ( Debug::get_debug_level() == 5 ) {
        //cout<<"Plotando com o metodo explicito"<<endl;
    }
    Quad2_Explicit_Hugoniot *q2eh = new Quad2_Explicit_Hugoniot((Quad2FluxFunction*)f);
    q2eh->set_reference_point(r);

    RealVector polar_domain(2);
    polar_domain.component(0) = 0.0;
    polar_domain.component(1) = 3.14159256;

    double delta_theta = 0.001;
    
    int gridRows = g.grid.rows();
    int gridCols = g.grid.cols();
    
    RealVector pmin = g.grid.operator ()(0,0);
    RealVector pmax = g.grid.operator ()(gridRows-1,gridCols-1);
    
    if ( Debug::get_debug_level() == 5 ) {
        //cout <<"Pmin: "<<pmin<<endl;
        //cout <<"Pmax: "<<pmax<<endl;
    }

    
    RectBoundary boundary(pmin,pmax);

    std::vector<std::deque<RealVector> > out;

    SimplePolarPlot::curve((void*)q2eh, &boundary,
                           &Quad2_Explicit_Hugoniot::PolarHugoniot,
                           polar_domain, 1000,
                           out);
    delete q2eh;
    
    ColorCurve colorCurve(*f,*a);
    
    ReferencePoint refPoint(r,f,a,vm);

    for (int i = 0; i < out.size(); i++) {
        
        HugoniotPolyLine polyLine;
        colorCurve.classify_continuous_curve(out.at(i), refPoint,polyLine,transitionList);
        
        hugoniot_curve.push_back(polyLine);

    }

    
    

}

int Quad2Hugoniot::curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, const RealVector &r,
        std::vector<RealVector> &hugoniot_curve) {

}
