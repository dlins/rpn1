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

private:

    ShockFlowParams  * params_;
    
public:
    
    ShockFlow(const RealVector &,const FluxFunction &);
    
    ShockFlow(const ShockFlowParams &, const FluxFunction &);

    virtual const ShockFlowParams & getParams()const;

    virtual void setParams(const ShockFlowParams &);

    virtual ~ShockFlow();
    
};










#endif //! _ShockFlow_H
