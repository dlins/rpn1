/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ConservationShockFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ConservationShockFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

//ConservationShockFlow::ConservationShockFlow(const ShockFlowParams & params,const FluxFunction & flux) :fluxFunction_((FluxFunction *) flux.clone()),flowParams_(new ShockFlowParams(params.getPhasePoint(),params.getSigma())) {

ConservationShockFlow::ConservationShockFlow(const ShockFlowParams & params, const FluxFunction & flux) : WaveFlow(flux), flowParams_(new ShockFlowParams(params.getPhasePoint(), params.getSigma())) {

    fx0_ = new RealVector(2); //TODO Hardcoded !! To be initalizated in updateZeroTerms method

}

//ConservationShockFlow::ConservationShockFlow(const ConservationShockFlow & copy):fluxFunction_((FluxFunction *) copy.fluxFunction().clone()),flowParams_(new ShockFlowParams(copy.getParams())){

ConservationShockFlow::ConservationShockFlow(const ConservationShockFlow & copy) : WaveFlow(copy.fluxFunction()), flowParams_(new ShockFlowParams(copy.getParams())) {
    fx0_ = new RealVector(2);
}

ConservationShockFlow::~ConservationShockFlow() {


    delete flowParams_;
    delete fx0_;
}

int ConservationShockFlow::flux(const WaveState & input, JetMatrix &output) const {

    fluxFunction().jet(input, output, 0);

    RealVector fx(input.stateSpaceDim());

    //    fx.sub(&fx0_);

    output.f(fx);


    fx = fx - (*fx0_);



    //        RealVector fx = getFlux().F(x);//RPNUMERICS.fluxFunction().F(x);
    // F(x) - F(x0)
    //    fx.sub(fx0_);
    // x - x0_
    RealVector xMinusX0(input.stateSpaceDim());

    xMinusX0 = xMinusX0 - flowParams_->getPhasePoint();


    //    xMinusX0.sub(flowParams_.getPhasePoint().getCoords());
    // (x - x0_)*sigma
    //    xMinusX0.scale(getSigma());
    xMinusX0 = flowParams_->getSigma() * xMinusX0; //Teste


    // (Fx - Fx0) - sigma*(x - x0)
    //    fx.sub(xMinusX0);

    fx = fx - xMinusX0;

    output.setF(fx);


    //    WavePoint returned = new WavePoint(fx, getSigma());

    return 2;

    //    return fx;


}

int ConservationShockFlow::fluxDeriv(const WaveState & input, JetMatrix &output) const {


    fluxFunction().jet(input, output, 1);
   
    
    JacobianMatrix fluxDF_x(input.stateSpaceDim());


    output.jacobian(fluxDF_x);


    //

    //    for (int i = 0; i < output.n_comps(); i++) {
    //        for (int j = 0; j < output.n_comps(); j++) {
    //            fluxDF_x(i,j,output(i, j));
    //        }
    //    }


    //        RealMatrix2 fluxDF_x = getFlux().DF(x);//RPNUMERICS.fluxFunction().DF(x);
    // flux.DFX(x) - sigma scaled matrix

    //    RealMatrix2 identity(output.n_comps());

    JacobianMatrix identity(input.stateSpaceDim());



    identity.scale(flowParams_->getSigma());


    fluxDF_x - identity;

    output.setJacobian(fluxDF_x);



    return 2;


    //    return fluxDF_x;
}

int ConservationShockFlow::fluxDeriv2(const WaveState & input, JetMatrix & output) const {

    HessianMatrix hessianMatrix(input.stateSpaceDim());
    JetMatrix tempOutput(input.stateSpaceDim());
    fluxFunction().jet(input, tempOutput, 2);

    tempOutput.hessian(hessianMatrix);

    output.setHessian(hessianMatrix);

    return 2;




}

int ConservationShockFlow::jet(const WaveState & x, JetMatrix & y, int degree = 2)const {

    switch (degree) {

        case 0:

            flux(x, y);


            break;

        case 1:

            flux(x, y);
            fluxDeriv(x, y);

            break;

        case 2:

            flux(x, y);
            fluxDeriv(x, y);
            fluxDeriv2(x, y);


            break;

    }

    return 2; //SUCCESSFUL_PROCEDURE;
}



