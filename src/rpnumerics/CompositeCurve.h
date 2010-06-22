/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CompositeCurve.h
 */

#ifndef _CompositeCurve_H
#define _CompositeCurve_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionCurve.h"
#include "ShockCurve.h"
#include "WaveCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class CompositeCurve:public WaveCurve {
    
private:
    ShockCurve & shockCurve_;
    RarefactionCurve & rarefactionCurve_;

public:

    CompositeCurve(const RarefactionCurve &, const ShockCurve &,const int);
    ~CompositeCurve();
    const RarefactionCurve & getRarefactionCurve();
    const ShockCurve & getShockCurve();
    const string getType()const;

};

inline const RarefactionCurve & CompositeCurve::getRarefactionCurve() {
    return rarefactionCurve_;
}

inline const ShockCurve & CompositeCurve::getShockCurve(){
    return shockCurve_;
}

inline const string CompositeCurve::getType() const {
    return "compositecurve";
}

#endif //! _CompositeCurve_H
