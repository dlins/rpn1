#include "Quad4.h"

Quad4::Quad4(const Quad4FluxParams & params) :SubPhysics(Quad4FluxFunction(params),Quad4AccumulationFunction(),*defaultBoundary(),*new Space("R4", 4),"QuadraticR4",_SIMPLE_ACCUMULATION_) {

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

}



Quad4::~Quad4() {

}

Quad4::Quad4(const Quad4 & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.getBoundary(),*new Space("R4",4),"QuadraticR4",_SIMPLE_ACCUMULATION_) {
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

}



vector<double> *  Quad4::getParams(){
     
     
     
     vector<double> * paramsVector = new vector<double>();
      
      for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
          paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

      return paramsVector;

}

SubPhysics * Quad4::clone()const {
    return new Quad4(*this);
}


 Boundary * Quad4::defaultBoundary()const {

    RealVector min(4);

    min.component(0) = -0.5;
    min.component(1) = -0.5;
    min.component(2) = -0.5;
    min.component(3) = -0.5;

    RealVector max(4);

    max.component(0) = 0.5;
    max.component(1) = 0.5;
    max.component(2) = 0.5;
    max.component(3) = 0.5;

    return new RectBoundary(min, max);

}