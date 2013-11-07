/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockFlowPlugin.h
 **/

#ifndef _SHOCKFLOWPLUGIN_H
#define	_SHOCKFLOWPLUGIN_H


/*!
 *
 *
 * TODO:
 * NOTE :
 *
 * @ingroup plugins
 */


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

//
//    int flux(const RealVector &, RealVector &) ;
//    int fluxDeriv(const RealVector &, JacobianMatrix &);
//    int fluxDeriv2(const RealVector &, HessianMatrix &);

    WaveFlow * clone()const;
    
    virtual~ShockFlowPlugin();
    

};



#endif	/* _FLOWPLUGIN_H */

