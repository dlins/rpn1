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

#include <vector>
#include <deque>
#include <sstream>
using namespace std;

#include <stdio.h>
#include <stdlib.h>

#include "canvasmenuscroll.h"
#include "canvas.h"
#include "point2d.h"
#include "curve2d.h"
#include "quiverplot.h"
#include "segmentedcurve.h"
#include "WaveCurvePlot.h"

#include "Rarefaction.h"
#include "Hugoniot_Curve.h"

#include "SinglePhaseBoundary.h"
#include "Thermodynamics_Common.h"

#include "FluxSinglePhaseLiquidAdimensionalized_Params.h"
#include "FluxSinglePhaseLiquidAdimensionalized.h"

#include "AccumulationSinglePhaseLiquidAdimensionalized_Params.h"
#include "AccumulationSinglePhaseLiquidAdimensionalized.h"

//#include "StoneFluxFunction.h"
//#include "StoneAccumulation.h"
//#include "WaveCurve.h"
//#include "RiemannSolver.h"

//#include "Extension_Curve.h"

SinglePhaseBoundary *boundary = (SinglePhaseBoundary*)0;

// Flux objects
//FluxSinglePhaseLiquidAdimensionalized_Params *fpar;
FluxSinglePhaseLiquidAdimensionalized        *f;

//AccumulationSinglePhaseLiquidAdimensionalized_Params *apar;
AccumulationSinglePhaseLiquidAdimensionalized        *a;

GridValues *grid;

// FLTK Objects
Fl_Double_Window               *win  = (Fl_Double_Window*)0;
    CanvasMenuScroll         *scroll = (CanvasMenuScroll*)0;

    Fl_Tabs                    *tabs = (Fl_Tabs*)0;
        Fl_Group *vpgrp = (Fl_Group*)0;
            Fl_Float_Input           *xinput = (Fl_Float_Input*)0;
            Fl_Float_Input           *yinput = (Fl_Float_Input*)0;

            Fl_Float_Input           *xref = (Fl_Float_Input*)0;
            Fl_Float_Input           *yref = (Fl_Float_Input*)0;

            Fl_Spinner         *curve_family = (Fl_Spinner*)0;
            Fl_Spinner        *domain_family = (Fl_Spinner*)0;
            Fl_Round_Button        *increase = (Fl_Round_Button*)0;
            Fl_Round_Button        *decrease = (Fl_Round_Button*)0;

        Fl_Group *seggrp = (Fl_Group*)0;
            Fl_Float_Input *PX, *PY, *qx, *qy;
            Fl_Button      *intersectionbtn;

        Fl_Button               *compute = (Fl_Button*)0;

Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas            *canvas = (Canvas*)0;

Fl_Double_Window   *riemannprofilewin = (Fl_Double_Window*)0;
    Canvas            *riemann_canvas = (Canvas*)0;

Fl_Double_Window       *char1 = (Fl_Double_Window*)0;
    Canvas       *char1canvas = (Canvas*)0;

Fl_Double_Window       *char2 = (Fl_Double_Window*)0;
    Canvas       *char2canvas = (Canvas*)0;

Fl_Double_Window       *lambdawin = (Fl_Double_Window*)0;
    Canvas       *lambda_canvas = (Canvas*)0;

Curve2D        *wave_curve = (Curve2D*)0;

SegmentedCurve *segcur = (SegmentedCurve*)0;

// FLTK callbacks
void wincb(Fl_Widget*, void*){
    exit(0);
    return;
}

void computecb(Fl_Widget*, void*){
//    xref->value(xinput->value());
//    yref->value(yinput->value());

    win->deactivate();
    canvaswin->deactivate();

    scroll->hide();
    Fl::check();

    // Compute the rarefaction
    double deltaxi = 1e-3;

    double x, y;
    sscanf(xinput->value(), "%lf", &x);
    sscanf(yinput->value(), "%lf", &y);

    printf("Initial point: (%f, %f)\n", x, y);

    RealVector initial_point(3);
    initial_point.component(0) = x;
    initial_point.component(1) = y;
    initial_point.component(2) = 1.0; // U

    int family = curve_family->value();
    int should_increase = (increase->value()) ? WAVE_FORWARD : WAVE_BACKWARD;

    std::vector<RealVector> hugoniot_curve;
    std::vector< std::deque <RealVector> > curves;
    std::vector <bool> is_circular;
    int method = SEGMENTATION_METHOD;

    Hugoniot_Curve hc;
    int info = hc.curve(f, a, *grid, initial_point, hugoniot_curve, curves, is_circular, method);

    printf("Info = %d; size = %d\n", info, hugoniot_curve.size());

    // Plot
    std::vector<Point2D> p;
    for (int i = 0; i < hugoniot_curve.size(); i++) p.push_back(Point2D(hugoniot_curve[i].component(0), hugoniot_curve[i].component(1)));

    Curve2D *curve2d = new Curve2D(p, 1, 0, 0);
    canvas->add(curve2d);

    char char_info[100];
    sprintf(char_info, "Init = (%f, %f). %s. Family = %d. Total number of points: %d", x, y, (increase->value()) ? "Forward" : "Backward", (int)curve_family->value(), hugoniot_curve.size());
    scroll->add(char_info, canvas, curve2d);

    win->redraw();

    canvaswin->show();

    scroll->show();
    Fl::check();

    canvaswin->activate();
    win->activate();
    //Fl::flush();
//    return;
}

