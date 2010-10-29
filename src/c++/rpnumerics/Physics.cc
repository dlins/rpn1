#include "Physics.h"

Physics::~Physics() {

    delete physicsVector_;
    delete ID_;
}

Physics::Physics(const string & physicsID) : physicsVector_(new vector<SubPhysics *>()), ID_(new string(physicsID)) {
    if (physicsID.compare("QuadraticR2")==0) {
        physicsVector_->push_back(new Quad2(Quad2FluxParams()));
    }

    if (physicsID.compare("QuadraticR3")==0) {
        physicsVector_->push_back(new Quad3(Quad3FluxParams()));
    }

    if (physicsID.compare( "QuadraticR4")==0) {
        physicsVector_->push_back(new Quad4(Quad4FluxParams()));
    }

    if (physicsID.compare( "TriPhase")==0) {
        physicsVector_->push_back(new TriPhase(TriPhaseParams(), PermParams(), CapilParams(0.4, 3.0, 44.0, 8.0), ViscosityParams(0.5)));
    }

    if (physicsID.compare( "Corey")==0) {
        physicsVector_->push_back(new Corey(CoreyParams(), PermParams(), CapilParams(0.4, 3.0, 44.0, 8.0), ViscosityParams(0.5)));
    }

    boundary_ = physicsVector_->at(0)->boundary().clone(); //TODO Using the default boundary.Replace
    space_=new Space(physicsVector_->at(0)->domain());
}

Physics::Physics(const vector<SubPhysics> & inputPhysicsVector, const Boundary & boundary, const string & id) : physicsVector_(new vector<SubPhysics*>()), boundary_(boundary.clone()), ID_( new string(id)){

    for (unsigned int i = 0; i < inputPhysicsVector.size(); i++) {

        physicsVector_->push_back((SubPhysics *) inputPhysicsVector.at(i).clone());

    }
    boundary_ = boundary.clone();

    space_=new Space(physicsVector_->at(0)->domain());

}

Physics::Physics(const Physics & physics) : physicsVector_(new vector<SubPhysics*>()), boundary_(physics.boundary().clone()), ID_(new string(physics.ID())),space_(new Space(physics.domain())) {

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