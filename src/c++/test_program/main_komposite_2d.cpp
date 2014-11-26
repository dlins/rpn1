//#define CREATE_COINCIDENCE_CURVE
//#define CREATE_INFLECTION_CURVES

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
#include "Hugoniot_Curve.h"

#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"

#include "ReferencePoint.h"
#include "ColorCurve.h"

// TEST
#include "HugoniotContinuation2D2D.h"
#include "HugoniotContinuation_nDnD.h"
#include "ShockCurve.h"
#include <string>
#include <sstream>
#include "KompositeCurve.h"
#include "Inflection_Curve.h"
// TEST

// Input here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas     *canvas = (Canvas*)0;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves;

Three_Phase_Boundary *Boundary = (Three_Phase_Boundary*)0;

// Flux objects
StonePermParams   *stonepermparams = (StonePermParams*)0;
StoneParams       *stoneparams     = (StoneParams*)0;
StoneFluxFunction *stoneflux       = (StoneFluxFunction*)0;
StoneAccumulation *stoneaccum      = (StoneAccumulation*)0;

GridValues *grid;

void no_close_cb(Fl_Widget*, void*){
    return;
}

void close_cb(Fl_Widget*, void*){
    ContourMethod::deallocate_arrays();

    exit(0);

    return;
}

