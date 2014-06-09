#include "ExplicitHugoniot.h"

ExplicitHugoniot::ExplicitHugoniot(const Boundary *bb) : b(bb){
}

ExplicitHugoniot::~ExplicitHugoniot(){
}

void ExplicitHugoniot::curve(const RealVector &ref, std::vector<Curve> &curve){
    referencepoint = ref;

//    PolarPlot::plot(&f, (void*)this, 0.0, M_PI, 1000, b, curve);
    ParametricPlot::plot(&f, (void*)this, phi_begin, phi_end, 50, b, curve);

    return;
}

RealVector ExplicitHugoniot::f(void *obj, double phi){
    return ((ExplicitHugoniot*)obj)->fobj(phi);
}

