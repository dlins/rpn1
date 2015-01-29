#include "IsoTriang2DBoundary.h"
#include "Stone_Explicit_Bifurcation_Curves.h"

#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "HugoniotContinuation_nDnD.h"
#include "LSODE.h"
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
#include "quiverplot.h"
#include "WaveCurvePlot.h"
#include "RiemannProblem.h"
#include "Double_Contact.h"

#include "StoneSubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "FoamSubPhysics.h"

#include <time.h>
#include "TestTools.h"
#include <cassert>

ThreePhaseFlowSubPhysics *subphysics;

const FluxFunction *flux;
const AccumulationFunction *accum;
const Boundary *boundary;
GridValues *grid;

Fl_Double_Window *win;
    Canvas *canvas;

Fl_Double_Window *instwin;
    Fl_Box *instbox;

Fl_Double_Window *riemannwin;
    Canvas *riemanncanvas;

// Hoover
Fl_Double_Window *output;
    Fl_Box       *ob = (Fl_Box*)0;

int step;
int family, increase;
WaveCurve wavecurve1, wavecurve2;
RealVector pmin, pmax;

RealVector L_point, M_point, R_point;

RealVector init0, init1;

std::vector<std::string> instructions;

int subc1, subc1_point;

void liuwavecurve(const RealVector &point, int type, WaveCurve &w){
    HugoniotContinuation *hug = subphysics->Hugoniot_continuation();

    WaveCurveFactory *wavecurvefactory = subphysics->wavecurvefactory();

    int reason_why, edge;
    wavecurvefactory->wavecurve(type, point, family, increase, hug, w, reason_why, edge);

    return;
}

