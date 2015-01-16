#ifndef _GRAPHICOBJECT_
#define _GRAPHICOBJECT_

#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include "point2d.h"
#include "DoubleMatrix.h"

#include <string>
#include <sstream>
#include <fstream>
using namespace std;

class GraphicObject {
    private:
    protected:
        double r, g, b;
        Point2D pmin, pmax;
        bool visible_;

    public:
        GraphicObject(double, double, double);
        virtual ~GraphicObject(){}

        virtual void draw(void) = 0;
        virtual void minmax(Point2D &, Point2D &) = 0;
        virtual void pstricks(ofstream *) = 0;
        string num2str(double);
        void show(void);
        void hide(void);
        bool visible(void);
        void setrgb(const double, const double, const double);
        void getrgb(double&, double&, double&);

        // Do something with this 3x3 matrix of the form:
        //
        //     [a b c; d e f; 0 0 1]
        //
        // that implements a chain of transformations in
        // homogeneous coordinates.
        //
        virtual void transform(const double *matrix);

        virtual void canvas_transformation(const DoubleMatrix &m){return;}
};

#endif // _GRAPHICOBJECT_

