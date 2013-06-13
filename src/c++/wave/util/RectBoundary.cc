/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RectBoundary.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <vector>

#include "RectBoundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

using namespace std;

RectBoundary::RectBoundary(const RectBoundary & copy) {

    minimums_ = new RealVector(copy.minimums());
    maximums_ = new RealVector(copy.maximums());
    type_ = "rect";
    test_dimension.push_back(true);
    test_dimension.push_back(true);
    test_dimension.push_back(false); // u will not be tested for belonging to the HyperBox.

    epsilon = 0.0;
}

RectBoundary & RectBoundary::operator=(const RectBoundary & source) {

    if (this == &source)
        return *this;

    delete minimums_;
    delete maximums_;
    type_ = "rect";
    minimums_ = new RealVector(source.minimums());
    maximums_ = new RealVector(source.maximums());
    test_dimension = source.test_dimension;

    return *this;


}

void RectBoundary::extension_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        int fam, int characteristic,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {
    c.clear();
    d.clear();

    std::vector<RealVector> seg;

    RealVector limMin = gv.grid(0, 0);
//    RealVector limMax = gv.grid(gv.noc[0], gv.noc[1]);
    

    RealVector limMax = gv.grid(gv.grid.rows()-1,gv.grid.cols()-1);
    edge_segments(where_constant, number_of_steps, limMin, limMax, seg);

    Extension_Curve extension_curve;

    IF_DEBUG
        cout<<"Primeiro seg: "<<seg[0]<<endl;
        cout <<"Ultimo seg: "<<seg[seg.size()-1]<<endl;
    END_DEBUG

    extension_curve.curve(f, a, gv, characteristic, singular, fam,
            seg,
            c, d);

    return;
}

int RectBoundary::edge_segments(int where_constant, int number_of_steps, const RealVector & limMin, const RealVector & limMax, std::vector<RealVector> &seg) {
    seg.clear();

    if (number_of_steps < 3) number_of_steps = 3; // Extremes must be eliminated, thus the minimum number of desired segments is 3: of which only one segment will remain.

    double p[number_of_steps + 1][3];

    double p_alpha[3], p_beta[3];

    double delta = 1.0 / (double) number_of_steps;

    //    double end_edge = 1.000001;

    // The following double is the old end_edge without the kludge.
    //    double RealEndEdge = end_edge - 0.000001;





    if (where_constant == RECT_BOUNDARY_SG_ZERO) {
        // The following protection exists for physical boundary outside the real boundary.
        //        if (pmin->component(1) > 0.0) return 0;

        p_alpha[0] = 0.0;
        p_alpha[1] = limMin(1);
        p_alpha[2] = limMin(2);
        p_beta[0] = 0.0;
        p_beta[1] = limMax(1);
        p_beta[2] = limMax(2);
    } else { // where_constant == RECT_BOUNDARY_SG_ONE
        // The following protection exists for physical boundary outside the real boundary.
        //        if (RealEndEdge < 1.0) return 0;



        p_alpha[0] = 1.0;
        p_alpha[1] = limMin(1);
        p_alpha[2] = limMin(2);
        p_beta[0] = 1.0;
        p_beta[1] = limMax(1);
        p_beta[2] = limMax(2);


    }

//
//    p_alpha[2]=1.0;
//    p_beta[2] = 1.0;


    for (int i = 0; i < number_of_steps + 1; i++) {
        double beta = (double) i*delta;
        double alpha = 1.0 - beta;
        for (int j = 0; j < 3; j++) p[i][j] = p_alpha[j] * alpha + p_beta[j] * beta;
    }

    // Boundary extension segments.
    seg.resize(2 * (number_of_steps));

    for (int i = 0; i < number_of_steps; i++) {
        seg[2 * i].resize(3);
        seg[2 * i + 1].resize(3);

        for (int j = 0; j < 3; j++) {
            seg[2 * i].component(j) = p[i][j];
            seg[2 * i + 1].component(j) = p[i + 1][j];
        }

    }

    return 1;
}




//
//
//void RectBoundary::edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg) {
//
//
//}

void RectBoundary::physical_boundary(std::vector<RealVector> &) {

}

