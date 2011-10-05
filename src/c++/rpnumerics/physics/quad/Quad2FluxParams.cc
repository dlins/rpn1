#include "Quad2FluxParams.h"


const double Quad2FluxParams::DEFAULT_A[2] = {0., 0.};
const double Quad2FluxParams::DEFAULT_B[2][2] = {
    { 0., .1},
    { -.1, 0.}
};
const double Quad2FluxParams::DEFAULT_C[2][2][2] = {
    {
        { -1., 0.},
        { 0., 1.}
    },
    {
        { 0., 1.},
        { 1., 0.}
    }
};
//

Quad2FluxParams::Quad2FluxParams(double a, double b) : FluxParams(RealVector(10)) {

    //TODO Fazer com o Vitor

}

Quad2FluxParams::Quad2FluxParams(void) : FluxParams(RealVector(10)) {
   


    component(0, 1); //A1

    component(1, -1); //B1

    component(2, 0); //C1

    component(3, 0); //D1

    component(4, 0); //E1

    component(5, 0); //A2

    component(6, -1); //B2

    component(7, 1); //C2

    component(8, 0); //D2

    component(9, 0); //E2








}

Quad2FluxParams::~Quad2FluxParams() {
}

Quad2FluxParams::Quad2FluxParams(const RealVector & params) : FluxParams(params) {
}



