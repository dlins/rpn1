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
    

    ShockFlowParams  * params_;
    
public:
    
    ShockFlow(const RealVector &,const FluxFunction &);
    ShockFlow(const ShockFlowParams &, const FluxFunction &);

    int jet(const WaveState &u, JetMatrix &m, int degree) const;

    const ShockFlowParams & getParams()const;
    
    void setParams(const ShockFlowParams &);
    
    virtual ~ShockFlow();
    
};



inline int ShockFlow::jet(const WaveState &u, JetMatrix &m, int degree) const {
    
    return OK;
}






#endif //! _ShockFlow_H
