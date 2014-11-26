#include "FluxSinglePhaseVaporAdimensionalized_Params.h"
#include "FluxSinglePhaseVaporAdimensionalized.h"

#include "AccumulationSinglePhaseVaporAdimensionalized_Params.h"
#include "AccumulationSinglePhaseVaporAdimensionalized.h"

#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

#include "FluxSinglePhaseLiquidAdimensionalized_Params.h"
#include "FluxSinglePhaseLiquidAdimensionalized.h"

#include "AccumulationSinglePhaseLiquidAdimensionalized_Params.h"
#include "AccumulationSinglePhaseLiquidAdimensionalized.h"

#include "Thermodynamics.h"

#include "RectBoundary.h"

#include "LSODE.h"
#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "HugoniotContinuation3D2D.h"
#include "WaveCurveFactory.h"
#include "Inflection_Curve.h"

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

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;

GridValues *grid;

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();
    wsdscroll->clear_all_graphics();
    return;
}

void liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(3);
    canvas->getxy(initial_point(0), initial_point(1));

    // u.
    //
    initial_point(2) = 1.0;

    FILE *fid = fopen("initial_point.txt", "w");
    fprintf(fid, "%lg, %lg", initial_point(0), initial_point(1));
    fclose(fid);

    RarefactionCurve rc(accum, flux, boundary);

    std::cout << "Main, flux = " << flux << ", accum = " << accum << std::endl;

    HugoniotContinuation3D2D hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    CompositeCurve cmp(accum, flux, boundary, &sc, 0);

    LSODE lsode;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, &lsode, &rc, &sc, &cmp);

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
    // Thermo
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // ************************* Physics ************************* //

    // TPCW
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

    Flux2Comp2PhasesAdimensionalized_Params fp(fpp, tc);
    flux = new Flux2Comp2PhasesAdimensionalized(fp);

    double phi = 0.38;

    Accum2Comp2PhasesAdimensionalized_Params ap(tc, phi);
    accum = new Accum2Comp2PhasesAdimensionalized(ap);

    // ************************* Physics ************************* //


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
    number_of_cells[0] = number_of_cells[1] = 128;

    boundary = new RectBoundary(pmin, pmax);

    // ************************* GridValues ************************* //
    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //
    
    // Window
    int main_w  = 900;
    int main_h  = main_w;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Liu\'s wavecurve");
    {
        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());
        canvas->xlabel("saturation of vapor (TPCW)");
        canvas->ylabel("Theta");

        canvas->setextfunc(liuwavecurve, canvas, 0);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

    // ************************* Draw Boundaries ************************* //
    std::vector<std::vector<RealVector> > side;
    boundary->physical_boundary(side);

    for (int i = 0; i < side.size(); i++){
        Curve2D *side_curve = new Curve2D(side[i], 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(side_curve);
    }

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

//    wsdcanvaswin->show();
//    wsdscrollwin->show();

    {
    Inflection_Curve *ic = new Inflection_Curve;

    for (int fam = 0; fam < 2; fam++){
        std::vector<RealVector> inflection_curve;
        ic->curve(flux, accum, *grid, fam, inflection_curve);

        std::cout << "Ok!" << std::endl;

        if (inflection_curve.size() > 0){
            SegmentedCurve *sc = new SegmentedCurve(inflection_curve, 0.0, 1.0, .5);
            canvas->add(sc);

            std::stringstream ss;
            ss << "Inflection, fam = " << fam;
            scroll->add(ss.str().c_str(), canvas, sc);
        }
    }
    }

    Fl::scheme("gtk+");

    return Fl::run();
}

