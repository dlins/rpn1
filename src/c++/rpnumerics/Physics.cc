#include <vector>

#include "Physics.h"


string Physics::rpnHome_ = string();

void Physics::setRPnHome(const string & newRPnHome) {
    rpnHome_ = newRPnHome;
}

const string & Physics::getRPnHome() {
    return rpnHome_;
}

void Physics::setParams(vector<string> paramVector) {
//
//    for (int i = 0; i < paramVector.size(); i++) {
//        cout << paramVector[i] << " Chamando setParams Physics" << endl;
//    }

    GridValues & grid = getGrid(0);

    grid.e_computed=false;
    grid.Jacobians_on_grid_computed=false;
    grid.functions_on_grid_computed=false;
    grid.dd_computed = false;




    physicsVector_->at(0)->setParams(paramVector);

}

Physics::~Physics() {

    delete physicsVector_;
    delete ID_;
    delete gridArray_;
}

Physics::Physics(const string & physicsID) : physicsVector_(new vector<SubPhysics *>()), ID_(new string(physicsID)), gridArray_(new vector<GridValues *>()) {
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


    if (physicsID.compare("Polydisperse") == 0) {

        physicsVector_->push_back(new PolydispersePhysics());
    }


    if (physicsID.compare("Cub2") == 0) {
        physicsVector_->push_back(new Cub2(Cub2FluxParams()));
    }


    if (physicsID.compare("TPCW") == 0) {

        RealVector params(12);
        //Flux parameters
        params.component(0) = 3e-12;
        params.component(1) = 0.0;
        params.component(2) = 0.0;
        params.component(3) = 1.0;
        //Horizontal and vertical flux parameters
        params.component(4) = 0.0;
        params.component(5) = 0.0;
        params.component(6) = 2.0;
        params.component(7) = 2.0;

        //Accumulation parameters

        params.component(8) = 0.38;

        //Thermodynamics parameters
        params.component(9) = 304.63;
        params.component(10) = 998.2;
        params.component(11) = 4.22e-3;


        physicsVector_->push_back(new TPCW(params, getRPnHome()));



    }


    boundary_ = physicsVector_->at(0)->boundary().clone(); //TODO Using the default boundary.Replace
    space_ = new Space(physicsVector_->at(0)->domain());



    const Boundary * b = &boundary();

    std::vector<int> noc(2);
    noc[0] = 124;
    noc[1] = 124;


    gridArray_->push_back(new GridValues(b, b->minimums(), b->maximums(), noc));


}

Physics::Physics(const vector<SubPhysics> & inputPhysicsVector, const Boundary & boundary, const string & id) : physicsVector_(new vector<SubPhysics*>()), boundary_(boundary.clone()), ID_(new string(id)) {

    for (unsigned int i = 0; i < inputPhysicsVector.size(); i++) {

        physicsVector_->push_back((SubPhysics *) inputPhysicsVector.at(i).clone());

    }



}

Physics::Physics(const Physics & physics) : physicsVector_(new vector<SubPhysics*>()), boundary_(physics.boundary().clone()), ID_(new string(physics.ID())), space_(new Space(physics.domain())), gridArray_(new vector<GridValues *>()) {

    for (unsigned int i = 0; i < physics.getPhysicsVector().size(); i++) {

        physicsVector_->push_back(physics.getPhysicsVector().at(i)->clone());

    }



    const Boundary * b = boundary_;

    std::vector<int> noc(2);
    noc[0] = 128;
    noc[1] = 128;




    gridArray_->push_back(new GridValues(b, b->minimums(), b->maximums(), noc));


}

SubPhysics & Physics::getSubPhysics(const int index) {

    return *physicsVector_->at(index);

}

GridValues & Physics::getGrid(const int index) const {
    return *gridArray_->at(index);
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

