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
#include "Hugoniot_TP.h"

#include "RectBoundary.h"
#include "SinglePhaseBoundary.h"
#include "Thermodynamics.h"
#include "Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized.h"

#include "FluxSinglePhaseVaporAdimensionalized_Params.h"
#include "FluxSinglePhaseVaporAdimensionalized.h"

#include "AccumulationSinglePhaseVaporAdimensionalized_Params.h"
#include "AccumulationSinglePhaseVaporAdimensionalized.h"

#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

//#include "StoneFluxFunction.h"
//#include "StoneAccumulation.h"
//#include "WaveCurve.h"
//#include "RiemannSolver.h"

//#include "Extension_Curve.h"

//Thermodynamics_Common *thermo;
Thermodynamics_SuperCO2_WaterAdimensionalized *thermo;

SinglePhaseBoundary *boundary = (SinglePhaseBoundary*)0;
RectBoundary *plot_boundary = (RectBoundary*)0;

// Flux objects
//FluxSinglePhaseVaporAdimensionalized_Params *fpar;
FluxSinglePhaseVaporAdimensionalized        *f;

//AccumulationSinglePhaseVaporAdimensionalized_Params *apar;
AccumulationSinglePhaseVaporAdimensionalized        *a;

Flux2Comp2PhasesAdimensionalized   *tpcw_flux;
Accum2Comp2PhasesAdimensionalized  *tpcw_accum;

// Generic flux & accumulation
FluxFunction *flux_ref;
AccumulationFunction *accum_ref;

// GridValues
GridValues *g;

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

// Input here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas            *canvas = (Canvas*)0;

// ...and output here
Fl_Double_Window   *plot_win = (Fl_Double_Window*)0;
    Canvas            *plot_canvas = (Canvas*)0;

Curve2D        *wave_curve = (Curve2D*)0;

QuiverPlot     *quiver    = (QuiverPlot*)0;

// FLTK callbacks
void wincb(Fl_Widget*, void*){
    exit(0);
    return;
}

void computecb(Fl_Widget*, void*){
    double x, y;
    sscanf(xinput->value(), "%lf", &x);
    sscanf(yinput->value(), "%lf", &y);

    std::cout << "Ref.: (" << x << ", " << y << ")" << std::endl;

    Hugoniot_TP htp;
    std::vector<RealVector> hugoniot_curve;
    RealVector ref(3);
    ref.component(0) = x;
    ref.component(1) = y;
    ref.component(2) = 1.0;

    std::cout << "Before curve" << std::endl;

    htp.curve(tpcw_flux, tpcw_accum, 
              *g, 
              flux_ref, accum_ref,
              ref,
              hugoniot_curve);

    printf("After: hugoniot_curve.size() = %d\n", hugoniot_curve.size());
    if (hugoniot_curve.size() == 0) return;

    // Plot
    std::vector<Point2D> p;
    for (int i = 0; i < hugoniot_curve.size()/2; i++){
        p.push_back(Point2D(hugoniot_curve[2*i].component(0),     hugoniot_curve[2*i].component(1)));
        p.push_back(Point2D(hugoniot_curve[2*i + 1].component(0), hugoniot_curve[2*i + 1].component(1)));
    }

    SegmentedCurve *curve2d = new SegmentedCurve(p, 1, 0, 0);
    plot_canvas->add(curve2d);

    char char_info[100];
    sprintf(char_info, "Ref. = (%f, %f). Total number of segments: %d", x, y, hugoniot_curve.size()/2);
    scroll->add(char_info, plot_canvas, curve2d);

    win->redraw();

    canvaswin->show();

    scroll->show();
    Fl::check();

    canvaswin->activate();
    win->activate();
    //Fl::flush();
    return;
}

