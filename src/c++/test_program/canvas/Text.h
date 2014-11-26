#ifndef _TEXT_
#define _TEXT_

#include "graphicobject.h"
#include "RealVector.h"
#include <string>

class Text : public GraphicObject {
    private:
    protected:
        std::string string_;
        RealVector pos_, transformed_pos_;
        RealVector shift_in_pixels_;

        DoubleMatrix cm;

        Fl_Color textcolor;
    public:
        Text(const std::string &s, const RealVector &p, const RealVector &sh, int r, int g, int b);
        virtual ~Text();

        void draw(void);
        void minmax(Point2D &min, Point2D &max);
        void pstricks(ofstream *){}
        void transform(const double *matrix);

        void canvas_transformation(const DoubleMatrix &m){cm = m; return;}
};

#endif // _TEXT_

