#ifndef _SEGMENTEDCURVE_
#define _SEGMENTEDCURVE_

#include <FL/Fl.H>
#include <FL/fl_draw.H>

#include "point2d.h"
#include "curve2d.h"

#include "RealVector.h"

#include <stdlib.h>

#include <vector>
#include <string>
using namespace std;

class SegmentedCurve : public Curve2D {
    private:
    protected:
        void transform(const double *matrix);
    public:
        SegmentedCurve(const vector<Point2D> &, double, double, double);
        SegmentedCurve(const vector<RealVector> &, double, double, double);

        void draw();
        void pstricks(ofstream *);
};

#endif // _SEGMENTEDCURVE_

