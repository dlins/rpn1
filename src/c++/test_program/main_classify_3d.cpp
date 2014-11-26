// #define CREATE_COINCIDENCE_CURVE
// #define CREATE_INFLECTION_CURVES

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
#include "ClassifyShockCurve.h"
#include "ShockCurve.h"

#include "CoincidenceTP.h"
#include "KompositeCurve.h"
#include "Inflection_Curve.h"

#include <omp.h>

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

    Curve2D    *nearest_point = (Curve2D*)0;
    std::vector<std::vector<RealVector> > all_shockcurves;

    HugoniotContinuation *hug = (HugoniotContinuation*)0;
    ReferencePoint       *referencepoint = (ReferencePoint*)0;

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

void on_move_values(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

//    if (c != canvas_tpcw) return;

    c->deactivate();
    c->on_move(0, c, 0);

    RealVector p(3);
    c->getxy(p(0), p(1));
    p(2) = 1.0;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    WaveState ws(p);
    JetMatrix JetF(3), JetG(3);
    flux[subphysics_index]->jet(ws, JetF, 2);
    accum[subphysics_index]->jet(ws, JetG, 2);

    std::stringstream out;
    out << "Region: " << name[subphysics_index] << std::endl;
    out << "Point: " << p << std::endl;
    out << "F = " << JetF.function() << "\nG = " << JetG.function() << std::endl;
    out << "JF =\n" << JetF.Jacobian() << std::endl;
    out << "JG =\n" << JetG.Jacobian() << std::endl;

    std::vector<eigenpair> e;
    Eigen::eig(3, JetF.Jacobian().data(), JetG.Jacobian().data(), e);
    for (int i = 0; i < e.size(); i++) out << "lambda[" << i << "] = " << e[i].r << std::endl;

    Fl::check();

    ob->labelfont(FL_COURIER);
    if (ob != 0) ob->copy_label(out.str().c_str()); 

    output->show();
    c->activate();
    c->on_move(&on_move_values, c, 0);

    return;   
}

void on_move(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    if (c != canvas_tpcw) return;

    c->deactivate();
    c->on_move(0, c, 0);

    RealVector p(3);
    c->getxy(p(0), p(1));
    p(2) = 1.0;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    if (all_shockcurves.size() > 0){
        double distance = 10000.0;
        RealVector point;

        std::stringstream classification_stream;

        // Find nearest point
        //#pragma omp parallel for shared(distance, point)
        for (int i = 0; i < all_shockcurves.size(); i++){
            for (int j = 0; j < all_shockcurves[i].size(); j++){
                double temp = 0.0;
                for (int k = 0; k < p.size() - 1; k++) temp += (p(k) - all_shockcurves[i][j](k))*(p(k) - all_shockcurves[i][j](k));
                temp = sqrt(temp);

                if (distance > temp){
                    distance = temp;
                    point = all_shockcurves[i][j];

                    std::stringstream out;
                    out << "Distance: " << distance << "\nFound on curve " << i << "\nPoint on curve: " << point << "\nPoint under mouse: " << p;

                    ClassifyShockCurve csc(hug);

                    std::string s;
                    csc.classify_point(point, *referencepoint, s);

                    classification_stream.str(std::string());
                    classification_stream.clear();
                    classification_stream << s;

                    out << "\nClassification: " << classification_stream.str();


                    Fl::check();
                    if (ob != 0) ob->copy_label(out.str().c_str());
                }
            }
        }

        if (nearest_point != 0){
            c->erase(nearest_point);
            //delete nearest_point;
        }

        std::vector<RealVector> temp_vector;
        temp_vector.push_back(point);

        std::vector<std::string> classification;
        classification.push_back(classification_stream.str());

        nearest_point = new Curve2D(temp_vector, 0.0, 0.0, 1.0, classification, CURVE2D_MARKERS | CURVE2D_INDICES);
        c->add(nearest_point);
    }

    //output->position(Fl::event_x_root() + 10, Fl::event_y_root() + 10);
    output->show();
    c->activate();
    c->on_move(&on_move, c, 0);

    return;
}

