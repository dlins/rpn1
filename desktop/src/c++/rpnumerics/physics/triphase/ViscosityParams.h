/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ViscosityParams.h
 */

#ifndef _ViscosityParams_H
#define _ViscosityParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class ViscosityParams {
private:

    double epsl_;

public:

    const static double DEFAULT_EPS = 0.1;

    ViscosityParams(double eps);
    ViscosityParams();

    double & epsl();
    virtual ~ViscosityParams();

};

inline double & ViscosityParams::epsl()  {
    return epsl_;
}

#endif //! _ViscosityParams_H
