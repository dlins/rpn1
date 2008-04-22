/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PermParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "PermParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


const double PermParams::DEFAULT_LW = 0;
const  double PermParams:: DEFAULT_LOW = 0;
const  double PermParams:: DEFAULT_LG = 0;
const  double PermParams::DEFAULT_LOG = 0;
const  double PermParams::DEFAULT_CNW = 0;
const  double PermParams::DEFAULT_CNO = 0;
const  double PermParams::DEFAULT_CNG = 0;
const  double PermParams:: DEFAULT_EPSL = 0;





PermParams::PermParams(double lw, double low, double lg, double log, double cnw, double cno, double cng, double epsl) {
    lw_ = lw; low_ = low;
    lg_ = lg; log_ = log;
    cnw_ = cnw; cno_ = cno; cng_ = cng;
    epsl_ = epsl;
}
PermParams::PermParams() { reset(); }

void PermParams::reset() {
    lw_ = DEFAULT_LW;
    low_ = DEFAULT_LOW;
    lg_ = DEFAULT_LG;
    log_ = DEFAULT_LOG;
    cnw_ = DEFAULT_CNW;
    cno_ = DEFAULT_CNO;
    cng_ = DEFAULT_CNG;
    epsl_ = DEFAULT_EPSL;
}

//! Code comes here! daniel@impa.br

