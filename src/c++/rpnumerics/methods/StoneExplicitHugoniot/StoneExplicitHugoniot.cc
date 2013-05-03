#include "StoneExplicitHugoniot.h"

double StoneExplicitHugoniot::sign(double a, double b){
    if (b >= 0.0) return  fabs(a);
    else          return -fabs(a);
}

StoneExplicitHugoniot::StoneExplicitHugoniot(const StoneFluxFunction *ff){
    double grw = ff->fluxParams().component(0);
    double grg = ff->fluxParams().component(1);
    double gro = ff->fluxParams().component(2);

    // TODO: In the future quadratic direct formulae should be used for the permeabilities in the flux
    // function. Eliminate the test of epsl below.
    double epsl = ff->perm().params().params().component(12);

    double vel = ff->fluxParams().component(6);

    if ((grw == grg && grg == gro) && epsl == 0.0 && vel == 1.0){
        muw = ff->fluxParams().component(3);
        mug = ff->fluxParams().component(4);
        muo = ff->fluxParams().component(5);

        valid_ = true;
    }
    else valid_ = false;
}

StoneExplicitHugoniot::~StoneExplicitHugoniot(){
}

void StoneExplicitHugoniot::set_reference_point(const RealVector &ref){
    reference = ref;
    return;
}

void StoneExplicitHugoniot::PolarHugoniot(void *o, double theta, RealVector &out){

    cout<<"Passando pelo polar"<<endl;
    StoneExplicitHugoniot *obj = (StoneExplicitHugoniot*)o;
    if (obj->valid_){
        double muw = obj->muw;
        double muo = obj->muo;
        double mug = obj->mug;

        double sw = obj->reference.component(0);
        double so = obj->reference.component(1);

        double c = cos(theta);
        double s = sin(theta);

        double sg = 1.0 - (sw + so);

        double kw = sw*sw/muw;
        double ko = so*so/muo;
        double kg = sg*sg/mug;

        double numer1 = (sw*c/muw)*(ko + kg);
        double numer2 = kw*(so*s/muo - sg*(s + c)/mug);
        double numer3 = (so*s/muo)*(kw + kg);
        double numer4 = ko*(sw*c/muw - sg*(s + c)/mug);
        double numer  = s*(numer1 - numer2) - c*(numer3 - numer4);

        numer = -2.*numer;

        double denom1 = (c*c/muw)*(ko + kg);
        double denom2 = kw*(s*s/muo + (s + c)*(s + c)/mug);
        double denom3 = (s*s/muo)*(kw + kg);
        double denom4 = ko*(c*c/muw + (s + c)*(s + c)/mug);
        double denom  = s*(denom1 - denom2) - c*(denom3 - denom4);

        double r;
        if (fabs(denom) <= 1.0e-3*fabs(numer)){
            r = 1.0e3*sign(1.0, denom)*sign(1.0, numer);
        }
        else {
            r = numer/denom;
        }

        out.resize(2);
        out.component(0) = sw + r*c;
        out.component(1) = so + r*s;
    }
    else std::cout << "StoneExplicitHugoniot::PolarHugoniot: object at " << obj << " is invalid." << std::endl;

    return;
}
