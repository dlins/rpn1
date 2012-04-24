
#include "Boundary.h"
#include <iostream>
using namespace std;

Boundary::~Boundary() {
};
//double Boundary::epsilon = 1e-10;

int Boundary::intersection(const RealVector &p, const RealVector &q, RealVector &r, int &w) const {
    w = -1;


    if (inside(p) && inside(q)) return 1;
    else if (!inside(p) && !inside(q)) {
        cout << "Both outside, should abort" << endl;
        return -1;
    } else {
        int n = p.size();

        // Initialize the temporal points
        double *pp, *qq;
        pp = new double[n];
        qq = new double[n];

        for (int i = 0; i < n; i++) {
            pp[i] = p.component(i);
            qq[i] = q.component(i);
        }

        // Switch the temporal points if need be, such that pp is inside and qq is outside
        if (!inside(pp)) {
            double temp;
            for (int i = 0; i < n; i++) {
                temp = pp[i];
                pp[i] = qq[i];
                qq[i] = temp;
            }
        }

        // Minimum distance.
        double d = epsilon * distance(n, pp, qq);

        // Mean point
        double *mean = new double[n];

        // Iterate while the distance between the points is greater than d.
#ifdef _TEST_BOUNDARY_
        int it = 0;
#endif
        while (distance(n, pp, qq) > d) {
            for (int i = 0; i < n; i++) mean[i] = (pp[i] + qq[i]) / 2;

            if (inside(mean)) for (int i = 0; i < n; i++) pp[i] = mean[i];
            else for (int i = 0; i < n; i++) qq[i] = mean[i];
#ifdef _TEST_BOUNDARY_
            it++;
#endif
        }

        // Store the point in r
        r.resize(n);
        for (int i = 0; i < n; i++) r.component(i) = mean[i];

        delete mean;
        delete qq;
        delete pp;

#ifdef _TEST_BOUNDARY_
        printf("Iterations = %d\n", it);
#endif


        return 0;

        //
        //
        //    if (inside(p) && inside(q)) return 1;
        //    else if (!inside(p) && !inside(q)) return -1;
        //    else {
        //        int n = p.size();
        //
        //        // Initialize the temporal points
        //        double *pp, *qq;
        //        pp = new double[n];
        //        qq = new double[n];
        //
        //        for (int i = 0; i < n; i++) {
        //            pp[i] = p(i);
        //            qq[i] = q(i);
        //        }
        //
        //        // Switch the temporal points if need be, such that pp is inside and qq is outside
        //        if (!inside(RealVector(n, pp))) {
        //            double temp;
        //            for (int i = 0; i < n; i++) {
        //                temp = pp[i];
        //                pp[i] = qq[i];
        //                qq[i] = temp;
        //            }
        //        }
        //
        //        // Minimum distance.
        //        double d = epsilon * distance(RealVector(n, pp), RealVector(n, qq));
        //
        //        // Mean point
        //        double *mean = new double[n];
        //
        //        // Iterate while the distance between the points is greater than d.
        //#ifdef _TEST_DOMAIN_
        //        int it = 0;
        //#endif
        //        while (distance(RealVector(n, pp), RealVector(n, qq)) > d) {
        //            for (int i = 0; i < n; i++) mean[i] = (pp[i] + qq[i]) / 2;
        //
        //            if (inside(RealVector(n, mean))) for (int i = 0; i < n; i++) pp[i] = mean[i];
        //            else for (int i = 0; i < n; i++) qq[i] = mean[i];
        //#ifdef _TEST_DOMAIN_
        //            it++;
        //#endif
        //        }
        //
        //        // Store the point in r
        //        r.resize(n);
        //        for (int i = 0; i < n; i++) r[i] = mean[i];
        //
        //        delete mean;
        //        delete qq;
        //        delete pp;
        //
        //#ifdef _TEST_DOMAIN_
        //        printf("Iterations = %d\n", it);
        //#endif
        //
        //        return 0;
    }
}

double Boundary::distance(int n, const double * p, const double * q)const {
    double d = 0.0;
    for (int i = 0; i < n; i++) d += (p[i] - q[i])*(p[i] - q[i]);

    return sqrt(d);
}



