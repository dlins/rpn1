/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TPCW.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "TPCW.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


TPCW::TPCW(const RealVector & paramsVector, const string & rpnHomePath) :
SubPhysics(*defaultBoundary(), *new Space("R3", 3), "TPCW", _GENERAL_ACCUMULATION_) {


    RealVector fluxVector(8);


    fluxVector.component(0) = paramsVector.component(0);
    fluxVector.component(1) = paramsVector.component(1);
    fluxVector.component(2) = paramsVector.component(2);
    fluxVector.component(3) = paramsVector.component(3);
    fluxVector.component(4) = paramsVector.component(4);
    fluxVector.component(5) = paramsVector.component(5);
    fluxVector.component(6) = paramsVector.component(6);
    fluxVector.component(7) = paramsVector.component(7);


    // T_Typical,Rho_Typical,U_Typical

    TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(rpnHomePath, paramsVector.component(9), paramsVector.component(10), paramsVector.component(11));


    fluxFunction_ = new Flux2Comp2PhasesAdimensionalized(Flux2Comp2PhasesAdimensionalized_Params(fluxVector, TD));
    accumulationFunction_ = new Accum2Comp2PhasesAdimensionalized(Accum2Comp2PhasesAdimensionalized_Params(TD, paramsVector.component(8)));


    RealVector Uref(3);
    Uref.component(0) = 0;
    Uref.component(1) = 0;
    Uref.component(2) = 0;

    ReducedTPCWHugoniotFunctionClass * tpcwhc = new ReducedTPCWHugoniotFunctionClass(Uref, (Flux2Comp2PhasesAdimensionalized*) fluxFunction_, (Accum2Comp2PhasesAdimensionalized *) accumulationFunction_);
    //
    setHugoniotFunction(tpcwhc);





}


TPCW::TPCW(const TPCW & copy) :
SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), *new Space("R3", 3), "TPCW", _GENERAL_ACCUMULATION_),
TD(new Thermodynamics_SuperCO2_WaterAdimensionalized(*copy.TD)) {

    ReducedTPCWHugoniotFunctionClass * tpcwhc = new ReducedTPCWHugoniotFunctionClass((ReducedTPCWHugoniotFunctionClass &) * copy.getHugoniotFunction());
    setHugoniotFunction(tpcwhc);

}

SubPhysics * TPCW::clone() const {

    return new TPCW(*this);
}

double TPCW::T2Theta(double T)const {
    return TD->T2Theta(T);
}

double TPCW::Theta2T(double theta)const {

    return TD->Theta2T(theta);
}

Boundary * TPCW::defaultBoundary()const {

    RealVector min(3);

    min.component(0) = 0;
    //    min.component(1) = T2Theta(304.63);
    min.component(1) = 304.63;
    //    min.component(2) = TD->u2U(0);
    min.component(2) = 1 * 4.22e-3;


    //    cout <<min.component(0)<<"<--------MIN 0"<<endl;
    //    cout << min.component(1) << "<--------MIN 1" << endl;
    //    cout << min.component(2) << "<--------MIN 2" << endl;

    RealVector max(3);

    max.component(0) = 1.0;

    max.component(1) = 450;
    //    max.component(2) = TD->u2U(2 * 4.22e-5);
    max.component(2) = 2 * 4.22e-3; // The domain is 20 times as much as U_typical


    //    cout <<max.component(0)<<"<----------MAX 0"<<endl;
    //    cout << max.component(1) << "<-------MAX 1" << endl;
    //    cout << max.component(2) << "<------MAX 2" << endl;


    return new RectBoundary(min, max);

}

void TPCW::preProcess(RealVector & input) {
    input.component(1) = TD->T2Theta(input.component(1));
    if (input.size() == 3) {

        input.component(2) = TD->u2U(input.component(2));

    }

}

void TPCW::postProcess(vector<RealVector> & input) {
    cout << "Antes tamanho do vetor de saida do double contact: "<< endl;

    int inputSize = input[0].size();
    cout << "Depois tamanho do vetor de saida do double contact: "<<input[0].size()<< endl;


    for (int i = 0; i < input.size(); i++) {

        switch (inputSize) {
            case 4://Rarefaction
                input[i].component(1) = TD->Theta2T(input[i].component(1));
                input[i].component(2) = TD->U2u(input[i].component(2));
                input[i].component(3) = TD->U2u(input[i].component(3));
                break;

            case 8://Shock
                input[i].component(1) = TD->Theta2T(input[i].component(1));
                input[i].component(2) = TD->U2u(input[i].component(2));
                input[i].component(5) = TD->U2u(input[i].component(5));
                break;

            case 7: //Coincidence,BL,etc 
                input[i].component(2) = TD->U2u(input[i].component(2));
                input[i].component(1) = TD->Theta2T(input[i].component(1));

                break;

            case 2://Double contact , etc
                RealVector temp(input[i]);

                input[i].resize(3);
                input[i].component(0) = temp.component(0);
                input[i].component(1) = TD->Theta2T(temp.component(1));
                input[i].component(2) = boundary().maximums().component(2);
                break;

        }


    }

}

TPCW::~TPCW() {
    delete TD;

}


