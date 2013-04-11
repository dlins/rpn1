#include "Quad2_Explicit_Hugoniot.h"

double Quad2_Explicit_Hugoniot::sign(double a, double b){
    if (b >= 0.0) return  fabs(a);
    else          return -fabs(a);
}

Quad2_Explicit_Hugoniot::Quad2_Explicit_Hugoniot(const Quad2FluxFunction *ff) : Quad2_Explicit(ff, (const Viscosity_Matrix*)0, (const RectBoundary*)0){
//    double grw = ff->fluxParams().component(0);
//    double grg = ff->fluxParams().component(1);
//    double gro = ff->fluxParams().component(2);

//    // TODO: In the future quadratic direct formulae should be used for the permeabilities in the flux
//    // function. Eliminate the test of epsl below.
//    double epsl = ff->perm().params().params().component(12);

//    double vel = ff->fluxParams().component(6);
}

Quad2_Explicit_Hugoniot::~Quad2_Explicit_Hugoniot(){
}

void Quad2_Explicit_Hugoniot::set_reference_point(const RealVector &ref){
    reference = ref;
    return;
}

// Translation from rp/phys/quad/expl.F::hugrad
//
void Quad2_Explicit_Hugoniot::PolarHugoniot(void *o, double phi, RealVector &out){

    Quad2_Explicit_Hugoniot *obj = (Quad2_Explicit_Hugoniot*)o;

    double ul = obj->reference.component(0);
    double vl = obj->reference.component(1);
      
    double cosphi = cos(phi);
    double sinphi = sin(phi);


    // Generic point.
    //
    double c2phi = cosphi*cosphi - sinphi*sinphi; 
    double s2phi = 2.*cosphi*sinphi;
          
    double alphai, betai, gammai;
    qcoef(alphai, betai, gammai, obj, c2phi, s2phi); // Defined in Quad2_Explicit (base class)
          
    double denom = alphai*cosphi + betai*sinphi;
    double numer = - 2.*(alphai*ul + betai*vl + gammai);

    double r;

    if (fabs(denom) <= 1.0e-3*fabs(numer)) r = 1.0e3*sign(1.0, denom)*sign(1.0, numer);
    else                                   r = numer/denom;

    out.resize(2);
    
    out.component(0) = ul + r*cosphi;
    out.component(1) = vl + r*sinphi;

    return;
}

