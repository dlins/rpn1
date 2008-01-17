/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethod.h
 **/

#ifndef _RarefactionMethod_H
#define _RarefactionMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include <vector>
#include "RealVector.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethod {
    
    
public:

virtual vector<RealVector> curve(const RealVector &, int ) =0 ;

virtual RarefactionMethod * clone() const =0;

virtual int direction() const =0;

virtual ~RarefactionMethod();


};



#endif //! _RarefactionMethod_H
