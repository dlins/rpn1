#include "graphicobject.h"

GraphicObject::GraphicObject(double rr, double gg, double bb){
    setrgb(rr, gg, bb);
    visible_ = true;
}

string GraphicObject::num2str(double n){
    stringstream s;
    s << n;
    return s.str();
}

void GraphicObject::show(void){
    visible_ = true;
    return;
}

void GraphicObject::hide(void){
    visible_ = false;
    return;
}

bool GraphicObject::visible(void){
    return visible_;
}

void GraphicObject::setrgb(const double rr, const double gg, const double bb){
    r = rr; g = gg; b = bb;
    return;
}

void GraphicObject::getrgb(double &rr, double &gg, double &bb){
    rr = r; gg = g; bb = b;
    return;
}

// Do something with this 3x3 matrix of the form:
//
//     [a b c; d e f; 0 0 1]
//
// that implements a chain of transformations in
// homogeneous coordinates.
//
void GraphicObject::transform(const double *matrix){
    return;
}

