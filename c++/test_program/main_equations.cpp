#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Float_Input.H>
#include <FL/Fl_Button.H>

#include "canvas.h"
#include "curve2d.h"
#include "Quad2C1Equations.h"
#include "Quad3Equations.h"
#include "NewRarefactionCurve.h"
#include "RectBoundary.h"
#include "LSODE.h"
#include <stdio.h>

Boundary *boundary;
Equations *eq;

Fl_Double_Window *win0;
    Canvas *canvas0;

Fl_Double_Window *win1;
    Canvas *canvas1;

Fl_Double_Window *coordwin;
    Fl_Float_Input *u, *v, *w;
    Fl_Button *go;

void gocb(Fl_Widget*, void*){
    RealVector p(3);

    sscanf(u->value(), "%lf", &(p(0)));
    sscanf(v->value(), "%lf", &(p(1)));
    sscanf(w->value(), "%lf", &(p(2)));

     {
        JetMatrix Fjet, Gjet, Cjet;
   
        ((Quad2C1Equations*)eq)->compute(p, 2, Fjet, Gjet, Cjet);
        std::cout << "Fjet:\n" << Fjet << std::endl << std::endl;
        std::cout << "Gjet:\n" << Gjet << std::endl << std::endl;
        std::cout << "Cjet:\n" << Cjet << std::endl << std::endl;

//        return;
    }

    NewRarefactionCurve nrc(eq, boundary);

    int fam = 0;
    int increase = RAREFACTION_SPEED_SHOULD_DECREASE;
    RealVector ref;
    double dd;

    RealVector direction;

    LSODE *odesolver = new LSODE;
    double deltaxi = 1e-3;

    Curve rarcurve;
    std::vector<RealVector> inflection_points;
    RealVector final_direction;
    int reason_why, edge;

    nrc.curve(p,
              fam,
              increase,
              RAREFACTION, // For itself or as engine for integral curve.
              RAREFACTION_INITIALIZE,
              &direction,
              odesolver, // Should it be another one for the Bisection? Can it really be const? If so, how to use initialize()?
              deltaxi,
              rarcurve,
              inflection_points, // Will these survive/be added to the Curve class?
              final_direction,
              reason_why, // Similar to Composite.
              edge);

            {
                std::vector<RealVector> v;
                for (int i = 0; i < rarcurve.curve.size(); i++){
                    RealVector p(2);
                    p(0) = rarcurve.curve[i](0);
                    p(1) = rarcurve.curve[i](1);

                    v.push_back(p);
                }

                Curve2D *c = new Curve2D(v, 1.0, 0.0, 0.0/*, CURVE2D_MARKERS*/);
                canvas0->add(c);
//                canvas0->nozoom();
            }


            {
                std::vector<RealVector> v;
                for (int i = 0; i < rarcurve.curve.size(); i++){
                    RealVector p(2);
                    p(0) = rarcurve.curve[i](2);
                    p(1) = rarcurve.curve[i](1);

                    v.push_back(p);
                }

                Curve2D *c = new Curve2D(v, 1.0, 0.0, 0.0/*, CURVE2D_MARKERS*/);
                canvas1->add(c);
//                canvas1->nozoom();
            }

    return;
}

int main(){
    RealVector pmin(3), pmax(3);
    pmin(0) = pmin(1) = pmin(2) = -2.0;
    pmax = -pmin;

    boundary = new RectBoundary(pmin, pmax);
    eq = new Quad2C1Equations;

    //    Quad3Equations q;

    {
        win0 = new Fl_Double_Window(10, 10, 800, 800, "u, v");
            canvas0 = new Canvas(0, 0, win0->w(), win0->h());
            canvas0->xlabel("u");
            canvas0->ylabel("v");
            canvas0->axis(-2.0, 2.0, -2.0, 2.0);
        win0->end();
    }

    {
        win1 = new Fl_Double_Window(10, 10, 800, 800, "w, v");
            canvas1 = new Canvas(0, 0, win1->w(), win1->h());
            canvas1->xlabel("w");
            canvas1->ylabel("v");
            canvas1->axis(-2.0, 2.0, -2.0, 2.0);
        win1->end();
    }

    coordwin = new Fl_Double_Window(10, 10, 100, 40 + 3*25 + 35, "Coordinates");
    {
        u = new Fl_Float_Input(30, 10, coordwin->w() - (10 + 30), 25, "u");
        u->value("0.1");

        v = new Fl_Float_Input(u->x(), u->y() + u->h() + 10, u->w(), u->h(), "v");
        v->value("0.1");

        w = new Fl_Float_Input(v->x(), v->y() + v->h() + 10, v->w(), v->h(), "w");
        w->value("0.1");

        go = new Fl_Button(10, w->y() + w->h() + 10, coordwin->w() - 20, 25, "Go!");
        go->callback(gocb);
    }
    coordwin->end();

    win0->show();
    win1->show();
    coordwin->show();

    return Fl::run();
}

