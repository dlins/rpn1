/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneNegativePermParams.h
 */

#ifndef _StoneNegativePermParams_H
#define _StoneNegativePermParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StoneNegativePermParams {
private:
    RealVector * comp;

public:

    StoneNegativePermParams(double expw, double expg, double expo,
            double expow, double expog,  double krw_p,  double krg_p, double kro_p,
            double cnw, double cng, double cno,
            double lw, double lg,double lo,
            double low, double log,
            double epsl,  double negWS,  double negGS,  double negOS);

    StoneNegativePermParams();
    StoneNegativePermParams (const RealVector &);
    StoneNegativePermParams(const StoneNegativePermParams &);
    virtual ~StoneNegativePermParams();

    void reset();

    double component(int);

    const RealVector & params() const;
};

inline const RealVector & StoneNegativePermParams::params()const {
    return *comp;
}


#endif //! _StoneNegativePermParams_H
