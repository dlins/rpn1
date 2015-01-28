#include <iostream>
#include "StoneSubPhysics.h"
//#include "GasVolatileDeadSubPhysics.h"
#include "JDSubPhysics.h"
#include "Brooks_CoreySubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "ExplicitTransitionalWavecurve.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Choice.H>
#include <FL/Fl_Tile.H>
#include <FL/Fl_Float_Input.H>
#include <FL/Fl_Scroll.H>
#include "canvas.h"
#include "canvasmenuscroll.h"
#include "curve2d.h"
#include "segmentedcurve.h"
#include "MultiColoredCurve.h"
#include "WaveCurvePlot.h"
#include "LSODE.h"
#include "Inflection_Curve.h"
#include "CharacteristicPolynomialLevels.h"
#include "Text.h"

Fl_Double_Window *win;
    Fl_Tile *tile;
    Canvas *canvas;

    Fl_Group *scroll_grp;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves, *nozoom;

Fl_Double_Window *commandwin;
    Fl_Round_Button *hugrnd;
    Fl_Group *huggrp;

    Fl_Round_Button *wavernd;
    Fl_Group *wavegrp;
        Fl_Group *wavegrpfam;
            Fl_Round_Button *slowfamrnd, *fastfamrnd;
        Fl_Group *wavegrpinc;
            Fl_Round_Button *incrnd, *decrnd;

    Fl_Group *textgrp;
        Fl_Input *textinput;

    Fl_Group *coordgrp;
        Fl_Box *coordbox;


CoreyQuadSubPhysics *subphysics;

RealVector pvertex(2), pside(2);

void Hugoniot(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    ReferencePoint ref(point, subphysics->flux(), subphysics->accumulation(), 0);

    std::vector<HugoniotCurve*> Hugoniot_methods;
    subphysics->list_of_Hugoniot_methods(Hugoniot_methods);

    std::vector<HugoniotPolyLine> classified_curve;
    Hugoniot_methods[0]->curve(ref, 0, classified_curve);

    if (classified_curve.size() > 0){
        MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve, -100.0, 100.0, 100);
        canvas->add(mcc);

        scroll->add("Hug.", canvas, mcc);
    }

    Fl::check();

    return;
}

void wincb(Fl_Widget *w, void*){
    Fl_Double_Window *thiswindow = (Fl_Double_Window*)w;

    if (thiswindow == win) exit(0);

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();

    return;
}

void nozoomcb(Fl_Widget*, void*){
    canvas->nozoom();

    return;
}

void Mcb(Fl_Widget*, void *t){
    int *type = (int*)t;
    std::cout << "Type = " << (*type) << std::endl;

    RealVector p(2);
    canvas->getxy(p(0), p(1));

    RealVector M = project_point_onto_line_2D(p, pvertex, pside);

    {
        Curve2D *c = new Curve2D(M, 0.0, 1.0, 0.0, CURVE2D_MARKERS);
        canvas->add(c);
        scroll->add("M", canvas, c);

        ExplicitTransitionalWavecurve etwc;

        std::vector<double> s;
        std::vector<RealVector> points;
        std::vector<std::string> names;

        etwc.subdivide_curve(*type, s, points, names);

        for (int i = 0; i < s.size(); i++) std::cout << "s[" << i << "] = " << s[i] << ", " << names[i] << std::endl;

        RealVector point_sl(1), point_sr(1);
        int info = etwc.find_subdivision(*type, M, s, point_sl, point_sr);
        std::cout << "point_sl = " << point_sl << std::endl;
        std::cout << "point_sr = " << point_sr << std::endl;

        if (info == SUBDIVISION_ERROR){
            std::cout << "Error!" << std::endl;
        }
        else {
            std::vector<RealVector> segvec;
            segvec.push_back(point_sl);
            segvec.push_back(point_sr);

            Curve2D *seg = new Curve2D(segvec, 1.0, 0.27, 0.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE);
            canvas->add(seg);
            scroll->add("Interval", canvas, seg);
        }
    }

    // TODO: L in W-U-D:
    //       M bewteen E1 and U: non-local composite from X1, etc.
    //
    //       L in W-U-B:
    //       M between E2 and U: initial point at: largest root of the 2-degree polynomial (Fred will send it). A point P will be found.
    //       From P: The transtional segment X2-P remains (P-X1 is discarded).
    //           If L belongs to the region between UW and its slow-family extension:
    //               1. A slow-family shock curve from P to (probably) the boundary GO with L as reference point.
    //           If L belongs to the region between the extension of U-W and B-W:
    //               If L lies to the left of the extension of G-O:
    //                   Non-local composite from P, etc.
    //               If L lies to the right of the extension of G-O:
    //                   Non-local composite from P which will end on G-O.









//    ReferencePoint ref(M, subphysics->flux(), subphysics->accumulation(), 0);

//    std::vector<HugoniotCurve*> Hugoniot_methods;
//    subphysics->list_of_Hugoniot_methods(Hugoniot_methods);

//    std::vector<HugoniotPolyLine> classified_curve;
//    Hugoniot_methods[0]->curve(ref, 0, classified_curve);

//    if (classified_curve.size() > 0){
//        MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve, -100.0, 100.0, 100);
//        canvas->add(mcc);

//        scroll->add("Hug.", canvas, mcc);
//    }

    Fl::check();

    return;
}

