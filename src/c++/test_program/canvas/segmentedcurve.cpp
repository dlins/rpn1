#include "segmentedcurve.h"

SegmentedCurve::SegmentedCurve(const vector<Point2D> &v, double rr, double gg, double bb) : Curve2D(v, rr, gg, bb, CURVE2D_SOLID_LINE){};

SegmentedCurve::SegmentedCurve(const vector<RealVector> &v, double rr, double gg, double bb) : Curve2D(v, rr, gg, bb, CURVE2D_SOLID_LINE){};

void SegmentedCurve::draw(){
    Fl_Color prevcolor = fl_color();

    fl_color(fl_rgb_color((uchar)(255.0*r), (uchar)(255.0*g), (uchar)(255.0*b)));
    for (int i = 0; i < vp.size()/2; i++){
        fl_begin_line();
            fl_vertex(vp[2*i].x, vp[2*i].y);
            fl_vertex(vp[2*i + 1].x, vp[2*i + 1].y);
        fl_end_line();
    }

    fl_color(prevcolor);

    return;
}

void SegmentedCurve::pstricks(ofstream *name){
    *name << "%%%%%%%%%%%%%%%% SegmentedCurve %%%%%%%%%%%%%%%%\n";
    *name << "\\definecolor{SegmentedCurveColor}{rgb}{" << num2str(r) << ", " << num2str(g) << ", " << num2str(b) << "}\n";

    char buf[20];

    for (int i = 0; i < vp.size()/2; i++){
        *name << "\\psline[linecolor=SegmentedCurveColor]";
        sprintf(buf, "%3f", vp[2*i].x);
        *name << "(" << buf << ", ";
        sprintf(buf, "%3f", vp[2*i].y);
        *name << buf << ")";

        sprintf(buf, "%3f", vp[2*i + 1].x);
        *name << "(" << buf << ", ";
        sprintf(buf, "%3f", vp[2*i + 1].y);
        *name << buf << ")\n";
    }

    *name << "\n%%%%%%%%%%%%%%%% SegmentedCurve %%%%%%%%%%%%%%%%\n\n";
    return;
}

void SegmentedCurve::transform(const double *matrix){
    Curve2D::transform(matrix);
    return;
}
