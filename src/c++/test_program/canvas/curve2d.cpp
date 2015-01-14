#include "curve2d.h"
#include <math.h>
#include "DoubleMatrix.h"
#include "RealVector.h"

Curve2D::Curve2D(const RealVector &p, double rr, double gg, double bb, int t) : GraphicObject(rr, gg, bb), type_(t){
    vp.push_back(Point2D(p(0), p(1)));
}

Curve2D::Curve2D(const vector<RealVector> &v, double rr, double gg, double bb, int t) : GraphicObject(rr, gg, bb), type_(t){
    if (v.size() > 0){
        double xmin = std::numeric_limits<double>::max();
        double xmax = std::numeric_limits<double>::min();
        double ymin = std::numeric_limits<double>::max();
        double ymax = std::numeric_limits<double>::min();

        vp.resize(v.size());

        for (int i = 0; i < v.size(); i++){
            if (v[i].size() < 2) continue;

            vp[i].x = v[i].component(0);
            vp[i].y = v[i].component(1);

            if (xmin > vp[i].x) xmin = vp[i].x;
            if (xmax < vp[i].x) xmax = vp[i].x;
            if (ymin > vp[i].y) ymin = vp[i].y;
            if (ymax < vp[i].y) ymax = vp[i].y;
        }

        pmin.x = xmin; pmin.y = ymin;
        pmax.x = xmax; pmax.y = ymax;
    }
}

Curve2D::Curve2D(const vector<RealVector> &v, double rr, double gg, double bb, const std::vector<std::string> &strorg, int t) : GraphicObject(rr, gg, bb), type_(t){
//    std::cout << "Ctor 1" << std::endl;

    if (v.size() > 0){
        double xmin = std::numeric_limits<double>::max();
        double xmax = std::numeric_limits<double>::min();
        double ymin = std::numeric_limits<double>::max();
        double ymax = std::numeric_limits<double>::min();

        vp.resize(v.size());

        for (int i = 0; i < v.size(); i++){
            if (v[i].size() < 2) continue;

            vp[i].x = v[i].component(0);
            vp[i].y = v[i].component(1);

            if (xmin > vp[i].x) xmin = vp[i].x;
            if (xmax < vp[i].x) xmax = vp[i].x;
            if (ymin > vp[i].y) ymin = vp[i].y;
            if (ymax < vp[i].y) ymax = vp[i].y;
        }

        pmin.x = xmin; pmin.y = ymin;
        pmax.x = xmax; pmax.y = ymax;

        // Copy string
        for (int i = 0; i < strorg.size(); i++) str.push_back(strorg[i]);
    }
}

Curve2D::Curve2D(const vector<Point2D> &v, double rr, double gg, double bb, int t) : GraphicObject(rr, gg, bb), type_(t){
//    std::cout << "Ctor 2" << std::endl;

    if (v.size() > 0){
        double xmin = std::numeric_limits<double>::max();
        double xmax = std::numeric_limits<double>::min();
        double ymin = std::numeric_limits<double>::max();
        double ymax = std::numeric_limits<double>::min();

        vp.resize(v.size());

        for (int i = 0; i < v.size(); i++){
            vp[i] = v[i];

            if (xmin > v[i].x) xmin = v[i].x;
            if (xmax < v[i].x) xmax = v[i].x;
            if (ymin > v[i].y) ymin = v[i].y;
            if (ymax < v[i].y) ymax = v[i].y;
        }

        pmin.x = xmin; pmin.y = ymin;
        pmax.x = xmax; pmax.y = ymax;
    }
}

Curve2D::Curve2D(const vector<Point2D> &v, double rr, double gg, double bb, const std::vector<std::string> &strorg, int t) : GraphicObject(rr, gg, bb), type_(t){
//    std::cout << "Ctor 3" << std::endl;

    if (v.size() > 0){
        double xmin = std::numeric_limits<double>::max();
        double xmax = std::numeric_limits<double>::min();
        double ymin = std::numeric_limits<double>::max();
        double ymax = std::numeric_limits<double>::min();

        vp.resize(v.size());

        for (int i = 0; i < v.size(); i++){
            vp[i] = v[i];

            if (xmin > v[i].x) xmin = v[i].x;
            if (xmax < v[i].x) xmax = v[i].x;
            if (ymin > v[i].y) ymin = v[i].y;
            if (ymax < v[i].y) ymax = v[i].y;
        }

        pmin.x = xmin; pmin.y = ymin;
        pmax.x = xmax; pmax.y = ymax;

        // Copy string
        for (int i = 0; i < strorg.size(); i++) str.push_back(strorg[i]);
    }
}

