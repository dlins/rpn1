/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PermParams.h
 */

#ifndef _PermParams_H
#define _PermParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class PermParams {
private:

    RealVector * data_;

public:

    const static double DEFAULT_LW;
    const static double DEFAULT_LOW;
    const static double DEFAULT_LG;
    const static double DEFAULT_LOG;
    const static double DEFAULT_CNW;
    const static double DEFAULT_CNO;
    const static double DEFAULT_CNG;
    const static double DEFAULT_EPSL;


    PermParams(double lw, double low, double lg, double log, double cnw, double cno, double cng, double epsl);
    PermParams(const PermParams &);
    ~PermParams();

    void setValue(int, double);
    double getValue(int)const;
    int size()const;



    PermParams();

    double lw();

    double low();

    double lg();

    double log();

    double cnw();

    double cno();

    double cng();

    double epsl();

    void reset();



};

inline double PermParams::lw() {
    return data_->component(0);
}

inline double PermParams::low() {
    return data_->component(1);
}

inline double PermParams::lg() {
    return data_->component(2);
}

inline double PermParams::log() {
    return data_->component(3);
}

inline double PermParams::cnw() {
    return data_->component(4);
}

inline double PermParams::cno() {
    return data_->component(5);
}

inline double PermParams::cng() {
    return data_->component(6);
}

inline double PermParams::epsl() {
    return data_->component(7);
}



#endif //! _PermParams_H
