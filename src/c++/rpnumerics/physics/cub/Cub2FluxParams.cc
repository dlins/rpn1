#include "Cub2FluxParams.h"




Cub2FluxParams::Cub2FluxParams(void) : FluxParams(RealVector(18)) {


  


    component(0, 1.0); //A1

    component(1, -1.0); //B1

    component(2, 0.0); //C1

    component(3, 0.0); //D1

    component(4, 0.0); //E1

    component(5, 1.0); //F1

    component(6, 0.0); //G1

    component(7, 0.0); //H1

    component(8, 0.0); //K1




    component(9, 1.0); //A2

    component(10, -1.0); //B2

    component(11, 0.0); //C2

    component(12, 0.0); //D2

    component(13, 0.0); //E2

    component(14, 1.0); //F2

    component(15, 0.0); //G2

    component(16, 0.0); //H2

    component(17, 0.0); //K2










}

Cub2FluxParams::~Cub2FluxParams() {
}

Cub2FluxParams::Cub2FluxParams(const RealVector & params) : FluxParams(params) {
}