Curve2D::~Curve2D(){
    vp.clear();
}

void Curve2D::draw(){
    Fl_Color prevcolor = fl_color();

    fl_color(fl_rgb_color((uchar)(255.0*r), (uchar)(255.0*g), (uchar)(255.0*b)));

    if ((type_ & CURVE2D_SOLID_LINE) == CURVE2D_SOLID_LINE){
//        char *dashes[8] = {"0xff", "0xff", "0xff", "0xff", "0xff", "0xff", "0xff", "0xff"};
//        fl_line_style(style_, 0, dashes[0]);
        fl_line_style(style_, 0);

        fl_begin_line();
            for (int i = 0; i < vp.size(); i++) fl_vertex(vp[i].x, vp[i].y);
        fl_end_line();

        fl_line_style(0);
    }

    if ((type_ & CURVE2D_MARKERS) == CURVE2D_MARKERS /*||
        (type_ & CURVE2D_ARROWS) == CURVE2D_ARROWS*/){

        for (int i = 0; i < vp.size(); i++){
            // The marker will be drawn with width = 2*w and height = 2*h.
            // These values, however, are defined in pixels, not in space-coordinates.
            // Therefore, and since the transformation matrix is not explicit at this level,
            // the coordinates of the vertices of the points that form the curve
            // are taken back to GUI-coordinates.
            //
            double xtemp, ytemp;
            xtemp = fl_transform_x(vp[i].x, vp[i].y);
            ytemp = fl_transform_y(vp[i].x, vp[i].y);

            if ((type_ & CURVE2D_MARKERS) == CURVE2D_MARKERS){
                int w, h;
                w = h = 5;

                // Now that the vertices" coordinates" values are known in GUI-space coordinates,
                // it is possible to use them to draw the marker.
                // The current transformation matrix will be stacked, the marker will be drawn, and the
                // transformation matrix will be restored, so future operations in space-coordinates can
                // be performed, i.e., other graphic objects can be drawn.
                //
                fl_push_matrix();
                    fl_color(fl_rgb_color((uchar)(255.0*r), (uchar)(255.0*g), (uchar)(255.0*b)));
                    fl_pie(xtemp - w, ytemp - h, 2*w, 2*h, 0, 360);
                    fl_color(FL_WHITE);
                    fl_pie(xtemp - (w - 2), ytemp - (h - 2), 2*(w - 2), 2*(h - 2), 0, 360);
                fl_pop_matrix();
            }

            if ((type_ & CURVE2D_INDICES) == CURVE2D_INDICES){
                // Now that the vertices" coordinates" values are known in GUI-space coordinates,
                // it is possible to use them to draw the indices.
                // The current transformation matrix will be stacked, the indices will be drawn, and the
                // transformation matrix will be restored, so future operations in space-coordinates can
                // be performed, i.e., other graphic objects can be drawn.
                //

                int old_font_face = fl_font();
                int old_font_size = fl_size();

                fl_push_matrix();
                    Fl_Color current = fl_color();
                    fl_color(fl_rgb_color((uchar)(255.0*r), (uchar)(255.0*g), (uchar)(255.0*b)));
                    char buf[100000];
                    sprintf(buf, "%d", i);

                    // If the corresponding string is not empty, use it too.
                    if (str.size() > i && str[i].size() > 0) sprintf(buf, "%s", str[i].c_str());

                    fl_font(FL_HELVETICA, 14);
                    fl_draw(buf, xtemp + 15, ytemp + 15);

                    
                    fl_line_style(FL_DOT);
                    fl_line(xtemp + 5, ytemp + 5, xtemp + 10, ytemp + 10);
                    fl_line_style(0);

                    fl_color(current);
                fl_pop_matrix();

                fl_font(old_font_face, old_font_size);
            }
        }
    }

    // Draw the arrowheads.
    //
    if ((type_ & CURVE2D_ARROWS) == CURVE2D_ARROWS){
        // Base and tip of the arrow. 
        // The arrow will be draw at the tip, the direction being given by (tip - base).
        //
        RealVector p_base(2), p_tip(2);

        // The arrow's extremes (which do not change, these sizes seem pleasant).
        // It would be even better to have them not in pixels, but in pixels per inch.
        //
        double w, h;
        w = 6.0;
        h =  3.0;

        for (int i = 0; i < arrow_tip.size(); i++){
            int tip_pos  = arrow_tip[i];
            int base_pos = arrow_base[i];

            p_tip(0) = fl_transform_x(vp[tip_pos].x, vp[tip_pos].y);
            p_tip(1) = fl_transform_y(vp[tip_pos].x, vp[tip_pos].y);

            p_base(0) = fl_transform_x(vp[base_pos].x, vp[base_pos].y);
            p_base(1) = fl_transform_y(vp[base_pos].x, vp[base_pos].y);

//            double theta = atan2(p_tip(1) - p_base(1), p_tip(0) - p_base(0));
//            double c = std::cos(theta);
//            double s = std::sin(theta);

            double inv_hypotenuse_length = 1.0/norm(p_tip - p_base);
            double c = (p_tip(0) - p_base(0))*inv_hypotenuse_length;
            double s = (p_tip(1) - p_base(1))*inv_hypotenuse_length;

            // The code below is equivalent to the one using matrix notation.
            // For the sake of clarity and to use it as a future reference, the commented code
            // above will not be erased.
            //
            fl_push_matrix();
                // From the upper tip to the tip.
                //
                fl_line(-w*c - s*h + p_tip(0), -w*s + c*h + p_tip(1),
                        p_tip(0), p_tip(1));
 
                // From the tip to the lower tip.
                //
                fl_line(p_tip(0), p_tip(1),
                        -w*c + s*h + p_tip(0), -w*s - c*h + p_tip(1));

             fl_pop_matrix();

//            RealVector upper_tip(3), lower_tip(3);
//            upper_tip(0) = -w;
//            upper_tip(1) =  h;
//            upper_tip(2) =  1.0;

//            lower_tip(0) = -w;
//            lower_tip(1) = -h;
//            lower_tip(2) =  1.0;

//            // The transformation matrices acting here.
//            //
//            DoubleMatrix Rotation, Tplus, total_transformation;
//            Rotation = Tplus = DoubleMatrix::eye(3);

//            Rotation(0, 0) = c; Rotation(0, 1) = -s;
//            Rotation(1, 0) = s; Rotation(1, 1) =  c;

//            Tplus(0, 2) = p_tip(0);
//            Tplus(1, 2) = p_tip(1);

//            total_transformation = Tplus*Rotation;

//            RealVector upper_tip_screen = total_transformation*upper_tip;
//            RealVector lower_tip_screen = total_transformation*lower_tip;

//            fl_push_matrix();
//                fl_line(upper_tip_screen(0), upper_tip_screen(1),
//                        p_tip(0), p_tip(1));
// 
//                fl_line(p_tip(0), p_tip(1),
//                        lower_tip_screen(0), lower_tip_screen(1));

//             fl_pop_matrix();

         }
    }

    fl_color(prevcolor);
    return;
}

