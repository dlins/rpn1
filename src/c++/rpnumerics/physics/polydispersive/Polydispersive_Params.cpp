#include "Polydispersive_Params.h"

Polydispersive_Params::Polydispersive_Params(const double phimax,
                           const double V1inf, const double V2inf,
                           const double n1, const double n2) : FluxParams(RealVector(5)){
    component(0, phimax);

    component(1, V1inf);
    component(2, V2inf);
                      
    component(3, n1);
    component(4, n2);
}

Polydispersive_Params::Polydispersive_Params() : FluxParams(RealVector(5)){
    component(0, 1.0);

    component(1, 1.0);
    component(2, 0.5);
                      
    component(3, 2.0);
    component(4, 3.0);
}

Polydispersive_Params::~Polydispersive_Params(){
}
