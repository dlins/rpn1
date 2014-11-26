#include "SplineFlux.h"
#include "StoneAccumulation.h"
#include "RectBoundary.h"

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

        JetMatrix tempj(2);
        flux->jet(temp, tempj, 0);

        temp(1) = .4*tempj.get(0);

        out.curve.push_back(temp);
    }

    return;
}

void modify_output(const std::vector<RealVector> &in, std::vector<RealVector> &out){
    out.clear();

    for (int i = 0; i < in.size(); i++){
        RealVector temp = in[i];

        JetMatrix tempj(2);
        flux->jet(temp, tempj, 0);

        temp(1) = .4*tempj.get(0);

        out.push_back(temp);
    }

    return;
}

void liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    initial_point(0) = -1.26992;
    initial_point(1) = -0.392327;

    FILE *fid = fopen("initial_point.txt", "w");
    fprintf(fid, "%lg, %lg", initial_point(0), initial_point(1));
    fclose(fid);

    RarefactionCurve rc(accum, flux, boundary);

    std::cout << "Main, flux = " << flux << ", accum = " << accum << std::endl;

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    CompositeCurve cmp(accum, flux, boundary, &sc);
//    cmp.set_graphics(canvas, scroll);
    //cmp.set_modify(&modify_output);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;
    odesolver = &eulersolver;
//    odesolver = &lsode;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);
//    wavecurvefactory.setcanvas(canvas, scroll);
    //wavecurvefactory.set_modify(&modify_output);

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
//        // Modify the output
//        for (int i = 0; i < hwc.wavecurve.size(); i++){
//            for (int j = 0; j < hwc.wavecurve[i].curve.size(); j++){
//                RealVector temp = hwc.wavecurve[i].curve[j];

//                JetMatrix tempj(2);
//                flux->jet(temp, tempj, 0);

//                temp(1) = .4*tempj.get(0);

//                hwc.wavecurve[i].curve[j] = temp;
//            }
//        }
//        // Modify the output

        // Modify the output
        for (int i = 0; i < hwc.wavecurve.size(); i++){
            Curve temp(hwc.wavecurve[i]);
            modify_output(hwc.wavecurve[i], temp);
            hwc.wavecurve[i] = temp;
        }
        // Modify the output

        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void shock(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    ReferencePoint referencepoint(initial_point, flux, accum, 0);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
//    odesolver = &eulersolver;

    // Invoke.
    //
    int family = (fam0->value() == 1) ? 0 : 1;

    int increase;
    if (increase_button->value() == 1) increase = SHOCK_SPEED_SHOULD_INCREASE;
    else                               increase = SHOCK_SPEED_SHOULD_DECREASE;

    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);


