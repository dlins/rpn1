#include "Curve.h"

void Curve::init(const Curve &orig){
    type = orig.type;

    back_curve_index = orig.back_curve_index;

    family = orig.family;

    increase = orig.increase;

    reference_point = orig.reference_point;

    for (int i = 0; i < orig.curve.size(); i++) curve.push_back(orig.curve[i]);
    for (int i = 0; i < orig.back_pointer.size(); i++) back_pointer.push_back(orig.back_pointer[i]);

    last_point = orig.last_point;
    final_direction = orig.final_direction;

    reason_to_stop = orig.reason_to_stop;

    for (int i = 0; i < orig.speed.size(); i++) speed.push_back(orig.speed[i]);
    for (int i = 0; i < orig.eigenvalues.size(); i++) eigenvalues.push_back(orig.eigenvalues[i]);        

    return;
}

Curve::Curve(){
}

Curve::Curve(const Curve &orig){
    init(orig);
}

Curve::Curve(const Curve *orig){
    init(*orig);
}

Curve::~Curve(){
}

Curve Curve::operator=(const Curve &orig){
    // Avoid self-assignment
    if (this != &orig) init(orig);

    return *this;
}

void Curve::clear(){
    curve.clear();
    speed.clear();
    eigenvalues.clear();
    back_pointer.clear();

    return;             
}