void coPY_coordinates(Fl_Widget *w, void*){
    // Stone-only
    double x, y;

    char bufx[20], bufy[20];

    // Use the correct flux & accumulation for the reference point
    if (w == canvas){
        std::cout << "Reference at SinglePhase" << std::endl;

        canvas->getxy(x, y);

        flux_ref = f;
        accum_ref = a;
    }
    else {
        std::cout << "Reference at TPCW" << std::endl;

        plot_canvas->getxy(x, y);

        flux_ref = tpcw_flux;
        accum_ref = tpcw_accum;
    }

    sprintf(bufx, "%lf", x); xinput->value(bufx);
    sprintf(bufy, "%lf", y); yinput->value(bufy);

    std::cout << "    For reference: flux = " << flux_ref << ", accumulation = " << accum_ref << std::endl;

    compute->do_callback();

    return;
}


void default_win_cb(Fl_Widget*, void*){
    return;
}

int main(int argc, char *argv[]){
    // Grid Values
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(3), pmax(3);

    pmin.component(0) = 0.0;
    pmin.component(1) = Theta_min;
    pmin.component(2) = 0.0;

    pmax.component(0) = 1.0;
    pmax.component(1) = Theta_max;
    pmax.component(2) = 2.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 256;

    RectBoundary rect(pmin, pmax);

    g = new GridValues(&rect, pmin, pmax, number_of_cells);

    // Thermodynamics_Common
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");
    tc->set_flash(flash);

    //thermo = new Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized();

    /* TEST HERE */ 

    // Create Thermodynamics
    double Tref_rock  = 273.15;
    double Tref_water = 274.3775;
    double pressure   = 100.9;
    double Cr         = 2.029e6;
    double Cw         = 4297.;
    double rhoW_init  = 998.2;
    double T_typical  = 304.63;
    double Rho_typical= 998.2; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
    double U_typical  = 4.22e-6;
    double h_typical  = Cw*(T_typical - Tref_water);   

    thermo = new Thermodynamics_SuperCO2_WaterAdimensionalized(std::string("./c++/rpnumerics/physics/tpcw/"), 
                                                               T_typical, Rho_typical, U_typical);

    /* TEST HERE */


    // TPCW flux & accumulation
    double abs_perm = 3e-12; 
    double sin_beta = 0.0;
    double const_gravity = 9.8;
    bool has_gravity = false;
    bool has_horizontal = true;

    RealVector fpp(12);
    fpp.component(0) = abs_perm;
    fpp.component(1) = sin_beta;
    fpp.component(2) = (double)has_gravity;
    fpp.component(3) = (double)has_horizontal;
    
    fpp.component(4) = 0.0;
    fpp.component(5) = 0.0;
    fpp.component(6) = 2.0;
    fpp.component(7) = 2.0;
    fpp.component(8) = 0.38;
    fpp.component(9) = 304.63;
    fpp.component(10) = 998.2;
    fpp.component(11) = 4.22e-3;

    Flux2Comp2PhasesAdimensionalized_Params fp(fpp, thermo);
    tpcw_flux = new Flux2Comp2PhasesAdimensionalized(fp);

    double phi = 0.38;

    Accum2Comp2PhasesAdimensionalized_Params ap(thermo, phi);
    tpcw_accum = new Accum2Comp2PhasesAdimensionalized(ap);

    std::cout << "Main, TPCW. Flux = " << tpcw_flux << ", accumulation = " << tpcw_accum << std::endl;


    // ********************* //


    // Create the Boundary

//    double Theta_min = 0.099309;
//    double Theta_max = 0.576511;

    boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_VAPOR, &Thermodynamics::Theta2T);

    // SinglePhase flux and accumulation
    FluxSinglePhaseVaporAdimensionalized_Params fpar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0);

    f = new FluxSinglePhaseVaporAdimensionalized(fpar, tc);

//    AccumulationSinglePhaseVaporAdimensionalized_Params apar(mc, mw, 0.0, 
//                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
//                                                    0.0, 0.0,
//                                                    0.0,
//                                                    0.0,
//                                                    0.0,
//                                                    0.0,
//                                                    0.0,
//                                                    0.0,
//                                                    phi);

