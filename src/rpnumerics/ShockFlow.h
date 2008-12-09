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
#include "WaveFlow.h"
#include "ShockFlowParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



/*!  Definition of class ShockFlow.
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

class ShockFlow : public WaveFlow {

protected:
    
    RealVector * xZero_;
    ShockFlowParams  * params_;
    
    

    
public:
    
    int jet(const int degree, const RealVector &u, JetMatrix &m);

    ShockFlow(const RealVector &,const FluxFunction &);
    ShockFlow(const ShockFlowParams &, const FluxFunction &);

    
    
    const RealVector & XZero(void) const;
    void setXZero(const RealVector & xzero) ;

    virtual ~ShockFlow();
    
};

inline int ShockFlow::jet(const int degree, const RealVector &u, JetMatrix &m) {
    return OK;
}




#endif //! _ShockFlow_H