void Curve2D::minmax(Point2D &min, Point2D &max){
    min = pmin;
    max = pmax;

    return;
}

void Curve2D::pstricks(ofstream *name){
    *name << "%%%%%%%%%%%%%%%% Curve %%%%%%%%%%%%%%%%\n";
    *name << "\\definecolor{CurveColor}{rgb}{" << num2str(r) << ", " << num2str(g) << ", " << num2str(b) << "}\n";

    *name << "\\psline[linecolor=CurveColor]";
    char buf[40];
    for (int i = 0; i < vp.size(); i++){
        //*name << "(" << num2str(vp[i].x) << ", " << num2str(vp[i].y) << ")";
        sprintf(buf, "(%3f, %3f)", vp[i].x, vp[i].y);
        *name << buf;
        if ((i + 1) % 5 == 0) *name << "%\n";
    }
    *name << "\n%%%%%%%%%%%%%%%% Curve %%%%%%%%%%%%%%%%\n\n";
    return;
}

void Curve2D::transform(const double *matrix){
    for (int i = 0; i < vp.size(); i++){
        double x = vp[i].x;
        double y = vp[i].y;

        vp[i].x = x*matrix[0] + y*matrix[1] + matrix[2];
        vp[i].y = x*matrix[3] + y*matrix[4] + matrix[5];
    }

    double xmin = vp[0].x;
    double xmax = vp[0].x;
    double ymin = vp[0].y;
    double ymax = vp[0].y;

    for (int i = 0; i < vp.size(); i++){
        if (xmin > vp[i].x) xmin = vp[i].x;
        if (xmax < vp[i].x) xmax = vp[i].x;
        if (ymin > vp[i].y) ymin = vp[i].y;
        if (ymax < vp[i].y) ymax = vp[i].y;
    }

    pmin.x = xmin; pmin.y = ymin;
    pmax.x = xmax; pmax.y = ymax;

    return;
}

