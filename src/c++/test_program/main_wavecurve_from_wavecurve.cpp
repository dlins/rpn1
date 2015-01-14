#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"
#include "Stone_Explicit_Bifurcation_Curves.h"

#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "HugoniotContinuation_nDnD.h"
#include "LSODE.h"
#include "WaveCurveFactory.h"

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
#include "segmentedcurve.h"
#include "quiverplot.h"
#include "WaveCurvePlot.h"
#include "RiemannProblem.h"

#include <time.h>

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;

Fl_Double_Window *canvaswin;
    Canvas *canvas;

// Hoover
Fl_Double_Window *output;
    Fl_Box       *ob = (Fl_Box*)0;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves;

    Fl_Group *famgrp;
        Fl_Round_Button  *fam0, *fam1;

    Fl_Group *incgrp;
        Fl_Round_Button  *increase_button, *decrease_button;

    Fl_Group *wavecurvegrp;
        Fl_Round_Button *prev_wavecurve, *next_wavecurve;

WaveCurve original_wavecurve;

void wincb(Fl_Widget *w, void*){
    if ((Fl_Double_Window*)w == canvaswin) exit(0);

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();
//    wsdscroll->clear_all_graphics();
    return;
}

// Plot.
//
void plot(const WaveCurve &wavecurve, const RealVector &initial_point){
    if (wavecurve.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;
        for (int i = 0; i < wavecurve.wavecurve.size(); i++) ss << ", " << wavecurve.wavecurve[i].curve.size();

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    RarefactionCurve rc(accum, flux, boundary);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;

    ODE_Solver *odesolver;

    odesolver = &lsode;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

    int reason_why, edge;

    if (prev_wavecurve->value() == 1){
        original_wavecurve.clear();

        int family = (fam0->value() == 1) ? 0 : 1;

        int increase;
        if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
        else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

        wavecurvefactory.wavecurve(initial_point, family, increase, &hug, original_wavecurve, reason_why, edge);

        plot(original_wavecurve, initial_point);
    }
    else {
        WaveCurve hwc;
        wavecurvefactory.wavecurve_from_wavecurve(original_wavecurve, initial_point, &hug, hwc, reason_why, edge);

        plot(hwc, initial_point);
    }

    return;
}

int main(){
    // Create fluxFunction
    double expw, expg, expo; expw = expg = 2.0; expo = 2.0;
    double expow, expog;     expow = expog = 2.0;
    double cnw, cng, cno;    cnw = cng = cno = 0.0;
    double lw, lg;           lw = lg = 0.0;
    double low, log;         low = log = 0.0;
    double epsl = 0.0;

    StonePermParams stonepermparams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);

    double grw = 1.0; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

//    muw = 1.0;
//    mug = 0.5;
//    muo = 2.0;

    double vel = 1.0; // 0.0

//    // Panters' special problem.
//    grw = 1.0;
//    grg = 0.7;
//    gro = 0.8;
//    muw = 0.515;
//    mug = 0.3;
//    muo = 0.8;
//    vel = 0.0;

    RealVector p(7);
    p.component(0) = grw;
    p.component(1) = grg;
    p.component(2) = gro;
    p.component(3) = muw;
    p.component(4) = mug;
    p.component(5) = muo;
    p.component(6) = vel;

    StoneParams stoneparams(p);

    StoneFluxFunction stoneflux(stoneparams, stonepermparams);

    StoneAccumulation stoneaccum;

    flux = &stoneflux;
    accum = &stoneaccum;
    boundary = new Three_Phase_Boundary();

    // Main window.
    //
    canvaswin = new Fl_Double_Window(10, 10, 800, 800, "Wavecurve");
    {
        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());
        canvas->xlabel("sw");
        canvas->ylabel("so");

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);

        canvas->setextfunc(liuwavecurve, canvas, 0);
//        canvas->on_move(&on_move, canvas, 0);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(wincb);

    canvaswin->show();

    // List of curves
    scrollwin = new Fl_Double_Window(canvaswin->x() + canvaswin->w() + 10, canvaswin->y(), 500, 500 + 45 + 40, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 2*10 - 25 - 45 - 40, "Curves");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        famgrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        fam0 = new Fl_Round_Button(10, clear_all_curves->y() + clear_all_curves->h() + 10, (scrollwin->w() - 30)/2, 25, "Family 0");
        fam0->type(FL_RADIO_BUTTON);
        fam0->value(1);

        fam1 = new Fl_Round_Button(fam0->x() + fam0->w() + 10, fam0->y(), fam0->w(), fam0->h(), "Family 1");
        fam1->type(FL_RADIO_BUTTON);
        fam1->value(0);
        }
        famgrp->end();

        incgrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        increase_button = new Fl_Round_Button(fam0->x(), fam0->y() + fam0->h() + 10, fam0->w(), fam0->h(), "Increase");
        increase_button->type(FL_RADIO_BUTTON);
        increase_button->value(1);

        decrease_button = new Fl_Round_Button(fam1->x(), fam1->y() + fam1->h() + 10, fam1->w(), fam1->h(), "Decrease");
        decrease_button->type(FL_RADIO_BUTTON);
        decrease_button->value(0);
        }
        incgrp->end();

        wavecurvegrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        prev_wavecurve = new Fl_Round_Button(increase_button->x(), increase_button->y() + increase_button->h() + 10, increase_button->w(), increase_button->h(), "Wavecurve");
        prev_wavecurve->type(FL_RADIO_BUTTON);
        prev_wavecurve->value(1);
//        prev_wavecurve->callback(prev_wavecurvecb);

        next_wavecurve = new Fl_Round_Button(decrease_button->x(), decrease_button->y() + decrease_button->h() + 10, decrease_button->w(), decrease_button->h(), "Wavecurve from wavecurve");
        next_wavecurve->type(FL_RADIO_BUTTON);
        next_wavecurve->value(0);
//        next_wavecurve->callback(next_wavecurvecb);
        }
        wavecurvegrp->end();
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(wincb);
    scrollwin->set_non_modal();
    scrollwin->show();

    // Output
    output = new Fl_Double_Window(10, 10, 300, 50, "Info");
    {
        ob = new Fl_Box(0, 0, output->w(), output->h(), "Info");
        ob->box(FL_THIN_UP_BOX);
        ob->labelfont(FL_COURIER);
    }
    output->end();
    output->clear_border();

    // Draw the boundary.
    //
    std::vector<RealVector> side;
    boundary->physical_boundary(side);
    side.push_back(side[0]);

    Curve2D side_curve(side, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);
    canvas->nozoom();

    Fl::scheme("gtk+");

    return Fl::run();
}

