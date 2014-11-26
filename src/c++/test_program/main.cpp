#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"

#include "LSODE.h"
#include "EulerSolver.h"
#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "HugoniotContinuation_nDnD.h"
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
#include "MultiColoredCurve.h"
#include "quiverplot.h"
#include "WaveCurvePlot.h"

// Input here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas     *canvas = (Canvas*)0;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves;

    Fl_Group *famgrp;
        Fl_Round_Button  *fam0, *fam1;

    Fl_Group *incgrp;
        Fl_Round_Button  *increase_button, *decrease_button;

// Wavespeed diagram
// Input here...
Fl_Double_Window   *wsdcanvaswin = (Fl_Double_Window*)0;
    Canvas     *wsdcanvas = (Canvas*)0;

Fl_Double_Window *wsdscrollwin;
    CanvasMenuScroll *wsdscroll;
    Fl_Button        *wsdclear_all_curves;

// Flux objects
StonePermParams   *stonepermparams = (StonePermParams*)0;
StoneParams       *stoneparams     = (StoneParams*)0;
StoneFluxFunction *stoneflux       = (StoneFluxFunction*)0;
StoneAccumulation *stoneaccum      = (StoneAccumulation*)0;

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;

GridValues *grid;

void wincb(Fl_Widget *w, void*){
    if ((Fl_Double_Window*)w == canvaswin) exit(0);

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();
    wsdscroll->clear_all_graphics();
    return;
}

void modify_output(const Curve &in, Curve &out){
    out.curve.clear();

    for (int i = 0; i < in.curve.size(); i++){
        RealVector temp = in.curve[i];

        out.curve.push_back(temp);
    }

    return;
}

void liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

//    initial_point(0) = 0.278362;
//    initial_point(1) = 0.27104;

    // Panters' problem.
    //
    initial_point(0) = 0.4714;
    initial_point(1) = 0.5229;

    FILE *fid = fopen("initial_point.txt", "w");
    fprintf(fid, "%lg, %lg", initial_point(0), initial_point(1));
    fclose(fid);

    RarefactionCurve rc(accum, flux, boundary);
    rc.set_graphics(canvas, scroll);
    rc.set_modify(&modify_output);

    std::cout << "Main, flux = " << flux << ", accum = " << accum << std::endl;

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    CompositeCurve cmp(accum, flux, boundary, &sc);
    cmp.set_graphics(canvas, scroll);
    cmp.set_modify(&modify_output);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver = &lsode;
//    ODE_Solver *odesolver = &eulersolver;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);
//    wavecurvefactory.setcanvas(canvas, scroll);
//    wavecurvefactory.set_modify(&modify_output);

    // Invoke.
    //
    int family = (fam0->value() == 1) ? 0 : 1;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    WaveCurve hwc;
    int reason_why, edge;
    wavecurvefactory.wavecurve(initial_point, family, increase, &hug, hwc, reason_why, edge);

    std::cout << "Main. Wavecurve completed." << std::endl;

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;

        scroll->add(ss.str().c_str(), canvas, wcp);
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

    StonePermParams *stonepermparams  = new StonePermParams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);
    //StonePermeability stonepermeability(stonepermparams);

    double grw = 1.0; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    double vel = 1.0; // 0.0

    // Panters' special problem.
    grw = 1.0;
    grg = 0.7;
    gro = 0.8;
    muw = 0.515;
    mug = 0.3;
    muo = 0.8;
    vel = 0.0;

    RealVector p(7);
    p.component(0) = grw;
    p.component(1) = grg;
    p.component(2) = gro;
    p.component(3) = muw;
    p.component(4) = mug;
    p.component(5) = muo;
    p.component(6) = vel;

    stoneparams = new StoneParams(p);

    stoneflux   = new StoneFluxFunction(*stoneparams, *stonepermparams);

    stoneaccum  = new StoneAccumulation;

    flux = stoneflux;
    accum = stoneaccum;
    boundary = new Three_Phase_Boundary();

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 1024;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //
    
    // Window
    int main_w  = 900;
    int main_h  = main_w;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Liu\'s wavecurve");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());
        canvas->xlabel("sw");
        canvas->ylabel("so");
        canvas->number_of_horizontal_ticks(5);
        canvas->number_of_vertical_ticks(5);

        canvas->setextfunc(&liuwavecurve, canvas, 0);
        //canvas->on_move(&on_move, canvas, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(wincb);

    // ************************* Draw Boundaries ************************* //
    std::vector<RealVector> side;
    boundary->physical_boundary(side);
    side.push_back(side[0]);

    Curve2D side_curve(side, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);
    canvas->nozoom();

    // List of curves
    scrollwin = new Fl_Double_Window(canvaswin->x() + canvaswin->w() + 10, canvaswin->y(), 500, 500 + 45, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 2*10 - 25 - 45, "Curves");

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
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(wincb);
    scrollwin->set_non_modal();


    // Wavespeed diagram
    wsdcanvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Wavespeed diagram");
    {
        wsdcanvas = new Canvas(0, 0, wsdcanvaswin->w(), wsdcanvaswin->h());
        wsdcanvas->xlabel("t");
        wsdcanvas->ylabel("speed");
        wsdcanvas->number_of_horizontal_ticks(5);
        wsdcanvas->number_of_vertical_ticks(5);
    }
    wsdcanvaswin->end();
    wsdcanvaswin->resizable(wsdcanvaswin);

    // List of curves
    wsdscrollwin = new Fl_Double_Window(wsdcanvaswin->x() + wsdcanvaswin->w() + 10, wsdcanvaswin->y(), 500, 500 + 45, "Wavespeed");
    {
        wsdscroll = new CanvasMenuScroll(10, 20, wsdscrollwin->w() - 20, wsdscrollwin->h() - 30, "Wavespeed");
    }
    wsdscrollwin->end();
    wsdscrollwin->resizable(wsdscrollwin);
    //scrollwin->callback(no_close_cb);
    wsdscrollwin->set_non_modal();

    // Show all

    canvaswin->show();
    scrollwin->show();

//    wsdcanvaswin->show();
//    wsdscrollwin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

