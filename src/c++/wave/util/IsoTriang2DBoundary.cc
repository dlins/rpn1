/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) IsoTriang2DBoundary.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "IsoTriang2DBoundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
IsoTriang2DBoundary::~IsoTriang2DBoundary() {

    delete minimums_;
    delete maximums_;
    //    delete type_;
}

Boundary * IsoTriang2DBoundary::clone() const {
    return new IsoTriang2DBoundary(*this);
}

IsoTriang2DBoundary::IsoTriang2DBoundary(const IsoTriang2DBoundary & copy) {

    minimums_ = new RealVector(copy.minimums());
    maximums_ = new RealVector(copy.maximums());

    A_ = new RealVector(copy.getA());
    B_ = new RealVector(copy.getB());
    C_ = new RealVector(copy.getC());

    type_ = "triang";
}

IsoTriang2DBoundary::IsoTriang2DBoundary(const RealVector & A, const RealVector & B, const RealVector & C) {

    A_ = new RealVector(A);
    B_ = new RealVector(B);
    C_ = new RealVector(C);

    double minAbs = A.component(0);
    double minOrd = A.component(1);


    double maxAbs = minAbs;
    double maxOrd = minOrd;
    // ABSSISSA
    // B
    if (B.component(0) < minAbs)
        minAbs = B.component(0);
    if (B.component(0) > maxAbs)
        maxAbs = B.component(0);
    // C
    if (C.component(0) < minAbs)
        minAbs = C.component(0);
    if (C.component(0) > maxAbs)
        maxAbs = C.component(0);
    // ORDENADE
    // B
    if (B.component(1) < minOrd)
        minOrd = B.component(1);
    if (B.component(1) > maxOrd)
        maxOrd = B.component(1);
    // C
    if (C.component(1) < minOrd)
        minOrd = C.component(1);
    if (C.component(1) > maxOrd)
        maxOrd = C.component(1);

    double minArray [2];

    double maxArray[2];

    minArray[0] = minAbs;
    minArray[1] = minOrd;

    maxArray[0] = maxAbs;
    maxArray[1] = maxOrd;

    //    minimums_ = new RealVector(2, minArray);//TODO Esse construtor pode ter um problema !

    minimums_ = new RealVector(2);

    minimums_->component(0) = minArray[0];
    minimums_->component(0) = minArray[1];

    maximums_ = new RealVector(2);
    maximums_->component(0) = maxArray[0];
    maximums_->component(1) = maxArray[1];

    type_ = "triang";

}

bool IsoTriang2DBoundary::inside(const double*)const {

}

RealVector IsoTriang2DBoundary::intersect(RealVector &y1, RealVector &y2)const {
} //TODO