#include "CoreyQuad_Params.h"

CoreyQuad_Params::CoreyQuad_Params(const double grw, const double grg, const double gro,
                           const double muw, const double mug, const double muo,
                           const double vel,
                           const double krw_p, const double krg_p, const double kro_p,
                           const double cnw, const double cng, const double cno) : FluxParams(RealVector(13)){
    component(0, grw);
    component(1, grg);
    component(2, gro);

    component(3, muw);
    component(4, mug);
    component(5, muo);

    component(6, vel);

    component(7, krw_p);
    component(8, krg_p);
    component(9, kro_p);

    component(10, cnw);
    component(11, cng);
    component(12, cno);
}

CoreyQuad_Params::CoreyQuad_Params() : FluxParams(RealVector(13)){
    component(0, 0.0);
    component(1, 0.0);
    component(2, 0.0);

    component(3, 1.0);
    component(4, 1.0);
    component(5, 1.0);

    component(6, 1.0);

    component(7, 1.0);
    component(8, 1.0);
    component(9, 1.0);

    component(10, 0.0);
    component(11, 0.0);
    component(12, 0.0);
}

CoreyQuad_Params::CoreyQuad_Params(const RealVector & params):FluxParams(params){}
CoreyQuad_Params::CoreyQuad_Params(const CoreyQuad_Params &copy):FluxParams(copy.params()){}




CoreyQuad_Params::~CoreyQuad_Params(){
}
