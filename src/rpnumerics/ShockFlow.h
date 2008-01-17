/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockFlow.h
 **/

#ifndef _ShockFlow_H
#define _ShockFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RpFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class ShockFlow : public RpFunction {

protected:
    
    RealVector * xZero_;
    
    
public:
    
    int jet(const int degree, const RealVector &u, JetMatrix &m);

    ShockFlow(const RealVector &);
    
    const RealVector * XZero(void) const;
    void setXZero(const RealVector & xzero) ;
    virtual ~ShockFlow();
    
};

inline int ShockFlow::jet(const int degree, const RealVector &u, JetMatrix &m) {
    return OK;
}




#endif //! _ShockFlow_H
