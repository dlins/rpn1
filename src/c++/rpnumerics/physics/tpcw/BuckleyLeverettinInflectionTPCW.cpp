#include "BuckleyLeverettinInflectionTPCW.h"

BuckleyLeverettinInflectionTPCW::BuckleyLeverettinInflectionTPCW(FracFlow2PhasesHorizontalAdimensionalized *f){
    fh = f;
}

double BuckleyLeverettinInflectionTPCW::HugoniotFunction(const RealVector &u){

    double sw    = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);

    fh->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 2, m);
    double d2f_ds2 = m(0,0,0);
   
    return d2f_ds2 ;
}