void rarefactioncb(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector initial(2);

//    initial(0) = .511431;
//    initial(1) = .177665;

//    initial(0) = 0.356668;
//    initial(1) = 0.323604;

// Problem with decrease & family 0 (composite will go back and not forward, Newton doesn't converge).
//    initial(0) = 0.478865;
//    initial(1) = 0.337563;

//    initial(0) = 0.298451;
//    initial(1) = 0.251726;

    // Won't advance:
    initial(0) = 0.161983;
    initial(1) = 0.285533;

    // Advances:
    initial(0) = 0.171752;
    initial(1) = 0.279188;

    FILE *fid = fopen("in.txt", "r");
    if (fid == NULL){
        c->getxy(initial.component(0), initial.component(1));
        std::cout << "Reading from canvas: initial = " << initial << std::endl;
    }
    else {
        fscanf(fid, "%lg %lg", &(initial.component(0)), &(initial.component(1)));
        fclose(fid);

        std::cout << "Reading from file: initial = " << initial << std::endl;
    }

    std::vector<RealVector> rarcurve, inflection_points;

    int increase = RAREFACTION_SPEED_INCREASE;
    int family = 1;

    Rarefaction::curve(initial, 
                       RAREFACTION_INITIALIZE_YES,
                       0/*  const RealVector *initial_direction*/,
                       family/*  int curve_family*/, 
                       increase /*  int increase*/,
                       RAREFACTION_FOR_ITSELF /* int type_of_rarefaction*/,
                       1e-3 /* double deltaxi*/,
                       stoneflux, stoneaccum,
                       RAREFACTION_GENERAL_ACCUMULATION /* int type_of_accumulation*/,
                       Boundary,
                       rarcurve,
                       inflection_points);

    if (rarcurve.size() > 0){
        std::vector<std::string> lambda;
        for (int j = 0; j < rarcurve.size(); j++){
            std::stringstream ss;
            ss << rarcurve[j](2);

            lambda.push_back(ss.str());
        }

        Curve2D *test = new Curve2D(rarcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, lambda, CURVE2D_MARKERS | CURVE2D_SOLID_LINE  | CURVE2D_INDICES);
        c->add(test);

        std::stringstream buf;
        buf << "Rarefaction. Init. = " << initial << ", size = " << rarcurve.size();
        scroll->add(buf.str().c_str(), c, test);

        //for (int i = 0; i < rarcurve.size(); i++) std::cout << "Rarefaction[" << i << "] = " << rarcurve[i] << std::endl;

    }

    // Komposite test
//    HugoniotContinuation_nDnD hug(stoneflux, stoneaccum, Boundary);
    HugoniotContinuation2D2D hug(stoneflux, stoneaccum, Boundary);
    ShockCurve shock(&hug);
    shock.set_graphics(c, scroll);

    KompositeCurve kc(stoneflux, stoneaccum, Boundary, &shock);
    kc.set_graphics(c, scroll);

    // Separate the points from the lambas, etc.
    std::vector<RealVector> pure_rarefaction_curve;
    std::vector<double> lambda;
    for (int i = 0; i < rarcurve.size(); i++){
        RealVector temp(2);
        for (int j = 0; j < 2; j++) temp(j) = rarcurve[i](j);

        pure_rarefaction_curve.push_back(temp);

        lambda.push_back(rarcurve[i](2));
    }

    // Composite proper
    std::vector<RealVector> compositecurve;
    std::vector<int> corresponding_index_in_rarefaction;
    std::vector<double> sigma;

    std::cout << "Will compute composition. Rarefaction had " << rarcurve.size() << " points." << std::endl;

    kc.curve(pure_rarefaction_curve, lambda, family, increase, stoneflux, stoneaccum, // Attributes of the rarefaction
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

//    // Test
//    initial(0) = .45;
//    initial(1) = .45;
//    family = 0;
//    increase = RAREFACTION_SPEED_INCREASE;

//        Rarefaction::curve(initial, 
//                       RAREFACTION_INITIALIZE_YES,
//                       0/*  const RealVector *initial_direction*/,
//                       family/*  int curve_family*/, 
//                       increase /*  int increase*/,
//                       RAREFACTION_FOR_ITSELF /* int type_of_rarefaction*/,
//                       1e-3 /* double deltaxi*/,
//                       stoneflux, stoneaccum,
//                       RAREFACTION_GENERAL_ACCUMULATION /* int type_of_accumulation*/,
//                       Boundary,
//                       rarcurve,
//                       inflection_points);
//    if (rarcurve.size() > 0){

//        Curve2D *test = new Curve2D(rarcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
//        c->add(test);

//        std::stringstream buf;
//        buf << "Test. Rarefaction. Init. = " << initial << ", size = " << rarcurve.size();
//        scroll->add(buf.str().c_str(), c, test);

//    }


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

    ref(0) = 0.700784;
    ref(1) = 0.112944;

    ReferencePoint referencepoint(ref, stoneflux, stoneaccum, 0);

    std::cout << "Reference point: " << referencepoint.point << std::endl;

    // Hugoniot (search)
    Hugoniot_Curve hs;

    std::vector<HugoniotPolyLine> hugoniot_curve;

    hs.classified_curve(stoneflux, stoneaccum, 
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

//    HugoniotContinuation2D2D hug(stoneflux, stoneaccum, Boundary);
    HugoniotContinuation_nDnD hug(stoneflux, stoneaccum, Boundary);
    hug.set_reference_point(referencepoint);
////    hug.set_bifurcation_space_coordinate(1);

    std::vector< std::vector<RealVector> > curve;
    std::vector< std::vector<RealVector> > transition_current;
    std::vector< std::vector<RealVector> > transition_reference;
//    int info = hug.curve(curve);

    ShockCurve sc(&hug);
    int info = sc.curve(referencepoint, SHOCKCURVE_SHOCK_CURVE, SHOCKCURVE_EQUALITY_OF_LAMBDA_OF_FAMILY_AT_REF_AND_SIGMA_ON_THE_RIGHT, curve, transition_current, transition_reference);

    std::cout << "All done. Info = " << info << std::endl;

//    if (info == HUGONIOTCONTINUATION3D2D_NEAR_COINCIDENCE_CURVE){
//        std::cout << "The initial point was too close to the coincidence curve: nothing was done." << std::endl;
//        return;
//    }

    for (int i = 0; i < curve.size(); i++){
        if (curve[i].size() > 0){
            Curve2D *shock_test = new Curve2D(curve[i], 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            canvas->add(shock_test);

            std::stringstream s;
            s << "Hugoniot (cont.). Ref. = " << referencepoint.point << ". Size = " << curve[i].size();

            scroll->add(s.str().c_str(), canvas, shock_test);
        }
    }

    for (int i = 0; i < transition_current.size(); i++){
        if (transition_current[i].size() > 0){
            Curve2D *shock_test = new Curve2D(transition_current[i], 0.0/255.0, 255.0/255.0, 0.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            canvas->add(shock_test);

            std::stringstream s;
            s << "Transitions (current). Ref. = " << referencepoint.point << ". Size = " << transition_current[i].size();

            scroll->add(s.str().c_str(), canvas, shock_test);
        }
    }

    for (int i = 0; i < transition_reference.size(); i++){
        if (transition_reference[i].size() > 0){
            Curve2D *shock_test = new Curve2D(transition_reference[i], 0.0/255.0, 0.0/255.0, 255.0/255.0, CURVE2D_MARKERS /* | CURVE2D_SOLID_LINE*/ /* | CURVE2D_INDICES*/);
            canvas->add(shock_test);

            std::stringstream s;
            s << "Transitions (reference). Ref. = " << referencepoint.point << ". Size = " << transition_reference[i].size();

            scroll->add(s.str().c_str(), canvas, shock_test);
        }
    }

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
    // Create the Boundary
    Boundary = new Three_Phase_Boundary();

    // Create StoneFluxFunction
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

    // ************************* GridValues ************************* //
    // GridValues for the extensions
    RealVector pmin(2); pmin.component(0) = 0.0; pmin.component(1) = 0.0;
    RealVector pmax(2); pmax.component(0) = 1.0; pmax.component(1) = 1.0;
    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 256;

    grid = new GridValues(Boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //

    // Window
    int main_w  = 900;
    int main_h  = main_w;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Hugoniot2D");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        canvas = new Canvas(10, 10, canvaswin->w() - 20, canvaswin->h() - 20);
        canvas->xlabel("sw");
        canvas->ylabel("so");
        canvas->setextfunc(&all_of_Hugoniot, canvas, 0);
        canvas->setextfunc(&rarefactioncb, canvas, 0);
//        canvas->on_move(&on_move, canvas, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

    // ************************* Draw Boundaries ************************* //
    std::vector<Point2D> side;
    side.push_back(Point2D(0, 1));
    side.push_back(Point2D(1, 0));
    side.push_back(Point2D(0, 0));
    side.push_back(Point2D(0, 1));
    Curve2D side_curve(side, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);
    canvas->nozoom();
    // ************************* Draw Boundaries ************************* //

    // List of curves
    scrollwin = new Fl_Double_Window(canvaswin->x() + canvaswin->w() + 10, canvaswin->y(), 500, 500, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 2*10 - 25, "Curves");
//    initial(0) = 0.298451;
//    initial(1) = 0.251726;
        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    //scrollwin->callback(no_close_cb);
    scrollwin->set_non_modal();

    canvaswin->show();

    scrollwin->show();


    Fl::scheme("gtk+");

    // ************************* Inflection over Stone ************************* //

    std::vector<RealVector> inflection_curve;
    #ifdef CREATE_INFLECTION_CURVES
        Inflection_Curve ic;

        for (int family = 0; family < 2; family++){
            ic.curve(stoneflux, stoneaccum, *grid, family, inflection_curve);

            std::stringstream ss;
            ss << "Stone_Inflection_family_" << family;
 
            FILE *fid = fopen(ss.str().c_str(), "w");

            fprintf(fid, "%d\n", inflection_curve.size());
            for (int i = 0; i < inflection_curve.size(); i++) fprintf(fid, "%g %g\n", inflection_curve[i](0), inflection_curve[i](1));

            fclose(fid);
        }
    #else
        for (int family = 0; family < 2; family++){
            std::stringstream ss;
            ss << "Stone_Inflection_family_" << family;
 
            FILE *fid = fopen(ss.str().c_str(), "r");

            int c_size;
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
            canvas->add(ic_curve);
        }
    #endif

    // ************************* Inflection over Stone ************************* //

    return Fl::run();
}

