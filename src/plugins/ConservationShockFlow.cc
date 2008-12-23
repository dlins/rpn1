#include "ConservationShockFlow.h"

int ShockFlowPlugin::jet(const WaveState & input, JetMatrix & output, int degree) const {

    cout <<"ShockFlowPlugin jet (default)"<<"\n";
//--------------------Stub-----------------------------------
    int dimension = input.stateSpaceDim();

    RealVector F(dimension);

    JacobianMatrix jacobian(dimension);

    jacobian.operator ()(0, 0, 0.1);
    jacobian.operator ()(0, 1, 0.2);
    jacobian.operator ()(1, 0, 0.3);
    jacobian.operator ()(1, 1, 0.4);

    F.component(0) = 0.1;
    F.component(1) = 0.2;

    output.setF(F);
    output.setJacobian(jacobian);

    return 2;
//------------------------------------------------------------------
    
    
    
    
}

const ShockFlowParams & ShockFlowPlugin::getParams()const {
    return ShockFlow::getParams();
}

void ShockFlowPlugin::setParams(const ShockFlowParams & params) {
    ShockFlow::setParams(params);

}

ShockFlowPlugin::ShockFlowPlugin(const ShockFlowParams &params, const FluxFunction &flux) : ShockFlow(params, flux) {
}

ShockFlowPlugin::~ShockFlowPlugin() {
}

RpFunction * ShockFlowPlugin::clone() const {

    return new ShockFlowPlugin(getParams(), fluxFunction());
}

extern "C" RpnPlugin * create() {

    PhasePoint phasePoint(RealVector(2));

    ShockFlowParams newParams(phasePoint, 0);

    Quad2FluxParams fluxParams;

    Quad2FluxFunction fluxfunction(fluxParams);

    return new ShockFlowPlugin(newParams, fluxfunction);
}

extern "C" void destroy(RpnPlugin * plugin) {
    delete plugin;
}

