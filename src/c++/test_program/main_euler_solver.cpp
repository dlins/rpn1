#include <FL/Fl.H>
#include <FL/x.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Group.H>
#include <FL/Fl_Tabs.H>
#include <FL/Fl_Spinner.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Float_Input.H>
#include <FL/fl_ask.H>

#include "canvas.h"
#include "canvasmenuscroll.h"
#include "curve2d.h"

#include "Bisection.h"
#include "EulerSolver.h"
#include "RealVector.h"
#include "RectBoundary.h"

Fl_Double_Window *win;
Fl_Slider *slider;
Canvas *canvas;

//int circle_field(const RealVector &in, RealVector &out, int *field_object, int *field_data){

int circle_field(int *dimension, double *xi, double *in, double *out, int *obj, double *data){
    // First circle

    double x = in[0];
    double y = in[1];

    out[0] = -y;
    out[1] =  x;

    // Second circle
    x = in[2];
    y = in[3];

    out[2] = -y/2.0;
    out[3] =  x/1.0;

    return BISECTION_FUNCTION_OK ;
}

void slidercb(Fl_Widget*, void*){
    RealVector pmin(4), pmax(4);
    pmin(0) = pmin(1) = pmin(2) = pmin(3) = -4.0;
    pmax = -pmin;

    RectBoundary boundary(pmin, pmax);

    EulerSolver ev(&boundary, 20); // 20 subdivisions

    RealVector init(4);
    init(0) = 1.0;
    init(1) = 0.0;
    init(2) = 2.0;
    init(3) = 0.0;

    double time = 0.0;
    double delta_t = .1;

    std::vector<RealVector> v;
    v.push_back(init);

    for (int i = 0; i < 100; i++){
        RealVector out(4);
        double final_time = time + delta_t;

        int info = EulerSolver::euler_solver(circle_field, 0 /*function_object*/, 0 /*function_data*/, 
                                             time,  init,  
                                             final_time, out,
                                             (int*)&ev, 0 /* *euler_data*/);
        v.push_back(out);

        init = out;
        time = final_time;
    }

    for (int i = 0; i < v.size(); i++) std::cout << "v[" << i << "] = " << v[i] << std::endl;

    // Extract first circle:
    std::vector<RealVector> c1;
    for (int i = 0; i < v.size(); i++){
        RealVector temp(2);
        temp(0) = v[i](0);
        temp(1) = v[i](1);

        c1.push_back(temp);
    }

    Curve2D *test1 = new Curve2D(c1, 0.0/255.0, 0.0/255.0, 255.0/255.0, /*CURVE2D_MARKERS | */ CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
    canvas->add(test1);

    // Extract second circle:
    std::vector<RealVector> c2;
    for (int i = 0; i < v.size(); i++){
        RealVector temp(2);
        temp(0) = v[i](2);
        temp(1) = v[i](3);

        c2.push_back(temp);
    }

    Curve2D *test2 = new Curve2D(c2, 255.0/255.0, 0.0/255.0, 0.0/255.0, /*CURVE2D_MARKERS | */ CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
    canvas->add(test2);

    return;
}

int signal_event(const RealVector & where, double & event_measure, int *signal_event_object, int *signal_event_data){
    event_measure = where(0) - where(1);

    return BISECTION_FUNCTION_OK;
}

int main(){
//    // Test the Bisection
//    double t_in = 0.0;
//    RealVector p_in(2); p_in(0) = 1.0; p_in(1) = 0.0;

//    double t_fin = 3.14159256;
//    RealVector p_fin(2); p_fin(0) = -1.0; p_fin(1) = 0.0;

//    double epsilon = 1e-10;

//    double c_t;
//    RealVector p_c;

//    RealVector pmin(2), pmax(2);
//    pmin(0) = pmin(1) = -2.0;
//    pmax = -pmin;

//    RectBoundary boundary(pmin, pmax);

//    EulerSolver ev(&boundary, 20);

//    int info = Bisection::bisection_method(t_in,  p_in,
//                                           t_fin, p_fin,
//                                           epsilon, 
//                                           c_t, p_c, // Output
//                                           &circle_field, 0, 0,
//                                           &EulerSolver::euler_solver, (int*)(&ev), 0,
//                                           &signal_event, 0, 0);

//    std::cout << "Bisection\'s info = " << info << std::endl;
//    std::cout << "    c_t = " << c_t << std::endl;
//    std::cout << "    p_c = " << p_c << std::endl;

    //return 0;

    win = new Fl_Double_Window(10, 10, 700, 700, "Euler Solver");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
        canvas->axis(-2.0, 2.0, -2.0, 2.0);
        canvas->xlabel("x");
        canvas->ylabel("y");
    }
    win->end();

    win->show();

    slidercb(0, 0);

    return Fl::run();
}

