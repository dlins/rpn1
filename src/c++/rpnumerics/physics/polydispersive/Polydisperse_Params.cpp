#include "Polydisperse_Params.h"

Polydisperse_Params::Polydisperse_Params(const double phimax,
                           const double rho1, const double rho2,
                           const double dVinf1, const double dVinf2,
                           const double n1, const double n2) : FluxParams(RealVector(7)){
    // Maximum packing concentration
    component(0, phimax);

    // Particle densities, the fluid density rho_f and the "Stokes constant" mu were normalized
    //                     thus rho1 is actually rho1/rho_f
    component(1, rho1);
    component(2, rho2);
                      
    // We can introduce for
    //     MLB model:  particle diameters d1 and d2, with [dVinf1 = d1^2] and [dVinf2 = d2^2]
    // Bassoon model:  Stokes velocities Vinf1 and Vinf2, with [dVinf1 = - Vinf1] and [dVinf2 = - Vinf2]
    component(3, dVinf1);
    component(4, dVinf2);

    // Velocity exponents
    component(5, n1);
    component(6, n2);
}

Polydisperse_Params::Polydisperse_Params() : FluxParams(RealVector(7)){
    // The initial data is for the MLB model
    component(0, 1.0);

    component(1, 2.0);
    component(2, 3.0);
                      
    component(3, 1.0);
    component(4, 0.25);

    component(5, 1.0);
    component(6, 0.5);
}

Polydisperse_Params::~Polydisperse_Params(){
}
