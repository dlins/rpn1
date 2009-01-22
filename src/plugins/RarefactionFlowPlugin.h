/* 
 * File:   FlowPlugin.h
 * Author: edsonlan
 *
 * Created on September 22, 2008, 6:23 PM
 */


#ifndef _RAREFACTIONFLOW_H
#define	_RAREFACTIONFLOW_H


#include "RpnPlugin.h"
#include "RarefactionFlow.h"



class RarefactionFlowPlugin : public RpnPlugin,public RarefactionFlow{
    
public:
    
    RarefactionFlowPlugin(const int ,const int ,const FluxFunction &);
    
    int flux(const RealVector &, RealVector &) const;
    int fluxDeriv(const RealVector &, JacobianMatrix &)const;
    int fluxDeriv2(const RealVector &, HessianMatrix &)const;

    
    virtual~RarefactionFlowPlugin();
   
    RpFunction * clone() const;
    
};



#endif	/* _FLOWPLUGIN_H */

