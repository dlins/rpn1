#ifndef _MULTICOLOREDCURVE_
#define _MULTICOLOREDCURVE_

#include "graphicobject.h"
#include "curve2d.h"
#include "ColorCurve.h"
#include "DoubleMatrix.h"

class MultiColoredCurve : public GraphicObject {
    private:
    protected:
        std::vector<GraphicObject*> sons;

        void transform(const double *matrix);

        DoubleMatrix color_table;
    public:
        MultiColoredCurve(const std::vector<HugoniotPolyLine> &w, int = CURVE2D_SOLID_LINE);
        MultiColoredCurve(const std::vector<HugoniotPolyLine> &w, double min_speed, double max_speed, int speed_steps, int = CURVE2D_SOLID_LINE);

        ~MultiColoredCurve();

        void draw();
        void minmax(Point2D &, Point2D &);
        void pstricks(ofstream *){}
};

#endif // _MULTICOLOREDCURVE_

