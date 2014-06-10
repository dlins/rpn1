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
using namespace std;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
using namespace std;

Three_Phase_Boundary::Three_Phase_Boundary() {
    pmin = new RealVector(2);
    pmin->component(0) = 0.0;
    pmin->component(1) = 0.0;

    pmax = new RealVector(2);
    pmax->component(0) = 1.0;
    pmax->component(1) = 1.0;

    end_edge = 1.000001;

    A_ = new RealVector(*pmin);

    B_ = new RealVector(2);

    B_->component(0) = pmin->component(0);
    B_->component(1) = pmax->component(1);


    C_ = new RealVector(2);

    C_->component(0) = pmax->component(0);
    C_->component(1) = pmin->component(1);




}

Three_Phase_Boundary::Three_Phase_Boundary(const RealVector &ppmin, const RealVector &ppmax) {
    pmin = new RealVector(ppmin);

    pmax = new RealVector(ppmax);

    end_edge = ( (pmax->component(0) + pmin->component(0)) 
               + (pmax->component(1) + pmin->component(1)) ) / 2.0 + 0.000001;

    A_ = new RealVector(*pmin);

    B_ = new RealVector(2);

    B_->component(0) = pmin->component(0);
    B_->component(1) = pmax->component(1);


    C_ = new RealVector(2);

    C_->component(0) = pmax->component(0);
    C_->component(1) = pmin->component(1);


}

Three_Phase_Boundary::Three_Phase_Boundary(const Three_Phase_Boundary &original) {
    pmin = new RealVector(*(original.pmin));

    pmax = new RealVector(*(original.pmax));

    end_edge = original.end_edge;

    A_ = new RealVector(*original.A_);

    B_ = new RealVector(*original.B_);
    C_ = new RealVector(*original.C_);

}

void Three_Phase_Boundary::envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {
    c.clear();
    d.clear();

    std::vector<RealVector> seg;
    edge_segments(where_constant, number_of_steps, seg);

    Envelope_Curve envelope_curve;

    envelope_curve.curve(f, a, gv, singular,
            seg,
            c, d);

    return;
}

void Three_Phase_Boundary::extension_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        int fam,int characteristic,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {
    c.clear();
    d.clear();

    std::vector<RealVector> seg;
    edge_segments(where_constant, number_of_steps, seg);

    Extension_Curve extension_curve;
   
        extension_curve.curve(f, a, gv, characteristic, singular, fam,
                              seg,
                              c, d);
       return;
}

Three_Phase_Boundary::~Three_Phase_Boundary() {
    delete pmax;
    delete pmin;

    delete A_;
    delete B_;
    delete C_;
}

bool Three_Phase_Boundary::inside(const RealVector &p) const {
    return ((p.component(0) >= pmin->component(0)) && (p.component(1) >= pmin->component(1)) &&
            ((p.component(0) + p.component(1)) <= end_edge)
            );
}

bool Three_Phase_Boundary::inside(const double *p) const {
    return ((p[0] >= pmin->component(0)) &&( p[1] >= pmin->component(1)) &&
            ((p[0] + p[1]) <= end_edge)
            );
}

Boundary * Three_Phase_Boundary::clone() const {
    return new Three_Phase_Boundary(*this);
}

const RealVector& Three_Phase_Boundary::minimums(void) const {
    return *pmin;
}

const RealVector& Three_Phase_Boundary::maximums(void) const {
    return *pmax;
}

RealVector Three_Phase_Boundary::intersect(RealVector &p1, RealVector &p2) const {
    return RealVector(2);
}

//int Three_Phase_Boundary::intersection(const RealVector &p, const RealVector &q, RealVector &r, int &w) const {

//    w = -1;
//    r.resize(2);
////    return Boundary::intersection(p, q, r, w);
//    if (inside(p) && inside(q)) return 1;
//    if (!inside(p) && !inside(q)) {
//        //cout << "Both outside, should abort" << endl;
//        return -1;
//    } else {
//        RealVector pin(2), pout(2);

//        if (inside(p)) {
//            pin = p;
//            pout = q;
//        } else {
//            pin = q;
//            pout = p;
//        }

//        // This is an auxiliary variable, setted as 1.0 do the work to be replaced.
//        double a = 1.0;

