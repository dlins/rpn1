#include "CoreyQuad4Phase_Params.h"

CoreyQuad4Phase_Params::CoreyQuad4Phase_Params(
        const double grw, const double grg, const double gro, const double grc,
        const double muw, const double mug, const double muo, const double muc,
        const double vel) : FluxParams(RealVector(9)) {
    component(0, grw);
    component(1, grg);
    component(2, gro);
    component(3, grc);

    component(4, muw);
    component(5, mug);
    component(6, muo);
    component(7, muc);

    component(8, vel);
}

CoreyQuad4Phase_Params::CoreyQuad4Phase_Params() : FluxParams(RealVector(9)) {
    component(0, 0.0);
    component(1, 0.0);
    component(2, 0.0);
    component(3, 0.0);

    component(4, 1.0);
    component(5, 1.0);
    component(6, 1.0);
    component(7, 1.0);

    component(8, 1.0);
}

CoreyQuad4Phase_Params::CoreyQuad4Phase_Params(const RealVector & params):FluxParams(params){}
CoreyQuad4Phase_Params::CoreyQuad4Phase_Params(const CoreyQuad4Phase_Params &copy):FluxParams(copy.params()){}

CoreyQuad4Phase_Params::~CoreyQuad4Phase_Params(){
}
