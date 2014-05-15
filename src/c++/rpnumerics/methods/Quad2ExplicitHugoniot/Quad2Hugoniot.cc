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



 Quad2Hugoniot::Quad2Hugoniot (const FluxFunction *f, const AccumulationFunction *a):Hugoniot_Curve(f,a){
     
 }
 
  int Quad2Hugoniot::classified_curve(GridValues &g, ReferencePoint &r,std::vector<HugoniotPolyLine> & hugoniot_curve ,std::vector<RealVector> & transitionList){
      
  
    if ( Debug::get_debug_level() == 5 ) {
        cout<<"Plotando com o metodo explicito"<<endl;
    }
    Quad2_Explicit_Hugoniot *q2eh = new Quad2_Explicit_Hugoniot((Quad2FluxFunction*)ff);
    q2eh->set_reference_point(r.point);

    RealVector polar_domain(2);
    polar_domain.component(0) = 0.0;
    polar_domain.component(1) = 3.14159256;

    double delta_theta = 0.001;
    
    int gridRows = g.grid.rows();
    int gridCols = g.grid.cols();
    
    RealVector pmin = g.grid.operator ()(0,0);
    RealVector pmax = g.grid.operator ()(gridRows-1,gridCols-1);
    
    if ( Debug::get_debug_level() == 5 ) {
        cout <<"Pmin: "<<pmin<<endl;
        cout <<"Pmax: "<<pmax<<endl;
    }

    
    RectBoundary boundary(pmin,pmax);

    std::vector<std::deque<RealVector> > out;

    SimplePolarPlot::curve((void*)q2eh, &boundary,
                           &Quad2_Explicit_Hugoniot::PolarHugoniot,
                           polar_domain, 1000,
                           out);
    delete q2eh;
    
    ColorCurve colorCurve(*ff,*aa,0);

    for (int i = 0; i < out.size(); i++) {
        
        HugoniotPolyLine polyLine;
        colorCurve.classify_continuous_curve(out.at(i), r,polyLine,transitionList);
        
        hugoniot_curve.push_back(polyLine);

    }

    
    

}

