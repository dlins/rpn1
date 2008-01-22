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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ContinuationRarefactionMethod: public RarefactionMethod {
    
private:

    int increase_;

public:
    
    vector<RealVector> curve(const RealVector &, int );
    
    RarefactionMethod * clone() const;
    
    ContinuationRarefactionMethod(const RarefactionFlow &);
    
    ContinuationRarefactionMethod(const ContinuationRarefactionMethod &);
    
    int direction() const;
    
};

inline int ContinuationRarefactionMethod::direction() const {return increase_;}

inline RarefactionMethod * ContinuationRarefactionMethod::clone() const { return new ContinuationRarefactionMethod(*this);}


#endif //! _ContinuationRarefationMethod_H
