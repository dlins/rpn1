//#define CREATE_COINCIDENCE_CURVE
#define GRID_SIZE 128

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
#include "RarefactionCurve.h"
#include "Hugoniot_Curve.h"
#include "CompositeCurve.h"

#include "LSODE.h"

#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"
#include "RectBoundary.h"
#include "SimpleFlux.h"
#include "SplineFlux.h"

#include "ReferencePoint.h"
#include "ColorCurve.h"

// TEST
#include "HugoniotContinuation2D2D.h"
#include "HugoniotContinuation_nDnD.h"
#include "ShockCurve.h"
#include <string>
#include <sstream>
// TEST

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

// Flux objects
StonePermParams   *stonepermparams = (StonePermParams*)0;
StoneParams       *stoneparams     = (StoneParams*)0;
StoneFluxFunction *stoneflux       = (StoneFluxFunction*)0;
StoneAccumulation *stoneaccum      = (StoneAccumulation*)0;

SimpleFlux *simpleflux;

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;

GridValues *grid;

void load_dc(const std::string &side, int grid_size, int fam){
    RealVector p0(2), p1(2);
    std::vector<RealVector> vrs;

    char name[1000];
    sprintf(name, "%s_domain_double_contact_%d_fam_%d.txt", side.c_str(), grid_size, fam);

    FILE *fid = fopen(name, "r");
    if (fid != 0){
        while (fscanf(fid, "%lf %lf %lf %lf", &p0.component(0), &p0.component(1), &p1.component(0), &p1.component(1)) != EOF) {
            vrs.push_back(p0);
            vrs.push_back(p1);
        }
        fclose(fid);
    }
    else return;

    // Side domain:
    std::cout << side << ". vrs.size() = " << vrs.size() << std::endl;

    std::vector<Point2D> dp(vrs.size());
    for (int i = 0; i < vrs.size(); i++){
        dp[i] = Point2D(vrs[i].component(0), vrs[i].component(1));
    }

    double r = 0.0, g = 0.0, b = 0.0;
    if (side == "left") r = 1.0;
    else                b = 1.0;

    SegmentedCurve *segcurve = new SegmentedCurve(dp, r, g, b);

    canvas->add(segcurve);
    canvas->axis(0.0, 1.0, 0.0, 1.0);

    char char_info[200];
    sprintf(char_info, "DC, %s-domain. Family = %d. Curve size = %d. ", side.c_str(), fam, vrs.size()/2);
    scroll->add(char_info, canvas, segcurve);

    return;
}

void no_close_cb(Fl_Widget*, void*){
    return;
}

void close_cb(Fl_Widget*, void*){
    ContourMethod::deallocate_arrays();

    exit(0);

    return;
}

