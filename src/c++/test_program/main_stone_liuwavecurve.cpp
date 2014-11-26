#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"

#include "LSODE.h"
#include "EulerSolver.h"
#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "HugoniotContinuation_nDnD.h"
#include "WaveCurveFactory.h"
#include "Inflection_Curve.h"
#include "Hugoniot_Curve.h"
#include "Explicit_Bifurcation_Curves.h"
#include "Three_Phase_Flow_Explicit_Bifurcation_Curves.h"
#include "Stone_Explicit_Bifurcation_Curves.h"
#include "CharacteristicPolynomialLevels.h"

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
#include "GridValuesPlot.h"

#include "ParametricPlot.h"
//#include "StoneExplicitHugoniot.h"
#include "Secondary_Bifurcation.h"
#include <time.h>

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

    Fl_Group *wavecurvegrp;
        Fl_Round_Button *prev_wavecurve, *next_wavecurve;

    Fl_Group *curvetypegrp;
        Fl_Round_Button *Hugoniot_curve, *rarefaction_curve;

// Wavespeed diagram
// Input here...
Fl_Double_Window   *wsdcanvaswin = (Fl_Double_Window*)0;
    Canvas     *wsdcanvas = (Canvas*)0;

Fl_Double_Window *wsdscrollwin;
    CanvasMenuScroll *wsdscroll;
    Fl_Button        *wsdclear_all_curves;

// Test
Fl_Double_Window *output;
    Fl_Box       *ob = (Fl_Box*)0;

// Riemann problem
Fl_Double_Window *riemannwin;
    Canvas *riemanncanvas;

// Flux objectsHugoniotContinuation_nDnD
StonePermParams   *stonepermparams = (StonePermParams*)0;
StoneParams       *stoneparams     = (StoneParams*)0;
StoneFluxFunction *stoneflux       = (StoneFluxFunction*)0;
StoneAccumulation *stoneaccum      = (StoneAccumulation*)0;

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;

GridValues *grid;

std::vector<std::vector<RealVector> > inflection_curve;
std::vector<RealVector> static_hugoniot_curve;
Curve2D *Hugoniot_segment;

WaveCurve previous_wavecurve;

void eigenvalue_contour(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    std::vector<RealVector> curve;
    double level;

    int family = (fam0->value() == 1) ? 0 : 1;

    CharacteristicPolynomialLevels cpl;
    cpl.eigenvalue_curve(flux, accum, *grid, initial_point, family, 
                         curve, level);

    if (curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(curve, 0.0, 0.0, 1.0);
        canvas->add(sc);
       
        std::stringstream ss;
        ss << "Eigenvalue contour, lev. = " << level;

        scroll->add(ss.str().c_str(), canvas, sc);
    }

    return;
}

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

