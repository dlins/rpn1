#include "Physics.h"

Physics::~Physics() {

    delete physicsVector_;
    delete ID_;
}

Physics::Physics(const string & physicsID) : physicsVector_(new vector<SubPhysics *>()), ID_(new string(physicsID)) {
    if (physicsID.compare("QuadraticR2") == 0) {
        physicsVector_->push_back(new Quad2(Quad2FluxParams()));
    }

    if (physicsID.compare("QuadraticR3") == 0) {
        physicsVector_->push_back(new Quad3(Quad3FluxParams()));
    }

    if (physicsID.compare("QuadraticR4") == 0) {
        physicsVector_->push_back(new Quad4(Quad4FluxParams()));
    }

    if (physicsID.compare("TriPhase") == 0) {
        physicsVector_->push_back(new TriPhase(TriPhaseParams(), PermParams(), CapilParams(0.4, 3.0, 44.0, 8.0), ViscosityParams(0.5)));
    }

    if (physicsID.compare("Corey") == 0) {
        physicsVector_->push_back(new Corey(CoreyParams(), PermParams(), CapilParams(0.4, 3.0, 44.0, 8.0), ViscosityParams(0.5)));
    }

    if (physicsID.compare("Stone") == 0) {
        physicsVector_->push_back(new Stone());
    }

    if (physicsID.compare("TPCW") == 0) {
        // Create Thermodynamics
        double Tref_rock = 273.15;
        double Tref_water = 274.3775;
        double pressure = 100.9;
        double Cr = 2.029e6;
        double Cw = 4297.;
        double rhoW_init = 998.2;
        double T_typical = 304.63;
        double Rho_typical = 998.2; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
        double U_typical = 4.22e-6;
        double h_typical = Cw * (T_typical - Tref_water);

       

//                Thermodynamics_SuperCO2_WaterAdimensionalized * TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(Tref_rock, Tref_water, pressure,
//                        "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmac_spline.txt",
//                        "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmaw_spline.txt",
//                        "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoac_spline.txt",
//                        "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoaw_spline.txt",
//                        "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoW_spline.txt",
//                        "/home/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
//                        rhoW_init,
//                        Cr,
//                        Cw,
//                        T_typical,
//                        Rho_typical,
//                        h_typical,
//                        U_typical);


        string dataPath("/src/c++/rpnumerics/physics/tpcw/");
        Thermodynamics_SuperCO2_WaterAdimensionalized * TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(dataPath);



//
//        rpnHome.append(dataPath);
//
//        string splineFile("rhosigmac_spline.txt");
//
//
//        rpnHome.append(splineFile);
        //        char * destString = new char[strlen(source) + 10];
        //        destString[0] = 'a';
        //        char * novaString = strcat(destString, source);

//        cout << rpnHome << endl;


        //        delete destString;


        // Create the Flux and its params
        double abs_perm = 3e-12;
        double sin_beta = 0.0;
        double const_gravity = 9.8;
        bool has_gravity = false, has_horizontal = true;


        double cnw = 0., cng = 0., expw = 2., expg = 2.;
        FracFlow2PhasesHorizontalAdimensionalized *fh = new FracFlow2PhasesHorizontalAdimensionalized(cnw, cng, expw, expg, TD);
        FracFlow2PhasesVerticalAdimensionalized * fv = new FracFlow2PhasesVerticalAdimensionalized(cnw, cng, expw, expg, TD);


        Flux2Comp2PhasesAdimensionalized_Params * flux_params = new Flux2Comp2PhasesAdimensionalized_Params(abs_perm, sin_beta, const_gravity,
                has_gravity, has_horizontal, TD, fh, fv); // Check pointer fh and fv allocation

        FluxFunction * flux = new Flux2Comp2PhasesAdimensionalized(*flux_params);


        // Create the Accum and its params
        double phi = 0.38;
        Accum2Comp2PhasesAdimensionalized_Params * accum_params = new Accum2Comp2PhasesAdimensionalized_Params(TD, &phi);
        AccumulationFunction * accum = new Accum2Comp2PhasesAdimensionalized(*accum_params);

        physicsVector_->push_back(new TPCW((FluxFunction&) * flux, (AccumulationFunction&) * accum, *TD));

        delete flux_params;
        delete flux;
        delete accum_params;
        delete accum;
        //        delete fh;
        //        delete fv;


    }




    boundary_ = physicsVector_->at(0)->boundary().clone(); //TODO Using the default boundary.Replace
    space_ = new Space(physicsVector_->at(0)->domain());
}

Physics::Physics(const vector<SubPhysics> & inputPhysicsVector, const Boundary & boundary, const string & id) : physicsVector_(new vector<SubPhysics*>()), boundary_(boundary.clone()), ID_(new string(id)) {

    for (unsigned int i = 0; i < inputPhysicsVector.size(); i++) {

        physicsVector_->push_back((SubPhysics *) inputPhysicsVector.at(i).clone());

    }
    boundary_ = boundary.clone();

    space_ = new Space(physicsVector_->at(0)->domain());

}

Physics::Physics(const Physics & physics) : physicsVector_(new vector<SubPhysics*>()), boundary_(physics.boundary().clone()), ID_(new string(physics.ID())), space_(new Space(physics.domain())) {

    for (unsigned int i = 0; i < physics.getPhysicsVector().size(); i++) {

        physicsVector_->push_back(physics.getPhysicsVector().at(i)->clone());

    }

}

const Space & Physics::domain() const {
    return *space_;
}

const string & Physics::ID() const {
    return *ID_;
}

const FluxFunction & Physics::fluxFunction() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->fluxFunction();
}

const AccumulationFunction & Physics::accumulation() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->accumulation();
}

void Physics::fluxParams(const FluxParams &newParams) {

    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->fluxParams(newParams);


}

const AccumulationParams & Physics::accumulationParams() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->accumulation().accumulationParams();

}

const vector<SubPhysics *> & Physics::getPhysicsVector() const {

    return *physicsVector_;

}

void Physics::accumulationParams(const AccumulationParams & newParams) {
    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->accumulationParams(newParams);


}

void Physics::boundary(const Boundary & boundary) {

    delete boundary_;
    boundary_ = boundary.clone();
}

const Boundary & Physics::boundary() const {
    return *boundary_;
}

Physics * Physics::clone()const {

    return new Physics(*this);
}