void all_of_Hugoniot(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector ref(2);

    c->getxy(ref.component(0), ref.component(1));

//    ref(0) = 0.935375;
//    ref(1) = 0.034264;

//    ref(0) = 0.127757;
//    ref(1) = 0.78934;

//    ref(0) = 0.700784;
//    ref(1) = 0.112944;


    //ref(0) = .696474;
    //ref(1) = .123762;


    ReferencePoint referencepoint(ref, flux, accum, 0);

    std::cout << "Reference point: " << referencepoint.point << std::endl;

    // Hugoniot (search)
    Hugoniot_Curve hs;

    std::vector<HugoniotPolyLine> hugoniot_curve;

    hs.classified_curve(flux, accum, 
                        *(grid), 
                        referencepoint, 
                        hugoniot_curve);

    if (hugoniot_curve.size() > 2){
        MultiColoredCurve *mc = new MultiColoredCurve(hugoniot_curve);
        canvas->add(mc);

        std::stringstream s;
        s << "Hugoniot (search). Ref. = " << referencepoint.point;

        scroll->add(s.str().c_str(), canvas, mc);
    }
////            std::vector<Point2D> p;
////            for (int i = 0; i < hugoniot_curve.size()/2; i++){
////                p.push_back(Point2D(hugoniot_curve[2*i].component(0),     hugoniot_curve[2*i].component(1)));
////                p.push_back(Point2D(hugoniot_curve[2*i + 1].component(0), hugoniot_curve[2*i + 1].component(1)));
////            }

////            SegmentedCurve *curve2d = new SegmentedCurve(p, 0, 0, 1);

////            // Classified curve
////            Viscosity_Matrix v;
////            ColorCurve cc(*(flux[k]), *(accum[k]), &v);    
//////            ColorCurve cc(*(flux[k]), *(accum[k])); 

////            std::vector<HugoniotPolyLine> classified_curve;
////            std::vector<RealVector> transition_list;

////            cc.classify_segmented_curve(hugoniot_curve, referencepoint,
////                                        classified_curve,
////                                        transition_list);

////            // Color
////            MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve);
////            canvas[k]->add(mcc);

////            char buf[100];
////            sprintf(buf, "Hugoniot (search) over %s with reference on %s. Size = %d.", name[k].c_str(), where_ref.c_str(), hugoniot_curve.size()/2); 
////            scroll->add(buf, canvas[k], mcc);
////        }
////    }
////    // Hugoniot (search)

    // Hugoniot (continuation)

////    // Theta_ref
////    std::vector<Point2D> Theta_ref_line;
////    Theta_ref_line.push_back(Point2D(0.0, referencepoint.point(1)));
////    Theta_ref_line.push_back(Point2D(1.0, referencepoint.point(1)));

////    Curve2D *Theta_curve = new Curve2D(Theta_ref_line, 0.0/255.0, 0.0/255.0, 0.0/255.0);
////    cref->add(Theta_curve);
////    scroll->add("Theta ref.", cref, Theta_curve);

//    HugoniotContinuation2D2D hug(flux, accum, boundary);
    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    hug.set_reference_point(referencepoint);
////    hug.set_bifurcation_space_coordinate(1);

    std::vector< std::vector<RealVector> > curve;
    std::vector< std::vector<RealVector> > transition;
//    int info = hug.curve(curve);

    ShockCurve sc(&hug);
//    int info = sc.curve(referencepoint, SHOCKCURVE_SHOCK_CURVE, SHOCKCURVE_EQUALITY_OF_LAMBDA_OF_FAMILY_AT_REF_AND_SIGMA_ON_THE_RIGHT, curve, transition);

    std::vector<ShockCurvePoints> shockcurve_with_transitions;

    int info = sc.curve(referencepoint, SHOCKCURVE_SHOCK_CURVE, SHOCKCURVE_EQUALITY_OF_LAMBDA_OF_FAMILY_AT_REF_AND_SIGMA_ON_THE_RIGHT, 
                        USE_ALL_FAMILIES /* int what_family_to_use */, STOP_AFTER_TRANSITION /*int after_transition*/,
                        shockcurve_with_transitions);

    std::cout << "All done. Info = " << info << ", size = " << shockcurve_with_transitions.size() << std::endl;

    for (int i = 0; i < shockcurve_with_transitions.size(); i++){
        Curve2D *shock_test = new Curve2D(shockcurve_with_transitions[i].curve, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
        canvas->add(shock_test);

        std::stringstream s;  
//        s << "Shock[" << i << "]. Ref. = " << shockcurve_with_transitions[i].ref << ", fam. = " << shockcurve_with_transitions[i].family;
        s << "Shock[" << i << "], fam. = " << shockcurve_with_transitions[i].family;
        scroll->add(s.str().c_str(), canvas, shock_test);

        // Print the transitions (current)
        if (shockcurve_with_transitions[i].transition_current_index.size() > 0){

            std::vector<RealVector> temp;

            for (int j = 0; j < shockcurve_with_transitions[i].transition_current_index.size(); j++){
                temp.push_back(shockcurve_with_transitions[i].curve[shockcurve_with_transitions[i].transition_current_index[j]]);
            }

            shock_test = new Curve2D(temp, 0.0/255.0, 255.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            canvas->add(shock_test);

            std::stringstream ss;  
            ss << "Transition, current. Size = " << shockcurve_with_transitions[i].transition_current_index.size();
            scroll->add(ss.str().c_str(), canvas, shock_test);
        }

        // Print the transitions (reference)
        if (shockcurve_with_transitions[i].transition_reference_index.size() > 0){
            std::vector<RealVector> temp;

            for (int j = 0; j < shockcurve_with_transitions[i].transition_reference_index.size(); j++){
                temp.push_back(shockcurve_with_transitions[i].curve[shockcurve_with_transitions[i].transition_reference_index[j]]);
            }

            shock_test = new Curve2D(temp, 0.0/255.0, 0.0/255.0, 255.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            canvas->add(shock_test);

            std::stringstream ss;  
            ss << "Transition, reference. Size = " << shockcurve_with_transitions[i].transition_reference_index.size();
            scroll->add(ss.str().c_str(), canvas, shock_test);
        }
    }

//    if (info == HUGONIOTCONTINUATION3D2D_NEAR_COINCIDENCE_CURVE){
//        std::cout << "The initial point was too close to the coincidence curve: nothing was done." << std::endl;
//        return;
//    }

//    for (int i = 0; i < curve.size(); i++){
//        if (curve[i].size() > 0){
//            Curve2D *shock_test = new Curve2D(curve[i], 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
//            canvas->add(shock_test);

//            std::stringstream s;
//            s << "Hugoniot (cont.). Ref. = " << referencepoint.point << ". Size = " << curve[i].size();

//            scroll->add(s.str().c_str(), canvas, shock_test);
//        }
//    }

//    for (int i = 0; i < transition.size(); i++){
//        if (transition[i].size() > 0){
//            Curve2D *shock_test = new Curve2D(transition[i], 0.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
//            canvas->add(shock_test);

//            std::stringstream s;
//            s << "Transitions (cont.). Ref. = " << referencepoint.point << ". Size = " << transition[i].size();

//            scroll->add(s.str().c_str(), canvas, shock_test);
//        }
//    }

////    // Hugoniot (continuation)

//    // Hugoniot (continuation)

//    // Theta_ref
//    std::vector<Point2D> Theta_ref_line;
//    Theta_ref_line.push_back(Point2D(0.0, referencepoint.point(1)));
//    Theta_ref_line.push_back(Point2D(1.0, referencepoint.point(1)));

//    Curve2D *Theta_curve = new Curve2D(Theta_ref_line, 0.0/255.0, 0.0/255.0, 0.0/255.0);
//    cref->add(Theta_curve);
//    scroll->add("Theta ref.", cref, Theta_curve);

//    HugoniotContinuation3D2D hug(fref, aref, bref);

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
//        scroll->add(buf, cref, shock_test);
//    }

//    // Hugoniot (continuation)

    return;
}

void only_shock(Fl_Widget*, void*){

    int family = (fam0->value() == 1) ? 0 : 1;
    double deltaxi = 1e-4;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    RealVector rar(2);
    rar(0) = -1.33676;
    rar(1) =   .92203;

    ReferencePoint referencepoint(rar, flux, accum, 0);

    RealVector initial_shock(2);
    initial_shock(0) = 2.02815;
    initial_shock(1) = 0.92203;

    RealVector direction(2);
    direction(0) = 1.0;
    direction(1) = 0.0;

            std::vector<RealVector> shockcurve; 
            std::vector<int> stop_current_index;
            std::vector<int> stop_current_family;
            std::vector<int> stop_reference_index;
            std::vector<int> stop_reference_family;  
            int edge;
        
            HugoniotContinuation_nDnD hug(flux, accum, boundary);

            ShockCurve sc(&hug);

            int shck_info = sc.curve_engine(referencepoint, initial_shock, direction, family, 
                                            SHOCKCURVE_SHOCK_CURVE, SHOCKCURVE_EQUALITY_OF_LAMBDA_OF_FAMILY_AT_REF_AND_SIGMA_ON_THE_RIGHT,
                                            USE_CURRENT_FAMILY /*int what_family_to_use*/,
                                            STOP_AFTER_TRANSITION /*int after_transition*/,
                                            shockcurve, 
                                            stop_current_index,
                                            stop_current_family,
                                            stop_reference_index,
                                            stop_reference_family,  
                                            edge);
                                            
            std::cout << "shck_info  = " << shck_info << std::endl;
            std::cout << "shock size = " << shockcurve.size() << std::endl;
            
            if (shockcurve.size() > 0){
                Curve2D *shockcurve2d = new Curve2D(shockcurve, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS /*| CURVE2D_INDICES*/);
                canvas->add(shockcurve2d);

                std::stringstream s;
                s << "Shock curve. Size = " << shockcurve.size();

                scroll->add(s.str().c_str(), canvas, shockcurve2d);
            }

    return;
}

void rar_and_comp(Fl_Widget*, void*){
    int max_rar = -1;

    RealVector initial(2);

    canvas->getxy(initial.component(0), initial.component(1));
//    initial(0) = .288597;
//    initial(1) = .268564;


    // Composite goes up and returns, fam 1
//    initial(0) = .328541;
//    initial(1) = .340347;

    // Fam. 1. The composite returns
//    initial(0) = .284129;
//    initial(1) = .403465;

//    initial(0) = .343157;
//    initial(1) = .365099;

    // Family = 1
//    initial(0) = .203863;
//    initial(1) = .594746;

    // Family = 0;
//    initial(0) = .245364;
//    initial(1) = .126238;

    //initial(0) = .533807;
    //initial(1) = .153465;
    
    //initial(0) = .314006;
    //initial(1) = .019802;

    //initial(0) = .696474;
    //initial(1) = .123762;

//    initial(0) = -1.33676;
//    initial(1) =   .92203;

    initial(0) = -1.24036;
    initial(1) = -0.235767;

    //all_of_Hugoniot(canvas, 0);

    LSODE lsode;
    lsode.initialize();

    std::vector<RealVector> rarcurve, inflection_points;

    int family = (fam0->value() == 1) ? 0 : 1;
    double deltaxi = 1e-4;

    int increase;
    if (increase_button->value() == 1) increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    else                               increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    std::vector<RealVector> stacked_rarefaction;
    std::vector<double>     stacked_lambda;

    int retreat = 2;

    begin_rarefaction:

    max_rar++;
    if (max_rar == 2){
        return;
    }

    int info_rar = Rarefaction::curve(initial, 
                       RAREFACTION_INITIALIZE,
                       0/*  const RealVector *initial_direction*/,
                       family, 
                       increase /*  int increase*/,
                       RAREFACTION_FOR_ITSELF /* int type_of_rarefaction*/,
                       &lsode,
                       deltaxi,
                       flux, accum, 
                       RAREFACTION_GENERAL_ACCUMULATION /* int type_of_accumulation*/,
                       boundary,
                       rarcurve,
                       inflection_points);
    std::cout << "******************** RAREFACTION FINISHED!!! ********************" << std::endl;
    std::cout << "    info_rar = " << info_rar << std::endl;


    if (rarcurve.size() > 0){
        std::vector<RealVector> rarplot;
        for (int i = 0; i < rarcurve.size(); i++){
            RealVector temp = rarcurve[i];

            JetMatrix tempj(2);
            flux->jet(rarcurve[i], tempj, 0);

            temp(1) = .4*tempj.get(0);

            rarplot.push_back(temp);
        }

        Curve2D *test = new Curve2D(rarplot, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
        canvas->add(test);

        std::stringstream buf;
        buf << "Rarefaction. Init. = " << initial << ", size = " << rarcurve.size();
        scroll->add(buf.str().c_str(), canvas, test);

        //return;

        // Test composite now.

        //if (info_rar == SUCCESSFUL_PROCEDURE) return;

        if (info_rar != RAREFACTION_REACHED_INFLECTION){
            std::cout << "The rarefaction did not reach the inflection. Aborting..." << std::endl;
            return;
        }
        else std::cout << "The rarefaction reached the inflection." << std::endl;

        HugoniotContinuation_nDnD hug(flux, accum, boundary);
        // hug.set_reference_point(referencepoint);

        ShockCurve sc(&hug);

        CompositeCurve cmp(flux, accum, boundary, &sc);

        std::cout << "Composite instantiated." << std::endl;

        std::vector<RealVector> rarcurve_clean;
        std::vector<double> lambda;

        int n = 2;

        for (int i = 0; i < rarcurve.size(); i++){
            rarcurve_clean.push_back(RealVector(n, &(rarcurve[i].components()[0])));

            lambda.push_back(rarcurve[i](n));
        }

        std::vector<RealVector> newrarcurve;
        std::vector<RealVector> compositecurve;

        std::vector<double> determinants;

        RealVector final_direction;
        int reason_why;

        std::cout << "Main. Composite will begin. rar.size() = " << rarcurve_clean.size() << std::endl;

        int edge;

        lsode.initialize();
        int info_cmp = cmp.curve2(flux /* *RarFlux */, accum /* *RarAccum */, 
                       boundary /* *Rarboundary */, rarcurve_clean, lambda,
                       rarcurve_clean[rarcurve_clean.size() - 1],
                       &lsode, deltaxi,
                       COMPOSITE_BEGINS_AT_INFLECTION /*int where_composite_begins*/, family, 
                       newrarcurve,
                       compositecurve, 
                       final_direction,
                       determinants,
                       reason_why,
                       edge);

        std::cout << "After composite. Size = " << compositecurve.size() << std::endl;
        if (compositecurve.size() > 0){
            std::vector<std::string> dets;
            for (int i = 0; i < determinants.size(); i++){
//                 // Find lambda
//                 JetMatrix Fm_jet(2), Gm_jet(2);
//                 flux->jet(newrarcurve[i], Fm_jet, 1);
//                 accum->jet(newrarcurve[i], Gm_jet, 1);

//                 std::vector<eigenpair> e;
//                 Eigen::eig(2, Fm_jet.Jacobian().data(), Gm_jet.Jacobian().data(), e);

//                 double lambda = e[family].r;

//                 // The rest
//                 JetMatrix Fp_jet(2), Gp_jet(2);
//                 flux->jet(newrarcurve[i], Fp_jet, 1);
//                 accum->jet(newrarcurve[i], Gp_jet, 1);

//                 DoubleMatrix characteristic_matrix = Fp_jet.Jacobian() - lambda*Gp_jet.Jacobian();

                 // Print
                 std::stringstream s;
                 
                 if (determinants[i] > 0) s << "+";
                 else                     s << "-";
                 
                 s << i;

                 //std::cout << "Det: " << s.str() << std::endl;

                 dets.push_back(s.str());
            }

            std::vector<RealVector> cmpplot;
            for (int i = 0; i < compositecurve.size(); i++){
                RealVector temp = compositecurve[i];

                JetMatrix tempj(2);
                flux->jet(temp, tempj, 0);

                temp(1) = .4*tempj.get(0);

                cmpplot.push_back(temp);
            }

            Curve2D *cmpcrv = new Curve2D(cmpplot, 0.0, 1.0, 0.0, dets, CURVE2D_SOLID_LINE | CURVE2D_MARKERS /*| CURVE2D_INDICES*/);
            canvas->add(cmpcrv);

            std::stringstream s;
            s << "Composite. Size = " << compositecurve.size();

            scroll->add(s.str().c_str(), canvas, cmpcrv);

            std::vector<RealVector> newrarplot;
            for (int i = 0; i < newrarcurve.size(); i++){
                RealVector temp = newrarcurve[i];

                JetMatrix tempj(2);
                flux->jet(temp, tempj, 0);

                temp(1) = .4*tempj.get(0);

                newrarplot.push_back(temp);
            }

            Curve2D *rarcrv = new Curve2D(newrarplot, 0.0, 1.0, 1.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS /*| CURVE2D_INDICES*/);
            canvas->add(rarcrv);

            s.str(std::string());
            s << "New rar. Size = " << compositecurve.size();

            scroll->add(s.str().c_str(), canvas, rarcrv);
        }
        
        // Continue as a shock, the rarefaction's initial point is the reference point.
        // 
        if (info_cmp == COMPOSITE_OK && reason_why == COMPOSITE_COMPLETED){
            std::cout << "IN THE STACK THERE ARE " << stacked_rarefaction.size() << " POINTS." << std::endl;

            if (stacked_rarefaction.size() == 0){
                std::cout << "Shock will start now!" << std::endl;
        
                //ReferencePoint refp(initial, flux, accum, 0);
                ReferencePoint refp(newrarcurve[newrarcurve.size() - 1], flux, accum, 0);
        
                std::vector<RealVector> shockcurve; 
                std::vector<int> stop_current_index;
                std::vector<int> stop_current_family;
                std::vector<int> stop_reference_index;    
                std::vector<int> stop_reference_family;  
                int edge;
        
                RealVector initial_point_shock = compositecurve[compositecurve.size() - 1];

                // We are only testing this. Morante.
                std::cout << "Testing the shock. Final direction (before) = " << final_direction << std::endl;

                //normalize(final_direction);

                std::cout << "For the shock. Initial point = " << initial_point_shock << ", direction = " << final_direction << std::endl;
        
                int shck_info = sc.curve_engine(refp, initial_point_shock, final_direction, family, 
                                                SHOCKCURVE_SHOCK_CURVE, SHOCKCURVE_EQUALITY_OF_LAMBDA_OF_FAMILY_AT_REF_AND_SIGMA_ON_THE_RIGHT,
                                                USE_CURRENT_FAMILY /*int what_family_to_use*/,
                                                STOP_AFTER_TRANSITION /*int after_transition*/,
                                                shockcurve, 
                                                stop_current_index,
                                                stop_current_family,
                                                stop_reference_index,
                                                stop_reference_family,  
                                                edge);
                                            
                std::cout << "shck_info  = " << shck_info << std::endl;
                std::cout << "shock size = " << shockcurve.size() << std::endl;
            
                if (shockcurve.size() > 0){
                    std::vector<RealVector> shockplot;
                    for (int i = 0; i < shockcurve.size(); i++){
                        RealVector temp = shockcurve[i];

                        JetMatrix tempj(2);
                        flux->jet(temp, tempj, 0);

                        temp(1) = .4*tempj.get(0);
    
                        shockplot.push_back(temp);
                    }

                    Curve2D *shockcurve2d = new Curve2D(shockplot, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS /*| CURVE2D_INDICES*/);
                    canvas->add(shockcurve2d);

                    std::stringstream s;
                    s << "Shock curve. Size = " << shockcurve.size();

                    scroll->add(s.str().c_str(), canvas, shockcurve2d);

                    initial(0) = shockcurve[shockcurve.size() - 1](0);
                    initial(1) = shockcurve[shockcurve.size() - 1](1);

                    goto begin_rarefaction;
                }
            }
            else { // The stack of rarefactions is not empty.
                RealVector composite_initial_point = compositecurve[compositecurve.size() - 1];

                compositecurve.clear();
                determinants.clear();
                newrarcurve.clear();

                int info_cmp = cmp.curve2(flux /* *RarFlux */, accum /* *RarAccum */, 
                                              boundary /* *Rarboundary */, stacked_rarefaction, stacked_lambda,
                                              composite_initial_point,
                                              &lsode, deltaxi,
                                              COMPOSITE_AFTER_COMPOSITE /*int where_composite_begins*/, family, 
                                              newrarcurve,
                                              compositecurve, 
                                              final_direction,
                                              determinants,
                                              reason_why,
                                              edge);

                std::vector<RealVector> cmpplot;
                for (int i = 0; i < compositecurve.size(); i++){
                    RealVector temp = compositecurve[i];

                    JetMatrix tempj(2);
                    flux->jet(temp, tempj, 0);
    
                    temp(1) = .4*tempj.get(0);

                    cmpplot.push_back(temp);
                }
    
                Curve2D *cmpcrv = new Curve2D(cmpplot, 0.0, 1.0, 0.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS /*| CURVE2D_INDICES*/);
                canvas->add(cmpcrv);

                std::stringstream s;
                s << "Composite. Size = " << compositecurve.size();
    
                scroll->add(s.str().c_str(), canvas, cmpcrv);

                std::vector<RealVector> newrarplot;
                for (int i = 0; i < newrarcurve.size(); i++){
                    RealVector temp = newrarcurve[i];

                    JetMatrix tempj(2);
                    flux->jet(temp, tempj, 0);

                    temp(1) = .4*tempj.get(0);

                    newrarplot.push_back(temp);
                }

                Curve2D *rarcrv = new Curve2D(newrarplot, 0.0, 1.0, 1.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS /*| CURVE2D_INDICES*/);
                canvas->add(rarcrv);

                s.str(std::string());
                s << "New rar. Size = " << compositecurve.size();

                scroll->add(s.str().c_str(), canvas, rarcrv);

                // TODO: Finish this.
                return;
            }
        }

        if (info_cmp == COMPOSITE_OK && reason_why == COMPOSITE_REACHED_DOUBLE_CONTACT){
            std::cout << "\n\n\n\n\nSTACK WILL BE CREATED. " << rarcurve_clean.size() - retreat - compositecurve.size() << " POINTS ADDED." << std::endl;

            initial = compositecurve[compositecurve.size() - 1];

            // We assume the rarefaction was not competely calculated here.
            stacked_rarefaction.clear();
            stacked_lambda.clear();

            for (int i = 0; i < rarcurve_clean.size() - retreat - compositecurve.size(); i++){
                stacked_rarefaction.push_back(rarcurve_clean[i]);
                stacked_lambda.push_back(lambda[i]);
            }

            std::cout << "\n\n\n\n\nMAIN\n\n\n\nSTACKED!!!\n\n\n\n\n";

            goto begin_rarefaction;
        }

    }

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();
    return;
}

void on_move(Fl_Widget *w, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    std::cout << "Moving over: " << p << std::endl;

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

    stonepermparams  = new StonePermParams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);
    //StonePermeability stonepermeability(stonepermparams);

    double grw = 1.0; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    double vel = 1.0; // 0.0

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
    boundary = new Three_Phase_Boundary();

    // Simple Flux

    double aa = -0.1; // 0.0
    double bb = -0.3; // -1.0

    RealVector coef(6);
    coef(0) =  -4.0*aa*bb;                   //  0.0
    coef(1) =  -(4.0*aa + 4.0)*bb - 4.0*aa;  //  4.0;
    coef(2) =  (aa - 4.0)*bb - 4.0*aa - 4.0; //  0.0;
    coef(3) =  (aa + 1.0)*bb + aa - 4.0;     // -5.0;
    coef(4) =  bb + aa + 1.0;                //  0.0;
    coef(5) =  1.0;

    simpleflux = new SimpleFlux(coef);
    RealVector pmin(2); pmin(0) = pmin(1) = -1.4;
    RealVector pmax(2); pmax(0) = pmax(1) =   .9;

    boundary = new RectBoundary(pmin, pmax);

    flux = simpleflux;
    accum = stoneaccum;

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
    accum = stoneaccum;

    // ************************* GridValues ************************* //
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
        canvas->number_of_horizontal_ticks(pmax(0) - pmin(0));
        canvas->number_of_vertical_ticks(pmax(1) - pmin(1));

        canvas->setextfunc(&all_of_Hugoniot, canvas, 0);
        canvas->setextfunc(&rar_and_comp, canvas, 0);
        //canvas->setextfunc(&only_shock, canvas, 0);

        //canvas->on_move(&on_move, canvas, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        //canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

    // ************************* Draw Boundaries ************************* //
    std::vector<RealVector> side;
//    side.push_back(Point2D(0, 1));
//    side.push_back(Point2D(1, 0));
//    side.push_back(Point2D(0, 0));
//    side.push_back(Point2D(0, 1));

    boundary->physical_boundary(side);
    side.push_back(side[0]);

    Curve2D side_curve(side, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);
    canvas->nozoom();

    std::vector<RealVector> f, df, d2f;


    int n = 100;
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
        f.push_back(p);

        p(1) = jm.get(0, 0);
        df.push_back(p);

        p(1) = jm.get(0, 0, 0);
        d2f.push_back(p);
    }

    Curve2D *cf = new Curve2D(f, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
    canvas->add(cf);

    // ************************* Draw Boundaries ************************* //

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

    // ************************* Draw Double Contact ************************* //
//    for (int i = 0; i < 2; i++){
//        load_dc(std::string("left"),  GRID_SIZE, i);
//        load_dc(std::string("right"), GRID_SIZE, i);
//    }
    // ************************* Draw Double Contact ************************* //

    canvaswin->show();

    scrollwin->show();


    Fl::scheme("gtk+");

    return Fl::run();
}