RectBoundary::RectBoundary(const RealVector & minimums, const RealVector & maximums)
: minimums_(new RealVector(minimums)),
maximums_(new RealVector(maximums)),
size_(minimums.size()), type_("rect") {
    epsilon = 0.0;
    test_dimension.push_back(true);
    test_dimension.push_back(true);
    test_dimension.push_back(false); // u will not be tested for belonging to the HyperBox.

}

RectBoundary::RectBoundary(const RealVector & minimums, const RealVector & maximums, const std::vector<bool> & test, const double eps) {
    IF_DEBUG
        printf("Here\n");
    END_DEBUG

    minimums_ = new RealVector(minimums.size());
    maximums_ = new RealVector(minimums.size());



    for (int i = 0; i < minimums.size(); i++) {
        if (minimums.component(i) < maximums.component(i)) {
            minimums_->component(i) = minimums.component(i);
            maximums_->component(i) = maximums.component(i);
        } else {
            minimums_->component(i) = maximums.component(i);
            maximums_->component(i) = minimums.component(i);
        }
    }

    test_dimension.resize(minimums.size());
    for (int i = 0; i < minimums.size(); i++) test_dimension[i] = true;

    for (int i = 0; i < min((int) test.size(), minimums.size()); i++) test_dimension[i] = test[i]; //TODO ???

    //    for (int i = 0; i < test.size(); i++) test_dimension[i] = test[i];
    epsilon = eps;
}

bool RectBoundary::inside(const double *p)const {
    bool in = true;
    int pos = 0;

    while (in && pos < minimums().size()) {
        // Check if the current component should be skipped, as stated in the list of
        // exceptions.
        if (test_dimension[pos]) {
            if (p[pos] < minimums().component(pos) || p[pos] > maximums().component(pos)) in = false;
        }
        pos++;
    }

    return in;
}

bool RectBoundary::inside(const RealVector &p) const {
    bool in = true;
    int pos = 0;

    while (in && pos < minimums().size()) {
        if (test_dimension[pos]) {
            if (p.component(pos) < minimums().component(pos) || p.component(pos) > maximums().component(pos)) in = false;
        }
        //        if (p(pos) < minimums()(pos) || p(pos) > maximums()(pos)) in = false;
        pos++;
    }
    IF_DEBUG
        cout << "tamanho dentro de inside"<<in<<" "<<p.size() << endl;
    END_DEBUG
    return in;


    //
    //
    //
    //
    //     double pp[p.size()];
    //    for (int i = 0; i < p.size(); i++) pp[i] = p.component(i);
    //
    //    return inside(pp);



    //
    //
    //
    //    bool in = true;
    //    int pos = 0;
    //
    //    while (in && pos < minimums().size()) {
    //        if (p(pos) < minimums()(pos) || p(pos) > maximums()(pos)) in = false;
    //        pos++;
    //    }
    //    IF_DEBUG
    //        cout << "tamanho dentro de inside" << in << " " << p.size() << endl;
    //    END_DEBUG
    //    return in;
}

// Check if a line segment intersects the box. If so, where.
//
// Returns:
//
//     1: Both points lie within the box.Finland
//     0: One point lies within the box and the other one is outside.
//    -1: Both points lie outside the box.
//
// The point where the line intersects the box is stored in r (but only when the function
// returns 0 this point's coordinates are meaningful).
//

