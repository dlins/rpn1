#include "Quad2.h"

Quad2::Quad2(const Quad2FluxParams & params) : SubPhysics(Quad2FluxFunction(params),Quad2AccumulationFunction(),*defaultBoundary(),Multid::PLANE,"QuadraticR2") {

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
}


Quad2::~Quad2() {
   

}

Quad2::Quad2(const Quad2 & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(),Multid::PLANE,"QuadraticR2") {



}