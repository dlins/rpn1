#include "BuckleyLeverettinInflectionTPCW.h"

BuckleyLeverettinInflectionTPCW::BuckleyLeverettinInflectionTPCW(Flux2Comp2PhasesAdimensionalized *f,RectBoundary * boundary):HugoniotFunctionClass(){
    fh = f->getHorizontalFlux();
    boundary_=boundary;
}

double BuckleyLeverettinInflectionTPCW::HugoniotFunction(const RealVector &u){

    double sw    = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);

    fh->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 2, m);
    double d2f_ds2 = m.get(0,0,0);

    return d2f_ds2 ;
}


void BuckleyLeverettinInflectionTPCW::completeCurve(std::vector<RealVector> & curve){
    std::vector<RealVector> temp;
    temp.resize(2*curve.size());

    RealVector min(boundary_->minimums());
    RealVector max(boundary_->maximums());

    double umin = min.component(2), umax = max.component(2);

    for (int i = 0; i < curve.size(); i++){
        temp[2*i].resize(3);
        temp[2*i + 1].resize(3);

        for (int j = 0; j < 2; j++) temp[2*i].component(j) = temp[2*i + 1].component(j) = curve[i].component(j);

        temp[2*i].component(2)     = umin;
        temp[2*i + 1].component(2) = umax;
    }

    curve.clear();
    curve.resize(temp.size());

    for (int i = 0; i < temp.size(); i++) curve[i] = temp[i];

    return;

}
