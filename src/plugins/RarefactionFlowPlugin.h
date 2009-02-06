
/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlowPlugin.h
 **/


#ifndef _RAREFACTIONFLOW_H
#define	_RAREFACTIONFLOW_H

/*!
 *
 *
 * TODO:
 * NOTE :
 *
 * @ingroup plugins
 */




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

