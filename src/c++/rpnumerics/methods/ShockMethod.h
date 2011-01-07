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

    int dimension_;
    FluxFunction * fluxFunction_;
    AccumulationFunction * accFunction_;
    Boundary * boundary_;


protected:

    ColorCurve * sorter_;

public:

    ShockMethod(int, const FluxFunction &, const AccumulationFunction &,const Boundary &);

    const FluxFunction & fluxFunction()const;

    const AccumulationFunction & accumulationFunction()const;

    const Boundary & boundary()const ;

    int dimension()const;

//    virtual void curve(const RealVector &, int direction, vector<RealVector> &) = 0;

//    virtual ShockMethod * clone() const = 0;

    virtual ~ShockMethod();

};




#endif //! _ShockMethod_H
