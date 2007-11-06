/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) AccumulationFunction.h
 **/
#ifndef _AccumulationFunction_H
#define	_AccumulationFunction_H

//! Definition of class AccumulationFunction.
/*!


 TODO:
 NOTE :

 @ingroup rpnumerics
 */

#include "RpFunction.h"
#include "AccumulationParams.h"

class AccumulationFunction : public RpFunction {
    
private:
	AccumulationParams params_;
	
public:
	AccumulationFunction(void);
	AccumulationFunction(const AccumulationParams & params);
	~AccumulationFunction(void);
	
	void accumulationParams(const AccumulationParams & params);
	const AccumulationParams & accumulationParams(void);

};

inline AccumulationFunction::AccumulationFunction(void) :
	params_(AccumulationParams())
{
}

inline AccumulationFunction::AccumulationFunction(const AccumulationParams & params) :
	params_(params)
{
}

inline AccumulationFunction::~AccumulationFunction(void) 
{
}

inline void AccumulationFunction::accumulationParams(const AccumulationParams & params)
{
	params_ = params;
}

inline const AccumulationParams & AccumulationFunction::accumulationParams(void)
{
	return params_;
}

#endif	/* _AccumulationFunction_H */

