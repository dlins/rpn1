/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockCurve.h
 */

#ifndef _ShockCurve_H
#define _ShockCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <vector>
#include "WaveCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ShockCurve : public WaveCurve {
private:

public:

    ShockCurve();
    ShockCurve(const vector<RealVector> &, const int);
    ~ShockCurve();
    const string getType()const;



};

inline const string ShockCurve::getType() const {
    return "shockcurve";
}

#endif //! _ShockCurve_H
