#include <string>
#include <fstream>

#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include "canvas.h"
#include "segmentedcurve.h"

#include "Double_Contact.h"
#include "CoreyQuad.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"
#include "WaveCurveFactory.h"
#include "Utilities.h"
#include "WaveCurveFactory.h"
#include "HugoniotContinuation_nDnD.h"
#include "Stone_Explicit_Bifurcation_Curves.h"
#include "LSODE.h"
#include "WaveCurvePlot.h"

Fl_Double_Window *win;
    Canvas *canvas;

Fl_Double_Window *speedwin;
    Canvas *speedcanvas;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;

    Fl_Group *famgrp;
        Fl_Round_Button  *fam0, *fam1;

    Fl_Group *incgrp;
        Fl_Round_Button  *increase_button, *decrease_button;

    Fl_Button        *clear_all_curves;
    Fl_Button        *restoreview;

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;
GridValues *grid;

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();

    return;
}

void restoreviewcb(Fl_Widget *w, void*){
    canvas->axis(0.0, 1.0, 0.0, 0.866025404);

    return;
}

void wincb(Fl_Widget *w, void*){
    if ((Fl_Double_Window*)w == win) exit(0);

    return;
}

void liuwavecurve(Fl_Widget*, void*){
    RealVector initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    RarefactionCurve rc(accum, flux, boundary);

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

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;
        for (int i = 0; i < hwc.wavecurve.size(); i++) ss << ", " << hwc.wavecurve[i].curve.size();

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    {
        std::vector<Curve> arclength_speed, arclength_eigenvalues;
        std::vector<Curve> arclength_reference_eigenvalues;
        
        hwc.speed_map(arclength_speed, arclength_eigenvalues, arclength_reference_eigenvalues);

        // Speed.
        //
        for (int i = 0; i < arclength_speed.size(); i++){
            {
                double r, g, b;
                r = g = b = 0.0;

                if      (arclength_speed[i].type == RAREFACTION_CURVE) r = 1.0;
                else if (arclength_speed[i].type == COMPOSITE_CURVE)   g = 1.0;
                else                                                   b = 1.0;

                Curve2D *c = new Curve2D(arclength_speed[i].curve, r, g, b);
                speedcanvas->add(c);

                scroll->add("Speed", speedcanvas, c);
            }
        }

        // Eigenvalues.
        //
        for (int i = 0; i < arclength_eigenvalues.size(); i++){
            double r, g, b;
            r = g = b = 0.0;

            if      (arclength_eigenvalues[i].type == RAREFACTION_CURVE) r = 1.0;
            else if (arclength_eigenvalues[i].type == COMPOSITE_CURVE)   g = 1.0;
            else                                                         b = 1.0;

            for (int k = 1; k < arclength_eigenvalues[i].curve.front().size(); k++){
                RealVector p(2);
                std::vector<RealVector> vp;
                for (int j = 0; j < arclength_eigenvalues[i].curve.size(); j++) {
                    p(0) = arclength_eigenvalues[i].curve[j](0);
                    p(1) = arclength_eigenvalues[i].curve[j](k);

                    vp.push_back(p);
                }

                Curve2D *c = new Curve2D(vp, r, g, b);
                speedcanvas->add(c);

                std::stringstream ss;
                ss << "Eig. " << k;
                scroll->add(ss.str().c_str(), speedcanvas, c);
            }
        }

        // Reference eigenvalues.
        //
        for (int i = 0; i < arclength_reference_eigenvalues.size(); i++){
            Curve2D *c = new Curve2D(arclength_reference_eigenvalues[i].curve, 0.0, 0.0, 0.0);
            speedcanvas->add(c);

            std::stringstream ss;
            ss << "Ref. eig. " << i;
            scroll->add(ss.str().c_str(), speedcanvas, c);
        }
    }

//    {
//        std::vector<double> arc_length, speed;
//        std::vector<RealVector> eigenvalues;
//        RealVector ref_eig;

//        hwc.speed_map(arc_length, speed, eigenvalues, ref_eig);

//        if (arc_length.size() > 0){
//            std::vector<RealVector> vr;
//            RealVector p(2);

//            for (int i = 0; i < arc_length.size(); i++){
//                p(0) = arc_length[i];
//                p(1) = speed[i];

//                vr.push_back(p);
//            }

//            Curve2D *c = new Curve2D(vr, 0.0, 0.0, 0.0);
//            speedcanvas->add(c);

//            scroll->add("Speed", speedcanvas, c);
//        }

//        if (eigenvalues.size() > 0){
//            for (int i = 0; i < eigenvalues[0].size(); i++){

//                std::vector<RealVector> ve;
//                RealVector p(2);
//                for (int j = 0; j < eigenvalues.size(); j++){
//                    p(0) = arc_length[j];
//                    p(1) = eigenvalues[j](i);

//                    ve.push_back(p);
//                }

//                Curve2D *c = new Curve2D(ve, 0.0, 0.0, 0.0);
//                speedcanvas->add(c);

//                std::stringstream ss;
//                ss << "Eigenvalues, fam = " << i;
//                scroll->add(ss.str().c_str(), speedcanvas, c);
//            }

//            speedcanvas->nozoom();
//        }
//    }

    return;
}

