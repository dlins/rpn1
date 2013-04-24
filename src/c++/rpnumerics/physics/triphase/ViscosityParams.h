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

    void setEpsl(double ) ;
    double getEpsl()const;
    virtual ~ViscosityParams();

};

inline double ViscosityParams::getEpsl()const{
    return epsl_;
}

inline void ViscosityParams::setEpsl(double eps) {
    epsl_=eps;
}

#endif //! _ViscosityParams_H
