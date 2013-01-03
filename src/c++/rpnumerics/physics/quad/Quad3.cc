#include "Quad3.h"

Quad3::Quad3(const Quad3FluxParams & params) : SubPhysics(Quad3FluxFunction(params), Quad3AccumulationFunction(), *defaultBoundary(),*new Space("R3", 3),"QuadraticR3",_SIMPLE_ACCUMULATION_) {

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

}


Quad3::~Quad3() {

  
}

Quad3::Quad3(const Quad3 & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.getBoundary(), *new Space("R3",3),"QuadraticR3",_SIMPLE_ACCUMULATION_){


}


vector<double> *  Quad3::getParams(){
     
     
     
     vector<double> * paramsVector = new vector<double>();
      
      for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
          paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

      return paramsVector;

}

 SubPhysics * Quad3::clone()const {
    return new Quad3(*this);
}


Boundary * Quad3::defaultBoundary()const {



    RealVector min(3);

    min.component(0) = -10.5;
    min.component(1) = -10.5;
    min.component(2) = -10.5;

    RealVector max(3);

    max.component(0) = 10.5;
    max.component(1) = 10.5;
    max.component(2) = 10.5;

    return new RectBoundary(min, max);

}


