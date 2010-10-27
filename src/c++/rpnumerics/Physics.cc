#include "Physics.h"

Physics::~Physics() {

    delete physicsVector_;
}

Physics::Physics(const char * physicsID) : physicsVector_(new vector<SubPhysics *>()), ID_(physicsID) {
    if (!strcmp(physicsID, "QuadraticR2")) {
        physicsVector_->push_back(new Quad2(Quad2FluxParams()));
    }

    if (!strcmp(physicsID, "QuadraticR3")) {
        physicsVector_->push_back(new Quad3(Quad3FluxParams()));
    }

    if (!strcmp(physicsID, "QuadraticR4")) {
        physicsVector_->push_back(new Quad4(Quad4FluxParams()));
    }

    if (!strcmp(physicsID, "TriPhase")) {
        physicsVector_->push_back(new TriPhase(TriPhaseParams(), PermParams(), CapilParams(0.4, 3.0, 44.0, 8.0), ViscosityParams(0.5)));
    }

    if (!strcmp(physicsID, "Corey")) {
        physicsVector_->push_back(new Corey(CoreyParams(), PermParams(), CapilParams(0.4, 3.0, 44.0, 8.0), ViscosityParams(0.5)));
    }

    boundary_ = physicsVector_->at(0)->boundary().clone(); //TODO Using the default boundary.Replace
    space_=new Space(physicsVector_->at(0)->domain());
}

Physics::Physics(const vector<SubPhysics> & inputPhysicsVector, const Boundary & boundary, const char * id) : physicsVector_(new vector<SubPhysics*>()), boundary_(boundary.clone()), ID_(id) {

    for (unsigned int i = 0; i < inputPhysicsVector.size(); i++) {

        physicsVector_->push_back((SubPhysics *) inputPhysicsVector.at(i).clone());

    }
    boundary_ = boundary.clone();

    space_=new Space(physicsVector_->at(0)->domain());

}

Physics::Physics(const Physics & physics) : physicsVector_(new vector<SubPhysics*>()), boundary_(physics.boundary().clone()), ID_(ID()),space_(new Space(physics.domain())) {

    for (unsigned int i = 0; i < physics.getPhysicsVector().size(); i++) {

        physicsVector_->push_back(physics.getPhysicsVector().at(i)->clone());

    }

}

const Space & Physics::domain() const {
    return *space_;
}

const char * Physics::ID() const {
    return ID_;
}

const FluxFunction & Physics::fluxFunction() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //Receive index

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