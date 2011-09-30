/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2FluxParams.h
 **/

#ifndef _Quad2FluxParams_H
#define _Quad2FluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxParams.h"

class Quad2FluxParams : public FluxParams {
public:
    Quad2FluxParams(void);
    Quad2FluxParams(const RealVector &);

    Quad2FluxParams(double, double);

    virtual ~Quad2FluxParams();

    static const double DEFAULT_A[2];
    static const double DEFAULT_B[2][2];
    static const double DEFAULT_C[2][2][2];

    Quad2FluxParams defaultParams(void);

};

inline Quad2FluxParams Quad2FluxParams::defaultParams(void) {
    return Quad2FluxParams();
}


#endif //! _Quad2FluxParams_H

/*
 * JAVA CODE
 *
    public Quad2FluxParams(double[] A, double[] [] B, double[] [] [] C,int index) {
        super(Quad2.FLUX_ID, 2, A, B, C,index);
    }

    public Quad2FluxParams() {
        this(DEFAULT_A, DEFAULT_B, DEFAULT_C,0);
    }

 */
