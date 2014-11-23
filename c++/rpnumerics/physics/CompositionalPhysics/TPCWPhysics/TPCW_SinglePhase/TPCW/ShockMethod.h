/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockMethod.h
 */

#ifndef _ShockMethod_H
#define _ShockMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RealVector.h"
#include "ReferencePoint.h"
#include <vector>
#include "ColorCurve.h"
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Boundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ShockMethod {
private:

    


protected:
    
    int dimension_;
    FluxFunction * fluxFunction_;
    AccumulationFunction * accFunction_;
    Boundary * boundary_;

    ColorCurve * sorter_;

public:
    ShockMethod();
    ShockMethod(int d, FluxFunction *f, AccumulationFunction *a, Boundary *b);

    virtual ~ShockMethod(){}

    const FluxFunction & fluxFunction()const;

    const AccumulationFunction & accumulationFunction()const;

    const Boundary & boundary()const;

    int dimension()const;

    virtual void curveCalc(const RealVector &ref, bool local_shock, const RealVector &in, int increase, int family, int type_of_shock, const RealVector *orig_direction, int number_ignore_doub_contact, FluxFunction *ff, AccumulationFunction *aa, Boundary *boundary,
            std::vector<RealVector> &shockcurve, int &info_shockcurve, std::vector<RealVector> &shockcurve_alt, int &info_shockcurve_alt,double newtonTolerance) = 0;

};




#endif //! _ShockMethod_H
