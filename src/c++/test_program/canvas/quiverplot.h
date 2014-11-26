#ifndef _QUIVERPLOT_
#define _QUIVERPLOT_

#include <math.h>
#include <vector>
#include <iostream>
#include "graphicobject.h"

using namespace std;

class QuiverPlot : public GraphicObject {
    private:
        vector<Point2D> p0, p1, p2, p3;
    protected:
    public:
        QuiverPlot(const vector<Point2D> &, const vector<Point2D> &, double, double, double);
        ~QuiverPlot();
        void draw();
        void minmax(Point2D &, Point2D &);
        void pstricks(ofstream *);
        void transform(const double *matrix);
};

#endif // _QUIVERPLOT_
