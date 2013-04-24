/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) GenericFluxFunction.h
 **/

#ifndef _GenericFluxFunction_H
#define _GenericFluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxFunction.h"
#include "GenericFluxParams.h"

class GenericFluxFunction : public FluxFunction {
    
public:
	GenericFluxFunction(const GenericFluxParams &);
	GenericFluxFunction(const GenericFluxFunction &);

	virtual ~GenericFluxFunction(void);
    
	GenericFluxFunction * clone() const ;
    
	int jet(const WaveState &u, JetMatrix &m, int degree) const;
};


#endif //! _GenericFluxFunction_H
