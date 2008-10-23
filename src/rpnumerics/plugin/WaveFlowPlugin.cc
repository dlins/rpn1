#include "WaveFlowPlugin.h"

int WaveFlowPlugin::jet(const WaveState & input, JetMatrix & output, int degree) const {

    return 0;
    
}

WaveFlowPlugin::WaveFlowPlugin(const FluxFunction &flux):WaveFlow(flux){}

WaveFlowPlugin::~WaveFlowPlugin(){}

RpFunction * WaveFlowPlugin::clone() const {

    return new WaveFlowPlugin(flux());
}