void wavecurvecb(const RealVector &initial_point, int fam, int inc){
    WaveCurve hwc;
    int reason_why, edge;

    WaveCurveFactory *wcf = subphysics->wavecurvefactory();
    int info = wcf->wavecurve(WAVECURVEFACTORY_GENERIC_POINT /*type*/, initial_point, fam, inc, subphysics->Hugoniot_continuation(), 0/*object[bifindex]*/, 0/*function[bifindex]*/, hwc, reason_why, edge);
    std::cout << "info = " << info << ", hwc.wavecurve.size() = " << hwc.wavecurve.size() << std::endl;

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, initial_point/*, CURVE2D_MARKERS | CURVE2D_SOLID_LINE*/);
        canvas->add(wcp);

        std::stringstream ss;
        ss << "Wavecurve. Initial = " << initial_point;
        for (int i = 0; i < hwc.wavecurve.size(); i++) ss << ", " << hwc.wavecurve[i].curve.size();

        scroll->add(ss.str().c_str(), canvas, wcp);
    }

    return;
}

void textcb(Fl_Widget*, void*){
    RealVector pos(2);
    canvas->getxy(pos(0), pos(1));

    RealVector shift(2);
    shift(0) = 0.0;
    shift(1) = -10.0;

    Text *t = new Text(textinput->value(), pos, shift, 1.0, 0.0, 0.0);
    canvas->add(t);
    scroll->add(textinput->value(), canvas, t);

    return;
}

void on_move_coords(Fl_Widget*, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    std::stringstream s;
    s << "Mouse over " << p;

    coordbox->copy_label(s.str().c_str());    

    return;
}

