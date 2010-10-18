#include "Quad2.h"

Quad2::Quad2(const Quad2FluxParams & params) : fluxFunction_(new Quad2FluxFunction(params)), accumulationFunction_(new Quad2AccumulationFunction()),
boundary_(defaultBoundary()) {
    FLUX_ID = "QuadraticR2";
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
}

const char * Quad2::ID(void) const {
    return FLUX_ID;
}

Quad2::~Quad2() {

    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;
}

Quad2::Quad2(const Quad2 & copy) {
    cout <<"chamando construtor de copia de quad2"<<endl
    fluxFunction_ = new Quad2FluxFunction((Quad2FluxFunction&) copy.fluxFunction());
    //    fluxVector_->push_back(new Quad2FluxFunction(Quad2FluxParams()));
    accumulationFunction_ = new Quad2AccumulationFunction((Quad2AccumulationFunction &) copy.accumulation());
    boundary_ = copy.boundary().clone();
    FLUX_ID = "QuadraticR2";

}

Quad2::Quad2(vector<Quad2FluxFunction> inputFluxVector) {
    cout << "chamando construtor de quad2 com vetor" << endl;
    fluxFunction_=new Quad2FluxFunction(params);
    accumulationFunction_=new Quad2AccumulationFunction();
    boundary_=defaultBoundary();



    FLUX_ID = "QuadraticR2";


    //    for (unsigned int i=0; i < inputFluxVector.size();i++){
    //
    //        fluxVector_->push_back(new Quad2FluxFunction(Quad2FluxParams()));
    //
    //    }


}