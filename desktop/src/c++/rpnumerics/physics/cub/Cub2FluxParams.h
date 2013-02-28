/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Cub2FluxParams.h
 **/

#ifndef _Cub2FluxParams_H
#define _Cub2FluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxParams.h"

class Cub2FluxParams : public FluxParams {
public:
    Cub2FluxParams(void);
    Cub2FluxParams(const RealVector &);

    Cub2FluxParams(double, double);

    virtual ~Cub2FluxParams();


    Cub2FluxParams defaultParams(void);

};

inline Cub2FluxParams Cub2FluxParams::defaultParams(void) {
    return Cub2FluxParams();
}


#endif //! _Quad2FluxParams_H
