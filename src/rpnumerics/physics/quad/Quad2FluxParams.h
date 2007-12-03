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
	Quad2FluxParams(const Quad2FluxParams &);

	static const double DEFAULT_A[2];
	static const double DEFAULT_B[2][2];
	static const double DEFAULT_C[2][2][2];

	Quad2FluxParams defaultParams(void);

};

inline Quad2FluxParams Quad2FluxParams::defaultParams(void)
{
	return Quad2FluxParams();
}


const double Quad2FluxParams::DEFAULT_A[2] = { 0., 0. };
const double Quad2FluxParams::DEFAULT_B[2][2] = { { 0., .1 }, { -.1, 0. } };
const double Quad2FluxParams::DEFAULT_C[2][2][2] = { { { -1., 0. }, { 0., 1. } }, { { 0., 1. }, { 1., 0. } } };


Quad2FluxParams::Quad2FluxParams(void) :FluxParams(* new RealVector()) 
{
}

Quad2FluxParams::Quad2FluxParams(const RealVector & params) :FluxParams(* new RealVector (params)) 
{
}

Quad2FluxParams::Quad2FluxParams(const Quad2FluxParams & copy) :FluxParams(copy)
{
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
