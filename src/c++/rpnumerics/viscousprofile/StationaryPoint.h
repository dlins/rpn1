/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StationaryPoint.h
 */

#ifndef _StationaryPoint_H
#define _StationaryPoint_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "eigen.h"
#include "RealVector.h"
#include <vector>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StationaryPoint {
private:

    RealVector * coords_;
    vector<eigenpair> epVector_;

public:


    StationaryPoint(const RealVector &, std::vector<eigenpair> &ep);
    virtual ~StationaryPoint();


    std::ostream & operator<<(std::ostream &out, const StationaryPoint &r);




};

#endif //! _StationaryPoint_H
