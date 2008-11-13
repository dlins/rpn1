/* 
 * File:   FlowPlugin.h
 * Author: edsonlan
 *
 * Created on September 22, 2008, 6:23 PM
 */


#ifndef _FLOWPLUGIN_H
#define	_FLOWPLUGIN_H


#include "RpnPlugin.h"
#include "RpFunction.h"
#include "WaveFlow.h"



class WaveFlowPlugin : public RpnPlugin,public WaveFlow{

public:


    int jet (const WaveState &,JetMatrix & ,int) const;
    WaveFlowPlugin(const FluxFunction &);
    virtual~WaveFlowPlugin();
    RpFunction * clone() const;
    
};



#endif	/* _FLOWPLUGIN_H */

