#include "MyFluxParams.h"


MyFluxParams::MyFluxParams(void) :FluxParams(RealVector(2)) {
}

MyFluxParams::MyFluxParams(const RealVector & params) :FluxParams(params) {
}

MyFluxParams::~MyFluxParams() {
}

