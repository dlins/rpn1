#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
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

#include <time.h>

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;
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

void liuwavecurve(const RealVector &point, WaveCurve &w){
    RarefactionCurve rc(accum, flux, boundary);

    HugoniotContinuation_nDnD hug(flux, accum, boundary);
    ShockCurve sc(&hug);

    Stone_Explicit_Bifurcation_Curves bc((StoneFluxFunction*)flux);
    CompositeCurve cmp(accum, flux, boundary, &sc, &bc);

    LSODE lsode;

    ODE_Solver *odesolver;

    odesolver = &lsode;

    WaveCurveFactory wavecurvefactory(accum, flux, boundary, odesolver, &rc, &sc, &cmp);

    int reason_why, edge;
    wavecurvefactory.wavecurve(point, family, increase, &hug, w, reason_why, edge);

    return;
}

void dec_fast(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    increase = RAREFACTION_SPEED_SHOULD_DECREASE;
    family = 1;

    WaveCurve w;

    liuwavecurve(point, w);    

    if (w.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(w, point);
        canvas->add(wcp);
    }

    return;
}

void comp(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    std::cout << "Step = " << step << ", point = " << point << std::endl;

    if (step == 0){
        init0 = point;

        std::ofstream out("initial_points_increase", std::ofstream::out);
        out << point << std::endl;
        out.close();

        family = 0;
//        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

        liuwavecurve(point, wavecurve1);

        if (wavecurve1.wavecurve.size() > 0){
            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve1, point);
            canvas->add(wcp);
        }
    }
    else if (step == 1){
        int curve_index;
        int segment_index_in_curve;
        double speed;

        Utilities::pick_last_point_from_wavecurve(wavecurve1, point, 
                                                  subc1, subc1_point,
                                                  /*curve_index, segment_index_in_curve,*/ init1, speed);

        std::cout << "Selected M point is: " << init1 << std::endl;
        TestTools::pause("M point selected.");

        family = 1;
//        increase = RAREFACTION_SPEED_SHOULD_DECREASE;

        liuwavecurve(init1, wavecurve2);

        if (wavecurve2.wavecurve.size() > 0){
            WaveCurvePlot *wcp = new WaveCurvePlot(wavecurve2, point/*, CURVE2D_SOLID_LINE | CURVE2D_MARKERS | CURVE2D_INDICES*/);
            canvas->add(wcp);
        }
    }
    else if (step == 2){
        int curve_index;
        int segment_index_in_curve;
        double speed_on_point;
        RealVector R;

        Utilities::pick_point_from_wavecurve(wavecurve2, point, 
                                             curve_index, segment_index_in_curve, R, speed_on_point);


        std::ofstream out("initial_points_increase", std::ofstream::out | std::ofstream::app);
        out << R << std::endl;
        out.close();

        Curve2D *pp = new Curve2D(R, 1.0, 0.0, 1.0, CURVE2D_MARKERS);
        canvas->add(pp);

        std::cout << "curve_index = " << curve_index << ", segment_index_in_curve = " << segment_index_in_curve << std::endl;
        TestTools::pause("Check console");

        // Riemann problem.
        //
        RiemannProblem rp;

        std::vector<RealVector> phase_state;
        std::vector<double> speed;

        rp.all_increase_profile(wavecurve1, subc1, subc1_point, 0,
                                wavecurve2, curve_index, segment_index_in_curve, 1,
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
    if (step == 3) step = 0;

    instbox->copy_label(instructions[step].c_str());

    return;
}

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

int main(){
    step = 0;
    increase = RAREFACTION_SPEED_SHOULD_INCREASE; // Always forward.

    instructions.push_back(std::string("Select the L-point."));
    instructions.push_back(std::string("Select the M-point."));
    instructions.push_back(std::string("Select the R-point."));

    // Create fluxFunction
    double expw, expg, expo; expw = expg = 2.0; expo = 2.0;
    double expow, expog;     expow = expog = 2.0;
    double cnw, cng, cno;    cnw = cng = cno = 0.0;
    double lw, lg;           lw = lg = 0.0;
    double low, log;         low = log = 0.0;
    double epsl = 0.0;

    StonePermParams stonepermparams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);
    //StonePermeability stonepermeability(stonepermparams);

    double grw = 1.0; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    muw = 1.0;
    mug = 0.5;
    muo = 2.0;

    double vel = 1.0;

    // Panters' special problem.
//    grw = 1.0;
//    grg = 0.7;
//    gro = 0.8;

//    grw = 1.0;
//    grg = 0.7;
//    gro = 0.7;

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

    StoneParams stoneparams(p);

    StoneFluxFunction stoneflux(stoneparams, stonepermparams);

    StoneAccumulation stoneaccum;

    flux = &stoneflux;
    accum = &stoneaccum;
    boundary = new Three_Phase_Boundary();

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
        canvas->setextfunc(dec_fast, canvas, 0);

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
    std::vector<std::vector<RealVector> > side;
    boundary->physical_boundary(side);
    for (int i = 0; i < side.size(); i++){
        Curve2D *side_curve = new Curve2D(side[i], 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(side_curve);
    }
    //
    // Draw the boundary.

    canvas->nozoom();

    Fl::scheme("gtk+");

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 64;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
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