int main(){
    subphysics = new CoreyQuadSubPhysics;

    int type;

    int scrollwidth = 300;
    win = new Fl_Double_Window(10, 10, 900 + scrollwidth + 20, 900);
    {
        // Canvas.
        //
        canvas = new Canvas(0, 0, win->w() - 20 - scrollwidth, win->h());

        std::vector<std::vector<RealVector> > b;
        subphysics->boundary()->physical_boundary(b);

        for (int i = 0; i < b.size(); i++){
            if (b[i].size() > 1){
                Curve2D *c = new Curve2D(b[i], 0.0, 0.0, 0.0);

                canvas->add(c);
            }
        }

        canvas->set_transform_matrix(subphysics->transformation_matrix().data());
        canvas->nozoom();

        canvas->xlabel(subphysics->xlabel().c_str());
        canvas->ylabel(subphysics->ylabel().c_str());
        canvas->setextfunc(Hugoniot, canvas, 0);
        canvas->setextfunc(Mcb, canvas, &type);
        canvas->on_move(&on_move_coords, canvas, 0);

        // Scroll.
        //
        scroll_grp = new Fl_Group(canvas->x() + canvas->w() + 1, canvas->y(), scrollwidth, canvas->h());
        scroll = new CanvasMenuScroll(canvas->x() + canvas->w() + 10, 20, scrollwidth, win->h() - 40 - 25, "Curves");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, (scroll->w() - 10)/2, 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        nozoom = new Fl_Button(clear_all_curves->x() + clear_all_curves->w() + 10, 
                               clear_all_curves->y(),
                               clear_all_curves->w(),
                               clear_all_curves->h(),
                               "No zoom");
        nozoom->callback(nozoomcb);

        scroll_grp->end();

        // Transitional.
        //
        std::vector<int> types;

        types.push_back(DG);
        types.push_back(EW);
        types.push_back(BO);

        for (int i = 0; i < types.size(); i++){
            type = types[i];

            std::vector<double> s;
            std::vector<RealVector> points;
            std::vector<std::string> names;

            ExplicitTransitionalWavecurve etwc;
            etwc.subdivide_curve(type, s, points, names);

            pvertex = points.front();
            pside   = points.back();

            // Plot. Print the Umbilic point only once.
            //
            for (int i = 0; i < points.size(); i++){
                Curve2D *c = new Curve2D(points[i], 1.0, 0.0, 0.0, CURVE2D_MARKERS);
                canvas->add(c);

                RealVector shift(2);
                shift(0) = 10;
                shift(1) = -10;
                Text *t = new Text(names[i], points[i], shift, 0.0, 0.0, 1.0);
                canvas->add(t);

                std::cout << names[i] << " = " << points[i] << ", s = " << s[i] << std::endl;
            }

            // Line
            //
            {
                Curve2D *c = new Curve2D(points, 0.0, 0.0, 0.0);
                canvas->add(c);
            }

        // Vertices.
        //
        {
            RealVector pos(2);
            pos(0) = 0.0;
            pos(1) = 1.0;

            RealVector shift(2);
            shift(0) = 0.0;
            shift(1) = -10.0;

            Text *t = new Text(std::string("O"), pos, shift, 1.0, 0.0, 0.0);
            canvas->add(t);
        }

        {
            RealVector pos(2);
            pos(0) = 1.0;
            pos(1) = 0.0;

            RealVector shift(2);
            shift(0) = 10.0;
            shift(1) = 10.0;

            Text *t = new Text(std::string("W"), pos, shift, 1.0, 0.0, 0.0);
            canvas->add(t);
        }

        {
            RealVector pos(2);
            pos(0) = 0.0;
            pos(1) = 0.0;

            RealVector shift(2);
            shift(0) = -10.0;
            shift(1) =  10.0;

            Text *t = new Text(std::string("G"), pos, shift, 1.0, 0.0, 0.0);
            canvas->add(t);
        }

//        // Wavecurves.
//        //
//        wavecurvecb(points[1], 0, RAREFACTION_SPEED_SHOULD_DECREASE); // E1
//        wavecurvecb(points[2], 0, RAREFACTION_SPEED_SHOULD_DECREASE); // E2

        }

        // Inflections.
        {
            Inflection_Curve *ic = subphysics->inflection_curve();

            for (int fam = 0; fam < 2; fam++){
                std::vector<RealVector> inflection_curve;
                ic->curve(subphysics->flux(), subphysics->accumulation(), *(subphysics->gridvalues()), fam, inflection_curve);

                if (inflection_curve.size() > 0){
                    SegmentedCurve *sc = new SegmentedCurve(inflection_curve, 0.0, 1.0, .5);
                    canvas->add(sc);

                    std::stringstream ss;
                    ss << "Inflection, fam = " << fam;
                    scroll->add(ss.str().c_str(), canvas, sc);
                }
            }
        }
    }
    win->end();
    win->copy_label(subphysics->info_subphysics().c_str());
    win->resizable(win);
    win->callback(wincb);
    win->show();

    // Command window.
    //
    commandwin = new Fl_Double_Window(win->x() + win->w(), win->y(), 300, 300, "Commands");
    {
//    Fl_Round_Button *hugrnd;
//    Fl_Group *huggrp;

//    Fl_Round_Button *wavernd;
//    Fl_Group *wavegrp;
//        Fl_Group *wavegrpfam;
//            Fl_Round_Button *slowfamrnd, *fastfamrnd;
//        Fl_Group *wavegrpinc;
//            Fl_Round_Button *incrnd, *decrnd;

//        textgrp = new Fl_Group();
//        textgrp->box(FL_EMBOSSED_BOX);
        
        textinput = new Fl_Input(10, 10, commandwin->w() - 20, 25);
//        canvas->setextfunc(textcb, canvas, 0);

//        coordgrp = new Fl_Group();
         coordbox = new Fl_Box(textinput->x(), textinput->y() + textinput->h() + 10, textinput->w(), textinput->h()); 
         coordbox->box(FL_EMBOSSED_BOX);

    }
    commandwin->end();
    commandwin->show();
    commandwin->callback(wincb);

    return Fl::run();
}