void kompositecb(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector initial(3);

    c->getxy(initial.component(0), initial.component(1));
    initial(2) = 1.0;

//    initial(0) = 0.780564;
//    initial(1) = 0.447792;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    std::vector<RealVector> rarcurve, inflection_points;

    int family = 1;
    int increase = RAREFACTION_SPEED_DECREASE;

    Rarefaction::curve(initial, 
                       RAREFACTION_INITIALIZE_YES,
                       0/*  const RealVector *initial_direction*/,
                       family, 
                       increase,
                       RAREFACTION_FOR_ITSELF /* int type_of_rarefaction*/,
                       1e-3 /* double deltaxi*/,
                       flux[subphysics_index], accum[subphysics_index],
                       RAREFACTION_GENERAL_ACCUMULATION /* int type_of_accumulation*/,
                       boundary[subphysics_index],
                       rarcurve,
                       inflection_points);

    std::cout << "Main. rarcurve.size() = " << rarcurve.size() << std::endl;

    if (rarcurve.size() > 0){
        std::vector<std::string> vs;
        for (int i = 0; i < rarcurve.size(); i++){
            RealVector p(3);
            for (int j = 0; j < 3; j++) p(j) = rarcurve[i](j);
            WaveState u(p);

            JetMatrix JetF(4), JetG(3);
            flux[subphysics_index]->jet(u, JetF, 1);
            accum[subphysics_index]->jet(u, JetG, 1);

            std::vector<eigenpair> e;
            Eigen::eig(3, JetF.Jacobian().data(), JetG.Jacobian().data(), e);

            stringstream ss;
            ss << "lambdas = (";
            for (int j = 0; j < e.size(); j++) ss << e[j].r << ", ";
            ss << ")";
            
            vs.push_back(ss.str());
        }

        Curve2D *test = new Curve2D(rarcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, vs, /*CURVE2D_MARKERS | */ CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
        canvas[subphysics_index]->add(test);

        std::stringstream buf;
        buf << "Rarefaction. Init. = " << initial << ", size = " << rarcurve.size();
        scroll->add(buf.str().c_str(), canvas[subphysics_index], test);

        // Komposite test
        HugoniotContinuation3D2D hug(flux[subphysics_index], accum[subphysics_index], boundary[subphysics_index]);
//    HugoniotContinuation2D2D hug(stoneflux, stoneaccum, Boundary);
        ShockCurve shock(&hug);

        KompositeCurve kc(flux[subphysics_index], accum[subphysics_index], boundary[subphysics_index], &shock);
        kc.set_graphics(c, scroll);

        // Separate the points from the lambas, etc.
        std::vector<RealVector> pure_rarefaction_curve;
        std::vector<double> lambda;
        for (int i = 0; i < rarcurve.size(); i++){
            RealVector temp(3);
            for (int j = 0; j < 3; j++) temp(j) = rarcurve[i](j);

            pure_rarefaction_curve.push_back(temp);

            lambda.push_back(rarcurve[i](2));
        }

        // Composite proper
        std::vector<RealVector> compositecurve;
        std::vector<int> corresponding_index_in_rarefaction;
        std::vector<double> sigma;

        std::cout << "Will compute composition. Rarefaction had " << rarcurve.size() << " points." << std::endl;

        kc.curve(pure_rarefaction_curve, lambda, family, increase, flux[subphysics_index], accum[subphysics_index], // Attributes of the rarefaction
                 pure_rarefaction_curve[pure_rarefaction_curve.size() - 1], 0 /*int index_in_rarefaction*/, COMPOSITE_BEGINS_IN_INFLECTION /*int where_composite_begins*/, // Known attributes of the composite
                 compositecurve, corresponding_index_in_rarefaction, sigma);

        std::cout << "compositecurve.size() = " << compositecurve.size() << std::endl;

        if (compositecurve.size() > 0){
            Curve2D *test = new Curve2D(compositecurve, 0.0/255.0, 120.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
            c->add(test);

            std::stringstream buf;
            buf << "Composite. Size = " << compositecurve.size();
            scroll->add(buf.str().c_str(), c, test);
        }
    }

    return;
}

void rarefactioncb(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector initial(3);

    c->getxy(initial.component(0), initial.component(1));
    initial(2) = 1.0;

//    initial(0) = 0.780564;
//    initial(1) = 0.447792;

    int subphysics_index;
    if      (c == canvas_vapor) subphysics_index = 0;
    else if (c == canvas_tpcw)  subphysics_index = 1;
    else                        subphysics_index = 2;

    std::vector<RealVector> rarcurve, inflection_points;

    Rarefaction::curve(initial, 
                       RAREFACTION_INITIALIZE_YES,
                       0/*  const RealVector *initial_direction*/,
                       1/*  int curve_family*/, 
                       RAREFACTION_SPEED_DECREASE /*  int increase*/,
                       RAREFACTION_FOR_ITSELF /* int type_of_rarefaction*/,
                       1e-3 /* double deltaxi*/,
                       flux[subphysics_index], accum[subphysics_index],
                       RAREFACTION_GENERAL_ACCUMULATION /* int type_of_accumulation*/,
                       boundary[subphysics_index],
                       rarcurve,
                       inflection_points);

    std::cout << "Main. rarcurve.size() = " << rarcurve.size() << std::endl;

    if (rarcurve.size() > 0){
        std::vector<std::string> vs;
        for (int i = 0; i < rarcurve.size(); i++){
            RealVector p(3);
            for (int j = 0; j < 3; j++) p(j) = rarcurve[i](j);
            WaveState u(p);

            JetMatrix JetF(3), JetG(3);
            flux[subphysics_index]->jet(u, JetF, 1);
            accum[subphysics_index]->jet(u, JetG, 1);

            std::vector<eigenpair> e;
            Eigen::eig(3, JetF.Jacobian().data(), JetG.Jacobian().data(), e);

            stringstream ss;
            ss << "lambdas = (";
            for (int j = 0; j < e.size(); j++) ss << e[j].r << ", ";
            ss << ")";
            
            vs.push_back(ss.str());
        }

        Curve2D *test = new Curve2D(rarcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, vs, /*CURVE2D_MARKERS | */ CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
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

    if (referencepoint != 0) delete referencepoint;
    referencepoint = new ReferencePoint(ref, fref, aref, 0);

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

    if (hug != 0) delete hug;
    hug = new HugoniotContinuation3D2D(fref, aref, bref);

//    hug = new HugoniotContinuation_nD_nm1D(fref, aref, bref);

//    HugoniotContinuation_nDnD hug(fref, aref, bref);

    hug->set_reference_point(*referencepoint);
    std::vector< std::vector<RealVector> > transition_current, transition_reference;
    all_shockcurves.clear();

    ShockCurve sc(hug);
    int info = sc.curve(*referencepoint, SHOCKCURVE_SHOCK_CURVE, SHOCKCURVE_EQUALITY_OF_LAMBDA_OF_FAMILY_AT_REF_AND_SIGMA_ON_THE_RIGHT, all_shockcurves, transition_current, transition_reference);

    clear_all_curves->do_callback();

    for (int i = 0; i < all_shockcurves.size(); i++){
        if (all_shockcurves[i].size() > 0){
            Curve2D *shock_test = new Curve2D(all_shockcurves[i], 255.0/255.0, 0.0/255.0, 0.0/255.0, /*CURVE2D_MARKERS | */ CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
            cref->add(shock_test);

            char buf[1000];
            sprintf(buf, "Hugoniot (cont.) over %s. Init. = Ref. = (%g, %g, %g). Size = %d.", 
                         where_ref.c_str(),
                         referencepoint->point(0), referencepoint->point(1), referencepoint->point(2), 
                         all_shockcurves[i].size());
            scroll->add(buf, cref, shock_test);
        }
    }

    for (int i = 0; i < transition_current.size(); i++){
        if (transition_current[i].size() > 0){
            Curve2D *shock_test = new Curve2D(transition_current[i], 0.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            cref->add(shock_test);

            std::stringstream s;
            s << "Transitions (current). Ref. = " << referencepoint->point << ". Size = " << transition_current[i].size();

            scroll->add(s.str().c_str(), cref, shock_test);
        }
    }

    for (int i = 0; i < transition_reference.size(); i++){
        if (transition_reference[i].size() > 0){
            Curve2D *shock_test = new Curve2D(transition_reference[i], 0.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            cref->add(shock_test);

            std::stringstream s;
            s << "Transitions (reference). Ref. = " << referencepoint->point << ". Size = " << transition_current[i].size();

            scroll->add(s.str().c_str(), cref, shock_test);
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
//        canvas_tpcw->setextfunc(&all_of_Hugoniot, canvas_tpcw, 0);

//        canvas_tpcw->setextfunc(&rarefactioncb, canvas_tpcw, 0);
        canvas_tpcw->setextfunc(&kompositecb, canvas_tpcw, 0);

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
//        canvas[i]->on_move(&on_move, canvas[i], 0);
//        canvas[i]->on_move(&on_move_values, canvas[i], 0);
//        canvas[i]->on_leave(&on_leave, canvas[i], 0);
//        canvas[i]->on_enter(&on_enter, canvas[i], 0);
    }

    // ************************* Inflection over TPCW ************************* //

    std::vector<RealVector> inflection_curve;
    #ifdef CREATE_INFLECTION_CURVES
        Inflection_Curve ic;

        for (int family = 0; family < 2; family++){
            ic.curve(tpcw_flux, tpcw_accum, *tpcw_grid, family, inflection_curve);

            std::stringstream ss;
            ss << "Inflection_family_" << family;
 
            FILE *fid = fopen(ss.str().c_str(), "w");

            fprintf(fid, "%d\n", inflection_curve.size());
            for (int i = 0; i < inflection_curve.size(); i++) fprintf(fid, "%g %g\n", inflection_curve[i](0), inflection_curve[i](1));

            fclose(fid);
        }
    #else
        for (int family = 0; family < 2; family++){
            std::stringstream ss;
            ss << "Inflection_family_" << family;
 
            FILE *fid = fopen(ss.str().c_str(), "r");

            fscanf(fid, "%d", &c_size);
            inflection_curve.resize(c_size);

            for (int i = 0; i < c_size/2; i++){
                for (int j = 0; j < 2; j++){
                    inflection_curve[2*i + j].resize(2);

                    for (int k = 0; k < 2; k++) fscanf(fid, "%lf", &(inflection_curve[2*i + j](k)));
                }
            }

            fclose(fid);

            SegmentedCurve *ic_curve = new SegmentedCurve(inflection_curve, 1.0, 127.0/255.0, 0.0);
            canvas_tpcw->add(ic_curve);
        }
    #endif



    // ************************* Inflection over TPCW ************************* //

    return Fl::run();
}

