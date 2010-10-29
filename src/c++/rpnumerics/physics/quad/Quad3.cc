#include "Quad3.h"

Quad3::Quad3(const Quad3FluxParams & params) : SubPhysics(Quad3FluxFunction(params), Quad3AccumulationFunction(), *defaultBoundary(),*new Space("R3", 3),"QuadraticR3") {

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

}


Quad3::~Quad3() {

  
}

Quad3::Quad3(const Quad3 & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(), *new Space("R3",3),"QuadraticR3"){


}
