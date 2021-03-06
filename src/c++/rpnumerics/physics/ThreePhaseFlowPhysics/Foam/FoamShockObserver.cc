#include "FoamShockObserver.h"
#include "FoamSubPhysics.h"

FoamShockObserver::FoamShockObserver(FoamSubPhysics *f): Observer(), foam_(f){
}

FoamShockObserver::~FoamShockObserver(){
}

void FoamShockObserver::change(){
    foam_->shock()->distance_to_contact_region(foam_->max_distance_to_contact_region_parameter()->value());

    return;
}

