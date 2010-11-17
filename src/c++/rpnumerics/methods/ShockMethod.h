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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ShockMethod {

private:

public:

    virtual void curve(const RealVector &, int direction, vector<RealVector> &) = 0;

    virtual ShockMethod * clone() const = 0;

    virtual ~ShockMethod();



};

#endif //! _ShockMethod_H
