/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Hugoniot_Locus.h
 */

#ifndef _Hugoniot_Locus_H
#define _Hugoniot_Locus_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "ColorCurve.h"
#include "ReferencePoint.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Hugoniot_Locus : public ImplicitFunction {
private:



public:


    virtual int classified_curve(GridValues & , ReferencePoint &,std::vector<HugoniotPolyLine> & ,std::vector<RealVector> &) = 0;

};

#endif //! _Hugoniot_Locus_H
