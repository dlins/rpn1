#include "quiverplot.h"

QuiverPlot::QuiverPlot(const vector<Point2D> &pb, const vector<Point2D> &pe, double rr, double gg, double bb) : GraphicObject(rr, gg, bb){
    int n = pb.size();
    if (n != 0){
        p0.resize(n);
        p1.resize(n);
        p2.resize(n);
        p3.resize(n);

        double xmin = pb[0].x;
        double xmax = pb[0].x;
        double ymin = pb[0].y;
        double ymax = pb[0].y;

        for (int i = 0; i < pb.size(); i++){
            p0[i] = pb[i];
            p1[i] = pe[i];

            double length = sqrt((p0[i].x - p1[i].x)*(p0[i].x - p1[i].x) 
                               + (p0[i].y - p1[i].y)*(p0[i].y - p1[i].y));

//            printf("length = %f\n", length);

            // NaN test
            // Details here: http://bytes.com/groups/cpp/588254-how-check-double-inf-nan
            if (length != length){
                p1[i] = p2[i] = p3[i] = p0[i];
            }
            else {
                double l = .2*length, w = 1.0*l;
                double theta = atan2(p1[i].y - p0[i].y, p1[i].x - p0[i].x);
                double c = cos(theta);
                double s = sin(theta);
    
                p2[i] = Point2D((length - l)*c - w*s/2.0 + p0[i].x, 
                                (length - l)*s + w*c/2.0 + p0[i].y);
                p3[i] = Point2D((length - l)*c + w*s/2.0 + p0[i].x, 
                                (length - l)*s - w*c/2.0 + p0[i].y);

            }

            if (xmin > pb[i].x) xmin = pb[i].x;
            if (xmax < pb[i].x) xmax = pb[i].x;
            if (ymin > pb[i].y) ymin = pb[i].y;
            if (ymax < pb[i].y) ymax = pb[i].y;
            if (xmin > pe[i].x) xmin = pe[i].x;
            if (xmax < pe[i].x) xmax = pe[i].x;
            if (ymin > pe[i].y) ymin = pe[i].y;
            if (ymax < pe[i].y) ymax = pe[i].y;
        }

//        pmin.x = xmin; pmin.y = ymin;
//        pmax.x = xmax; pmax.y = ymax;
    }
}

QuiverPlot::~QuiverPlot(){
    p0.clear();
    p1.clear();
    p2.clear();
    p3.clear();
}

void QuiverPlot::draw(){
    Fl_Color prevcolor = fl_color();

    fl_color(fl_rgb_color((uchar)(255.0*r), (uchar)(255.0*g), (uchar)(255.0*b)));

    for (int i = 0; i < p0.size(); i++){
        fl_begin_line();
            fl_vertex(p0[i].x, p0[i].y);
            fl_vertex(p1[i].x, p1[i].y);
        fl_end_line();
        fl_begin_line();
            fl_vertex(p2[i].x, p2[i].y);
            fl_vertex(p1[i].x, p1[i].y);
            fl_vertex(p3[i].x, p3[i].y);
        fl_end_line();
    }
    
    fl_color(prevcolor);
    return;
}

void QuiverPlot::minmax(Point2D &ppmin, Point2D &ppmax){
    ppmin = pmin;
    ppmax = pmax;
}

void QuiverPlot::pstricks(ofstream *name){
    *name << "%%%%%%%%%%%%%%%% QuiverPlot %%%%%%%%%%%%%%%%\n";
    *name << "\\definecolor{QuiverPlotColor}{rgb}{" << num2str(r) << ", " << num2str(g) << ", " << num2str(b) << "}\n";

    for (int i = 0; i < p0.size(); i++){
        *name << "\\psline[linecolor=QuiverPlotColor]";
        *name << "(" << num2str(p0[i].x) << ", " << num2str(p0[i].y) << ")";
        *name << "(" << num2str(p1[i].x) << ", " << num2str(p1[i].y) << ")\n";

        *name << "\\psline[linecolor=QuiverPlotColor]";
        *name << "(" << num2str(p2[i].x) << ", " << num2str(p2[i].y) << ")";
        *name << "(" << num2str(p1[i].x) << ", " << num2str(p1[i].y) << ")";
        *name << "(" << num2str(p3[i].x) << ", " << num2str(p3[i].y) << ")\n\n";
    }
    
    *name << "%%%%%%%%%%%%%%%% QuiverPlot %%%%%%%%%%%%%%%%\n";
    return;
}

void QuiverPlot::transform(const double *matrix){
    for (int i = 0; i < p0.size(); i++){
        double x = p0[i].x;
        double y = p0[i].y;

        p0[i].x = x*matrix[0] + y*matrix[1] + matrix[2];
        p0[i].y = x*matrix[3] + y*matrix[4] + matrix[5];

        x = p1[i].x;
        y = p1[i].y;

        p1[i].x = x*matrix[0] + y*matrix[1] + matrix[2];
        p1[i].y = x*matrix[3] + y*matrix[4] + matrix[5];


        x = p2[i].x;
        y = p2[i].y;

        p2[i].x = x*matrix[0] + y*matrix[1] + matrix[2];
        p2[i].y = x*matrix[3] + y*matrix[4] + matrix[5];


        x = p3[i].x;
        y = p3[i].y;

        p3[i].x = x*matrix[0] + y*matrix[1] + matrix[2];
        p3[i].y = x*matrix[3] + y*matrix[4] + matrix[5];
    }

    return;
}
