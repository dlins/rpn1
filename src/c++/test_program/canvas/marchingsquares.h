#ifndef _MARCHINGSQUARES_
#define _MARCHINGSQUARES_

#include "point2d.h"
#include "segmentedcurve.h"

#include <stdlib.h>
#include <vector>
#include <iostream>
#include <fstream>
using namespace std;

class MarchingSquares {
    private:
    protected:
        static int twopower[4];
        static int lut[16][4];
        static Point2D zero(const int *, int, const double*, Point2D*);
    public:
        static void marching_squares(double (*)(Point2D &), const vector<double> &, 
                              const Point2D&, const Point2D&,
                              int, int,
                              vector<SegmentedCurve*>&);
};

#endif // _MARCHINGSQUARES_

