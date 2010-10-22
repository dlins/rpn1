#include "Physics.h"

Physics::~Physics() {

    delete physicsVector_;
}

const FluxFunction & Physics::fluxFunction() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //Receive index

    return subPhysics->fluxFunction();
}

Physics::Physics(const vector<SubPhysics> & inputPhysicsVector ,const Boundary & boundary) : physicsVector_(new vector<SubPhysics*>()),boundary_(boundary.clone()) {

    cout << "chamando construtor de Physics com vector" << endl;
    for (unsigned int i = 0; i < inputPhysicsVector.size(); i++) {

        physicsVector_->push_back((SubPhysics *) inputPhysicsVector.at(i).clone());

    }

}

Physics::Physics(const Physics & physics) : physicsVector_(new vector<SubPhysics*>()) {

    for (unsigned int i = 0; i < physics.getPhysicsVector().size(); i++) {

        physicsVector_->push_back(physics.getPhysicsVector().at(i)->clone());

    }

}

const AccumulationFunction & Physics::accumulation() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->accumulation();
}

void Physics::fluxParams(const FluxParams &) {




}

const AccumulationParams & Physics::accumulationParams() const {

    SubPhysics * subPhysics = physicsVector_->at(0); //TODO Receive index

    return subPhysics->accumulation().accumulationParams();

}

const vector<SubPhysics *> & Physics::getPhysicsVector() const {

    return *physicsVector_;

}

const Boundary & Physics::boundary() const {
    return *boundary_;
}