void rarefaction(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
//    odesolver = &eulersolver;

    // Invoke.
    //
    int family = (fam0->value() == 1) ? 0 : 1;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    RarefactionCurve rarefactioncurve(accum, flux, boundary);
    rarefactioncurve.set_canvas(canvas, scroll);

    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    RealVector final_direction;

    int edge;

    int info_rar = rarefactioncurve.curve(initial_point,
                                          family,
                                          increase,
                                          RAREFACTION,
                                          RAREFACTION_INITIALIZE,
                                          0,
                                          odesolver,
                                          deltaxi,
                                          rarcurve,
                                          inflection_point,
                                          final_direction,
                                          rar_stopped_because,
                                          edge);

    std::cout << "Rar stopped because = " << rar_stopped_because << std::endl;

    if (rarcurve.curve.size() > 0){
        WaveCurve w;
        w.wavecurve.push_back(rarcurve);

//        Curve2D *r = new Curve2D(rarcurve.curve, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
        WaveCurvePlot *r = new WaveCurvePlot(w, CURVE2D_SOLID_LINE | CURVE2D_ARROWS, 0.0, 2.0, 10);

        canvas->add(r);

        std::stringstream ss;
        ss << "Integral curve " << initial_point;
        scroll->add(ss.str().c_str(), canvas, r);
    }

    if (inflection_point.size() > 0){
        Curve2D *ip = new Curve2D(inflection_point, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvas->add(ip);
        scroll->add("Integral points", canvas, ip);
    }

    return;
}

void rarefaction_from_boundary(Fl_Widget*, void*){
    int side;
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    initial_point(0) = .4;
    initial_point(1) = .6;
    side = THREE_PHASE_BOUNDARY_SG_ZERO;

    initial_point(0) = .0;
    initial_point(1) = .453632;
    side = THREE_PHASE_BOUNDARY_SW_ZERO;

    initial_point(0) = .453632;
    initial_point(1) = .0;
    side = THREE_PHASE_BOUNDARY_SO_ZERO;

//    initial_point(0) = .0;
//    initial_point(1) = 0.432188;
//    side = THREE_PHASE_BOUNDARY_SW_ZERO;

//    initial_point(0) = 0.538996;
//    initial_point(1) = 0.461004;
//    side = THREE_PHASE_BOUNDARY_SG_ZERO;

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
//    odesolver = &eulersolver;

    // Invoke.
    //
    int family = (fam0->value() == 1) ? 0 : 1;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    RarefactionCurve rarefactioncurve(accum, flux, boundary);
    rarefactioncurve.set_canvas(canvas, scroll);

    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_points;
    Curve rarcurve;

    int rar_stopped_because;
    RealVector final_direction;

    int edge;

    std::cout << "Before" << std::endl;

    int info_rar = rarefactioncurve.curve_from_boundary(initial_point, side, 
                  family,
                  increase,
                  RAREFACTION, // For itself or as engine for integral curve.
                  odesolver, // Should it be another one for the Bisection? Can it really be const? If so, how to use initialize()?
                  deltaxi,
                  rarcurve,
                  inflection_points, // Will these survive/be added to the Curve class?
                  final_direction,
                  rar_stopped_because, // Similar to Composite.
                  edge);

    std::cout << "After" << std::endl;

    if (rarcurve.curve.size() > 0){
        Curve2D *r = new Curve2D(rarcurve.curve, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS);
        canvas->add(r);

        std::stringstream ss;
        ss << "Rar., inc = " << increase << ", init. = " << initial_point << ", side = " << side << ", size = " << rarcurve.curve.size();
        scroll->add(ss.str().c_str(), canvas, r);
    }

    if (inflection_points.size() > 0){
        Curve2D *ip = new Curve2D(inflection_points, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvas->add(ip);
        scroll->add("Integral points", canvas, ip);
    }

    return;
}

void liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

//    initial_point(0) = .57735026919;
//    initial_point(1) = .42264973081;

    RarefactionCurve rc(accum, flux, boundary);
//    rc.set_canvas(canvas, scroll);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;

    ODE_Solver *odesolver;

    odesolver = &lsode;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

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

    for (int i = 0; i < hwc.wavecurve.size(); i++){
        std::cout << "Curve " << i << " is of type " << hwc.wavecurve[i].type << ", back curve = " << hwc.wavecurve[i].back_curve_index << std::endl;
    }

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;
        for (int i = 0; i < hwc.wavecurve.size(); i++) ss << ", " << hwc.wavecurve[i].curve.size();

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void next_liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    RarefactionCurve rc(accum, flux, boundary);
    rc.set_canvas(canvas, scroll);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
//    odesolver = &eulersolver;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

    // Invoke.
    //
//    int family = (fam0->value() == 1) ? 0 : 1;
    int family = 1;

//    int increase;
//    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
//    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    int increase = SPEED_INCREASE;

    int curve_index;
    int segment_index_in_curve;
    RealVector closest_point;
    double speed;

    Utilities::pick_point_from_wavecurve(previous_wavecurve, initial_point, 
                                         curve_index, segment_index_in_curve, closest_point, speed);

    WaveCurve hwc;
    int reason_why, edge;
    wavecurvefactory.wavecurve(closest_point, family, increase, &hug, hwc, reason_why, edge);

    previous_wavecurve.validate_compatibility(hwc, curve_index, segment_index_in_curve, increase);

    std::cout << "Main. Wavecurve completed." << std::endl;

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, closest_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void liuwavecurve_from_boundary(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

//    initial_point(0) = 0.278362;
//    initial_point(1) = 0.27104;

//    // Panters' problem.
//    //HugoniotContinuation_nDnD
//    initial_point(0) = 0.4714;
//    initial_point(1) = 0.5229;

//    initial_point -= .007;

    initial_point(0) = 0.4;
    initial_point(1) = 0.6;


    // Case where the rarefaction has only two points. Change retreat accordingly.
//    0.337348, 0.330446


    FILE *fid = fopen("initial_point.txt", "w");
    fprintf(fid, "%lg, %lg", initial_point(0), initial_point(1));
    fclose(fid);

    RarefactionCurve rc(accum, flux, boundary);

    std::cout << "Main, flux = " << flux << ", accum = " << accum << std::endl;

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
    odesolver = &eulersolver;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

    // Invoke.
    //
    int family = (fam0->value() == 1) ? 0 : 1;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    WaveCurve hwc;
    int reason_why, edge;

    int side = THREE_PHASE_BOUNDARY_SO_ZERO;
    initial_point(0) = .0;
    initial_point(1) = .4;

    wavecurvefactory.wavecurve_from_boundary(initial_point, side, family, increase, &hug, hwc, reason_why, edge);
//    wavecurvefactory.wavecurve(initial_point, family, increase, &hug, hwc, reason_why, edge);

    std::cout << "Main. Wavecurve completed. Size = " << hwc.wavecurve.size() << std::endl;

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void on_move(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    std::stringstream ss;
    ss << point;
    
    ob->copy_label(ss.str().c_str());
    output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
    output->show();

    Fl::check();

    return;
}

void liuwavecurve_from_inflection(Fl_Widget*, void*){
    if (inflection_curve.size() < 2){
        TestTools::pause("The inflection curves were not computed yet!\nAborting...");
        return;
    }

    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    initial_point(0) = 0.149778;
    initial_point(1) = 0.235149;

    initial_point(0) = 0.296984;
    initial_point(1) = 0.428889;

    initial_point(0) = 0.456181;
    initial_point(1) = 0.271585;

    int family = (fam0->value() == 1) ? 0 : 1;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    RealVector closest_point;

    Utilities::pick_point_from_segmented_curve(inflection_curve[family], initial_point, closest_point);

    std::cout << "Closest point: " << closest_point << std::endl;
//    return;

//    FILE *fid = fopen("initial_point.txt", "w");
//    fprintf(fid, "%lg, %lg", initial_point(0), initial_point(1));
//    fclose(fid);

    RarefactionCurve rc(accum, flux, boundary);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
//    odesolver = &eulersolver;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

    WaveCurve hwc;
    int reason_why, edge;

    wavecurvefactory.wavecurve_from_inflection(inflection_curve[family], initial_point, family, increase, &hug, hwc, reason_why, edge);

    std::cout << "Main. Wavecurve completed. Size = " << hwc.wavecurve.size() << std::endl;

    if (hwc.wavecurve.size() > 0){
//        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point, canvas, scroll);
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void on_move_regions(Fl_Widget*, void*){
//    const char *region_string[6] = {"---", "-+-", "++-", "+++", "+-+", "--+"};

//    RealVector point(2);
//    canvas->getxy(point(0), point(1));

//    Stone_Explicit_Bifurcation_Curves sebc(stoneflux);

//    RealVector crossing_point;
//    int region = -10;

//    sebc.cross_sec_bif(point, point, crossing_point, region);

//    std::cout << "region = " << region << std::endl;

//    if (region >= 0 && region <= 5){
//        ob->copy_label(region_string[region]);
//        output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
//        output->show();
//    }
//    else output->hide();

//    Fl::check();

    return;
}

void on_move_Hugoniot_regions(Fl_Widget*, void*){
    const char *region_string[6] = {"---", "-+-", "++-", "+++", "+-+", "--+"};

    if (static_hugoniot_curve.size() > 2){
        RealVector point(2);
        canvas->getxy(point(0), point(1));

        int index_p0, index_p1;

        RealVector closest_point;

        Utilities::pick_point_from_segmented_curve(static_hugoniot_curve, point, closest_point, index_p0, index_p1);

        std::vector<RealVector> segment;
        segment.push_back(static_hugoniot_curve[index_p0]);
        segment.push_back(static_hugoniot_curve[index_p1]);

        std::vector<std::string> segment_string;
        Stone_Explicit_Bifurcation_Curves s((StoneFluxFunction*)flux);
        
        for (int i = 0; i < segment.size(); i++){
            std::stringstream ss;
            ss << i << ": " << region_string[s.region(s.expressions(segment[i]))];

            segment_string.push_back(ss.str());
        }

        if (Hugoniot_segment != 0) canvas->erase(Hugoniot_segment);
        Hugoniot_segment = new Curve2D(segment, 0.0, 0.0, 0.0, segment_string, CURVE2D_MARKERS | CURVE2D_INDICES);
        canvas->add(Hugoniot_segment);
    }

//    Stone_Explicit_Bifurcation_Curves sebc(stoneflux);

//    RealVector crossing_point;
//    int region = -10;

//    sebc.cross_sec_bif(point, point, crossing_point, region);

//    std::cout << "region = " << region << std::endl;

//    if (region >= 0 && region <= 5){
//        ob->copy_label(region_string[region]);
//        output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
//        output->show();
//    }
//    else output->hide();

//    Fl::check();

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
        

        Curve2D *r = new Curve2D(shockcurve.curve, 0.0, 0.0, 1.0, speed, CURVE2D_SOLID_LINE | CURVE2D_MARKERS | CURVE2D_INDICES);
        canvas->add(r);

        std::stringstream ss;
        ss << "Shock curve " << initial_point;
        scroll->add(ss.str().c_str(), canvas, r);
    }

    if (stop_right_index.size() > 0){
        for (int i = 0; i < stop_right_index.size(); i++){
            std::vector<RealVector> temp;
            temp.push_back(shockcurve.curve[stop_right_index[i]]);

            Curve2D *ip = new Curve2D(temp, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
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
                }
            }
        }
    }

    return;
}

void Hugoniot_transition_explicit_bifurcation(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    // Hugoniot
    Hugoniot_Curve hc;
    ReferencePoint referencepoint(initial_point, flux, accum, 0);

    std::vector<RealVector> hugoniot_curve;
    std::vector< std::deque <RealVector> > curves;
    std::vector <bool> is_circular;
    int method = SEGMENTATION_METHOD;

    static_hugoniot_curve.clear();

    hc.curve(flux, accum, *grid, referencepoint, 
             static_hugoniot_curve,
             curves, is_circular,
             method);

//    {
//        SegmentedCurve *sc = new SegmentedCurve(static_hugoniot_curve, 0.0, 0.0, 1.0);
//        canvas->add(sc);
//        scroll->add("Hugoniot", canvas, sc);

//        Stone_Explicit_Bifurcation_Curves sexpbc((StoneFluxFunction*)flux);
//        
//        std::vector<RealVector> classified_curve;
//        std::vector<int> Lax_transition_index;
//        std::vector<int> region_transition_index;

//        sexpbc.subdivide_segmented_curve_in_regions(static_hugoniot_curve, 
//                                                    classified_curve,
//                                                    Lax_transition_index,
//                                                    region_transition_index);

//        if (region_transition_index.size() > 0){
//            std::vector<RealVector> transitions;
//            for (int i = 0; i < region_transition_index.size(); i++) transitions.push_back(classified_curve[region_transition_index[i]]);

//            Curve2D *c2d = new Curve2D(transitions, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
//            canvas->add(c2d);
//            scroll->add("Transitions", canvas, c2d);
//            std::cout << "region_transition_index.size() = " << region_transition_index.size() << std::endl;
//        }

//    }

    {
        Stone_Explicit_Bifurcation_Curves sexpbc((StoneFluxFunction*)flux);
        
        std::vector<RealVector> classified_curve;
        std::vector<int> Lax_transition_index;
        std::vector<int> region_transition_index;

        sexpbc.subdivide_segmented_curve_in_regions(static_hugoniot_curve, 
                                                    classified_curve,
                                                    Lax_transition_index,
                                                    region_transition_index);

        std::vector<HugoniotPolyLine> Lax_classified_curve;
        std::vector<RealVector> transition_list;

        ColorCurve cc(*flux, *accum);
        cc.classify_segmented_curve(classified_curve, referencepoint,
                                    Lax_classified_curve,
                                    transition_list);

        {
            MultiColoredCurve *mcc = new MultiColoredCurve(Lax_classified_curve);
            canvas->add(mcc);
            scroll->add("Hugoniot", canvas, mcc);
        }

        if (region_transition_index.size() > 0){
            std::vector<RealVector> transitions;
            for (int i = 0; i < region_transition_index.size(); i++) transitions.push_back(classified_curve[region_transition_index[i]]);

            Curve2D *c2d = new Curve2D(transitions, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
            canvas->add(c2d);
            scroll->add("Transitions", canvas, c2d);
            std::cout << "region_transition_index.size() = " << region_transition_index.size() << std::endl;
        }
    }

//    std::vector<HugoniotPolyLine> hugoniot_curve;
//    hc.classified_curve(flux, accum, *grid, referencepoint, hugoniot_curve);
//    {
//        MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve);
//        canvas->add(mcc);
//        scroll->add("Hugoniot", canvas, mcc);
//    }

    return;
}

void prev_wavecurvecb(Fl_Widget *, void*){
    canvas->setextfunc(&liuwavecurve, canvas, 0);

    return;
}

void next_wavecurvecb(Fl_Widget *, void*){
    canvas->setextfunc(&next_liuwavecurve, canvas, 0);

    return;
}

RealVector f(void*, double phi){
    RealVector p(2);

    p(0) = .2 + .05*std::cos(phi);
    p(1) = .2 + .05*std::sin(phi);

//    p(0) = .75*std::cos(phi);
//    p(1) = .75*std::sin(phi);

    return p;
}

void implicit_Hugoniot(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    // Hugoniot
    Hugoniot_Curve hc;
    ReferencePoint referencepoint(initial_point, flux, accum, 0);
    std::vector<HugoniotPolyLine> hugoniot_curve;
    clock_t time_start, time_stop;

    time_start = clock();
 
    hc.classified_curve(flux, accum, *grid, referencepoint, hugoniot_curve);

    time_stop = clock() - time_start;

    std::cout << "Time: " << (double)time_stop/CLOCKS_PER_SEC << std::endl;

    {
//        MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve);
        MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve, 0.0, 2.0, 10);
        canvas->add(mcc);
        scroll->add("Hugoniot", canvas, mcc);
    }

//    // Point on side
//    {
//        int side[3] = {THREE_PHASE_BOUNDARY_SW_ZERO, THREE_PHASE_BOUNDARY_SO_ZERO, THREE_PHASE_BOUNDARY_SG_ZERO};
//        HugoniotContinuation_nDnD hc(flux, accum, boundary);
//        hc.set_reference_point(referencepoint);

//        for (int i = 0; i < 3; i++){
//            RealVector p;
//            bool info = hc.find_a_point_on_a_side(side[i], p);

//            if (info){
//                Curve2D *c = new Curve2D(p, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
//                canvas->add(c);

//                std::stringstream ss;
//                ss << "Seed on side " << i << std::endl;
//                scroll->add(ss.str().c_str(), canvas, c);
//            }
//        }
//    }
//    // Point on side

    // Continuation
    {
        HugoniotContinuation_nDnD hug(flux, accum, boundary);
        hug.set_reference_point(referencepoint);

        std::vector< std::vector<RealVector> > curve;
        hug.curve(curve);

        for (int i = 0; i < curve.size(); i++){
            ColorCurve colorCurve(*flux, *accum);

            std::vector<RealVector> testeTransitionalList;
            std::vector<HugoniotPolyLine> hugoniot_curve(1);

            std::deque<RealVector> curve_deque;
            for (int j = 0; j < curve[i].size(); j++) curve_deque.push_back(curve[i][j]);

            colorCurve.classify_continuous_curve(curve_deque, referencepoint, hugoniot_curve[0], testeTransitionalList); 

            MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve, 0.0, 2.0, 10);
            canvas->add(mcc);
            scroll->add("Hugoniot cont.", canvas, mcc);
        }
    }

    return;
}

void plotcb(Fl_Widget*, void*){
    std::vector<Curve> curve;

    ParametricPlot::plot(&f, (void*)0, boundary, curve);

    for (int i = 0; i < curve.size(); i++){
        if (curve[i].curve.size() > 0){
            Curve2D *c = new Curve2D(curve[i].curve, 0.0, 0.0, 1.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE);
            canvas->add(c);

            std::stringstream ss;
            ss << "Plot " << i << ", size = " << curve[i].curve.size();
            scroll->add(ss.str().c_str(), canvas, c);
        }
    }

    return;
}

void search_secondary_bifurcation(Fl_Widget*, void*){
    Secondary_Bifurcation sb;

    std::vector<RealVector> left_curve; std::vector<RealVector> right_curve;

    sb.curve(flux, accum, *grid,
             flux, accum, *grid,
             left_curve, right_curve);

    if (left_curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(left_curve, 1.0, 0.0, 0.0);
        canvas->add(sc);
        scroll->add("Left", canvas, sc);
    }

    if (right_curve.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(right_curve, 1.0, 0.0, 0.0);
        canvas->add(sc);
        scroll->add("Right", canvas, sc);
    }

    return;
}

void on_move_cell(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    int i, j;
    grid->cell(point, i, j);

    std::stringstream ss;
    ss << i << ", " << j << ", enabled = " << grid->cell_type(i, j);


    ob->copy_label(ss.str().c_str());
    output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
    output->show();


//    Stone_Explicit_Bifurcation_Curves sebc(stoneflux);

//    RealVector crossing_point;
//    int region = -10;

//    sebc.cross_sec_bif(point, point, crossing_point, region);

//    std::cout << "region = " << region << std::endl;

//    if (region >= 0 && region <= 5){
//        ob->copy_label(region_string[region]);
//        output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
//        output->show();
//    }
//    else output->hide();

//    Fl::check();

    return;
}

void R_regions(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    RarefactionCurve rc(accum, flux, boundary);
    rc.set_canvas(canvas, scroll);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;
    EulerSolver eulersolver(boundary, 1);

    ODE_Solver *odesolver;

    odesolver = &lsode;
//    odesolver = &eulersolver;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

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
        for (int i = 0; i < hwc.wavecurve.size(); i++) ss << ", " << hwc.wavecurve[i].curve.size();

        scroll->add(ss.str().c_str(), canvas, wcp);

        // R-regions.
        //
        {
            std::vector<WaveCurve> curves;
            wavecurvefactory.R_regions(&hug, hwc, curves);

            for (int i = 0; i < curves.size(); i++){
                WaveCurvePlot *c = new WaveCurvePlot(curves[i], curves[i].wavecurve[0].curve[0]);
                canvas->add(c);

                std::stringstream s;
                s << "    R-region wavecurve.";
                scroll->add(s.str().c_str(), canvas, c);
            }
        }
    }

    return;
}

double clamp(double x, double xmin, double xmax){
    if (x < xmin) return xmin;
    if (x > xmax) return xmax;

    return x;
}

void Hugoniot_continuation(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    ReferencePoint referencepoint(point, flux, accum, 0);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    std::vector<ShockCurvePoints> shockcurve_with_transitions;

    sc.curve(referencepoint, 
             SHOCKCURVE_SHOCK_CURVE,
             DONT_CHECK_EQUALITY_AT_LEFT /*SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_LEFT*/,
             SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_RIGHT,
             USE_ALL_FAMILIES,
             CONTINUE_AFTER_TRANSITION,
             shockcurve_with_transitions);

    if (shockcurve_with_transitions.size() > 0){
        for (int i = 0; i < shockcurve_with_transitions.size(); i++){
            Curve2D *c = new Curve2D(shockcurve_with_transitions[i].curve, 0.0, 0.0, 1.0);
            canvas->add(c);

            std::stringstream ss;
            ss << "Cont. Hug. i = " << i;
            scroll->add(ss.str().c_str(), canvas, c);
        }

        // Disable the segments on the sides
        for (int i = 0; i < shockcurve_with_transitions.size(); i++){
            RealVector last_point = shockcurve_with_transitions[i].curve.back();

            int n = 50;
            double delta = 1.0/(double)(n - 1);

            double length;
            int side;
            RealVector p0(2), p1(2);

            // What side are we on? Vertices are not dealt with right now.
            //
            if (last_point(0) <= 1e-5 && last_point(1) > 1e-5) {
                // W = 0
                length = clamp(last_point(0), 0.0, 1.0);
                side = THREE_PHASE_BOUNDARY_SW_ZERO;

                p0(0) = 0.0;
                p0(1) = 0.0;

                p1(0) = 0.0;
                p1(1) = 1.0;
            }           
            else if (last_point(1) <= 1e-5 && last_point(0) > 1e-5){
                // O = 0
                length = clamp(last_point(1), 0.0, 1.0);
                side = THREE_PHASE_BOUNDARY_SO_ZERO;

                p0(0) = 0.0;
                p0(1) = 0.0;

                p1(0) = 1.0;
                p1(1) = 0.0;
            }
            else if (std::abs(last_point(0) + last_point(1) - 1.0) < 1e-5){
                // G = 0
                length = clamp(last_point(0), 0.0, 1.0);
                side = THREE_PHASE_BOUNDARY_SG_ZERO;

                p1(0) = 0.0;
                p1(1) = 1.0;

                p0(0) = 1.0;
                p0(1) = 0.0;
            }

            // Segments that will be used or not to find a seed.
            //
            std::vector<bool> use_this_segment(n - 1, true);
            int min_i = (int)std::floor(length/delta);

            std::cout << "length/delta = " << length/delta << ", min_i = " << min_i << std::endl;
            std::cout << "use_this_segment.size() = " << use_this_segment.size() << std::endl;

            use_this_segment[min_i] = false;

            // Points that form the side.
            //
            std::vector<RealVector> side_points;
            for (int i = 0; i < n; i++){
                double alpha = ((double)i)*delta;
                side_points.push_back(alpha*p0 + (1.0 - alpha)*p1);
            }

            //

            RealVector seed;

            hug.set_reference_point(referencepoint);
            bool find = hug.find_a_point_on_a_side(side, side_points, use_this_segment, seed);

            if (find){
                Curve2D *seed_curve = new Curve2D(seed, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
                canvas->add(seed_curve);

                std::stringstream ss;
                ss << "Seed on side " << side;
                scroll->add(ss.str().c_str(), canvas, seed_curve);
            }
        }
    }

    return;
}

void Hugoniot_cb(Fl_Widget*, void*){
    canvas->setextfunc(&implicit_Hugoniot, canvas, 0);

    return;
}

void rarefaction_cb(Fl_Widget*, void*){
    canvas->setextfunc(&rarefaction, canvas, 0);
    return;
}

int main(){
    // Output
    output = new Fl_Double_Window(10, 10, 300, 50, "Info");
    {
        ob = new Fl_Box(0, 0, output->w(), output->h(), "Info");
        ob->labelfont(FL_COURIER);
    }
    output->end();
    output->clear_border();

    // Create fluxFunction
    double expw, expg, expo; expw = 0.0; expg = 0.0; expo = 2.0;
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
    double mug =  .5;
    double muo = 2.0;

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

    stoneparams = new StoneParams(p);

    stoneflux   = new StoneFluxFunction(*stoneparams, *stonepermparams);
//    stoneflux->gravity(false);

    stoneaccum  = new StoneAccumulation;

    flux = stoneflux;
    accum = stoneaccum;
    boundary = new Three_Phase_Boundary();
    std::cout << boundary->max_distance() << std::endl;

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 256;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //
    
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
//        canvas->setextfunc(&R_regions, canvas, 0);
//        canvas->setextfunc(&plotcb, canvas, 0);
//        canvas->setextfunc(&explicit_hugoniot, canvas, 0);
//        canvas->setextfunc(&implicit_Hugoniot, canvas, 0);
//        canvas->setextfunc(&eigenvalue_contour, canvas, 0);

//        canvas->setextfunc(&search_secondary_bifurcation, canvas, 0);

//        canvas->setextfunc(&liuwavecurve_from_inflection, canvas, 0);
//        canvas->setextfunc(&Hugoniot_transition_explicit_bifurcation, canvas, 0);
//        canvas->setextfunc(&shock, canvas, 0);
//        canvas->setextfunc(liuwavecurve_from_boundary, canvas, 0);
//        canvas->setextfunc(rarefaction_from_boundary, canvas, 0);
//        canvas->setextfunc(&rarefaction, canvas, 0);
//        canvas->on_move(&on_move, canvas, 0);
//        canvas->on_move(&on_move_Hugoniot_regions, canvas, 0);
//        canvas->on_move(&on_move_regions, canvas, 0);
//        canvas->on_move(&on_move_cell, canvas, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
//        canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(wincb);

    // ************************* Draw Boundaries ************************* //
    std::vector<std::vector<RealVector> > side;
    boundary->physical_boundary(side);

    for (int i = 0; i < side.size(); i++){
        Curve2D *side_curve = new Curve2D(side[i], 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(side_curve);
    }
    canvas->nozoom();

    // List of curves
    scrollwin = new Fl_Double_Window(canvaswin->x() + canvaswin->w() + 10, canvaswin->y(), 500, 500 + 45 + 40 + 45, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 2*10 - 25 - 45 - 40 - 45, "Curves");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        famgrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        fam0 = new Fl_Round_Button(10, clear_all_curves->y() + clear_all_curves->h() + 10, (scrollwin->w() - 30)/2, 25, "Family 0");
        fam0->type(FL_RADIO_BUTTON);
        fam0->value(0);

        fam1 = new Fl_Round_Button(fam0->x() + fam0->w() + 10, fam0->y(), fam0->w(), fam0->h(), "Family 1");
        fam1->type(FL_RADIO_BUTTON);
        fam1->value(1);
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

        wavecurvegrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        prev_wavecurve = new Fl_Round_Button(increase_button->x(), increase_button->y() + increase_button->h() + 10, increase_button->w(), increase_button->h(), "Slow wavecurve");
        prev_wavecurve->type(FL_RADIO_BUTTON);
        prev_wavecurve->value(1);
        prev_wavecurve->callback(prev_wavecurvecb);

        next_wavecurve = new Fl_Round_Button(decrease_button->x(), decrease_button->y() + decrease_button->h() + 10, decrease_button->w(), decrease_button->h(), "Fast wavecurve");
        next_wavecurve->type(FL_RADIO_BUTTON);
        next_wavecurve->value(0);
        next_wavecurve->callback(next_wavecurvecb);
        }
        wavecurvegrp->end();

        curvetypegrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        Hugoniot_curve = new Fl_Round_Button(increase_button->x(), prev_wavecurve->y() + prev_wavecurve->h() + 10, increase_button->w(), increase_button->h(), "Hugoniot");
        Hugoniot_curve->type(FL_RADIO_BUTTON);
        Hugoniot_curve->value(1);
        Hugoniot_curve->callback(Hugoniot_cb);

        rarefaction_curve = new Fl_Round_Button(decrease_button->x(), next_wavecurve->y() + next_wavecurve->h() + 10, decrease_button->w(), decrease_button->h(), "Rarefaction");
        rarefaction_curve->type(FL_RADIO_BUTTON);
        rarefaction_curve->value(0);
        rarefaction_curve->callback(rarefaction_cb);

//        Hugoniot_curve->do_callback();
        }
        curvetypegrp->end();
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(wincb);
    scrollwin->set_non_modal();

    // Show all

    canvaswin->show();
    scrollwin->show();

//    // Draw the Explicit Bifurcation Curves
//    {
//        Stone_Explicit_Bifurcation_Curves sebc(stoneflux);

//        int side[3] = {THREE_PHASE_BOUNDARY_SW_ZERO, THREE_PHASE_BOUNDARY_SO_ZERO, THREE_PHASE_BOUNDARY_SG_ZERO};
//        const char *side_name[3] = {"SW_ZERO", "SO_ZERO", "SG_ZERO"};
//        double rgb[6] = {255.0/255.0, 20.0/255.0, 147.0/255.0, 160.0, 32.0/255.0, 240.0/255.0};

//        for (int i = 0; i < 3; i++){
//            int nos = 20;
//            std::vector<RealVector> vtu, uts;

//            sebc.expl_sec_bif_crv(side[i], nos, vtu, uts);

//            if (vtu.size() > 0){
//                int j = 0;
//                SegmentedCurve *sc = new SegmentedCurve(vtu, rgb[j*3 + 0], rgb[j*3 + 1], rgb[j*3 + 2]);
//                canvas->add(sc);

//                std::stringstream ss;
//                ss << side_name[i] << ", Vertex to umbilicus";

//                scroll->add(ss.str().c_str(), canvas, sc);
//            }

//            if (uts.size() > 0){
//                int j = 1;
//                SegmentedCurve *sc = new SegmentedCurve(uts, rgb[j*3 + 0], rgb[j*3 + 1], rgb[j*3 + 2]);
//                canvas->add(sc);

//                std::stringstream ss;
//                ss << side_name[i] << ", Umbilicus to side";

//                scroll->add(ss.str().c_str(), canvas, sc);
//            }
//        }
//    }

    Fl::scheme("gtk+");

    return Fl::run();
}


