#include "GenericFluxParams.h"


GenericFluxParams::GenericFluxParams(void) :FluxParams(RealVector(2)) {
}

GenericFluxParams::GenericFluxParams(const RealVector & params) :FluxParams(params) {
}

GenericFluxParams::~GenericFluxParams() {
}