void comp(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    std::cout << "Step = " << step << ", point = " << point << std::endl;

    if (step == 0){
//        point(0) = 0.0171054;
//        point(1) = 0.139831;

//        point(0) = 0.198609;
//        point(1) = 0.210452;

//        point(0) = 0.20949;
//        point(1) = 0.135593;

//        point(0) = 0.309591;
//        point(1) = 0.0268362;

//        point(0) = 0.3009;
//        point(1) = 0.5724;

        // Foam, Riemann profile unavailable.
        //
//        point(0) = .4162;
//        point(1) = .0034;

        init0 = point;

        std::ofstream out("initial_points", std::ofstream::out);
        out << point << std::endl;
        out.close();

        family = 0;
        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

        liuwavecurve(point, WAVECURVEFACTORY_GENERIC_POINT, wavecurve1);

        if (wavecurve1.wavecurve.size() > 0){
            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve1, point);
            canvas->add(wcp);
        }
    }
    else if (step == 1){
//        point(0) = 0.0665092;
//        point(1) = 0.206215;

//        point(0) = 0.102214;
//        point(1) = 0.45339;


//        point(0) = 0.135618;
//        point(1) = 0.463277;

//        point(0) = 0.390482;
//        point(1) = 0.16003;

//        point(0) = 0.203594;
//        point(1) = 0.532812;

        // Foam, Riemann profile unavailable.
        //
//        point(0) = .677;
//        point(1) = .3088;

        init1 = point;

        std::ofstream out("initial_points", std::ofstream::out | std::ofstream::app);
        out << point << std::endl;
        out.close();

        family = 1;
        increase = RAREFACTION_SPEED_SHOULD_DECREASE;

        liuwavecurve(point, WAVECURVEFACTORY_GENERIC_POINT /*THREEPHASEFLOWWAVECURVEFACTORY_O_TO_W */, wavecurve2);

        TestTools::pause("Done here.");

        if (wavecurve2.wavecurve.size() > 0){
            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve2, point/*, CURVE2D_SOLID_LINE | CURVE2D_MARKERS | CURVE2D_INDICES*/);
            canvas->add(wcp);
        }
    }
    else if (step == 2){
//        point(0) = 0.0380668;
//        point(1) = 0.153955;

//        point(0) = 0.11335;
//        point(1) = 0.224342;

        pmin = point;
    }
    else if (step == 3){
//        point(0) = 0.0972889;
//        point(1) = 0.0974576;

//        point(0) = 0.246401;
//        point(1) = 0.301872;

        pmax = point;

        // Intersection.
        //
        RealVector p;
        int subc1, subc1_point, subc2, subc2_point;

        int info = WaveCurveFactory::intersection(wavecurve1, wavecurve2, pmin, pmax, 
                                       p, subc1, subc1_point, subc2, subc2_point);

        std::cout << "subc1 = " << subc1 << ", subc1_point = " << subc1_point << std::endl;
        std::cout << "subc2 = " << subc2 << ", subc2_point = " << subc2_point << std::endl;

        if (info == WAVE_CURVE_INTERSECTION_NOT_FOUND){
            Curve2D *cpmin = new Curve2D(pmin, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
            canvas->add(cpmin);

            Curve2D *cpmax = new Curve2D(pmax, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
            canvas->add(cpmax);

            return;
        }

        Curve2D *cc = new Curve2D(p, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvas->add(cc);

        // Riemann problem.
        //
        RiemannProblem rp;

        std::vector<RealVector> phase_state;
        std::vector<double> speed;

//        for (int i = 0; i < wavecurve1.wavecurve.size(); i++){
//            if (wavecurve1.wavecurve[i].type == COMPOSITE_CURVE){
//                std::cout << "Composite " << i << std::endl;
//                for (int j = 0; j < wavecurve1.wavecurve[i].curve.size(); j++){
//                    std::cout << "    j = " << j << ", back curve = " << wavecurve1.wavecurve[i].back_curve_pointer[j] << std::endl;
//                }
//            }
//        }

//        TestTools::pause("Check all the backcurves!");

//        rp.profile(wavecurve1, subc1, subc1_point, 0,
//                   wavecurve2, subc2, subc2_point, 1,
//                   phase_state, speed);

        rp.profile(wavecurve1, subc1, subc1_point, 0,
                   wavecurve2, subc2, subc2_point, 1,
                   phase_state, speed);

        std::cout << "phase_state.size() = " << phase_state.size() << ", speed.size() = " << speed.size() << std::endl;

        if (phase_state.size() > 0){
            Curve2D *c = new Curve2D(phase_state, 0.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_INDICES);
            canvas->add(c);
        }

        // Plot sw(lambda).
        //
        if (phase_state.size() > 0){
            std::vector<RealVector> v;
            for (int i = 0; i < phase_state.size(); i++){
                RealVector rv(2);
                rv(0) = speed[i];
                rv(1) = phase_state[i](0);

                v.push_back(rv);
            }
 
            Curve2D *c = new Curve2D(v, 1.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_INDICES | CURVE2D_SOLID_LINE);
            riemanncanvas->add(c);
        }

        // Plot so(lambda).
        //
        if (phase_state.size() > 0){
            std::vector<RealVector> v;
            for (int i = 0; i < phase_state.size(); i++){
                RealVector rv(2);
                rv(0) = speed[i];
                rv(1) = phase_state[i](1);

                v.push_back(rv);
            }
 
            Curve2D *c = new Curve2D(v, 0.0, 1.0, 0.0);
            riemanncanvas->add(c);
        }

        // Plot sg(lambda).
        //
        if (phase_state.size() > 0){
            std::vector<RealVector> v;
            for (int i = 0; i < phase_state.size(); i++){
                RealVector rv(2);
                rv(0) = speed[i];
                rv(1) = 1.0 - phase_state[i](0) - phase_state[i](1);

                v.push_back(rv);
            }
 
            Curve2D *c = new Curve2D(v, 0.0, 0.0, 1.0);
            riemanncanvas->add(c);
        }
    }

    step++;
    if (step == 5) step = 0;

    instbox->copy_label(instructions[step].c_str());

    return;
}

//void comp2(Fl_Widget*, void*){
//    RealVector point(2);
//    canvas->getxy(point(0), point(1));

//    std::cout << "Step = " << step << ", point = " << point << std::endl;

//    if (step == 0){
////        point(0) = 0.0171054;
////        point(1) = 0.139831;

////        point(0) = 0.198609;
////        point(1) = 0.210452;

////        point(0) = 0.2452;
////        point(1) = 0.5724;

//        init0 = point;

//        std::ofstream out("initial_points_increase", std::ofstream::out);
//        out << point << std::endl;
//        out.close();

//        family = 0;
//        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

//        liuwavecurve(point, wavecurve1);

//        if (wavecurve1.wavecurve.size() > 0){
//            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve1, point);
//            canvas->add(wcp);
//        }
//    }
//    else if (step == 1){
////        point(0) = 0.0665092;
////        point(1) = 0.206215;

////        point(0) = 0.102214;
////        point(1) = 0.45339;


////        point(0) = 0.1586;
////        point(1) = 0.586;

//        int curve_index;
//        int segment_index_in_curve;
//        double speed;

//        Utilities::pick_point_from_wavecurve(wavecurve1, point, 
//                                             subc1, subc1_point,
//                                             /*curve_index, segment_index_in_curve,*/ init1, speed);

//        family = 1;
////        increase = RAREFACTION_SPEED_SHOULD_DECREASE;

//        liuwavecurve(init1, wavecurve2);

//        if (wavecurve2.wavecurve.size() > 0){
//            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve2, point/*, CURVE2D_SOLID_LINE | CURVE2D_MARKERS | CURVE2D_INDICES*/);
//            canvas->add(wcp);
//        }
//    }
//    else if (step == 2){
//        int curve_index;
//        int segment_index_in_curve;
//        double speed_on_point;
//        RealVector R;

////        point(0) = .167282;
////        point(1) = .481152;

//        Utilities::pick_point_from_wavecurve(wavecurve2, point, 
//                                             curve_index, segment_index_in_curve, R, speed_on_point);


//        std::ofstream out("initial_points_increase", std::ofstream::out | std::ofstream::app);
//        out << R << std::endl;
//        out.close();

//        Curve2D *pp = new Curve2D(R, 1.0, 0.0, 1.0, CURVE2D_MARKERS);
//        canvas->add(pp);

//        std::cout << "curve_index = " << curve_index << ", segment_index_in_curve = " << segment_index_in_curve << std::endl;
//        TestTools::pause("Check console");

//        // Riemann problem.
//        //
//        RiemannProblem rp;

//        std::vector<RealVector> phase_state;
//        std::vector<double> speed;

//        rp.all_increase_profile(wavecurve1, subc1, subc1_point, 0,
//                                wavecurve2, curve_index, segment_index_in_curve, 1,
//                                phase_state, speed);

//        std::cout << "phase_state.size() = " << phase_state.size() << ", speed.size() = " << speed.size() << std::endl;
//        for (int i = 0; i < phase_state.size(); i++) std::cout << speed[i] << ", " << phase_state[i] << std::endl;

//        if (phase_state.size() > 0){
//            Curve2D *c = new Curve2D(phase_state, 0.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_INDICES);
//            canvas->add(c);
//        }

//        // Plot sw(lambda).
//        //
//        if (phase_state.size() > 0){
//            std::vector<RealVector> v;
//            for (int i = 0; i < phase_state.size(); i++){
//                RealVector rv(2);
//                rv(0) = speed[i];
//                rv(1) = phase_state[i](0);

//                v.push_back(rv);
//            }
// 
//            Curve2D *c = new Curve2D(v, 1.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_INDICES | CURVE2D_SOLID_LINE);
//            riemanncanvas->add(c);
//        }

//        // Plot so(lambda).
//        //
//        if (phase_state.size() > 0){
//            std::vector<RealVector> v;
//            for (int i = 0; i < phase_state.size(); i++){
//                RealVector rv(2);
//                rv(0) = speed[i];
//                rv(1) = phase_state[i](1);

//                v.push_back(rv);
//            }
// 
//            Curve2D *c = new Curve2D(v, 0.0, 1.0, 0.0);
//            riemanncanvas->add(c);
//        }

//        // Plot sg(lambda).
//        //
//        if (phase_state.size() > 0){
//            std::vector<RealVector> v;
//            for (int i = 0; i < phase_state.size(); i++){
//                RealVector rv(2);
//                rv(0) = speed[i];
//                rv(1) = 1.0 - phase_state[i](0) - phase_state[i](1);

//                v.push_back(rv);
//            }
// 
//            Curve2D *c = new Curve2D(v, 0.0, 0.0, 1.0);
//            riemanncanvas->add(c);
//        }
//    }

//    step++;
//    if (step == 3) step = 0;

//    instbox->copy_label(instructions[step].c_str());

//    return;
//}

void wincb(Fl_Widget *w, void*){
    if ((Fl_Double_Window*)w == win) exit(0);

    return;
}

void on_move(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    std::stringstream ss;
    ss << point << std::endl;
    
    std::vector<double> lambda;
    Eigen::fill_eigenvalues(flux, accum, point, lambda);

    ss << lambda[0] << ", " << lambda[1];

    // Speeds.
    if (init0.size() > 0 && init1.size() > 0){
        JetMatrix F(2), G(2);
        flux->jet(point, F, 0);
        accum->jet(point, G, 0);

        HugoniotContinuation_nDnD hug(flux, accum, boundary);

        hug.set_reference_point(ReferencePoint(init0, flux, accum, 0));
        ss << "\n" << hug.sigma(F.function(), G.function());

        hug.set_reference_point(ReferencePoint(init1, flux, accum, 0));
        ss << ", " << hug.sigma(F.function(), G.function());
    }

    ob->copy_label(ss.str().c_str());
    output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
    output->show();

    Fl::check();

    return;
}

void plot_Riemann_profile(const std::vector<RealVector> &phase_state, const std::vector<double> &speed, int type){
    // Plot sw(lambda).
    //
    if (phase_state.size() > 0){
        std::vector<RealVector> v;
        for (int i = 0; i < phase_state.size(); i++){
            RealVector rv(2);
            rv(0) = speed[i];
            rv(1) = phase_state[i](0);

            v.push_back(rv);
        }
 
        Curve2D *c = new Curve2D(v, 1.0, 0.0, 0.0, type);
        riemanncanvas->add(c);
    }

    // Plot so(lambda).
    //
    if (phase_state.size() > 0){
        std::vector<RealVector> v;
        for (int i = 0; i < phase_state.size(); i++){
            RealVector rv(2);
            rv(0) = speed[i];
            rv(1) = phase_state[i](1);

            v.push_back(rv);
        }
 
        Curve2D *c = new Curve2D(v, 0.0, 1.0, 0.0, type);
        riemanncanvas->add(c);
    }

    // Plot sg(lambda).
    //
    if (phase_state.size() > 0){
        std::vector<RealVector> v;
        for (int i = 0; i < phase_state.size(); i++){
            RealVector rv(2);
            rv(0) = speed[i];
            rv(1) = 1.0 - phase_state[i](0) - phase_state[i](1);

            v.push_back(rv);
        }
 
        Curve2D *c = new Curve2D(v, 0.0, 0.0, 1.0, type);
        riemanncanvas->add(c);
    }

    return;
}

//void compute_both_methods(Fl_Widget*, void*){
//    RealVector point(2);
//    canvas->getxy(point(0), point(1));

//    if (step == 0){
//        L_point = point;

//        std::ofstream out("initial_points_increase", std::ofstream::out);
//        out << point << std::endl;
//        out.close();

//        family = 0;
//        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

//        liuwavecurve(point, wavecurve1);

//        if (wavecurve1.wavecurve.size() > 0){
//            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve1, point);
//            canvas->add(wcp);
//        }
//    }
//    else if (step == 1){
//        int curve_index;
//        int segment_index_in_curve;
//        double speed;

//        Utilities::pick_point_from_wavecurve(wavecurve1, point, 
//                                             subc1, subc1_point,
//                                             /*curve_index, segment_index_in_curve,*/ M_point, speed);

//        family = 1;

//        liuwavecurve(M_point, wavecurve2);

//        if (wavecurve2.wavecurve.size() > 0){
//            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve2, point/*, CURVE2D_SOLID_LINE | CURVE2D_MARKERS | CURVE2D_INDICES*/);
//            canvas->add(wcp);
//        }
//    }
//    else if (step == 2){
//        int curve_index;
//        int segment_index_in_curve;
//        double speed_on_point;

//        Utilities::pick_point_from_wavecurve(wavecurve2, point, 
//                                             curve_index, segment_index_in_curve, R_point, speed_on_point);


//        std::ofstream out("initial_points_increase", std::ofstream::out | std::ofstream::app);
//        out << R_point << std::endl;
//        out.close();

//        Curve2D *pp = new Curve2D(R_point, 1.0, 0.0, 1.0, CURVE2D_MARKERS);
//        canvas->add(pp);

//        // Riemann problem.
//        //
//        RiemannProblem rp;

//        std::vector<RealVector> phase_state;
//        std::vector<double> speed;

//        rp.all_increase_profile(wavecurve1, subc1, subc1_point, 0,
//                                wavecurve2, curve_index, segment_index_in_curve, 1,
//                                phase_state, speed);

//        std::cout << "phase_state.size() = " << phase_state.size() << ", speed.size() = " << speed.size() << std::endl;

////        if (phase_state.size() > 0){
////            Curve2D *c = new Curve2D(phase_state, 0.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_INDICES);
////            canvas->add(c);
////        }

//        plot_Riemann_profile(phase_state, speed, /*CURVE2D_MARKERS | CURVE2D_INDICES |*/ CURVE2D_SOLID_LINE);

//        // Compute the classical problem.
//        //
//        {
//            family = 1;
//            increase = RAREFACTION_SPEED_SHOULD_DECREASE;
//            WaveCurve wavecurve3;
//            liuwavecurve(R_point, wavecurve3);

//            int subc1, subc1_point, subc2, subc2_point;

//            RealVector delta(2);
//            delta(0) = delta(1) = .2;

//            RealVector p;
//            WaveCurveFactory::intersection(wavecurve1, wavecurve3, M_point - delta, M_point + delta, 
//                                           p, subc1, subc1_point, subc2, subc2_point);

//            // Riemann problem.
//            //
//            std::vector<RealVector> phase_state2;
//            std::vector<double> speed2;

//            rp.profile(wavecurve1, subc1, subc1_point, 0,
//                       wavecurve3, subc2, subc2_point, 1,
//                       phase_state2, speed2);

//            plot_Riemann_profile(phase_state2, speed2, CURVE2D_MARKERS | CURVE2D_SOLID_LINE);
//        }

//    }

//    step++;
//    if (step == 3) step = 0;

//    instbox->copy_label(instructions[step].c_str());

//    return;
//    
//}

int main(){
    step = 0;

    instructions.push_back(std::string("Create a wavecurve\n(slow, increasing)."));
    instructions.push_back(std::string("Create a wavecurve\n(fast, decreasing)."));
    instructions.push_back(std::string("Set first point of\nintersection region."));
    instructions.push_back(std::string("Set second point of\nintersection region."));
    instructions.push_back(std::string("The Riemann profile is shown.\nClick again to start anew."));

//    subphysics = new StoneSubPhysics;
//    subphysics = new CoreyQuadSubPhysics;
    subphysics = new FoamSubPhysics;
//    {
//        std::vector<Parameter*> parameter;
//        subphysics->equation_parameter(parameter);

//        assert(parameter.size() == 22);

//        parameter[0]->value(1.0);
//        parameter[1]->value(1.0);
//        parameter[2]->value(1e-3);
//        parameter[3]->value(1.0);
//        parameter[4]->value(1.0);
//        parameter[5]->value(1.0);
//        parameter[6]->value(1.0);
//        parameter[7]->value(.1);
//        parameter[8]->value(.1);
//        parameter[9]->value(0.0);
//        parameter[10]->value(2.0);
//        parameter[11]->value(2.0);
//        parameter[12]->value(2.0);
//        parameter[13]->value(100.0);
//        parameter[14]->value(0.0);
//        parameter[15]->value(0.0);
//        parameter[16]->value(.3);
//        parameter[17]->value(2000.0); // 2000.0
//        parameter[18]->value(.3);
//        parameter[19]->value(2.0);
//        parameter[20]->value(1.0);
//        parameter[21]->value(1.0);
//    }

    flux = subphysics->flux();
    accum = subphysics->accumulation();
    boundary = subphysics->boundary();

    // Main window.
    //
    win = new Fl_Double_Window(10, 10, 800, 800, "Wavecurve");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
        canvas->xlabel("sw");
        canvas->ylabel("so");

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);

        canvas->setextfunc(comp, canvas, 0);
//        canvas->setextfunc(comp2, canvas, 0);
//        canvas->setextfunc(compute_both_methods, canvas, 0);
//        canvas->on_move(&on_move, canvas, 0);
    }
    win->end();
    win->resizable(win);
    win->callback(wincb);

    win->show();

    // Instructions window.
    //
    instwin = new Fl_Double_Window(win->x() + win->w(), win->y(), 300, 100, "Instructions");
    {
        instbox = new Fl_Box(0, 0, instwin->w(), instwin->h());
        instbox->copy_label(instructions[0].c_str());
    }
    instwin->end();
    instwin->show();
    instwin->callback(wincb);

    riemannwin = new Fl_Double_Window(instwin->x() + instwin->w(), instwin->y(), 800, 800, "Riemann Profile");
    {
        riemanncanvas = new Canvas(0, 0, riemannwin->w(), riemannwin->h());
        riemanncanvas->xlabel("speed");
        riemanncanvas->ylabel("sw (R), so (G), sg (B)");
    }
    riemannwin->end();
    riemannwin->resizable(riemannwin);
    riemannwin->show();
    riemannwin->callback(wincb);
    
    // Output
    output = new Fl_Double_Window(10, 10, 300, 100, "Info");
    {
        ob = new Fl_Box(0, 0, output->w(), output->h(), "Info");
        ob->box(FL_THIN_UP_BOX);
        ob->labelfont(FL_COURIER);
    }
    output->end();
    output->clear_border();

    // Draw the boundary.
    //
//    std::vector<std::vector<RealVector> > side;
//    boundary->physical_boundary(side);
//    for (int i = 0; i < side.size(); i++){
//        Curve2D side_curve(side[i], 0, 0, 0, CURVE2D_SOLID_LINE);
//        canvas->add(&side_curve);
//    }

    {
        RealVector W(2), G(2), O(2);
        W(0) = 1.0;
        W(1) = 0.0;

        G(0) = 0.0;
        G(1) = 0.0;

        O(0) = 0.0;
        O(1) = 1.0;

        std::vector<RealVector> side;
        side.push_back(W);
        side.push_back(G);
        side.push_back(O);
        side.push_back(W);

        Curve2D *side_curve = new Curve2D(side, 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(side_curve);
    }

    canvas->nozoom();

    Fl::scheme("gtk+");

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 64;

    grid = subphysics->gridvalues(); //new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //

//    std::vector<RealVector> left_curve, right_curve;
//    Double_Contact dctp;
//    dctp.curve(flux, accum, grid, 1,
//               flux, accum, grid, 1,
//               left_curve, right_curve);

//    if (left_curve.size() > 2){
//        SegmentedCurve *curve2d = new SegmentedCurve(left_curve, 1.0, 0.0, 0.0);
//        canvas->add(curve2d);
//    }

//    if (right_curve.size() > 2){
//        SegmentedCurve *curve2d = new SegmentedCurve(right_curve, 1.0, 0.0, 0.0);
//        canvas->add(curve2d);
//    }

    return Fl::run();
}

