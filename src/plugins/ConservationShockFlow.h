/* 
 * File:   FlowPlugin.h
 * Author: edsonlan
 *
 * Created on September 22, 2008, 6:23 PM
 */


#ifndef _CONSERVATIONSHOCKFLOW_H
#define	_CONSERVATIONSHOCKFLOW_H


#include "RpnPlugin.h"
#include "ShockFlow.h"
#include "Quad2FluxFunction.h"

class ShockFlowPlugin : public RpnPlugin, public ShockFlow {

private:


   
    RealVector * fx0_;




public:

    ShockFlowPlugin(const ShockFlowParams &, const FluxFunction &);

    const ShockFlowParams & getParams()const;
    void updateZeroTerms();
    void setParams(const ShockFlowParams & params);


    int flux(const RealVector &, RealVector &) const;
    int fluxDeriv(const RealVector &, JacobianMatrix &)const;
    int fluxDeriv2(const RealVector &, HessianMatrix &)const;

    
    

    virtual~ShockFlowPlugin();
    

};



#endif	/* _FLOWPLUGIN_H */

