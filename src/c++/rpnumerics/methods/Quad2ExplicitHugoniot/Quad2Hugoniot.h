/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2Hugoniot.h
 */

#ifndef _Quad2Hugoniot_H
#define _Quad2Hugoniot_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Hugoniot_Curve.h"
#include "Quad2_Explicit.h"
#include "Quad2_Explicit_Hugoniot.h"
#include "SimplePolarPlot.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Quad2Hugoniot : public Hugoniot_Curve {
private:


    
    
public:

    int classified_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<HugoniotPolyLine> &hugoniot_curve);


//    int classified_curve(const FluxFunction *f, const AccumulationFunction *a,
//            GridValues &g, const RealVector &r,
//            std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
//            std::vector<bool> &circular);


    
    
      int classified_curve(const FluxFunction *f, const AccumulationFunction *a,
                                     GridValues &g, const RealVector &r,
                                     std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
                                     std::vector<bool> &circular,const Viscosity_Matrix * vm ) ;
    
    


    int curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<RealVector> &hugoniot_curve);





};

#endif //! _Quad2Hugoniot_H
