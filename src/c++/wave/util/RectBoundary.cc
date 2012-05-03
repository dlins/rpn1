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

void RectBoundary::edge_segments(int where_constant, int number_of_steps, std::vector<RealVector> &seg) {


}

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
    printf("Here\n");

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
    //    cout << "tamanho dentro de inside"<<in<<" "<<p.size() << endl;
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
    //    cout << "tamanho dentro de inside" << in << " " << p.size() << endl;
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
//    cout << "min" << minimums() << endl;
//    cout << "max" << maximums() << endl;
//
//    if (inside(p) && inside(q)) {
//
//        cout << "tamanho de p " << p.size() << " q" << q.size() << " r" << r.size();
//        return 1;
//
//    } else if (!inside(p) && !inside(q)) {
//        cout << "tamanho de p " << p << " q" << q << " r --------------" << r.size();
//        return -1;
//
//    } else {
//        cout << "tamanho de p " << p.size() << " q" << q.size() << " r***************" << r.size();
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
//                    printf("ALPHA = %f, beta = %f, pos = %d\n", alpha, beta, pos);
//#endif
//                }
//
//                if (beta >= 0.0 && beta <= 1.0) {
//                    for (int i = 0; i < n; i++) r(i) = beta * p(i) + (1.0 - beta) * q(i);
//                    found = true;
//#ifdef _TEST_HYPERBOX_
//                    printf("alpha = %f, BETA = %f, pos = %d\n", alpha, beta, pos);
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
                    printf("ALPHA = %f, beta = %f, pos = %d\n", alpha, beta, pos);
#endif

                    // Return the index
                    w = pos * n + 0;
                }

                if (beta >= 0.0 && beta <= 1.0) {
                    for (int i = 0; i < n; i++) r.component(i) = beta * p.component(i) + (1.0 - beta) * q.component(i);
                    found = true;
#ifdef _TEST_HYPERBOX_
                    printf("alpha = %f, BETA = %f, pos = %d\n", alpha, beta, pos);
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

RectBoundary::~RectBoundary() {
    delete minimums_;
    delete maximums_;

    test_dimension.clear();

}



