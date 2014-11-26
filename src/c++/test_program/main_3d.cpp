//#define CREATE_COINCIDENCE_CURVE

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

#include "Rarefaction.h"
#include "Thermodynamics.h"
#include "Hugoniot_TP.h"
#include "RectBoundary.h"
#include "SinglePhaseBoundary.h"
#include "ShockContinuationMethod3D2D.h"

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

#include "ReferencePoint.h"
#include "ViscosityJetMatrix.h"
#include "ColorCurve.h"

#include "CoincidenceTP.h"

// TEST
#include "HugoniotContinuation3D2D.h"
#include "HugoniotContinuation_nD_nm1D.h"
#include "HugoniotContinuation_nDnD.h"
#include <string>
// TEST

// Input here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas       *canvas_tpcw = (Canvas*)0;
    Canvas      *canvas_vapor = (Canvas*)0;
    Canvas     *canvas_liquid = (Canvas*)0;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves;

// Test
Fl_Double_Window *output;
    Fl_Box       *ob = (Fl_Box*)0;

VLE_Flash_TPCW *flash;

Flux2Comp2PhasesAdimensionalized  *tpcw_flux;
Accum2Comp2PhasesAdimensionalized *tpcw_accum;
RectBoundary                      *tpcw_boundary;
GridValues                        *tpcw_grid;

FluxSinglePhaseVaporAdimensionalized         *vapor_flux;
AccumulationSinglePhaseVaporAdimensionalized *vapor_accum;
SinglePhaseBoundary                          *vapor_boundary;
GridValues                                   *vapor_grid;

FluxSinglePhaseLiquidAdimensionalized         *liquid_flux;
AccumulationSinglePhaseLiquidAdimensionalized *liquid_accum;
SinglePhaseBoundary                           *liquid_boundary;
GridValues                                    *liquid_grid;

// Common
FluxFunction         *fref;
AccumulationFunction *aref;
Canvas               *cref;
Boundary             *bref;

// To mymic the future use of Physics
//
std::vector<FluxFunction*> flux(3);
std::vector<AccumulationFunction*> accum(3);
std::vector<Canvas*> canvas(3);
std::vector<GridValues*> grid(3);
std::vector<Boundary*> boundary(3);
std::vector<std::string> name(3);

void no_close_cb(Fl_Widget*, void*){
    return;
}

void close_cb(Fl_Widget*, void*){
    ContourMethod::deallocate_arrays();

    exit(0);

    return;
}

void on_move(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector p(3);
    c->getxy(p(0), p(1));
    p(2) = 1.0;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    std::vector<double> lambda;
    Eigen::fill_eigenvalues(flux[subphysics_index], accum[subphysics_index], p, lambda);

    RealVector F(3), G(3);
    DoubleMatrix JF(3, 3), JG(3, 3);
    flux[subphysics_index]->fill_with_jet(3, p.components(), 1, F.components(), JF.data(), 0);
    accum[subphysics_index]->fill_with_jet(3, p.components(), 1, G.components(), JG.data(), 0);

    //std::cout << JF << std::endl;

    std::stringstream out;
    out << "Subphysics: " << name[subphysics_index] << "\nPoint = " << p << "\n";
    for (int i = 0; i < lambda.size(); i++) out << "lambda[" << i << "] = " << lambda[i] << "\n";
    out << "F = " << F << "\nG = " << G << "\n";

    out << "\nJF =\n" << std::scientific << JF << "\n\nJG =\n" << std::scientific << JG;

    std::cout << out.str() << std::endl;
    Fl::check();
    if (ob != 0) ob->copy_label(out.str().c_str());

    //output->position(Fl::event_x_root() + 10, Fl::event_y_root() + 10);
    output->show();

    return;
}

