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
    
    
    Quad2Hugoniot (const FluxFunction *f, const AccumulationFunction *a);
    
    int classified_curve(GridValues &, ReferencePoint &,std::vector<HugoniotPolyLine> & ,std::vector<RealVector> &);



};

#endif //! _Quad2Hugoniot_H
