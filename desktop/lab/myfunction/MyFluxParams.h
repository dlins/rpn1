/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) MyFluxParams.h
 **/

#ifndef _MyFluxParams_H
#define _MyFluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxParams.h"

class MyFluxParams : public FluxParams {

public:
	MyFluxParams(void);
	MyFluxParams(const RealVector &);
        virtual ~MyFluxParams();

	MyFluxParams defaultParams(void);
};

inline MyFluxParams MyFluxParams::defaultParams(void)
{
	return MyFluxParams();
}


#endif //! _MyFluxParams_H

