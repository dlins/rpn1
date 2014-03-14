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
#include "Double_Contact_TP.h"

#include "Debug.h"

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

    if ( Debug::get_debug_level() == 5 ) {
        cout << "Flux: " << fluxFunction_ << endl;
        cout << "Accum: " << accumulationFunction_ << endl;
    }


    setHugoniotFunction(new Hugoniot_TP(&fluxFunction(),&accumulation()));
    setDoubleContactFunction(new Double_Contact_TP());

    RealVector min(getBoundary().minimums());

    RealVector max(getBoundary().maximums());

    setViscosityMatrix(new Viscosity_Matrix());


    preProcess(min);

    preProcess(max);

    preProcessedBoundary_ = new RectBoundary(min, max);

    setHugoniotContinuationMethod(new HugoniotContinuation3D2D(fluxFunction_,accumulationFunction_,preProcessedBoundary_));


}

void TPCW::boundary(const Boundary & newBoundary) {

    SubPhysics::boundary(newBoundary);

    RealVector min(getBoundary().minimums());

    RealVector max(getBoundary().maximums());

    preProcess(min);

    preProcess(max);

    preProcessedBoundary_ = new RectBoundary(min, max);

}

void TPCW::setParams(vector<string> params) {



    TD->setTtypical(atof(params[9].c_str()));
    TD->setRhoTypical(atof(params[10].c_str()));
    TD->UTypical(atof(params[11].c_str()));

    RealVector fluxParamVector(8);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {
        std::stringstream stream(params[i]);
        stream >> paramValue;

        fluxParamVector.component(i) = paramValue;
    }



    fluxFunction_->fluxParams(Flux2Comp2PhasesAdimensionalized_Params(fluxParamVector, TD));


    accumulationFunction_->accumulationParams(Accum2Comp2PhasesAdimensionalized_Params(TD, atof(params[8].c_str())));



}

vector<double> * TPCW::getParams() {



    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

    for (int i = 0; i < accumulationFunction_->accumulationParams().params().size(); i++) {


        paramsVector->push_back(accumulationFunction_->accumulationParams().component(i));


    }




    return paramsVector;

}

TPCW::TPCW(const TPCW & copy) :
SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), *new Space("R3", 3), "TPCW", _GENERAL_ACCUMULATION_),
TD(new Thermodynamics_SuperCO2_WaterAdimensionalized(*copy.TD)) {


    setHugoniotFunction(new Hugoniot_TP(&copy.fluxFunction(),&copy.accumulation()));
    setDoubleContactFunction(new Double_Contact_TP());
//    setShockMethod(new ShockContinuationMethod3D2D());

    
    setViscosityMatrix(new Viscosity_Matrix());

    RealVector min = copy.preProcessedBoundary_->minimums();
    RealVector max = copy.preProcessedBoundary_->maximums();


    preProcessedBoundary_ = new RectBoundary(min, max);

//    setShockMethod (new ShockContinuationMethod3D2D());
    setHugoniotContinuationMethod(new HugoniotContinuation3D2D(fluxFunction_,accumulationFunction_,copy.getPreProcessedBoundary()));

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
    min.component(2) = 0 * 4.22e-3;


    if ( Debug::get_debug_level() == 5 ) {
        cout <<min.component(0)<<"<--------MIN 0"<<endl;
        cout << min.component(1) << "<--------MIN 1" << endl;
        cout << min.component(2) << "<--------MIN 2" << endl;
    }

    RealVector max(3);

    max.component(0) = 1.0;

    max.component(1) = 450;
    //    max.component(2) = TD->u2U(2 * 4.22e-5);
    max.component(2) = 2 * 4.22e-3; // The domain is 20 times as much as U_typical


    if ( Debug::get_debug_level() == 5 ) {
        cout <<max.component(0)<<"<----------MAX 0"<<endl;
        cout << max.component(1) << "<-------MAX 1" << endl;
        cout << max.component(2) << "<------MAX 2" << endl;
    }

    return new RectBoundary(min, max);

}

void TPCW::preProcess(RealVector & input) {
    input.component(1) = TD->T2Theta(input.component(1));
    if (input.size() == 3) {

        input.component(2) = TD->u2U(input.component(2));

    }

}

void TPCW::postProcess(RealVector & input) {

    RealVector temp(input);


    input.resize(3);
    input.component(0) = temp.component(0);
    input.component(1) = TD->Theta2T(temp.component(1));
    input.component(2) = getBoundary().maximums().component(2);

    //    input.component(2) = TD->U2u(temp.component(2));
}

void TPCW::postProcess(vector<RealVector> & input) {

    int inputSize = input[0].size();

    for (int i = 0; i < input.size(); i++) {

        switch (inputSize) {
            case 4://Rarefaction
                input[i].component(1) = TD->Theta2T(input[i].component(1));
                input[i].component(2) = TD->U2u(input[i].component(2)); // redimensionaliaztion of the darcy speed.
                input[i].component(3) = TD->U2u(input[i].component(3)); // redimensionalization of the the eigenvalue.
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
                input[i].component(2) = getBoundary().maximums().component(2);

                break;

        }


    }

}

TPCW::~TPCW() {
    delete TD;
    delete preProcessedBoundary_;

}


