/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveFlow.h
 **/


#ifndef _WaveFlow_H
#define	_WaveFlow_H

#include "RpFunction.h"
#include "PhasePoint.h"

//! Definition of class WaveFlow.
/*!

 TODO:
 NOTE :

 @ingroup rpnumerics
 */

class WaveFlow : public RpFunction {
    
public:
	~WaveFlow(void);

	virtual const PhasePoint XZero(void) const = 0;
	virtual void XZero(const PhasePoint & xzero) const = 0;

};

inline WaveFlow::~WaveFlow(void)
{
}

#endif	/* _WaveFlow_H */

