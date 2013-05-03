#include "Quad2_Explicit_Coincidence_Curve.h"

Quad2_Explicit_Coincidence_Curve::Quad2_Explicit_Coincidence_Curve(const Quad2FluxFunction *ff, 
                                                                   const Viscosity_Matrix *vvm,
                                                                   const RectBoundary *b) : Quad2_Explicit(ff, vvm, b) {
}

Quad2_Explicit_Coincidence_Curve::~Quad2_Explicit_Coincidence_Curve(){
}

// This routine gives locus where the discriminant vanishes.
// This assumes D = I.
//
// Was: coinpt.
//
void Quad2_Explicit_Coincidence_Curve::coincidence_point(void *o, double phi, RealVector &x){

    Quad2_Explicit_Coincidence_Curve *obj = (Quad2_Explicit_Coincidence_Curve*)o;

    x.resize(2);

    double c2phi = cos(2.*phi);
    double s2phi = sin(2.*phi);

    double alpha, beta, gamma;
    qcoef(alpha, beta, gamma, obj, c2phi, s2phi);

    double alphap, betap, gammap;
    qcoefp(alphap, betap, gammap, obj, c2phi, s2phi);

    double det = alpha*betap - beta*alphap;

    x.component(0) = -(gamma*betap  -  beta*gammap)/det;
    x.component(1) = -(alpha*gammap - gamma*alphap)/det;

    return;
}

void Quad2_Explicit_Coincidence_Curve::polar_plot(std::vector<std::deque<RealVector> > &out){
    out.clear();
    update_data();

    RealVector polar_domain(2);
    polar_domain.component(0) = 0.0;
    polar_domain.component(1) = 2.0*PI;

    SimplePolarPlot::curve((void*)this, 
                           rect, 
                           &coincidence_point,
                           polar_domain, 1000,
                           out);

    return;
}

