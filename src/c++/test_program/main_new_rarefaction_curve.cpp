#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"

#include "LSODE.h"
#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "HugoniotContinuation_nDnD.h"

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

int rarefaction(const RealVector &initial_point, int family, int increase, Curve &rarcurve, int &reason_why, RealVector &final_direction){
    std::cout << "Rarefaction will be computed now." << std::endl;

    int edge = -1;

    RealVector direction(2);
    direction(0) =  0.0;
    direction(1) = -1.0;

    double deltaxi = 1e-3;
    LSODE lsode;

    std::vector<RealVector> inflection_points;

    RarefactionCurve rc(accum, flux, boundary);
    int info = rc.curve(initial_point,
                        family,
                        increase,
                        RAREFACTION_FOR_ITSELF /* type_of_rarefaction */, // For itself or as engine for integral curve.
//                        RAREFACTION_AS_ENGINE_FOR_INTEGRAL_CURVE,
                        RAREFACTION_INITIALIZE /* should_initialize */,
                        &direction /* const RealVector *direction */,
                        &lsode, // Should it be another one for the Bisection? Can it really be const? If so, how to use initialize()?
                        deltaxi,
                        rarcurve,
                        inflection_points, // Will these survive/be added to the Curve class?
                        final_direction,
                        reason_why, // Similar to Composite.
                        edge);

    std::cout << "Rarefaction completed." << std::endl;
    std::cout << "    Info = " << info << ", edge = " << edge << std::endl;
    std::cout << "    Final direction = " << final_direction << std::endl;

//    for (int i = 0; i < rarcurve.back_pointer.size(); i++) std::cout << "Refers to: " << rarcurve.back_pointer[i] << std::endl;

    if (rarcurve.curve.size() > 0){
        Curve2D *test = new Curve2D(rarcurve.curve, 255.0/255.0, 0.0/255.0, 0.0/255.0,  CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
        canvas->add(test);

        std::stringstream buf;
        buf << "Rarefaction. Init. = " << initial_point << ", size = " << rarcurve.curve.size();
        scroll->add(buf.str().c_str(), canvas, test);

        // Wavespeed diagram
        for (int i = 0; i < 2; i++){

            std::vector<RealVector> eigenvalue;
            for (int j = 0; j < rarcurve.eigenvalues.size(); j++){
                RealVector temp(2);
                temp(0) = deltaxi*(double)j;
                temp(1) = rarcurve.eigenvalues[j](i);

                eigenvalue.push_back(temp);
            }

            Curve2D *lambda = new Curve2D(eigenvalue, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
            wsdcanvas->add(lambda);
            wsdcanvas->nozoom();

            std::stringstream ss;
            ss << "Lambda fam = " << i;
            wsdscroll->add(ss.str().c_str(), wsdcanvas, lambda);
        }
    }

    if (inflection_points.size() > 0){
        Curve2D *test = new Curve2D(inflection_points, 0.0/255.0, 0.0/255.0, 0.0/255.0,  CURVE2D_MARKERS /*| CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
        canvas->add(test);

        std::stringstream buf;
        buf << "Inflection points, size = " << inflection_points.size();
        scroll->add(buf.str().c_str(), canvas, test);
    }

    return info;
}

int composite(Curve &rarcurve, int family, int &reason_why){
    std::cout << "Composite will be computed now." << std::endl;

    int edge = -1;

    HugoniotContinuation_nDnD hug(flux, accum, boundary);

    ShockCurve sc(&hug);

    CompositeCurve cmp(flux, accum, boundary, &sc);
    cmp.set_graphics(wsdcanvas, wsdscroll);

    LSODE lsode;
    double deltaxi = 1e-3;

    Curve compositecurve;
    RealVector final_direction;

    int info = cmp.curve(flux /* *RarFlux */, accum /* *RarAccum */, 
                         boundary /* *Rarboundary */,
                         rarcurve,
                         rarcurve.curve[rarcurve.curve.size() - 1],
                         &lsode, deltaxi,
                         COMPOSITE_BEGINS_AT_INFLECTION /*int where_composite_begins*/, family, 
                         compositecurve, 
                         final_direction,
                         reason_why,
                         edge);

    std::cout << "Composite completed." << std::endl;
    std::cout << "    Info = " << info << ", edge = " << edge << std::endl;
    std::cout << "    Final direction = " << final_direction << std::endl;

    if (compositecurve.curve.size() > 0){
        Curve2D *test = new Curve2D(compositecurve.curve, 0.0/255.0, 255.0/255.0, 0.0/255.0,  CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
        canvas->add(test);

        std::stringstream buf;
        buf << "Composite, size = " << compositecurve.curve.size();
        scroll->add(buf.str().c_str(), canvas, test);

        // Wavespeed diagram
        int total = rarcurve.eigenvalues.size();
        int retreat = 5;

        std::vector<RealVector> sigma;
        for (int j = 0; j < compositecurve.curve.size(); j++){
            RealVector temp(2);
            temp(0) = deltaxi*(rarcurve.curve.size() + (double)j);
            temp(1) = rarcurve.eigenvalues[total - j - retreat](family);

            sigma.push_back(temp);
        }

        Curve2D *speed = new Curve2D(sigma, 0.0, 1.0, 0.0, CURVE2D_SOLID_LINE);
        wsdcanvas->add(speed);
        wsdcanvas->nozoom();

        std::stringstream ss;
        ss << "Speed fam = " << family;
        wsdscroll->add(ss.str().c_str(), wsdcanvas, speed);


        for (int i = 0; i < 2; i++){
            std::vector<RealVector> lambda;
            for (int j = 0; j < compositecurve.curve.size(); j++){
                std::vector<eigenpair> e;
                Eigen::eig(compositecurve.curve[j], flux, accum, e);

                RealVector temp(2);
                temp(0) = deltaxi*(rarcurve.curve.size() + (double)j);
                temp(1) = e[i].r;

                lambda.push_back(temp);
            }

            Curve2D *eigenvalue = new Curve2D(lambda, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE);
            wsdcanvas->add(eigenvalue);

            std::stringstream ss;
            ss << "Lambda over composite, fam = " << i;
            wsdscroll->add(ss.str().c_str(), wsdcanvas, eigenvalue);
        }
    }

    return info;
}

void curve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

//    initial_point(0) = 0.433214;
//    initial_point(1) = 0.102723;

    initial_point(0) = .425025;
    initial_point(1) = .0779703;

    std::cout << "initial = " << initial_point << std::endl;

    LSODE lsode;

    int family = (fam0->value() == 1) ? 0 : 1;
    double deltaxi = 1e-3;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    Curve rarcurve;
    int edge = -1;
    int reason_why;

    std::vector<RealVector> inflection_points;
    RealVector final_direction;

    int info_rar = rarefaction(initial_point, family, increase, rarcurve, reason_why, final_direction);

    if (info_rar == RAREFACTION_OK && reason_why == RAREFACTION_REACHED_INFLECTION){
        int info_cmp = composite(rarcurve, family, reason_why);
    }

    std::cout << "All done. Bye..." << std::endl;

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();
    wsdscroll->clear_all_graphics();
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

    double grw = 1.5; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    double vel = 0.0; // 0.0

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

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Hugoniot2D");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());
        canvas->xlabel("sw");
        canvas->ylabel("so");
        canvas->number_of_horizontal_ticks(5);
        canvas->number_of_vertical_ticks(5);

        canvas->setextfunc(&curve, canvas, 0);
        //canvas->on_move(&on_move, canvas, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

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
        increase_button->value(0);

        decrease_button = new Fl_Round_Button(fam1->x(), fam1->y() + fam1->h() + 10, fam1->w(), fam1->h(), "Decrease");
        decrease_button->type(FL_RADIO_BUTTON);
        decrease_button->value(1);
        }
        incgrp->end();
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    //scrollwin->callback(no_close_cb);
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

    wsdcanvaswin->show();
    wsdscrollwin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

