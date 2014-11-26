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
#include "LSODE.h"
#include "RealVector.h"
#include "RectBoundary.h"

Fl_Double_Window *win;
Fl_Slider *slider;
Canvas *canvas;

//int circle_field(const RealVector &in, RealVector &out, int *field_object, int *field_data){

//int circle_field(int *dimension, double *xi, double *in, double *out, int *obj, double *data){
//    // First circle

//    double x = in[0];
//    double y = in[1];

//    out[0] = -.01*y;
//    out[1] =  x;

//    return FIELD_OK;
//}

//int jacobian_circle(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrowpd){
//    pd[0] = 0.0;
//    pd[1] = -.01;
//    pd[2] =  1.0;
//    pd[3] = 0.0;

//    return JACOBIAN_FIELD_OK;
//}

double Gm = 1e1;

int single_body_field(int *dimension, double *xi, double *in, double *out, int *obj, double *data){
    double x  = in[0];
    double y  = in[1];
    double vx = in[2];
    double vy = in[3];

    double norm_r = norm(RealVector(0, 2, in));
    double inv_r3 = 1.0/(norm_r*norm_r*norm_r);

    out[0] = vx;
    out[1] = vy;
    out[2] = -Gm*x*inv_r3;
    out[3] = -Gm*y*inv_r3;

    return FIELD_OK;
}

int jacobian_circle(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrowpd){
    pd[0] = 0.0;
    pd[1] = -.01;
    pd[2] =  1.0;
    pd[3] = 0.0;

    return JACOBIAN_FIELD_OK;
}

int stiff_field(int *dimension, double *xi, double *in, double *out, int *obj, double *data){
    double x = in[0];
    double v = in[1]; // v = dx/dt

    double m = 1.0;
    double c = 1.0;
    double k = 1.0;

    out[0] = v;
    out[1] = (-c*v - k*x)/m;

    return FIELD_OK;
}

int jacobian_stiff_field(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrowpd){
    double m = 1.0;
    double c = 1.0;
    double k = 1.0;

    pd[0] = 0.0;
    pd[1] = -(c + k)/m;
    pd[2] =  1.0;
    pd[3] = 0.0;

    return JACOBIAN_FIELD_OK;
}

void slidercb(Fl_Widget*, void*){
    RealVector pmin(4), pmax(4);
    pmin(0) = pmin(1) = pmin(2) = pmin(3) = -1.5;
    pmax = -pmin;

    RectBoundary boundary(pmin, pmax);

    LSODE lsode;

    RealVector init(2);
    init(0) = 1.0;
    init(1) = 0.0;

    double time = 0.0;

    int n = 500;
    double delta_t = .5;

    std::vector<RealVector> v;
    v.push_back(init);

    std::vector<RealVector> vj;
    vj.push_back(init);

    int (*field)(int *, double *, double *, double *, int *, double *);
    field = stiff_field;

    for (int i = 0; i < n; i++){
        RealVector out(2);
        double final_time = time + delta_t;

        int info = lsode.integrate_step(field, 0, 0 /*function_object*/, 0 /*function_data*/, 
                                        time,  init,  
                                        final_time, out);

        v.push_back(out);

        info = lsode.integrate_step(field, jacobian_stiff_field, 0 /*function_object*/, 0 /*function_data*/, 
                                        time,  init,  
                                        final_time, out);

        vj.push_back(out);

        init = out;
        time = final_time;
    }

    std::cout << "v.size() = " << v.size() << std::endl;

    std::vector<RealVector> r;
    for (int i = 0; i < v.size(); i++) r.push_back(RealVector(0, 2, v[i]));

    Curve2D *nojac = new Curve2D(r, 0.0/255.0, 0.0/255.0, 255.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
    canvas->add(nojac);

    Curve2D *withjac = new Curve2D(vj, 255.0/255.0, 0.0/255.0, 255.0/255.0, /*CURVE2D_MARKERS | */ CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
    canvas->add(withjac);

    return;
}

int main(){
    win = new Fl_Double_Window(10, 10, 700, 700, "Euler Solver");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
        canvas->axis(-2.0, 2.0, -2.0, 2.0);
        canvas->xlabel("x");
        canvas->ylabel("y");
    }
    win->end();
    win->resizable(win);

    win->show();

    slidercb(0, 0);

    return Fl::run();
}

