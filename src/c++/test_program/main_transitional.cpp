#include <iostream>
#include "StoneSubPhysics.h"
#include "GasVolatileDeadSubPhysics.h"
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
        etwc.subdivide_curve(*type, s, points);

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

    ReferencePoint ref(M, subphysics->flux(), subphysics->accumulation(), 0);

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
        {
            type = EW;
//            type = BO;
//            type = DG;

            std::vector<double> s;
            std::vector<RealVector> points;

            ExplicitTransitionalWavecurve etwc;
            etwc.subdivide_curve(type, s, points);

            pvertex = points.front();
            pside   = points.back();

            std::vector<std::string> names;
            names.push_back(std::string("V"));
            names.push_back(std::string("E2"));
            names.push_back(std::string("E1"));
            names.push_back(std::string("C2"));
            names.push_back(std::string("U"));
            names.push_back(std::string("C1"));
            names.push_back(std::string("S"));

            // Plot.
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

//            // Line
//            //
//            {
//                Curve2D *c = new Curve2D(points, 0.0, 0.0, 0.0);
//                canvas->add(c);
//            }

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
        }
    }
    win->end();
    win->copy_label(subphysics->info_subphysics().c_str());
    win->resizable(win);
//    win->callback(wincb);
    win->show();

    return Fl::run();
}

