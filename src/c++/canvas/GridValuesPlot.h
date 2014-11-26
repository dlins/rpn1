#ifndef _GRIDVALUESPLOT_
#define _GRIDVALUESPLOT_

#include "graphicobject.h"
#include "curve2d.h"
#include "GridValues.h"

class GridValuesPlot : public GraphicObject {
    private:
    protected:
        std::vector<GraphicObject*> sons;

        void transform(const double *matrix);
    public:
        GridValuesPlot(const GridValues *g);
        ~GridValuesPlot();

        void draw();
        void minmax(Point2D &, Point2D &){return;}
        void pstricks(ofstream *){return;}
};

#endif // _GRIDVALUESPLOT_

