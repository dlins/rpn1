#include "RealVector.h"
#include "Extension_Curve.h"
#include <iostream>
#include <vector>
#include <algorithm>

#include "FL/Fl.H"
#include "FL/Fl_Double_Window.H"
#include "FL/Fl_Round_Button.H"

#include "canvas.h"
#include "curve2d.h"

Fl_Double_Window *win;
Canvas *canvas;

Fl_Double_Window *selection_win;
Fl_Round_Button *add_to_hull;
Fl_Round_Button *check_if_in_hull;

Curve2D *convex_hull_curve = 0;
Curve2D *vector_of_points = 0;
Curve2D *point_to_be_checked = 0;

std::vector<RealVector> vr;
std::vector<RealVector> convex_hull_points;

//// http://bbs.dartmouth.edu/~fangq/MATH/download/source/Determining%20if%20a%20point%20lies%20on%20the%20interior%20of%20a%20polygon.htm

//// val = (y - y0)*(x1 - x0) - (x - x0)*(y1 - y0)
////
//double evaluate_line_equation(const RealVector &p0, const RealVector &p1, const RealVector &p){
//    double x = p(0);
//    double y = p(1);

//    double x0 = p0(0);
//    double y0 = p0(1);

//    double x1 = p1(0);
//    double y1 = p1(1);

//    return (y - y0)*(x1 - x0) - (x - x0)*(y1 - y0);
//}

//bool inside_convex_polygon(const std::vector<RealVector> &polygon, const RealVector &point){
//    int n = polygon.size();

//    if (n < 3) return false;

//    // Obtain a first value for the equation of the line between p0 and p1 evaluated in the given point.
//    //
//    double val = evaluate_line_equation(polygon[0], polygon[1], point);

//    bool is_inside = true;
//    int pos = 1;

//    // The point is inside the polygon if the line segments that form the polygon,
//    // when evaluated in the given point, are all positive or negative.
//    //  
//    while (is_inside && pos < n){
//        if (val*evaluate_line_equation(polygon[pos], polygon[(pos + 1) % n], point) < 0.0) is_inside = false;
//        pos++;
//    }

//    return is_inside;
//}

////// To organize clockwise a group of points
////RealVector center(const std::vector<RealVector> &points){
////    RealVector c;

////    int n = points.size();
////    if (n > 0){
////        c.resize(points[0].size());

////        for (int i = 0; i < n; i++) c = c + points[i];

////        c = c/(double)n;
////    }

////    return c;
////}

//// Vector product in 2D of cp and cq.
////
//double vector_product_2D(const RealVector &c, const RealVector &p, const RealVector &q){
//    return (p(0) - c(0))*(q(1) - c(1)) - (p(1) - c(1))*(q(0) - c(0));
//}

//// Returns a list of points on the convex hull in counter-clockwise order.
//// Note: the last point in the returned list is the same as the first one.
////
//// http://en.wikibooks.org/wiki/Algorithm_Implementation/Geometry/Convex_hull/Monotone_chain
////
//void convex_hull(std::vector<RealVector> &polygon, std::vector<RealVector> &ch){
//    int n = polygon.size(), k = 0;
//    ch.resize(2*n);
// 
//    // Sort points lexicographically
//    sort(polygon.begin(), polygon.end());
// 
//    // Build lower hull
//    for (int i = 0; i < n; i++) {
//        while (k >= 2 && vector_product_2D(ch[k - 2], ch[k - 1], polygon[i]) <= 0) k--;
//        ch[k++] = polygon[i];
//    }
// 
//    // Build upper hull
//    for (int i = n - 2, t = k + 1; i >= 0; i--) {
//        while (k >= t && vector_product_2D(ch[k - 2], ch[k - 1], polygon[i]) <= 0) k--;
//        ch[k++] = polygon[i];
//    }
// 
//    ch.resize(k);

//    return;
//}

void clickcb(Fl_Widget*, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    std::cout << "Clicked in: " << p;

    if (isnan(p(0)) || isnan(p(1))){
        std::cout << "...leaving without processing." << std::endl;

//        canvas->nozoom();
        Fl::check();

        return;
    }

    std::cout << "...processing now." << std::endl;

    // Add to hull
    if (add_to_hull->value() == 1){
        vr.push_back(p);

        canvas->clear();

        Curve2D *vector_of_points = new Curve2D(vr, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvas->add(vector_of_points);

        if (vr.size() > 2){
            convex_hull(vr, convex_hull_points);

            Curve2D *convex_hull_curve = new Curve2D(convex_hull_points, 0.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_INDICES | CURVE2D_SOLID_LINE);
            canvas->add(convex_hull_curve);

            std::cout << "\n\nConvex hull:" << std::endl;
            for (int i = 0; i < convex_hull_points.size(); i++) std::cout << "    " << convex_hull_points[i] << std::endl; 
        }
    }
    else {
        if (point_to_be_checked != 0) canvas->erase(point_to_be_checked);

        if (vr.size() > 2){
            bool is_inside = inside_convex_polygon(convex_hull_points, p);
            std::cout << "Point " << p << " is inside convex hull: " << is_inside << std::endl;

            double r = 0.0, g = 0.0, b = 0.0;

            if (is_inside) g = 1.0;
            else           r = 1.0;

            std::vector<RealVector> temp;
            temp.push_back(p);

            point_to_be_checked = new Curve2D(temp, r, g, b, CURVE2D_MARKERS);
            canvas->add(point_to_be_checked);
        }
    }

    Fl::check();

    return;
}

int main(){
    std::vector<RealVector> polygon(3);

    polygon[2].resize(2);
    polygon[2](0) = 0.0;
    polygon[2](1) = 0.0;

    polygon[0].resize(2);
    polygon[0](0) = 1.0;
    polygon[0](1) = 0.0;

    polygon[1].resize(2);
    polygon[1](0) = 0.0;
    polygon[1](1) = 1.0;

    RealVector point(2);
    point(0) = -.3;
    point(1) = .3;

    std::cout << "Point " << point << " is inside the polygon: " << (bool)inside_convex_polygon(polygon, point) << "." << std::endl;

    std::cout << "Before sorting:" << std::endl;
    for (int i = 0; i < polygon.size(); i++) std::cout << "    " << polygon[i] << std::endl;

    sort(polygon.begin(), polygon.end());

    std::cout << "After sorting:" << std::endl;
    for (int i = 0; i < polygon.size(); i++) std::cout << "    " << polygon[i] << std::endl;

    // Convex hull
    win = new Fl_Double_Window(10, 10, 800, 800, "Convex hull");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
        canvas->axis(0, 1, 0, 1);
        canvas->xlabel("x");
        canvas->ylabel("y");

        canvas->setextfunc(&clickcb, canvas, 0);
    }
    win->end();

    win->resizable(win);
    win->show();
   
    selection_win = new Fl_Double_Window(win->x() + win->w(), win->y(), 200, 10 + 25 + 10 + 25 + 10, "Select");
    {
        add_to_hull = new Fl_Round_Button(10, 10, selection_win->w() - 20, 25, "Add to hull");
        add_to_hull->value(1);
        add_to_hull->type(FL_RADIO_BUTTON);

        check_if_in_hull = new Fl_Round_Button(10, add_to_hull->y() + add_to_hull->h() + 10, add_to_hull->w(), add_to_hull->h(), "Check if in hull");
        check_if_in_hull->type(FL_RADIO_BUTTON);
    }
    selection_win->end();

    selection_win->show();

    return Fl::run();
}