void coPY_coordinates(Fl_Widget*, void*){
    // Stone-only
    double x, y;

    canvas->getxy(x, y);

    char bufx[20], bufy[20];

    sprintf(bufx, "%lf", x); xinput->value(bufx);
    sprintf(bufy, "%lf", y); yinput->value(bufy);

    compute->do_callback();

    return;
}


void default_win_cb(Fl_Widget*, void*){
    return;
}

int main(int argc, char *argv[]){
    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    // Thermo
    double mc = 0.044, mw = 0.018;
    Thermodynamics_Common thermo(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");

    // Create the Boundary

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_LIQUID, &Thermodynamics_Common::Theta2T);

    // Grid
    RealVector pmin = boundary->minimums(), pmax = boundary->maximums();
    pmin.resize(3);
    pmax.resize(3);
    
    std::vector<int> number_of_cells(2);
    number_of_cells[0] = 16;
    number_of_cells[1] = 16;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    std::cout << "Grid: " << grid->grid(0, 0) << "->" << grid->grid(grid->grid.rows() - 1, grid->grid.cols() - 1) << std::endl;

    double phi = 0.15;

    // Flux, accumulation
    FluxSinglePhaseLiquidAdimensionalized_Params fpar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0);

    f = new FluxSinglePhaseLiquidAdimensionalized(fpar, &thermo);

    AccumulationSinglePhaseLiquidAdimensionalized_Params apar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    phi);

    a = new AccumulationSinglePhaseLiquidAdimensionalized(apar, &thermo);    

    // Main window
    win = new Fl_Double_Window(10, 10, 700, 600, "SinglePhase");
    {
        scroll = new CanvasMenuScroll(10, 30, win->w() - 20, 350, "Orbits");

                xinput = new Fl_Float_Input(50, scroll->y() + scroll->h() + 30, (scroll->w() - 100)/2, 25, "xc");
                xinput->value("0.012599");
                yinput = new Fl_Float_Input(xinput->x() + xinput->w() + 40, xinput->y(), xinput->w(), xinput->h(), "Theta");
                yinput->value("0.215024");

//                xref   = new Fl_Float_Input(xinput->x(), xinput->y() + xinput->h() + 10, xinput->w(), xinput->h(), "x ref");
//                xref->value(".312185");
//                xref->hide();
//                yref   = new Fl_Float_Input(yinput->x(), yinput->y() + yinput->h() + 10, yinput->w(), yinput->h(), "y ref");
//                yref->value(".513705");
//                yref->hide();
        
                curve_family = new Fl_Spinner(110, xinput->y() + xinput->h() + 10, 40, 25, "Curve family");
                curve_family->range(0.0, 1.0);
                curve_family->value(0.0);

//                domain_family = new Fl_Spinner(curve_family->x() + curve_family->w() + 120, curve_family->y(), curve_family->w(), curve_family->h(), "Domain family");
//                domain_family->range(0.0, 1.0);
//                domain_family->value(0.0);
//                domain_family->hide();

                increase = new Fl_Round_Button(curve_family->x() + curve_family->w() + 10, curve_family->y(), 90, curve_family->h(), "Forward");
                increase->type(FL_RADIO_BUTTON);
                increase->value(1);

                decrease = new Fl_Round_Button(increase->x() + increase->w() + 10, increase->y(), increase->w(), increase->h(), "Backward");
                decrease->type(FL_RADIO_BUTTON);


        compute = new Fl_Button(10, decrease->y() + decrease->h() + 10, win->w() - 20, 25, "Compute the curve");
        compute->callback(computecb);
    }
    win->end();
    win->resizable(scroll);
    win->callback(wincb);

    // Rarefaction canvas window
    int max_canvas_size = min(Fl::w(), Fl::h());
    canvaswin = new Fl_Double_Window(max_canvas_size, max_canvas_size, "Hugoniot (SinglePhase)");
    {
        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());    

        RealVector max = boundary->maximums();
        RealVector min = boundary->minimums();

        canvas->axis(min.component(0), max.component(0), min.component(1), max.component(1));
        canvas->color(FL_DARK_CYAN);
        canvas->labelcolor(FL_WHITE);
        canvas->xlabel("xc");
        canvas->ylabel("Theta");
        canvas->show_grid();
        //canvas->hide_text();
        //canvas->hide_ticks();

        canvas->setextfunc(&coPY_coordinates, 0, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        //canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(default_win_cb);

    // Draw the boundary
    std::vector<RealVector> edges;
    boundary->physical_boundary(edges);

    std::vector<Point2D> edges2d;
    for (int i = 0; i < edges.size(); i++) edges2d.push_back(Point2D(edges[i].component(0), edges[i].component(1)));
    edges.push_back(edges[0]);

    Curve2D side_curve(edges2d, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);

    canvaswin->show();


    // Appear!
    win->position((Fl::w() - win->w())/2, (Fl::h() - win->h())/2);
    win->show(argc, argv);

    Fl::scheme("gtk+");

    return Fl::run();
}

