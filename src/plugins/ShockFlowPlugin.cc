#include "ShockFlowPlugin.h"

int ShockFlowPlugin::flux(const RealVector & input, RealVector & output) const {

    WaveState tempInput(input);
    JetMatrix tempOutput(input.size());
    fluxFunction().jet(tempInput, tempOutput, 0);

    RealVector fx(input.size());

    tempOutput.f(fx);

    fx -= (*fx0_);

    RealVector xMinusX0(input);
    xMinusX0 -= getParams().getPhasePoint();
    xMinusX0 *= getParams().getSigma();
    fx -= xMinusX0;

    output = fx;


    return 2;


}

int ShockFlowPlugin::fluxDeriv(const RealVector & input, JacobianMatrix &output) const {

    WaveState tempInput(input);
    JetMatrix tempOutput(input.size());
    fluxFunction().jet(tempInput, tempOutput, 1);

    tempOutput.jacobian(output);
    JacobianMatrix identity(input.size());
    identity.scale(getParams().getSigma());
    output - identity;

    return 2;



}

int ShockFlowPlugin::fluxDeriv2(const RealVector & input, HessianMatrix & output) const {

    WaveState tempInput(input);
    JetMatrix tempOutput(input.size());
    fluxFunction().jet(tempInput, tempOutput, 2);

    tempOutput.hessian(output);


    return 2;
}


const ShockFlowParams & ShockFlowPlugin::getParams()const {
    return ShockFlow::getParams();
}

void ShockFlowPlugin::setParams(const ShockFlowParams & params) {
    ShockFlow::setParams(params);
    updateZeroTerms();

}

void ShockFlowPlugin::updateZeroTerms() {

    const PhasePoint & xzero = getParams().getPhasePoint();

    WaveState input(xzero);
    JetMatrix output(xzero.size());
    fluxFunction().jet(input, output, 0);
    output.f(*fx0_);


}

ShockFlowPlugin::ShockFlowPlugin(const ShockFlowParams &params, const FluxFunction &flux) : ShockFlow(params, flux), fx0_(new RealVector(params.getPhasePoint())) {
    updateZeroTerms();

}

ShockFlowPlugin::~ShockFlowPlugin() {
    delete fx0_;
}


extern "C" RpnPlugin * createConservation() {

    PhasePoint phasePoint(RealVector(2));

    phasePoint.component(0) = 0;

    phasePoint.component(1) = 0;

    ShockFlowParams newParams(phasePoint, 0);

    Quad2FluxParams fluxParams;

    Quad2FluxFunction fluxfunction(fluxParams);

    return new ShockFlowPlugin(newParams, fluxfunction);
}

extern "C" void destroyConservation(RpnPlugin * plugin) {

    delete plugin;
}

