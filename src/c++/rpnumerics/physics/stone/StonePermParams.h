/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StonePermParams.h
 */

#ifndef _StonePermParams_H
#define _StonePermParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StonePermParams {
private:
    RealVector * comp;

public:

    StonePermParams(double expw, double expg, double expo,
            double expow, double expog,  double krw_p,  double krg_p, double kro_p,
            double cnw, double cng, double cno,
            double lw, double lg,double lo,
            double low, double log,
            double epsl,  double negWS,  double negGS,  double negOS);

    StonePermParams();
    StonePermParams (const RealVector &);
    StonePermParams(const StonePermParams &);
    virtual ~StonePermParams();

    void reset();

    double component(int);

    const RealVector & params() const;
};

inline const RealVector & StonePermParams::params()const {
    return *comp;
}


#endif //! _StonePermParams_H
