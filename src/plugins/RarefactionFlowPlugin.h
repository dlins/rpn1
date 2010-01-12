
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
    
//    RarefactionFlowPlugin(const int ,const int ,const FluxFunction &);
    RarefactionFlowPlugin(const RealVector & , const int, const int, const FluxFunction &);
    
    int flux(const RealVector &, RealVector &) ;
    int fluxDeriv(const RealVector &, JacobianMatrix &);
    int fluxDeriv2(const RealVector &, HessianMatrix &);

    
    virtual~RarefactionFlowPlugin();
   
    WaveFlow * clone() const;
    
};



#endif	/* _FLOWPLUGIN_H */