void rarefactioncb(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector initial(3);

    c->getxy(initial.component(0), initial.component(1));
    initial(2) = 1.0;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    std::vector<RealVector> rarcurve, inflection_points;

    Rarefaction::curve(initial, 
                       RAREFACTION_INITIALIZE_YES,
                       0/*  const RealVector *initial_direction*/,
                       1/*  int curve_family*/, 
                       RAREFACTION_SPEED_INCREASE /*  int increase*/,
                       RAREFACTION_FOR_ITSELF /* int type_of_rarefaction*/,
                       1e-3 /* double deltaxi*/,
                       flux[subphysics_index], accum[subphysics_index],
                       RAREFACTION_GENERAL_ACCUMULATION /* int type_of_accumulation*/,
                       boundary[subphysics_index],
                       rarcurve,
                       inflection_points);

    if (rarcurve.size() > 0){
        Curve2D *test = new Curve2D(rarcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
        canvas[subphysics_index]->add(test);

        std::stringstream buf;
        buf << "Rarefaction. Init. = " << initial << ", size = " << rarcurve.size();
        scroll->add(buf.str().c_str(), canvas[subphysics_index], test);

    }

    return;
}

void all_of_Hugoniot(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector ref(3);

    c->getxy(ref.component(0), ref.component(1));
    ref(2) = 1.0;

//    ref(0) = .912109;
//    ref(1) = .417967;

//    ref(0) = 0.85214;
//    ref(1) = 0.414922;

//    ref(0) = 0.649805;
//    ref(1) = 0.227392;

//    ref(0) = 0.848273;
//    ref(1) = 0.188123;

    std::cout << "*************************************** Initial point: " << ref << std::endl;

    std::string where_ref;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    fref      = flux[subphysics_index];
    aref      = accum[subphysics_index];
    bref      = boundary[subphysics_index];
    where_ref = name[subphysics_index];
    cref      = canvas[subphysics_index];

    ReferencePoint referencepoint(ref, fref, aref, 0);

//    // Hugoniot (search)
//    Hugoniot_TP htp;

//    for (int k = 0; k < 3; k++){
//        std::vector<RealVector> hugoniot_curve;
// 
//        htp.curve(flux[k], accum[k], 
//                  *(grid[k]), 
//                  referencepoint, 
//                  hugoniot_curve);

//        std::cout << "Hugoniot " << k << " has " << hugoniot_curve.size()/2 << " points." << std::endl;

//        if (hugoniot_curve.size() > 2){
//            std::vector<Point2D> p;
//            for (int i = 0; i < hugoniot_curve.size()/2; i++){
//                p.push_back(Point2D(hugoniot_curve[2*i].component(0),     hugoniot_curve[2*i].component(1)));
//                p.push_back(Point2D(hugoniot_curve[2*i + 1].component(0), hugoniot_curve[2*i + 1].component(1)));
//            }

//            SegmentedCurve *curve2d = new SegmentedCurve(p, 0, 0, 1);

//            // Classified curve
//            Viscosity_Matrix v;

//            ColorCurve cc(*(flux[k]), *(accum[k]), &v);    
////            ColorCurve cc(*(flux[k]), *(accum[k])); 

//            std::vector<HugoniotPolyLine> classified_curve;
//            std::vector<RealVector> transition_list;

//            cc.classify_segmented_curve(hugoniot_curve, referencepoint,
//                                        classified_curve,
//                                        transition_list);

//            // Color
//            MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve);
//            canvas[k]->add(mcc);

//            char buf[100];
//            sprintf(buf, "Hugoniot (search) over %s with reference on %s. Size = %d.", name[k].c_str(), where_ref.c_str(), hugoniot_curve.size()/2); 
//            scroll->add(buf, canvas[k], mcc);
//        }
//    }
//    // Hugoniot (search)

//    // Hugoniot (continuation)

//    // Theta_ref
//    std::vector<Point2D> Theta_ref_line;
//    Theta_ref_line.push_back(Point2D(0.0, referencepoint.point(1)));
//    Theta_ref_line.push_back(Point2D(1.0, referencepoint.point(1)));

//    Curve2D *Theta_curve = new Curve2D(Theta_ref_line, 0.0/255.0, 0.0/255.0, 0.0/255.0);
//    cref->add(Theta_curve);
//    scroll->add("Theta ref.", cref, Theta_curve);

//    HugoniotContinuation3D2D hug(fref, aref, bref);

    HugoniotContinuation_nD_nm1D hug(fref, aref, bref);

//    HugoniotContinuation_nDnD hug(fref, aref, bref);

    hug.set_reference_point(referencepoint);
//    hug.set_bifurcation_space_coordinate(1);

    std::vector< std::vector<RealVector> > curve;
    int info = hug.curve(curve);
    
    std::cout << "All done. Info = " << info << std::endl;

//    if (info == HUGONIOTCONTINUATION3D2D_NEAR_COINCIDENCE_CURVE){
//        std::cout << "The initial point was too close to the coincidence curve: nothing was done." << std::endl;
//        return;
//    }

    for (int i = 0; i < curve.size(); i++){
        if (curve[i].size() > 0){
            Curve2D *shock_test = new Curve2D(curve[i], 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
            cref->add(shock_test);

            char buf[1000];
            sprintf(buf, "Hugoniot (cont.) over %s. Init. = Ref. = (%g, %g, %g). Size = %d.", 
                         where_ref.c_str(),
                         referencepoint.point(0), referencepoint.point(1), referencepoint.point(2), 
                         curve[i].size());
            scroll->add(buf, cref, shock_test);
        }
    }

    // Hugoniot (continuation)

//    // Hugoniot (continuation)

//    // Theta_ref
//    std::vector<Point2D> Theta_ref_line;
//    Theta_ref_line.push_back(Point2D(0.0, referencepoint.point(1)));
//    Theta_ref_line.push_back(Point2D(1.0, referencepoint.point(1)));

//    Curve2D *Theta_curve = new Curve2D(Theta_ref_line, 0.0/255.0, 0.0/255.0, 0.0/255.0);
//    cref->add(Theta_curve);
//    scroll->add("Theta ref.", cref, Theta_curve);

//    HugoniotContinuation3D2D hug(fref, aref, bref); fl_font(int face, int size)

//    std::vector< RealVector > curve;
//    int edge;
//    RealVector final_direction;

//    RealVector hint(3);
//    hint(0) =  0.0;
//    hint(1) =  1.0;
//    hint(2) =  0.0;

//    int family = 1;

//    RealVector initial_direction(3);
//    hug.find_initial_direction(referencepoint.point, hint, family, initial_direction);

//    hug.set_reference_point(referencepoint);
//    hug.set_bifurcation_space_coordinate(1);

//    hug.curve_engine(referencepoint.point, initial_direction, final_direction, curve, edge);

//    std::cout << "All done." << std::endl;

//    if (curve.size() > 0){
//        Curve2D *shock_test = new Curve2D(curve, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE | CURVE2D_INDICES);
//        cref->add(shock_test);

//        char buf[1000];
//        sprintf(buf, "Hugoniot (cont.) over %s. Init. = Ref. = (%g, %g, %g). Size = %d.", 
//                     where_ref.c_str(),
//                     referencepoint.point(0), referencepoint.point(1), referencepoint.point(2), 
//                     curve.size());
//        scroll->add(buf, cref, shock_test); fl_font(int face, int size)
//    }

//    // Hugoniot (continuation)

    return;
}

//void clickcb(Fl_Widget *w, void*){
//    Canvas *c = (Canvas*)w;

//    RealVector ref(3);

//    c->getxy(ref.component(0), ref.component(1));

//// SinglePhase incompleto
////    ref.component(0) = 0.0240304;
////    ref.component(1) = 0.45118;
//// TP que troca direcao
////    ref.component(0) = 0.840467;F.components()
////    ref.component(1) = 0.410034;

////    ref.component(0) = 0.589342;
////    ref.component(1) = 0.257853;
//    ref.component(2) = 1.0;

//    // A long one (1069 points)
////    ref(0) = 0.324903;
////    ref(1) = 0.107995;


//    ref(0) = 0.848273;
//    ref(1) = 0.188123;

//    std::cout << "Reference point = " << ref << std::endl; 

//    std::string where_ref;
//    
//    if (c == canvas_vapor){
//        fref = vapor_flux;
//        aref = vapor_accum;

//        where_ref = std::string("Vapor");

//        cref = canvas_vapor;

//        bref = vapor_boundary;
//    }
//    else if (c == canvas_tpcw){
//        fref = tpcw_flux;
//        aref = tpcw_accum;

//        where_ref = std::string("TPCW");

//        cref = canvas_tpcw;

//        bref = tpcw_boundary;
//    }
//    else{
//        fref = liquid_flux;
//        aref = liquid_accum;

//        where_ref = std::string("Liquid");

//        cref = canvas_liquid;

//        bref = liquid_boundary;
//    }

//    // Reference point
//    Viscosity_Matrix v;
//    ReferencePoint referencepoint(ref, fref, aref, &v);

//    // Compute the Hugoniot curves
//    //rarefactioncb
//    Hugoniot_TP htp;

//    // Compute Hugoniot between regions by continuation
//    std::vector<int> vector_family;
//    vector_family.push_back(0);
////    vector_family.push_back(1);

//    std::vector<int> vector_increase;
//    vector_increase.push_back(WAVE_FORWARD);
////    vector_increase.push_back(WAVE_BACKWARD);

////    for (int i = 0; i < vector_family.size(); i++){
////        for (int j = 0; j < vector_increase.size(); j++){
////            // Try to compute the Hugoniot through regions
////            RealVector initial_point_shock = ref;

////            int count = -1;
////            int initial_reference; fl_font(int face, int size)
////            int k;
////            if (c == canvas_vapor)     {k = 0; initial_reference = SHOCK_CURVE_AT_SINGLEPHASE;}
////            else if (c == canvas_tpcw) {k = 1; initial_reference = SHOCK_CURVE_IS_INITIAL;}
////            else                       {k = 2; initial_reference = SHOCK_CURVE_AT_SINGLEPHASE;}

////            while (count < 0){
////                #ifdef SHOCK_TEST_PLOT
////                ShockContinuationMethod3D2D::canvas = canvas[k];
////                ShockContinuationMethod3D2D::scroll = scroll;
////                #endif

////                count++;

////                std::vector<RealVector>         point(0) = .011514;shockcurve_test, shockcurve_alt_test;
////                int info_shockcurve, info_shockcurve_alt;

////                ShockContinuationMethod3D2D scm(3, flux[k], accum[k], boundary[k]);
////                int edge = scm.curveCalc(initial_reference,
////                              referencepoint, true /* bool local_shock*/, initial_point_shock /*const RealVector &in*/, 
////                              vector_increase[j] /*int increase*/, 
////                              vector_family[i] /*int family*/, 
////                              SHOCK_FOR_ITSELF /*int type_of_shock*/, 
////                              0 /*const RealVector *orig_direction*/, 
////                              1 /*int number_ignore_doub_contact*/, 
////                              flux[k], accum[k], boundary[k],
////                              shockcurve_test, info_shockcurve, 
////                              shockcurve_alt_test, info_shockcurve_alt, 
////                              2e-3 /*double newtonTolerance*/);  // Was: 1e-3.

////                Curve2D *shock_test = new Curve2D(shockcurve_test, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS);
////                canvas[k]->add(shock_test);

////                char buf[100];
////                sprintf(buf, "Continuation shock over %s with reference in %s. Fam. = %d. Increase = %d. Size = %d.", name[k].c_str(), where_ref.c_str(), vector_family[i], vector_increase[j], shockcurve_test.size());
////                scroll->add(buf, canvas[k], shock_test);

////                std::cout << "Main: " <<  name[k] << ", edge = " << edge << std::endl;

////                // When reaching the important boundaries, must follow into the other domain:
////                if (k == 0 && edge == SINGLEPHASE_TOTAL_COMPOSITION_SIDE) { //From SinglePhase vapor
////                    std::cout << "    From SinglePhase Vapor" << std::endl;
////                    k = 1; // Entering into TPCW
////                    initial_reference = SHOCK_CURVE_AT_BOUNDARY; fl_font(int face, int size)

////                    initial_point_shock.component(0) = 1.0;
////                    initial_point_shock.component(1) = shockcurve_test[shockcurve_test.size() - 1].component(1);
////                    initial_point_shock.component(2) = shockcurve_test[shockcurve_test.size() - 1].component(2);
////                }
////                else if (k == 2 && edge == SINGLEPHASE_TOTAL_COMPOSITION_SIDE) { //From SinglePhase liquid
////                    std::cout << "    From SinglePhase Liquid" << std::endl;
////                    k = 1; // Entering into TPCW
////                    initial_reference = SHOCK_CURVE_AT_BOUNDARY;

////                    initial_point_shock.component(0) = 0.0;
////                    initial_point_shock.component(1) = shockcurve_test[shockcurve_test.size() - 1].component(1);
////                    initial_point_shock.component(2) = shockcurve_test[shockcurve_test.size() - 1].component(2);
////                }
////                else if (k == 1){
////                    std::cout << "    From TPCW" << std::endl;
////                    double saturation = shockcurve_test[shockcurve_test.size() - 1].component(0);
////                    double Theta      = shockcurve_test[shockcurve_test.size() - 1].component(1);

////                    double xc, yw;
////                    flash->flash(Thermodynamics::Theta2T(Theta), xc, yw);

////                    initial_reference = SHOCK_CURVE_AT_BOUNDARY;

////                    if (saturation <= 0.0){
////                        k = 2; // Go to Liquid

////                        initial_point_shock.component(0) = xc;
////                        initial_point_shock.component(1) = shockcurve_test[shockcurve_test.size() - 1].component(1);
////                        initial_point_shock.component(2) = shockcurve_test[shockcurve_test.size() - 1].component(2);
////                    }
////                    else if (saturation >= 1.0){
////                        k = 0; // Go to Vapor

////                        initial_point_shock.component(0) = yw;
////                        initial_point_shock.component(1) = shockcurve_test[shockcurve_test.size() - 1].component(1);
////                        initial_point_shock.component(2) = shockcurve_test[shockcurve_test.size()  fl_font(int face, int size)- 1].component(2);
////                    }
////                    else break;
////                }
////                else break;
////            }

////            

////        }
////    }

//    // Hugoniot (search)
//    for (int k = 0; k < 3; k++){
//        std::vector<RealVector> hugoniot_curve;
// scroll->clear_all_graphics();
//        htp.curve(flux[k], accum[k], 
//                  *(grid[k]), 
//                  referencepoint, 
//                  hugoniot_curve);

//        std::cout << "Hugoniot " << k << " has " << hugoniot_curve.size()/2 << " points." << std::endl;

//        if (hugoniot_curve.size() > 2){
//            std::vector<Point2D> p;
//            for (int i = 0; i < hugoniot_curve.size()/2; i++){
//                p.push_back(Point2D(hugoniot_curve[2*i].component(0),     hugoniot_curve[2*i].component(1)));
//                p.push_back(Point2D(hugoniot_curve[2*i + 1].component(0), hugoniot_curve[2*i + 1].component(1)));
//            }

//            SegmentedCurve *curve2d = new SegmentedCurve(p, 0, 0, 1);

//            // Classified curve
//            Viscosity_Matrix v;

//            ColorCurve cc(*(flux[k]), *(accum[k]), &v);    
////            ColorCurve cc(*(flux[k]), *(accum[k])); 

//            std::vector<HugoniotPolyLine> classified_curve;
//            std::vector<RealVector> transition_list;

//            cc.classify_segmented_curve(hugoniot_curve, referencepoint,
//                                        classified_curve,
//                                        transition_list);

//            // Color
//            MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve);
//            canvas[k]->add(mcc);

//            char buf[100];
//            sprintf(buf, "Hugoniot (search) over %s with reference on %s. Size = %d.", name[k].c_str(), where_ref.c_str(), hugoniot_curve.size()/2); 
//            scroll->add(buf, canvas[k], mcc);
//        }
//    }


////    // New Hugoniot
////    HugoniotContinuation3D2D new_hug(fref, aref, bref);

////    RealVector hint_direction(3), initial_direction(3);
////    hint_direction(0) = 1.0;
////    hint_direction(0) = 0.0;
////    hint_direction(0) = 0.01;

////    new_hug.set_reference_point(referencepoint);
////    new_hug.find_initial_direction(ref, hint_direction, 0, initial_direction);

////    std::cout << "Main. initial_direction = " << initial_direction << std::endl;

////    RealVector final_direction;
////    std::vector<RealVector> shockcurve;
////    int edge;

////    int info = new_hug.curve_engine(ref, initial_direction, 
////                                    final_direction, shockcurve, edge); // <===

////    std::cout << "shockcurve.size() = " << shockcurve.size() << std::endl;
////    std::cout << "final_direction = " << final_direction << std::endl;
////    std::cout << "edge = " << edge << std::endl;
////    if (shockcurve.size() < 1) return;
////    
////    Curve2D *shock_test = new Curve2D(shockcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS);
////    canvas_tpcw->add(shock_test);

////    char buf[100];
////    sprintf(buf, "New Hugoniot. Init = (%g, %g, %g) Fam. = %d. Size = %d.", ref(0), ref(1), ref(2), 0, shockcurve.size());
////    scroll->add(buf, canvas_tpcw, shock_test);



//    // Cross boundaries
//    int max_number_hugoniots = 10;
//    int     number_hugoniots = 0;

//    bool carry_on = true;

//    // Initialize
//    //
//    RealVector initial_point = referencepoint.point;

//    // Hint direction
//    RealVector hint_direction(3), initial_direction(3), final_direction(3);
//    hint_direction(0) = 1.0;
//    hint_direction(1) = 0.0;
//    hint_direction(2) = 0.01;

//    int current_domain;
//    if (c == canvas_vapor)       current_domain = 0;
//    else if (c == canvas_tpcw)   current_domain = 1;
//    else if (c == canvas_liquid) current_domain = 2;
//    else return;

//    while (number_hugoniots < max_number_hugoniots && carry_on){
//        std::vector<RealVector> shockcurve;
//        int edge;

//        std::cout << "Carry on = " << carry_on << " on domain \"" << name[current_domain] << "\"." << std::endl;

//        HugoniotContinuation3D2D hug(flux[current_domain], accum[current_domain], boundary[current_domain]);
//        hug.set_reference_point(referencepoint);
//        
//        if (number_hugoniots == 0){
//            hug.find_initial_direction(initial_point, hint_direction, 0, initial_direction);
//            hug.set_bifurcation_space_coordinate(1);
//        }
//        else {
//            hug.find_continuation_direction(initial_point, hint_direction, initial_direction);
//            std::cout << "Continuation. initial_point = " << initial_point << ", hint_direction = " << hint_direction << ", initial_direction = " << initial_direction << std::endl;

//            // Add the point on the boundary.
//            shockcurve.push_back(initial_point);
//        }
//        
//        std::cout << "hint = " << hint_direction << ", initial direction = " << initial_direction << std::endl;

//        hug.curve_engine(initial_point, initial_direction, final_direction, shockcurve, edge);
//        std::cout << "    New Hugoniot over " << name[current_domain] << " has " << shockcurve.size() << " points." << std::endl;

//        if (shockcurve.size() > 0){
//            Curve2D *shock_test = new Curve2D(shockcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_INDICES);
//            canvas[current_domain]->add(shock_test);

//            char buf[1000];
//            sprintf(buf, "New Hugoniot over %s. Ref = (%g, %g, %g). Init = (%g, %g, %g) Fam. = %d. Size = %d.", 
//                         name[current_domain].c_str(),
//                         referencepoint.point(0), referencepoint.point(1), referencepoint.point(2), 
//                         initial_point(0), initial_point(1), initial_point(2), 
//                         0, shockcurve.size());
//            scroll->add(buf, canvas[current_domain], shock_test);

//            // Classified curve
//            Viscosity_Matrix v;

//            ColorCurve cc(*(flux[current_domain]), *(accum[current_domain]), &v);    
////            ColorCurve cc(*(flux[k]), *(accum[k])); 

//            std::vector<HugoniotPolyLine> classified_curve;
//            std::vector<RealVector> transition_list;

//            // Create the segments.
//            std::vector<RealVector> shockcurve_by_segments;
//            for (int i = 0; i < shockcurve.size() - 1; i++){
//                shockcurve_by_segments.push_back(shockcurve[i]);
//                shockcurve_by_segments.push_back(shockcurve[i + 1]);
//            }

//            cc.classify_segmented_curve(shockcurve_by_segments, referencepoint,
//                                        classified_curve,
//                                        transition_list);

//            // Color
//            MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve);
//            canvas[current_domain]->add(mcc);

//            sprintf(buf, "New Hugoniot (classified) over %s with reference on %s.", name[current_domain].c_str(), where_ref.c_str()); 
//            scroll->add(buf, canvas[current_domain], mcc);
//            // Classified curve

//            // Continue into the next domain.
//            //
//            // TPCW
//            if (current_domain == 1){
//                std::cout << "TPCW, edge = " << edge << std::endl;

//                if (edge == 1 || edge == 0){
//                    std::cout << "    Leaving TPCW..." << std::endl;
//                    double saturation = shockcurve[shockcurve.size() - 1](0);
//                    double Theta      = shockcurve[shockcurve.size() - 1](1);

//                    double xc, yw;
//                    flash->flash(Thermodynamics::Theta2T(Theta), xc, yw);
//                    // Call complete on shockcurve[shockcurve.size() - 1] to compute xc and yw.

//                    std::cout << "saturation = " << saturation << std::endl;

//                    if (edge == 1){ // Vapor Saturation == 1.0
//                        current_domain = 0; // Go to Vapor
//                        initial_point(0) = yw;
//                        initial_point(1) = shockcurve[shockcurve.size() - 1](1);
//                        initial_point(2) = shockcurve[shockcurve.size() - 1](2);

//                        hint_direction(0) = -1.0;
//                        hint_direction(1) = 0.0;
//                        hint_direction(2) = 0.01;
//                    }
//                    else if (edge == 0){ // Vapor Saturation == 0.0
//                        current_domain = 2; // Go to Liquid

//                        initial_point(0) = xc;
//                        initial_point(1) = shockcurve[shockcurve.size() - 1](1);
//                        initial_point(2) = shockcurve[shockcurve.size() - 1](2);

//                        hint_direction(0) = -1.0;
//                        hint_direction(1) = 0.0;
//                        hint_direction(2) = 0.01;
//                    }
//                    else break;
//                }
//                else break;
//            } // if (current_domain == 1)
//            else if (current_domain == 0 && edge == SINGLEPHASE_TOTAL_COMPOSITION_SIDE) { //From SinglePhase vapor
//                std::cout << "    From SinglePhase Vapor" << std::endl;
//                current_domain = 1; // Entering into TPCW

//                initial_point(0) = 1.0;
//                initial_point(1) = shockcurve[shockcurve.size() - 1](1);
//                initial_point(2) = shockcurve[shockcurve.size() - 1](2);

//                hint_direction(0) = -1.0;
//                hint_direction(1) = 0.0;
//                hint_direction(2) = 0.01;
//            }
//            else if (current_domain == 2 && edge == SINGLEPHASE_TOTAL_COMPOSITION_SIDE) { //From SinglePhase liquid
//                std::cout << "    From SinglePhase Liquid" << std::endl;
//                current_domain = 1; // Entering into TPCW

//                initial_point(0) = 0.0;
//                initial_point(1) = shockcurve[shockcurve.size() - 1](1);
//                initial_point(2) = shockcurve[shockcurve.size() - 1](2);

//                hint_direction(0) = 1.0;
//                hint_direction(1) = 0.0;
//                hint_direction(2) = 0.01;
//            }
//            else break;



//            // Update.
//            //
//            number_hugoniots++;


//        } // if (shockcurve.size() > 0)
//        else carry_on = false;
//    } // while (number_hugoniots < max_number_hugoniots && carry_on)

//    return;
//}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();
    return;
}

int main(){
    fl_font(FL_HELVETICA, 12);

    // Output
    output = new Fl_Double_Window(10, 10, 400, 400, "Info");
    {
        ob = new Fl_Box(0, 0, output->w(), output->h(), "Info");
        ob->labelfont(FL_COURIER);
    }
    output->end();
//    output->show();

    // Thermo
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");
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
    fpp.component(5) = 0.0;ABORTED_PROCEDURE;
    fpp.component(6) = 2.0;
    fpp.component(7) = 2.0;
    fpp.component(8) = 0.38;
    fpp.component(9) = 304.63;
    fpp.component(10) = 998.2;
    fpp.component(11) = 4.22e-3;

    Flux2Comp2PhasesAdimensionalized_Params fp(fpp, tc);
    tpcw_flux = new Flux2Comp2PhasesAdimensionalized(fp);

    double phi = 0.38;

    Accum2Comp2PhasesAdimensionalized_Params ap(tc, phi);
    tpcw_accum = new Accum2Comp2PhasesAdimensionalized(ap);

    // Vapor
    FluxSinglePhaseVaporAdimensionalized_Params vapor_fluxpar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0);

    vapor_flux = new FluxSinglePhaseVaporAdimensionalized(vapor_fluxpar, tc);

    AccumulationSinglePhaseVaporAdimensionalized_Params vapor_accumpar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    phi);

    vapor_accum = new AccumulationSinglePhaseVaporAdimensionalized(vapor_accumpar, tc);

    // Liquid
    FluxSinglePhaseLiquidAdimensionalized_Params liquid_fluxpar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0);

    liquid_flux = new FluxSinglePhaseLiquidAdimensionalized(liquid_fluxpar, tc);

    AccumulationSinglePhaseLiquidAdimensionalized_Params liquid_accumpar(mc, mw, 0.0, 
                                                    "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
                                                    0.0, 0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    phi);

    liquid_accum = new AccumulationSinglePhaseLiquidAdimensionalized(liquid_accumpar, tc);

    {
        RealVector point(3);
        point(0) = .475;
        point(1) = .4785;
        point(2) = 1.0;

        DoubleMatrix JF(3, 3), JG(3, 3);
        tpcw_flux->fill_with_jet(3, point.components(), 1, 0, JF.data(), 0);
        tpcw_accum->fill_with_jet(3, point.components(), 1, 0, JG.data(), 0);

        std::vector<eigenpair> e;
        Eigen::eig(3, JF.data(), JG.data(), e);

        std::cout << "Lambda = " << e[1].r << std::endl;
    }


    // ************************* Physics ************************* //


    // ************************* Boundaries ************************* //

    // TPCW
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

    tpcw_boundary = new RectBoundary(pmin, pmax);

    // Vapor
    vapor_boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_VAPOR, &Thermodynamics::Theta2T);

    // Liquid
    liquid_boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_LIQUID, &Thermodynamics::Theta2T);
    // ************************* Boundaries ************************* //

    // ************************* GridValues ************************* //
    tpcw_grid = new GridValues(tpcw_boundary, pmin, pmax, number_of_cells);

    RealVector vapor_max(pmax);
    vapor_max.component(0) = vapor_boundary->maximums().component(0); // Fix later
    vapor_grid = new GridValues(vapor_boundary, pmin, vapor_max, number_of_cells);

    RealVector liquid_max(pmax);
    liquid_max.component(0) = liquid_boundary->maximums().component(0); // Fix later
    liquid_grid = new GridValues(liquid_boundary, pmin, liquid_max, number_of_cells);
    // ************************* GridValues ************************* //

    // Window
    int main_w  = .7*Fl::w();
    int main_h  = .7*Fl::h();

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Hugoniot between physics");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        // vapor to the left
        canvas_vapor = new Canvas(10, 10, (main_w - 20)/3, main_h - 20);
        canvas_vapor->xlabel("yw (Vapor)");
        canvas_vapor->ylabel("Theta");
