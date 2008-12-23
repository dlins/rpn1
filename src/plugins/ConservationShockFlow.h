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



class ShockFlowPlugin : public RpnPlugin,public ShockFlow{

public:
    
    ShockFlowPlugin(const ShockFlowParams &,const FluxFunction &);
    
    int jet (const WaveState &,JetMatrix & ,int) const;

    const ShockFlowParams & getParams()const;
    void setParams(const ShockFlowParams & params);

    virtual~ShockFlowPlugin();
    RpFunction * clone() const;
    
};



#endif	/* _FLOWPLUGIN_H */

