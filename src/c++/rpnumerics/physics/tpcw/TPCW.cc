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

TPCW::TPCW(const TPCW& copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), *new Space("R3", 3), "TPCW", _GENERAL_ACCUMULATION_) {

    Flux2Comp2PhasesAdimensionalized_Params & params = (Flux2Comp2PhasesAdimensionalized_Params &) copy.fluxFunction().fluxParams();
    cout << "Tamanho de params" << params.params().size() << endl;

    Accum2Comp2PhasesAdimensionalized_Params & accParams = (Accum2Comp2PhasesAdimensionalized_Params &)copy.accumulation().accumulationParams();

    double T_Typical = 304.63;
    double Rho_Typical = 998.2;
    double U_Typical = 4.22e-3;


    const string testString("/home/edsonlan/Java/rpn");

    TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(testString, T_Typical, Rho_Typical, U_Typical);

    RealVector refVec(3);

    refVec.component(0)=0.0;
    refVec.component(1) = 0.0;
    refVec.component(2) = 0.0;


    HugoniotFunctionClass * tpcwHugoniot = new ReducedTPCWHugoniotFunctionClass(refVec,accParams.getPhi(), params.component(0), 9.8, TD,params.get_horizontal());

    setHugoniotFunction(tpcwHugoniot);



}

TPCW::TPCW(const RealVector & params) : SubPhysics(*defaultBoundary(), *new Space("R3", 3), "TPCW", _GENERAL_ACCUMULATION_) {

    //Flux parameters

    cout << "Aqui" << endl;
    double abs_perm = params.component(0);
    double sin_beta = params.component(1);
    double const_gravity = 9.8;

    //Horizontal and vertical flux parameters

    bool has_gravity;

    if (params.component(2) == 0.0)
        has_gravity = false;
    else
        has_gravity = true;



    bool has_horizontal;
    if (params.component(3) == 0.0)
        has_horizontal = false;
    else
        has_horizontal = true;


    double cnw = params.component(4);
    double cng = params.component(5);
    double expw = params.component(6);
    double expg = params.component(7);

    //Thermodynamics parameters


    double T_Typical = params.component(9);
    double Rho_Typical = params.component(10);
    double U_Typical = params.component(11);


    //Accumulation parameters

    double phi = params.component(8);

    const string testString("/home/edsonlan/Java/rpn");

    TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(testString, T_Typical, Rho_Typical, U_Typical);

    fh = new FracFlow2PhasesHorizontalAdimensionalized(cnw, cng, expw, expg, TD);
    fv = new FracFlow2PhasesVerticalAdimensionalized(cnw, cng, expw, expg, TD);


    Flux2Comp2PhasesAdimensionalized_Params flux_params(abs_perm, sin_beta, const_gravity,
            has_gravity, has_horizontal, TD, fh, fv); // Check pointer fh and fv allocation


    Accum2Comp2PhasesAdimensionalized_Params accum_params(TD, phi);


    accumulationFunction_ = new Accum2Comp2PhasesAdimensionalized(accum_params);

    fluxFunction_ = new Flux2Comp2PhasesAdimensionalized(flux_params);


    RealVector refVec(2);

    refVec.component(0)=0.0;
    refVec.component(1) = 0.0;
//    refVec.component(2) = 0.0;


    HugoniotFunctionClass * tpcwHugoniot = new ReducedTPCWHugoniotFunctionClass(refVec,phi, abs_perm, 9.8, TD,fh);

    setHugoniotFunction(tpcwHugoniot);


}

SubPhysics * TPCW::clone() const {
    cout << "Chamando clone" << endl;
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
    //    max.component(1) = T2Theta(450);
    max.component(1) = 450;
    //    max.component(2) = TD->u2U(2 * 4.22e-5);
    max.component(2) = 2 * 4.22e-3;

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

    int inputSize = input[0].size();

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

    delete fv;
    delete fh;
    delete TD;
}


