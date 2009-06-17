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

#include "RealVector.h"
#include "RarefactionFlow.h"
//#include "RPnMethod.h"
#include "ODESolver.h"
#include "RPnCurve.h"
//#include "RPnVectorField.h"
//#include "RPnGrid.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethod {//: public RPnMethod {
    
        
public:
    
    
    virtual RPnCurve & curve(const RealVector &, int direction)=0;
    
    //virtual RPnVectorField & vectorField(const RPnGrid &)=0;
    
    virtual RarefactionMethod * clone() const =0;
    
    virtual ~RarefactionMethod();
    
    
};





#endif //! _RarefactionMethod_H