//    RarefactionCurve rc(accum, flux, boundary, &disc_cont);
    RarefactionCurve rc(accum, flux, boundary);

    double dd;
    RealVector initial_direction;

    rc.initialize(initial_point, family, increase, initial_direction, dd);

    Curve shockcurve;
    std::vector<int> stop_right_index;
    std::vector<int> stop_right_family;
    std::vector<int> stop_reference_index;    
    std::vector<int> stop_reference_family;  
    int edge, shock_stopped_because;

    sc.curve_engine(referencepoint, initial_point, initial_direction, family, 
                    SHOCKCURVE_SHOCK_CURVE, 
                    SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_LEFT,
                    SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_RIGHT,
                    USE_ALL_FAMILIES,
                    CONTINUE_AFTER_TRANSITION,
                    shockcurve, 
                    stop_right_index,
                    stop_right_family,
                    stop_reference_index,
                    stop_reference_family,  
                    shock_stopped_because,
                    edge);

    if (shockcurve.curve.size() > 0){
        std::vector<std::string> speed;
        for (int i = 0; i < shockcurve.curve.size(); i++){
            std::stringstream ss;
            ss << shockcurve.speed[i];

            speed.push_back(ss.str().c_str());
        }
        

        // Modify the output
        Curve temp;
        modify_output(shockcurve, temp);
        shockcurve = temp;

        FILE *fid = fopen("spline_shock_points.txt", "w");
        for (int k = 0; k < temp.curve.size(); k++) fprintf(fid, "%lg, %lg\n", temp.curve[k](0), temp.curve[k](1));
        fclose(fid);

        Curve2D *r = new Curve2D(shockcurve.curve, 0.0, 0.0, 1.0, /*speed, */ CURVE2D_SOLID_LINE /* | CURVE2D_MARKERS | CURVE2D_INDICES*/);
        canvas->add(r);

        std::stringstream ss;
        ss << "Shock curve " << initial_point;
        scroll->add(ss.str().c_str(), canvas, r);
    }

    if (stop_right_index.size() > 0){
        for (int i = 0; i < stop_right_index.size(); i++){
            std::vector<RealVector> temp, temp2;
            temp.push_back(shockcurve.curve[stop_right_index[i]]);

            modify_output(temp, temp2);

            Curve2D *ip = new Curve2D(temp2, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
            canvas->add(ip);
            scroll->add("Right transition point", canvas, ip);

            // Extensions
            {
                std::vector<int> temp_transition_current_index;
                temp_transition_current_index.push_back(stop_right_index[i]);

                std::vector<int> temp_transition_current_family;
                temp_transition_current_family.push_back(stop_right_family[i]);

                std::vector<RealVector> extension;
                std::vector<int> corresponding_Bethe_Wendroff;

//                sc.Bethe_Wendroff_extension(shockcurve, temp_transition_current_index, temp_transition_current_family, shockcurve, 
//                                            extension, corresponding_Bethe_Wendroff);

                std::vector<ExtensionPoint> curve_with_extension;

                sc.Bethe_Wendroff_extension(shockcurve, temp_transition_current_index, 
                                            curve_with_extension);

                if (curve_with_extension.size() > 0){
                    std::vector<RealVector> temp_extension_vector;
                    for (int k = 0; k < curve_with_extension.size(); k++){
                        if (curve_with_extension[k].index_of_extended_point != -1) temp_extension_vector.push_back(curve_with_extension[k].point);
                    }

                    if (temp_extension_vector.size() > 0){
                        Curve2D *ext = new Curve2D(temp_extension_vector, 1.0, 0.0, 0.0, CURVE2D_MARKERS);
                        canvas->add(ext);
                        scroll->add("Bethe-Wendroff extension", canvas, ext);
                    }

                    // Draw a line between the origin and the extensions
                    for (int k = 0; k < curve_with_extension.size(); k++){
                        if (curve_with_extension[k].index_of_extended_point != -1){
                            std::vector<RealVector> orig_to_extension, orig_to_extension_mod;
                            orig_to_extension.push_back(initial_point);
                            orig_to_extension.push_back(curve_with_extension[k].point);

                            modify_output(orig_to_extension, orig_to_extension_mod);

                            Curve2D *org_ext = new Curve2D(orig_to_extension_mod, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
                            canvas->add(org_ext);
                            scroll->add("Orig. -> Ext.", canvas, org_ext);
                        }
                    }
                }
            }
        }
    }

    return;
}

int main(){
    // Spline flux
    RealVector splinepoint(2);
    std::vector<RealVector> splinevector;

    splinepoint(0) = -0.669617; splinepoint(1) = 0.330508;
    splinevector.push_back(splinepoint);

    splinepoint(0) = 0.0; splinepoint(1) = -0.327684;
    splinevector.push_back(splinepoint);

    splinepoint(0) = 0.660767; splinepoint(1) = 0.327684;
    splinevector.push_back(splinepoint);

    splinepoint(0) = -0.516224; splinepoint(1) = -0.00282486;
    splinevector.push_back(splinepoint);

    splinepoint(0) = -0.339233; splinepoint(1) = 0.217514;
    splinevector.push_back(splinepoint);

    splinepoint(0) = 0.330383;     splinepoint(1) = 0.223164;
    splinevector.push_back(splinepoint);

    splinepoint(0) = 0.486726;     splinepoint(1) = 0.00282486;
    splinevector.push_back(splinepoint);

    splinepoint(0) = -0.451327;     splinepoint(1) = -0.00282486;
    splinevector.push_back(splinepoint);

    splinepoint(0) = 0.179941;     splinepoint(1) = -0.00847458;
    splinevector.push_back(splinepoint);

    SplineFlux *sf = new SplineFlux(splinevector);
    flux = sf;
    accum = new StoneAccumulation;
    
    // Window
    int main_w  = 900;
    int main_h  = main_w;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Liu\'s wavecurve");
    {
        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());
        canvas->xlabel("sw");
        canvas->ylabel("so");
        canvas->number_of_horizontal_ticks(5);
        canvas->number_of_vertical_ticks(5);

        canvas->setextfunc(&liuwavecurve, canvas, 0);
        canvas->setextfunc(&shock, canvas, 0);
        //canvas->on_move(&on_move, canvas, 0);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(wincb);

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = -1.4;
    RealVector pmax(2); pmax(0) = pmax(1) =   .9;

    boundary = new RectBoundary(pmin, pmax);

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 1024;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //

    std::vector<RealVector> side;
    boundary->physical_boundary(side);
    side.push_back(side[0]);

    Curve2D side_curve(side, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);
    canvas->nozoom();

    // Plot the flux
    std::vector<RealVector> f;

    int n = 1000;
    double delta = (pmax(0) - pmin(0))/(double)(n - 1);
    for (int i = 0; i < n; i++){
        RealVector w(2);
        w(0) = pmin(0) + (double)i*delta;
        w(1) = 0.0;

        JetMatrix jm;
        flux->jet(w, jm, 2);

        RealVector p(2);
        p(0) = w(0);
        
        p(1) = .4*jm.get(0);

        if (p(1) < pmax(1) && p(1) > pmin(1)) f.push_back(p);
    }

    Curve2D *cf = new Curve2D(f, 0.75, 0.75, 0.75, CURVE2D_SOLID_LINE);
    canvas->add(cf);
    // Plot the flux


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
    scrollwin->callback(wincb);

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

