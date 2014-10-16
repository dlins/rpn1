#include "CoreyQuadTransitionalLine.h"

CoreyQuadTransitionalLine::CoreyQuadTransitionalLine(CoreyQuadSubPhysics *c): BifurcationCurve(), corey_(c){
}

CoreyQuadTransitionalLine::~CoreyQuadTransitionalLine(){
}

double CoreyQuadTransitionalLine::evaluate_point(int type, const RealVector &p){
    double sw = p(0);
    double so = p(1);

    double muw = corey_->muw()->value();
    double muo = corey_->muo()->value();
    double mug = corey_->mug()->value();

    if (type == COREYQUADTRANSITIONALLINE_O_B){
        return muw*(1.0 - so) - (muw + mug)*sw;
    }
    else if (type == COREYQUADTRANSITIONALLINE_W_E){
        return (mug + muo)*so - muo*(1.0 - sw);
    }
    else if (type == COREYQUADTRANSITIONALLINE_G_D){
        return muw*so - muo*sw;
    }
}

