/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Cub2AccumulationFunction.h
 **/
#ifndef _Cub2AccumulationFunction_H
#define	_Cub2AccumulationFunction_H

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

class Cub2AccumulationFunction : public AccumulationFunction {
    
    
public:
    
    Cub2AccumulationFunction(void);
    Cub2AccumulationFunction(const AccumulationParams & params);
    ~Cub2AccumulationFunction(void);
    
    Cub2AccumulationFunction * clone() const;
    
    int jet(const WaveState&, JetMatrix&, int) const;

};


inline Cub2AccumulationFunction::Cub2AccumulationFunction(const AccumulationParams & params) :AccumulationFunction(params) {}

#endif	/* _AccumulationFunction_H */

