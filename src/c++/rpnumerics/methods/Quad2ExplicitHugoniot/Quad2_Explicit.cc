#include "Quad2_Explicit.h"

Quad2_Explicit::Quad2_Explicit(const Quad2FluxFunction *ff, const Viscosity_Matrix *vvm, const RectBoundary *b) : f(ff), vm(vvm), rect(b) {
    update_data();
}

Quad2_Explicit::~Quad2_Explicit(){
}

void Quad2_Explicit::qcoef(double &alpha, double &beta, double &gamma, 
                           Quad2_Explicit *obj, double c2phi, double s2phi){

    alpha = obj->alpha1*c2phi + obj->alpha2*s2phi + obj->alpha0;
    beta  = obj->beta1*c2phi  + obj->beta2*s2phi  + obj->beta0;
    gamma = obj->gamma1*c2phi + obj->gamma2*s2phi + obj->gamma0;

    return;
}

void Quad2_Explicit::qcoeft(double &alphat, double &betat, double &gammat, 
                            Quad2_Explicit *obj, double c2phi, double s2phi){

    alphat = obj->alpht1*c2phi + obj->alpht2*s2phi + obj->alpht0;
    betat  = obj->betat1*c2phi + obj->betat2*s2phi + obj->betat0;
    gammat = obj->gammt1*c2phi + obj->gammt2*s2phi + obj->gammt0;

    return;
}

void Quad2_Explicit::qcoefp(double &alphap, double &betap, double &gammap, 
                            Quad2_Explicit *obj, double c2phi, double s2phi){

    alphap = 2.*(obj->alpha2*c2phi - obj->alpha1*s2phi);
    betap  = 2.*(obj->beta2*c2phi  - obj->beta1*s2phi);
    gammap = 2.*(obj->gamma2*c2phi - obj->gamma1*s2phi);

    return;
}

void Quad2_Explicit::update_data(){
    // Get the parameters
    //
    const FluxParams params = f->fluxParams();

    a1 = params.component(0);
    b1 = params.component(1);
    c1 = params.component(2);
    d1 = params.component(3);
    e1 = params.component(4);

    a2 = params.component(5);
    b2 = params.component(6);
    c2 = params.component(7);
    d2 = params.component(8);
    e2 = params.component(9);

    // Below, taken from inicff at hoopy.F:
    //
    alpha1 = .5*(a2 + b1);
    beta1  = .5*(b2 + c1);
    gamma1 = .5*(d2 + e1);
    alpha2 = .5*(b2 - a1);
    beta2  = .5*(c2 - b1);
    gamma2 = .5*(e2 - d1);
    alpha0 = .5*(a2 - b1);
    beta0  = .5*(b2 - c1);
    gamma0 = .5*(d2 - e1);

    alpht1 = -alpha2;
    betat1 = -beta2;
    gammt1 = -gamma2;
    alpht2 = alpha1;
    betat2 = beta1;
    gammt2 = gamma1;
    alpht0 = .5*(a1 + b2);
    betat0 = .5*(b1 + c2);
    gammt0 = .5*(d1 + e2);

    return;
}