//        if (pout.component(0) < pmin->component(0)) {
//            a = (pmin->component(0) - pin.component(0)) / (pout.component(0) - pin.component(0));
//            r.component(0) = pmin->component(0);
//            r.component(1) = pin.component(1) + a * (pout.component(1) - pin.component(1));
//            w = THREE_PHASE_EXTENDED_BOUNDARY_SW;
//        }

//        if (pout.component(1) < pmin->component(1)) {
//            double atemp = (pmin->component(1) - pin.component(1)) / (pout.component(1) - pin.component(1));

//            if (fabs(atemp) < fabs(a)) {
//                a = atemp;
//                r.component(0) = pin.component(0) + a * (pout.component(0) - pin.component(0));
//                r.component(1) = pmin->component(1);
//                w = THREE_PHASE_EXTENDED_BOUNDARY_SO;
//            }
//        }

//        double pout_sum = pout.component(0) + pout.component(1);
//        double end_sum = (pmax->component(0) + pmax->component(1) +pmin->component(0) + pmin->component(1))  / 2.0;

//        if (pout_sum > end_sum) {
//            double pin_sum = pin.component(0) + pin.component(1);
//            double atemp = (end_sum - pin_sum) / (pout_sum - pin_sum);

//            if (fabs(atemp) < fabs(a)) {
//                a = atemp;
//                for (int i = 0; i < 2; i++)
//                    r.component(i) = pin.component(i) + a * (pout.component(i) - pin.component(i));
//                w = THREE_PHASE_EXTENDED_BOUNDARY_SG;
//            }
//        }

//        return 0;
//    }
//}

const char * Three_Phase_Boundary::boundaryType() const {
    return "Three_Phase_Boundary";
}

void Three_Phase_Boundary::physical_boundary(std::vector<RealVector> &seg){

    std::vector<RealVector> tempSeg;

    for (int i = 0; i < 3; i++) {
        edge_segments(i,3,tempSeg);

        for (int j=0; j < tempSeg.size();j++) {
            seg.push_back(tempSeg[j]);
        }

    }













}

int Three_Phase_Boundary::edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg) {
    seg.clear();

    if (number_of_steps < 3) number_of_steps = 3; // Extremes must be eliminated, thus the minimum number of desired segments is 3: of which only one segment will remain.

    double p[number_of_steps + 1][2];

    double p_alpha[2], p_beta[2];

    double delta = 1.0 / (double) number_of_steps;

    // The following double is the old end_edge without the kludge.
    double RealEndEdge = end_edge - 0.000001;

    if (where_constant == THREE_PHASE_BOUNDARY_SO_ZERO) {
        // The following protection exists for physical boundary outside the real boundary.
        if (pmin->component(1) > 0.0) return 0;

        p_alpha[0] = pmin->component(0);
        p_alpha[1] = 0.0;
        p_beta[0]  = RealEndEdge;
        p_beta[1]  = 0.0;
    } else if (where_constant == THREE_PHASE_BOUNDARY_SW_ZERO) {
        // The following protection exists for physical boundary outside the real boundary.
        if (pmin->component(0) > 0.0) return 0;

        p_alpha[0] = 0.0;
        p_alpha[1] = pmin->component(0);
        p_beta[0]  = 0.0;
        p_beta[1]  = RealEndEdge;
    } else { // where_constant == THREE_PHASE_BOUNDARY_SG_ZERO
        // The following protection exists for physical boundary outside the real boundary.
        if (end_edge < 1.0) return 0;

        p_alpha[0] = pmin->component(0);
        p_alpha[1] = 1.0 - pmin->component(0);
        p_beta[0]  = 1.0 - pmin->component(1);
        p_beta[1]  = pmin->component(1);
    }

    for (int i = 0; i < number_of_steps + 1; i++) {
        double beta = (double) i*delta;
        double alpha = 1.0 - beta;
        for (int j = 0; j < 2; j++) p[i][j] = p_alpha[j] * alpha + p_beta[j] * beta;
    }

    // Boundary extension segments.
    seg.resize(2 * (number_of_steps));

    for (int i = 0; i < number_of_steps; i++) {
        seg[2 * i].resize(2);
        seg[2 * i + 1].resize(2);

        for (int j = 0; j < 2; j++) {
            seg[2 * i].component(j) = p[i][j];
            seg[2 * i + 1].component(j) = p[i + 1][j];
        }

    }

    return 1;
}

