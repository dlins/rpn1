/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneHugoniot.h
 */

#ifndef _StoneHugoniot_H
#define _StoneHugoniot_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StoneExplicitHugoniot.h"
#include "Hugoniot_Curve.h"
#include "IsoTriang2DBoundary.h"
#include "SimplePolarPlot.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StoneHugoniot: public Hugoniot_Curve {

private:

public:
    
    
    StoneHugoniot(const FluxFunction *, const AccumulationFunction *);
     
     int classified_curve(GridValues &, ReferencePoint &,std::vector<HugoniotPolyLine> & ,std::vector<RealVector> &);
  
    

};

#endif //! _StoneHugoniot_H
