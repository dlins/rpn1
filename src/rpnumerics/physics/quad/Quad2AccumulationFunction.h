/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2AccumulationFunction.h
 **/
#ifndef _Quad2AccumulationFunction_H
#define	_Quad2AccumulationFunction_H

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

class Quad2AccumulationFunction : public AccumulationFunction {
    
    
public:
    
    Quad2AccumulationFunction(void);
    Quad2AccumulationFunction(const AccumulationParams & params);
    ~Quad2AccumulationFunction(void);
    
    Quad2AccumulationFunction * clone() const;
    
    int jet(const WaveState&, JetMatrix&, int) const;

};


inline Quad2AccumulationFunction::Quad2AccumulationFunction(const AccumulationParams & params) :AccumulationFunction(params) {}

#endif	/* _AccumulationFunction_H */

