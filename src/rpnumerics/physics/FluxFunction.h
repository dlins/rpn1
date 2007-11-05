/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FluxFunction.h
 **/

#ifndef _FluxFunction_H
#define	_FluxFunction_H

#include "RpFunction.h"
#include "FluxParams.h"

//! Definition of class FluxFunction.
/*!
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

class FluxFunction: public RpFunction {
    
private:
	FluxParams params_;
    
public:
	FluxFunction(void);
	FluxFunction(const FluxParams & params);
	virtual ~FluxFunction(void);
    
//	int jet( const WaveState &u, JetMatrix &m, int degree);
    
	const FluxParams & fluxParams(void);
	void fluxParams(const FluxParams & params);
    
};

#endif	//! _FluxFunction_H
