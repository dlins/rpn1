/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionCurve.h
 */

#ifndef _RarefactionCurve_H
#define _RarefactionCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "WaveCurve.h"
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class RarefactionCurve : public WaveCurve {
private:
  

public:

    RarefactionCurve();
    RarefactionCurve(const vector<RealVector> &, const int);
    ~RarefactionCurve();
  
    const string getType()const;

};

inline const string RarefactionCurve::getType() const {
    return "rarefactioncurve";
}

#endif //! _RarefactionCurve_H
