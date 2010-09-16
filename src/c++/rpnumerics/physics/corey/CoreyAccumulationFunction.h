/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Corey2AccumulationFunction.h
 **/
#ifndef _CoreyAccumulationFunction_H
#define	_CoreyAccumulationFunction_H

//!
/*!
 *
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

#include "AccumulationFunction.h"

class CoreyAccumulationFunction : public AccumulationFunction {
    
    
public:
    
    CoreyAccumulationFunction(void);
    CoreyAccumulationFunction(const AccumulationParams & params);
    ~CoreyAccumulationFunction(void);
    
    CoreyAccumulationFunction * clone() const;
    
    int jet(const WaveState&, JetMatrix&, int) const;

};


inline CoreyAccumulationFunction::CoreyAccumulationFunction(const AccumulationParams & params) :AccumulationFunction(params) {


}

#endif	/* _AccumulationFunction_H */

