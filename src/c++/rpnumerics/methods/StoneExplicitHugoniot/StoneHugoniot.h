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
    
    
    int classified_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<HugoniotPolyLine> &hugoniot_curve);


    int classified_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
            std::vector<bool> &circular,const Viscosity_Matrix * vm);


    int curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<RealVector> &hugoniot_curve);

    

};

#endif //! _StoneHugoniot_H
