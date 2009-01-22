#include "RarefactionFlowPlugin.h"
#include "Quad2FluxFunction.h"



int RarefactionFlowPlugin::flux(const RealVector &, RealVector &) const {
    return 2;
}

int RarefactionFlowPlugin::fluxDeriv(const RealVector &, JacobianMatrix &)const {
    return 2;
}

int RarefactionFlowPlugin::fluxDeriv2(const RealVector &, HessianMatrix &)const {
    return 2;
}




RarefactionFlowPlugin::RarefactionFlowPlugin(const int familyIndex, const int direction, const FluxFunction &flux) : RarefactionFlow(familyIndex, direction, flux) {

}

RarefactionFlowPlugin::~RarefactionFlowPlugin() {

}


extern "C" RpnPlugin * createRarefaction() {

    Quad2FluxParams fluxParams;

    Quad2FluxFunction fluxfunction(fluxParams);

    return new RarefactionFlowPlugin(0, 1, fluxfunction);
}

extern "C" void destroyRarefaction(RpnPlugin * plugin) {
    delete plugin;
}