int main(int argc, char *argv[]){
    // Boundary.
    //
    boundary = new Three_Phase_Boundary;

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 128;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //

    // Flux parameters and flux proper.
    //
    double grw = 1.0;
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    double vel = 1.0;

    RealVector p(7);
    p.component(0) = grw;
    p.component(1) = grg;
    p.component(2) = gro;
    p.component(3) = muw;
    p.component(4) = mug;
    p.component(5) = muo;
    p.component(6) = vel;

    CoreyQuad_Params params(p);
    flux = new CoreyQuad(params);

    // Trivial accumulation
    accum = new StoneAccumulation;

    // Window
    win = new Fl_Double_Window(10, 10, 800, 800, "Corey Wavecurve");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
        canvas->xlabel("sw");
        canvas->ylabel("so");

        {
            std::vector<std::vector<RealVector> > pb;
            boundary->physical_boundary(pb);

            for (int i = 0; i < pb.size(); i++){
                Curve2D *side = new Curve2D(pb[i], 0.0, 0.0, 0.0);
                canvas->add(side);
            }
        }

        canvas->setextfunc(&liuwavecurve, canvas, 0);

        canvas->nozoom();
    }
    win->end();
    win->callback(wincb);

    win->resizable(win);
    win->show();

    // List of curves
    scrollwin = new Fl_Double_Window(win->x() + win->w() + 1, win->y(), 500, 500 + 45 + 35 + 35, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 45 - 35 - 35, "Curves");

        famgrp = new Fl_Group(0, 0, scrollwin->w(), scrollwin->h());
        {
        fam0 = new Fl_Round_Button(10, scroll->y() + scroll->h() + 10, (scrollwin->w() - 30)/2, 25, "Family 0");
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

        clear_all_curves = new Fl_Button(increase_button->x(), increase_button->y() + increase_button->h() + 10, (scroll->w() - 10)/2, 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        restoreview = new Fl_Button(clear_all_curves->x() + clear_all_curves->w() + 10, clear_all_curves->y(), clear_all_curves->w(), clear_all_curves->h(), "Restore view");
        restoreview->callback(restoreviewcb);
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(wincb);
    scrollwin->show();

    speedwin = new Fl_Double_Window(scrollwin->x() + scrollwin->w() + 1, scrollwin->y(), 800, 800, "Speed");
    {
        speedcanvas = new Canvas(0, 0, speedwin->w(), speedwin->h());
        speedcanvas->xlabel("Arc length");
        speedcanvas->ylabel("Speed");
    }
    speedwin->end();
    speedwin->resizable(speedwin);
    speedwin->callback(wincb);
    speedwin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