//int RectBoundary::intersection(const RealVector &p, const RealVector &q, RealVector &r)const {
//
//    IF_DEBUG
//        cout << "min" << minimums() << endl;
//        cout << "max" << maximums() << endl;
//    END_DEBUG
//
//    if (inside(p) && inside(q)) {
//
//        IF_DEBUG
//            cout << "tamanho de p " << p.size() << " q" << q.size() << " r" << r.size();
//        END_DEBUG
//        return 1;
//
//    } else if (!inside(p) && !inside(q)) {
//        IF_DEBUG
//            cout << "tamanho de p " << p << " q" << q << " r --------------" << r.size();
//        END_DEBUG
//        return -1;
//
//    } else {
//        IF_DEBUG
//            cout << "tamanho de p " << p.size() << " q" << q.size() << " r***************" << r.size();
//        END_DEBUG
//        int n = p.size();
//        double alpha, beta;
//        int pos = 0;
//        bool found = false;
//        r.resize(n);
//
//        while (pos < n && !found) {
//            double d = p(pos) - q(pos);
//            if (fabs(d) > epsilon * (maximums()(pos) - minimums()(pos))) {
//                alpha = (minimums()(pos) - q(pos)) / d;
//                beta = (maximums()(pos) - q(pos)) / d;
//
//                if (alpha >= 0.0 && alpha <= 1.0) {
//                    for (int i = 0; i < n; i++) r(i) = alpha * p(i) + (1.0 - alpha) * q(i);
//                    found = true;
//#ifdef _TEST_HYPERBOX_
//                    IF_DEBUG
//                        printf("ALPHA = %f, beta = %f, pos = %d\n", alpha, beta, pos);
//                    END_DEBUG
//#endif
//                }
//
//                if (beta >= 0.0 && beta <= 1.0) {
//                    for (int i = 0; i < n; i++) r(i) = beta * p(i) + (1.0 - beta) * q(i);
//                    found = true;
//#ifdef _TEST_HYPERBOX_
//                    IF_DEBUG
//                        printf("alpha = %f, BETA = %f, pos = %d\n", alpha, beta, pos);
//                    END_DEBUG
//#endif
//                }
//            }
//            pos++;
//        }
//
//        return 0;
//    }
//}




// Check if a line segment intersects the box. If so, where.
//
// Returns:
//
//     1: Both points lie within the box.Finland
//     0: One point lies within the box and the other one is outside.
//    -1: Both points lie outside the box.
//
// The point where the line intersects the box is stored in r (but only when the function
// returns 0 this point's coordinates are meaningful).
//
// The edge where the intersection occurs is returned in w. For more details, check
// HyperBox.h.
//

void RectBoundary::envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &gv,
        int where_constant, int number_of_steps, bool singular,
        std::vector<RealVector> &c, std::vector<RealVector> &d) {
    c.clear();
    d.clear();

    std::vector<RealVector> seg;
    RealVector limMin = gv.grid(0, 0);
//    RealVector limMax = gv.grid(gv.noc[0], gv.noc[1]);
    
    RealVector limMax = gv.grid(gv.grid.rows()-1,gv.grid.cols()-1);

    edge_segments(where_constant, number_of_steps, limMin, limMax, seg);


    Envelope_Curve envelope_curve;

    envelope_curve.curve(f, a, gv, singular,
            seg,
            c, d);

    return;
}

int RectBoundary::intersection(const RealVector &p, const RealVector &q, RealVector &r, int &w)const {


    if (inside(p) && inside(q)) return 1;
    else if (!inside(p) && !inside(q)) return -1;
    else {
        int n = minimums().size();
        double alpha, beta;
        int pos = 0;
        bool found = false;
        r.resize(p.size());

        while (pos < n && !found) {
            double d = p.component(pos) - q.component(pos);
            if (fabs(d) > epsilon * (maximums().component(pos) - minimums().component(pos))) {
                alpha = (minimums().component(pos) - q.component(pos)) / d;
                beta = (maximums().component(pos) - q.component(pos)) / d;

                if (alpha >= 0.0 && alpha <= 1.0) {
                    for (int i = 0; i < n; i++) r.component(i) = alpha * p.component(i) + (1.0 - alpha) * q.component(i);
                    found = true;
#ifdef _TEST_HYPERBOX_
                    IF_DEBUG
                        printf("ALPHA = %f, beta = %f, pos = %d\n", alpha, beta, pos);
                    END_DEBUG
#endif

                    // Return the index
                    w = pos * n + 0;
                }

                if (beta >= 0.0 && beta <= 1.0) {
                    for (int i = 0; i < n; i++) r.component(i) = beta * p.component(i) + (1.0 - beta) * q.component(i);
                    found = true;
#ifdef _TEST_HYPERBOX_
                    IF_DEBUG
                        printf("alpha = %f, BETA = %f, pos = %d\n", alpha, beta, pos);
                    END_DEBUG
#endif

                    // Return the index
                    w = pos * n + 1;
                }
            }
            pos++;
        }

        return 0;
    }
}

Boundary * RectBoundary::clone() const {
    return new RectBoundary(*this);
}

RectBoundary::~RectBoundary() {
    delete minimums_;
    delete maximums_;

    test_dimension.clear();

}



