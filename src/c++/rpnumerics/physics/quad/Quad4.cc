#include "Quad4.h"

Quad4::Quad4(const Quad4FluxParams & params) :SubPhysics(Quad4FluxFunction(params),Quad4AccumulationFunction(),*defaultBoundary(),*new Space("R4", 4),"QuadraticR4",_SIMPLE_ACCUMULATION_) {

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

}



Quad4::~Quad4() {

}

Quad4::Quad4(const Quad4 & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(),*new Space("R4",4),"QuadraticR4",_SIMPLE_ACCUMULATION_) {
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

}
