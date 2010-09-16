/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) GenericFluxParams.h
 **/

#ifndef _GenericFluxParams_H
#define _GenericFluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxParams.h"


class GenericFluxParams : public FluxParams {

public:
	GenericFluxParams(void);
	GenericFluxParams(const RealVector &);
        
        virtual ~GenericFluxParams();

	GenericFluxParams defaultParams(void);
};

inline GenericFluxParams GenericFluxParams::defaultParams(void)
{
	return GenericFluxParams();
}


#endif //! _GenericFluxParams_H

