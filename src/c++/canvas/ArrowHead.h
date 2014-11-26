#ifndef _ARROWHEAD_
#define _ARROWHEAD_

#include <FL/Fl.H>
#include <FL/fl_draw.H>

#include "point2d.h"
#include "graphicobject.h"
#include <vector>
#include <math.h>

class ArrowHead : public GraphicObject {
    private:
    protected:
        std::vector<Point2D> pos, orn;       // Position, orientation for keeping.
        std::vector<Point2D> pos_temp, head; // Position, head for drawing.
    public:
        ArrowHead(const std::vector<Point2D> &p, const std::vector<Point2D> &o, double rr, double gg, double bb);
        ~ArrowHead();

        void draw(void);

        void minmax(Point2D &, Point2D &);
        void pstricks(ofstream *){}

        void transform(const double *matrix);
};

#endif // _ARROWHEAD_