//    a = new AccumulationSinglePhaseVaporAdimensionalized(apar, thermo);

    std::cout << "Main, SinglePhase. Flux = " << f << ", accumulation = " << a << std::endl;

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

    // Input window
    int max_canvas_size = min(Fl::w(), Fl::h());
    canvaswin = new Fl_Double_Window(max_canvas_size, max_canvas_size, "Input");
    {
        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());    

        RealVector ppmax = boundary->maximums();
        RealVector ppmin = boundary->minimums();

        canvas->axis(ppmin.component(0), ppmax.component(0), ppmin.component(1), ppmax.component(1));
        canvas->color(FL_DARK_CYAN);
        canvas->labelcolor(FL_WHITE);
        canvas->xlabel("xc");
        canvas->ylabel("Theta");
        canvas->show_grid();
        //canvas->hide_text();
        //canvas->hide_ticks();

        canvas->setextfunc(&coPY_coordinates, canvas, 0);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(default_win_cb);

    // Draw the boundary
    std::vector<RealVector> edges;
    boundary->physical_boundary(edges);
    std::cout << "SinglePhase: Physical boundary has " << edges.size() << " elements." << std::endl;
    FILE *fid = fopen("b.m", "w");

    fprintf(fid, "boundary = [\n");
    for (int i = 0; i < edges.size(); i++) fprintf(fid, "    %g %g\n", edges[i].component(0), edges[i].component(1));
    fprintf(fid, "];\n\nplot(boundary(:, 1), boundary(:, 2), \'-\')");

    fclose(fid);


    std::vector<Point2D> edges2d;
    for (int i = 0; i < edges.size(); i++) edges2d.push_back(Point2D(edges[i].component(0), edges[i].component(1)));
    edges.push_back(edges[0]);

    Curve2D side_curve(edges2d, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);

    canvaswin->show();

    // Output window
    plot_win = new Fl_Double_Window(max_canvas_size, max_canvas_size, "Hugoniot");
    {
        plot_canvas = new Canvas(0, 0, plot_win->w(), plot_win->h());

        RealVector ppmax(2);
        ppmax.component(0) = 1.0; // Saturation
        ppmax.component(1) = Theta_max; // Saturation

        RealVector ppmin(2);
        ppmin.component(0) = 0.0; // Saturation
        ppmin.component(1) = Theta_min; // Saturation

        plot_canvas->axis(ppmin.component(0), ppmax.component(0), ppmin.component(1), ppmax.component(1));
        plot_canvas->color(FL_DARK_CYAN);
        plot_canvas->labelcolor(FL_WHITE);
        plot_canvas->xlabel("s");
        plot_canvas->ylabel("Theta");
        plot_canvas->show_grid();

        plot_canvas->setextfunc(&coPY_coordinates, plot_canvas, 0);
    }
    plot_win->end();
    plot_win->show();

    // Draw the border here too
//    RealVector max(2);
//    max.component(0) = 1.0; // Saturation
//    max.component(1) = Theta_max; // Saturation

//    RealVector min(2);
//    min.component(0) = 0.0; // Saturation
//    min.component(1) = Theta_min; // Saturation

    std::vector<Point2D> plot_boundary_points;
    plot_boundary_points.push_back(Point2D(pmin.component(0), pmin.component(1)));
    plot_boundary_points.push_back(Point2D(pmax.component(0), pmin.component(1)));
    plot_boundary_points.push_back(Point2D(pmax.component(0), pmax.component(1)));
    plot_boundary_points.push_back(Point2D(pmin.component(0), pmax.component(1)));
    plot_boundary_points.push_back(plot_boundary_points[0]);
    Curve2D rectplot(plot_boundary_points, 0, 0, 0, CURVE2D_SOLID_LINE);
    plot_canvas->add(&rectplot);


    // Appear!
    win->position((Fl::w() - win->w())/2, (Fl::h() - win->h())/2);
    win->show(argc, argv);

    Fl::scheme("gtk+");

    return Fl::run();
}