// THREE_PHASE_BOUNDARY_SW_ZERO 0
// THREE_PHASE_BOUNDARY_SO_ZERO 1
// THREE_PHASE_BOUNDARY_SG_ZERO 2
//
RealVector Three_Phase_Boundary::side_transverse_interior(const RealVector &p, int s) const {
    RealVector v(2);

    if (s == THREE_PHASE_BOUNDARY_SW_ZERO){
        v(0) = 1.0;
        v(1) = 0.0;
    }
    else if (s == THREE_PHASE_BOUNDARY_SO_ZERO){
        v(0) = 0.0;
        v(1) = 1.0;
    }
    else {
        v(0) = -0.707106781;
        v(1) = -0.707106781;
    }

    return v;
}




//
//
//
//
//
//
//
//IsoTriang2DBoundary::~IsoTriang2DBoundary() {
//
//    delete minimums_;
//    delete maximums_;
//    //    delete type_;
//}
//
//Boundary * IsoTriang2DBoundary::clone() const {
//    return new IsoTriang2DBoundary(*this);
//}
//
//IsoTriang2DBoundary::IsoTriang2DBoundary(const IsoTriang2DBoundary & copy) {
//    epsilon=1e-3;
//    minimums_ = new RealVector(copy.minimums());
//    maximums_ = new RealVector(copy.maximums());
//
//    A_ = new RealVector(copy.getA());
//    B_ = new RealVector(copy.getB());
//    C_ = new RealVector(copy.getC());
//
//    type_ = "triang";
//}
//
//IsoTriang2DBoundary::IsoTriang2DBoundary(const RealVector & A, const RealVector & B, const RealVector & C) {
//    epsilon=1e-3;
//    A_ = new RealVector(A);
//    B_ = new RealVector(B);
//    C_ = new RealVector(C);
//
//    double minAbs = A.component(0);
//    double minOrd = A.component(1);
//
//
//    double maxAbs = minAbs;
//    double maxOrd = minOrd;
//    // ABSSISSA
//    // B
//    if (B.component(0) < minAbs)
//        minAbs = B.component(0);
//    if (B.component(0) > maxAbs)
//        maxAbs = B.component(0);
//    // C
//    if (C.component(0) < minAbs)
//        minAbs = C.component(0);
//    if (C.component(0) > maxAbs)
//        maxAbs = C.component(0);
//    // ORDENADE
//    // B
//    if (B.component(1) < minOrd)
//        minOrd = B.component(1);
//    if (B.component(1) > maxOrd)
//        maxOrd = B.component(1);
//    // C
//    if (C.component(1) < minOrd)
//        minOrd = C.component(1);
//    if (C.component(1) > maxOrd)
//        maxOrd = C.component(1);
//
//    double minArray [2];
//
//    double maxArray[2];
//
//    minArray[0] = minAbs;
//    minArray[1] = minOrd;
//
//    maxArray[0] = maxAbs;
//    maxArray[1] = maxOrd;
//
//
//    minimums_ = new RealVector(2);
//
//    minimums_->component(0) = minArray[0];
//    minimums_->component(1) = minArray[1];
//
//    maximums_ = new RealVector(2);
//    maximums_->component(0) = maxArray[0];
//    maximums_->component(1) = maxArray[1];
//
//    type_ = "triang";
//
//}
//
//// 1.0000000001 is a kludge, in an ideal world it should be 1.0.
//
//bool IsoTriang2DBoundary::inside(const double *U)const {
//    double x = U[0];
//    double y = U[1];
//
//
//     if ((x >= minimums().component(0)) && (y >= minimums().component(1)) && (x + y <=  maximums().component(0)+minimums().component(1)+0.00000001)) {
////        //cout << "(" << x << ", " << y << ") is INSIDE" << endl;
//        return true;
//    } else {
////       //cout << "(" << x << ", " << y << ") is OUTSIDE" << endl;
//        return false;
//    }
//
//}
//
////
////
////    if ((x >= 0.) && (y >= 0.) && (x + y <= 1.)) {
//////        //cout << "(" << x << ", " << y << ") is INSIDE" << endl;
////        return true;
////    } else {
//////        //cout << "(" << x << ", " << y << ") is OUTSIDE" << endl;
////        return false;
//
//// 1.0000000001 is a kludge, in an ideal world it should be 1.0.
//bool IsoTriang2DBoundary::inside(const RealVector & U)const {
//    double x = U.component(0);
//    double y = U.component(1);
//
//
//
//     if ((x >= minimums().component(0)) && (y >= minimums().component(1)) && (x + y <=  maximums().component(0)+minimums().component(1)+0.00000001)) {
////        //cout << "(" << x << ", " << y << ") is INSIDE" << endl;
//        return true;
//    } else {
////       //cout << "(" << x << ", " << y << ") is OUTSIDE" << endl;
//        return false;
//    }
//
//
//
//
////
////    if ((x >= 0.) && (y >= 0.) && (x + y <= 1.)) {
//////        //cout << "(" << x << ", " << y << ") is INSIDE" << endl;
////        return true;
////    } else {
//////        //cout << "(" << x << ", " << y << ") is OUTSIDE" << endl;
////        return false;
////    }
//
//    //return false;
//
//}
//
//RealVector IsoTriang2DBoundary::intersect(RealVector &y1, RealVector &y2)const {
//    return RealVector(2);
//} //TODO
//
//
//void IsoTriang2DBoundary::envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
//                              GridValues &gv,
//                              int where_constant, int number_of_steps, bool singular,
//                              std::vector<RealVector> &c, std::vector<RealVector> &d){
//    c.clear();
//    d.clear();
//
//    std::vector<RealVector> seg;
//    edge_segments(where_constant, number_of_steps, seg);
//
//    Envelope_Curve envelope_curve;
//
//    envelope_curve.curve(f, a, gv, singular,
//                         seg,
//                         c, d);
//
//    return;
//}
//
//void IsoTriang2DBoundary::extension_curve(const FluxFunction *f, const AccumulationFunction *a,
//                               GridValues &gv,
//                               int where_constant, int number_of_steps, bool singular,
//                               int fam,
//                               std::vector<RealVector> &c, std::vector<RealVector> &d){
//    c.clear();
//    d.clear();
//
//    std::vector<RealVector> seg;
//    edge_segments(where_constant, number_of_steps, seg);
//
//    Extension_Curve extension_curve;
//
//    extension_curve.curve(f, a, gv, singular, CHARACTERISTIC_ON_DOMAIN, fam,
//                          seg,
//                          c, d);
//
//    return;
//}
//
//
//void IsoTriang2DBoundary::edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg){
//    seg.clear();
//
//    if (number_of_steps < 3) number_of_steps = 3; // Extremes must be eliminated, thus the minimum number of desired segments is 3: of which only one segment will remain.
//
//    double p[number_of_steps - 1][2];
//
//    double p_alpha[2], p_beta[2];
//
//    double delta = 1.0/(double)number_of_steps;
//
//    if (where_constant == THREE_PHASE_BOUNDARY_ENVELOPE_SO_CONST){
//        p_alpha[0] = 0.0001; p_alpha[1] = 0.0001;
//        p_beta[0]  = 1.0 - 0.0001; p_beta[1]  = 0.0001;
//    }
//    else if (where_constant == THREE_PHASE_BOUNDARY_ENVELOPE_SW_CONST){
//        p_alpha[0] = 0.0001; p_alpha[1] = 0.0001;
//        p_beta[0]  = 0.0001; p_beta[1]  = 1.0 - 0.0001;
//    }
//    else {
//        p_alpha[0] = 0.0001; p_alpha[1] = 1.0 - 0.0001;
//        p_beta[0]  = 1.0 - 0.0001; p_beta[1]  = 0.0001;
//    }
//
//    for (int i = 1; i < number_of_steps; i++){
//        double beta  = (double)i*delta;
//        double alpha = 1.0 - beta;
//        for (int j = 0; j < 2; j++) p[i - 1][j] = p_alpha[j]*alpha + p_beta[j]*beta;
//    }
//
//    // Boundary extension segments.
//    seg.resize(2*(number_of_steps - 2));
//
//    for (int i = 0; i < number_of_steps - 2; i++){
//        seg[2*i].resize(2);
//        seg[2*i + 1].resize(2);
//
//        for (int j = 0; j < 2; j++){
//            seg[2*i].component(j)     = p[i][j];
//            seg[2*i + 1].component(j) = p[i + 1][j];
//        }
//
//    }
//
//    return;
//}
