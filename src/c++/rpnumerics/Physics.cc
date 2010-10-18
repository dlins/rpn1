#include "Physics.h"
#include "Quad2FluxFunction.h"

Physics::~Physics() {

    delete fluxVector_;
}

Physics::Physics():fluxVector_(new vector<FluxFunction*>()) {
    cout << "chamando construtor default Physics" << endl;
    fluxVector_->push_back (new Quad2FluxFunction(Quad2FluxParams()));
}

const FluxFunction & Physics:: fluxFunction() const{

    cout<<"chamando get fluxFunction"<<endl;

    return *fluxVector_->at(0);
}

Physics::Physics(vector<FluxFunction > inputFluxVector):fluxVector_(new vector<FluxFunction*>()) {

    cout << "chamando construtor de Physics com vector" << endl;
    for (unsigned int i=0; i < inputFluxVector.size();i++){

        fluxVector_->push_back((FluxFunction *)inputFluxVector.at(i).clone());

    }

}

Physics::Physics(const Physics & physics):fluxVector_(new vector<FluxFunction*>()) {
    cout <<"Chamando construtor de copia de physics"<<endl;
    fluxVector_->push_back(new Quad2FluxFunction(Quad2FluxParams()));
}

