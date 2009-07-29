/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TriPhase2AccumulationFunction.h
 **/
#ifndef _TriPhaseAccumulationFunction_H
#define	_TriPhaseAccumulationFunction_H

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

class TriPhaseAccumulationFunction : public AccumulationFunction {
    
    
public:
    
    TriPhaseAccumulationFunction(void);
    TriPhaseAccumulationFunction(const AccumulationParams & params);
    ~TriPhaseAccumulationFunction(void);
    
    TriPhaseAccumulationFunction * clone() const;
    
    int jet(const WaveState&, JetMatrix&, int) const;

};


inline TriPhaseAccumulationFunction::TriPhaseAccumulationFunction(const AccumulationParams & params) :AccumulationFunction(params) {


}

#endif	/* _AccumulationFunction_H */