//        canvas_vapor->setextfunc(&clickcb, canvas_vapor, 0);
        canvas_vapor->setextfunc(&all_of_Hugoniot, canvas_vapor, 0);


        // TPCW at the middle
        canvas_tpcw = new Canvas(canvas_vapor->x() + canvas_vapor->w(), canvas_vapor->y(), canvas_vapor->w(), canvas_vapor->h());
        canvas_tpcw->xlabel("saturation of vapor (TPCW)");
        canvas_tpcw->ylabel("Theta");
//        canvas_tpcw->setextfunc(&clickcb, canvas_tpcw, 0);
        canvas_tpcw->setextfunc(&all_of_Hugoniot, canvas_tpcw, 0);

        canvas_tpcw->setextfunc(&rarefactioncb, canvas_tpcw, 0);

        canvas_tpcw->set_transform_matrix(mirror);

        // liquid to the right, mirrored.
        canvas_liquid = new Canvas(canvas_tpcw->x() + canvas_tpcw->w(), canvas_vapor->y(), canvas_vapor->w(), canvas_vapor->h());
        canvas_liquid->xlabel("xc (Liquid)");
        canvas_liquid->ylabel("Theta");
//        canvas_liquid->setextfunc(&clickcb, canvas_liquid, 0);
        canvas_liquid->setextfunc(&all_of_Hugoniot, canvas_liquid, 0);

        canvas_liquid->set_transform_matrix(mirror);
        //canvas_liquid->axis(-liquid_boundary->maximums().component(0), 0.0, Theta_min, Theta_max);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

    // ************************* Draw Boundaries ************************* //
    std::vector<RealVector> edges;
    std::vector<Point2D> edges2d;
    Curve2D *b;

    // TPCW
    edges2d.push_back(Point2D(pmin.component(0), pmin.component(1)));
    edges2d.push_back(Point2D(pmax.component(0), pmin.component(1)));
    edges2d.push_back(Point2D(pmax.component(0), pmax.component(1)));
    edges2d.push_back(Point2D(pmin.component(0), pmax.component(1)));
    edges2d.push_back(edges2d[0]);
    b = new Curve2D(edges2d, 0, 0, 0, CURVE2D_SOLID_LINE);

    canvas_tpcw->add(b);
    canvas_tpcw->nozoom();

    // Vapor
    vapor_boundary->physical_boundary(edges);
    edges.push_back(edges[0]);

    b = new Curve2D(edges, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas_vapor->add(b);
    canvas_vapor->nozoom();

    // Liquid
    liquid_boundary->physical_boundary(edges);
    edges.push_back(edges[0]);

    b = new Curve2D(edges, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas_liquid->add(b);
    canvas_liquid->nozoom();
    // ************************* Draw Boundaries ************************* //

    // ************************* Coincidence over TPCW ************************* //
    std::vector<RealVector> coincidence_curve;

    #ifdef CREATE_COINCIDENCE_CURVE
        CoincidenceTP ctp(tpcw_flux);
        ctp.curve(tpcw_flux, tpcw_accum, *tpcw_grid, coincidence_curve);

        FILE *fid = fopen("Coincidence.txt", "w");
        fprintf(fid, "%d\n", coincidence_curve.size());

        for (int i = 0; i < coincidence_curve.size(); i++) fprintf(fid, "%g %g\n", coincidence_curve[i](0), coincidence_curve[i](1));

        fclose(fid);
    #else
        int c_size;
        FILE *fid = fopen("Coincidence.txt", "r");
        fscanf(fid, "%d", &c_size);
        coincidence_curve.resize(c_size);

        for (int i = 0; i < c_size/2; i++){
            for (int j = 0; j < 2; j++){
                coincidence_curve[2*i + j].resize(2);

                for (int k = 0; k < 2; k++) fscanf(fid, "%lf", &(coincidence_curve[2*i + j](k)));
            }
        }

        fclose(fid);
    #endif

    SegmentedCurve *ctp_curve = new SegmentedCurve(coincidence_curve, .0, .0, 1.0);
    canvas_tpcw->add(ctp_curve);

    // ************************* Coincidence over TPCW ************************* //

    // List of curves
    scrollwin = new Fl_Double_Window(canvaswin->x() + canvaswin->w() + 10, canvaswin->y(), 500, 500, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 2*10 - 25, "Extensions");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    //scrollwin->callback(no_close_cb);
    scrollwin->set_non_modal();

    canvaswin->show();

    scrollwin->show();

    // This somewhat models what Physics is suppossed to do
    flux[0] = vapor_flux;
    flux[1] = tpcw_flux;
    flux[2] = liquid_flux;

    accum[0] = vapor_accum;
    accum[1] = tpcw_accum;
    accum[2] = liquid_accum;

    canvas[0] = canvas_vapor;
    canvas[1] = canvas_tpcw;
    canvas[2] = canvas_liquid;

    grid[0] = vapor_grid;
    grid[1] = tpcw_grid;
    grid[2] = liquid_grid;
    
    boundary[0] = vapor_boundary;
    boundary[1] = tpcw_boundary;
    boundary[2] = liquid_boundary;
    
    name[0] = std::string("Vapor");
    name[1] = std::string("TPCW");
    name[2] = std::string("Liquid"); 

    Fl::scheme("gtk+");

    for (int i = 0; i < 3; i++){
        canvas[i]->on_move(&on_move, canvas[i], 0);
//        canvas[i]->on_leave(&on_leave, canvas[i], 0);
//        canvas[i]->on_enter(&on_enter, canvas[i], 0);
    }

    return Fl::run();
}

