/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationRarefationMethod.h
 */

#ifndef _ContinuationRarefationMethod_H
#define _ContinuationRarefationMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionMethod.h"
#include "RarefactionCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ContinuationRarefactionMethod: public RarefactionMethod {
    
private:



public:
    
    struct RarefactionCurve curve (const RealVector & );
    
    RarefactionMethod * clone() const;
    
    ContinuationRarefactionMethod(const RarefactionFlow &);
    
    ContinuationRarefactionMethod(const ContinuationRarefactionMethod &);
    

    
};



inline RarefactionMethod * ContinuationRarefactionMethod::clone() const { return new ContinuationRarefactionMethod(*this);}


#endif //! _ContinuationRarefationMethod_H
