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


RectBoundary::RectBoundary(const RectBoundary & copy) {

    minimums_ = new RealVector(copy.minimums());
    maximums_ = new RealVector(copy.maximums());
    type_ = "rect";
    exception.push_back(2);
}

RectBoundary & RectBoundary::operator=(const RectBoundary & source) {

    if (this == &source)
        return *this;

    delete minimums_;
    delete maximums_;
    type_ = "rect";
    minimums_ = new RealVector(source.minimums());
    maximums_ = new RealVector(source.maximums());

    return *this;


}

RectBoundary::RectBoundary(const RealVector & minimums, const RealVector & maximums)
: minimums_(new RealVector(minimums)),
maximums_(new RealVector(maximums)),
size_(minimums.size()), type_("rect") {

    exception.push_back(2); //TODO Colocar como membro da classe (???) Vetor de excessoes . Lista de limites que nao serao checados
}

bool RectBoundary::inside(const double *p)const {
//    cout <<"Inicio do inside double"<<endl;
//    cout << "(\n";
    cout << p[0] << ", " << p[1] << ", " << p[2] << ")\n";

    bool in = true;
    int pos = 0;

    unsigned int exception_pos = 0;

    while (in && pos < minimums_->size()) {
        // Check if the current component should be skipped, as stated in the list of
        // exceptions.
        if (exception_pos < exception.size() - 1) { // TODO: Check this and see if it can be improved somehow.
            if (exception[exception_pos] == pos) {
                exception_pos++;
                continue;
            }
        }

        if (p[pos] < minimums_->component(pos) || p[pos] > maximums_->component(pos)) in = false;
        pos++;
    }

    return in;
}

bool RectBoundary::inside(const RealVector &p) const {
     bool in = true;
    int pos = 0;

    while (in && pos < minimums().size()) {
        if (p(pos) < minimums()(pos) || p(pos) > maximums()(pos)) in = false;
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
//     1: Both points lie within the box.
//     0: One point lies within the box and the other one is outside.
//    -1: Both points lie outside the box.
//
// The point where the line intersects the box is stored in r (but only when the function
// returns 0 this point's coordinates are meaningful).
//

int RectBoundary::intersection(const RealVector &p, const RealVector &q, RealVector &r)const {

    cout << "min" << minimums() << endl;
    cout << "max" << maximums() << endl;

    if (inside(p) && inside(q)) {

        cout << "tamanho de p " << p.size() << " q" << q.size() << " r" << r.size();
        return 1;

    } else if (!inside(p) && !inside(q)) {
        cout << "tamanho de p " << p << " q" << q << " r --------------" << r.size();
        return -1;

    } else {
        cout << "tamanho de p " << p.size() << " q" << q.size() << " r***************" << r.size();
        int n = p.size();
        double alpha, beta;
        int pos = 0;
        bool found = false;
        r.resize(n);

        while (pos < n && !found) {
            double d = p(pos) - q(pos);
            if (fabs(d) > epsilon * (maximums()(pos) - minimums()(pos))) {
                alpha = (minimums()(pos) - q(pos)) / d;
                beta = (maximums()(pos) - q(pos)) / d;

                if (alpha >= 0.0 && alpha <= 1.0) {
                    for (int i = 0; i < n; i++) r(i) = alpha * p(i) + (1.0 - alpha) * q(i);
                    found = true;
#ifdef _TEST_HYPERBOX_
                    printf("ALPHA = %f, beta = %f, pos = %d\n", alpha, beta, pos);
#endif
                }

                if (beta >= 0.0 && beta <= 1.0) {
                    for (int i = 0; i < n; i++) r(i) = beta * p(i) + (1.0 - beta) * q(i);
                    found = true;
#ifdef _TEST_HYPERBOX_
                    printf("alpha = %f, BETA = %f, pos = %d\n", alpha, beta, pos);
#endif
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

}



