#include <string>
#include <fstream>

#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include "canvas.h"
#include "segmentedcurve.h"
#include "MultiColoredCurve.h"

#include "Hugoniot_Curve.h"
#include "CoreyQuad.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"
#include "Extension_Curve.h"
#include "Utilities.h"
#include "CoreyQuadExplicitHugoniotCurve.h"
#include "ImplicitHugoniotCurve.h"

#include "Double_Contact.h"
#include "Double_Contact_GPU.h"

Fl_Double_Window *win;
    Canvas *canvas;

Fl_Double_Window *optionswin;
    Fl_Group *optionsgrp;
    std::vector<Fl_Round_Button*> options;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves;
    Fl_Button        *restoreview;

CoreyQuadExplicitHugoniotCurve *cqehc;
//StoneFluxFunction *stoneflux;
CoreyQuad *flux;
StoneAccumulation *accum;
GridValues *grid;
Three_Phase_Boundary *boundary;

std::vector<std::string> typesnames;
std::vector<int> types;

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();

    return;
}

void canvascb(Fl_Widget *w, void*){
//    ParametricPlot::canvas = canvas;
//    ParametricPlot::scroll = scroll;    

    int index;
    int type;

    for (int i = 0; i < options.size(); i++){
        if (options[i]->value() == 1){
            type  = types[i];
            index = i;

            break;
        }
    }

    RealVector point(2);
    canvas->getxy(point(0), point(1));

//    // Case 4.
//    point(0) = .0575837;
//    point(1) = .590547;

//    // Asymptote.
//    point(0) = .510324 + .01;
//    point(1) = .0;
//    index = 4;
//    type = 4;
 
//    RealVector projected_point = cqehc->project(point, type);
//    {
//        Curve2D *ppc = new Curve2D(projected_point, 0.0, 1.0, 0.0, CURVE2D_MARKERS);
//        canvas->add(ppc);

//        std::stringstream ss;
//        ss << "Reference point = " << projected_point;
//        scroll->add(ss.str().c_str(), canvas, ppc);
//    }
    
    // Explicit Hugoniot
    {
        ReferencePoint referencepoint(point, flux, accum, 0);
        std::vector<Curve> c;

//        cqehc->curve(referencepoint, type, c);
//        for (int i = 0; i < c.size(); i++){
//            if (c[i].curve.size() > 0){
//                Curve2D *cc = new Curve2D(c[i].curve, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS | CURVE2D_INDICES);
//                canvas->add(cc);

//                std::stringstream ss;
//                ss << "Hug. explicit, " << typesnames[index] << " (" << c[i].curve.size() << " points)";

//                scroll->add(ss.str().c_str(), canvas, cc);
//            }
//        }

        std::vector<HugoniotPolyLine> classified_curve;
        cqehc->curve(referencepoint, type, classified_curve);

        {
                MultiColoredCurve *cc = new MultiColoredCurve(classified_curve, 0.0, 2.0, 10);
                canvas->add(cc);

                std::stringstream ss;
//                ss << "Hug. explicit, " << typesnames[index] << ", ref. = " << projected_point;
                ss << "Hug. explicit, " << typesnames[index];

                scroll->add(ss.str().c_str(), canvas, cc);
        }
    }
    // Explicit Hugoniot

//    // Hugoniot (implicit, to verify)
//    {
//        Hugoniot_Curve hc;
//        ReferencePoint referencepoint(projected_point, flux, accum, 0);
//        std::vector<HugoniotPolyLine> hugoniot_curve;
//        clock_t time_start, time_stop;

//        time_start = clock();
// 
//        hc.classified_curve(flux, accum, *grid, referencepoint, hugoniot_curve);

//        time_stop = clock() - time_start;

//        std::cout << "Time: " << (double)time_stop/CLOCKS_PER_SEC << std::endl;

//        {
////        MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve);
//            MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve, 0.0, 2.0, 10);
//            canvas->add(mcc);

//            std::stringstream ss;
//            ss << "Hug. implicit, " << typesnames[index];

//            scroll->add(ss.str().c_str(), canvas, mcc);
//        }
//    }
//    // Hugoniot (implicit, to verify)

    // New ImplicitHugoniotCurve.
    //
    {
        ReferencePoint referencepoint(point, flux, accum, 0);
        ImplicitHugoniotCurve ihc(flux, accum, boundary);
        ihc.set_grid(grid);

        std::vector<HugoniotPolyLine> classified_curve;
        ihc.curve(referencepoint, type, classified_curve);

        {
//        MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve);
            MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve, 0.0, 2.0, 10);
            canvas->add(mcc);

            std::stringstream ss;
            ss << "New Hug. implicit, " << typesnames[index];

            scroll->add(ss.str().c_str(), canvas, mcc);
        }
    }

    return;
}

void restoreviewcb(Fl_Widget *w, void*){
    canvas->axis(0.0, 1.0, 0.0, 1.0);

    return;
}

void wincb(Fl_Widget *w, void*){
    if ((Fl_Double_Window*)w == win) exit(0);

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
    number_of_cells[0] = number_of_cells[1] = 32;

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

//    muw = 1.0;
//    mug = 0.5;
//    muo = 2.0;

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

    // CoreyQuadExplicitHugoniotCurve
    {
    // Create fluxFunction
        double expw, expg, expo; expw = expg = 2.0; expo = 2.0;
        double expow, expog;     expow = expog = 2.0;
        double cnw, cng, cno;    cnw = cng = cno = 0.0;
        double lw, lg;           lw = lg = 0.0;
        double low, log;         low = log = 0.0;
        double epsl = 0.0;

//        StonePermParams *stonepermparams  = new StonePermParams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);

//        StoneParams *stoneparams = new StoneParams(p);

//        stoneflux   = new StoneFluxFunction(*stoneparams, *stonepermparams);
    }
//    Stone_Explicit_Bifurcation_Curves sebc(stoneflux);
    cqehc = new CoreyQuadExplicitHugoniotCurve(flux, accum, boundary);

    // Window
    win = new Fl_Double_Window(10, 10, 800, 800, "CoreyQuadExplicitHugoniotCurve");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
        canvas->xlabel("sw");
        canvas->ylabel("so");

        canvas->setextfunc(&canvascb, canvas, 0);

//        RealVector A(2), B(2), C(2);
//        A(0) = 0.0;
//        A(1) = 0.0;

//        B(0) = 1.0;
//        B(1) = 0.0;

//        C(0) = 0.0;
//        C(1) = 1.0;

//        std::vector<RealVector> triangle;
//        triangle.push_back(A);
//        triangle.push_back(B);
//        triangle.push_back(C);
//        triangle.push_back(A);

//        Curve2D *triangle_curve = new Curve2D(triangle, 0.0, 0.0, 0.0);
//        canvas->add(triangle_curve);

        {
            std::vector<std::vector<RealVector> > pb;
            boundary->physical_boundary(pb);

            for (int i = 0; i < pb.size(); i++){
                Curve2D *c = new Curve2D(pb[i], 0.0, 0.0, 0.0);
                canvas->add(c);
            }
        }

        // Draw the Gas Critical Line
        std::vector<Point2D> gas_critical_line;
        gas_critical_line.push_back(Point2D(0, 0));
        gas_critical_line.push_back(Point2D(muw/(muw + muo), muo/(muw + muo)));
        Curve2D *gas_critical_line_curve = new Curve2D(gas_critical_line, 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(gas_critical_line_curve);

        // Draw the Oil Critical Line
        std::vector<Point2D> oil_critical_line;
        oil_critical_line.push_back(Point2D(0, 1));
        oil_critical_line.push_back(Point2D(muw/(muw + mug), 0));
        Curve2D *oil_critical_line_curve = new Curve2D(oil_critical_line, 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(oil_critical_line_curve);

        // Draw the Water Critical Line
        std::vector<Point2D> water_critical_line;
        water_critical_line.push_back(Point2D(1, 0));
        water_critical_line.push_back(Point2D(0, muo/(mug + muo)));
        Curve2D *water_critical_line_curve = new Curve2D(water_critical_line, 0, 0, 0, CURVE2D_SOLID_LINE);
        canvas->add(water_critical_line_curve);

        canvas->nozoom();
    }
    win->end();
    win->callback(wincb);

    win->resizable(win);
    win->show();

    cqehc->types(types, typesnames);
    optionswin = new Fl_Double_Window(win->x() + win->w() + 1, 10, 200, types.size()*25 + (types.size() + 1)*10, "Options");
    {
        optionsgrp = new Fl_Group(0, 0, optionswin->w(), optionswin->h());
        {
            for (int i = 0; i < types.size(); i++){
                Fl_Round_Button *btn = new Fl_Round_Button(10, 10 + i*35, optionswin->w() - 20, 25, typesnames[i].c_str());
                btn->value(0);
                btn->type(FL_RADIO_BUTTON);

                options.push_back(btn);
            }

            options.front()->value(1);
        }
    }
    optionswin->end();
    optionswin->callback(wincb);
    optionswin->set_non_modal();

    optionswin->show();

    // List of curves
    scrollwin = new Fl_Double_Window(optionswin->x() + optionswin->w() + 1, optionswin->y(), 500, 500 + 45, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 45, "Curves");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, (scroll->w() - 10)/2, 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        restoreview = new Fl_Button(clear_all_curves->x() + clear_all_curves->w() + 10, clear_all_curves->y(), clear_all_curves->w(), clear_all_curves->h(), "Restore view");
        restoreview->callback(restoreviewcb);
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(wincb);
    scrollwin->set_non_modal();
    scrollwin->show();

    Fl::scheme("gtk+");

//    // Classic Double Contact
//    {
//    Double_Contact dc;
//    std::vector<RealVector> l, r;

//    int lfam = 1;
//    int rfam = 1;

//    dc.curve(flux, accum, grid, lfam, flux, accum, grid, rfam, l, r);

//    if (l.size() > 0){
//        SegmentedCurve *sc = new SegmentedCurve(l, 1.0, 0.0, 0.0);
//        canvas->add(sc);

//        std::stringstream ss;
//        ss << "Double Contact, left, fam = " << lfam;
//        scroll->add(ss.str().c_str(), canvas, sc);
//    }

//    if (r.size() > 0){
//        SegmentedCurve *sc = new SegmentedCurve(r, 0.0, 0.0, 1.0);
//        canvas->add(sc);

//        std::stringstream ss;
//        ss << "Double Contact, right, fam = " << lfam;
//        scroll->add(ss.str().c_str(), canvas, sc);
//    }

//    }
//    // Classic Double Contact

    // GPU Double Contact
    {
    Double_Contact_GPU dc;
    std::vector<RealVector> l, r;

    int lfam = 1;
    int rfam = 1;

    dc.gpu_curve(flux, accum, grid, lfam, flux, accum, grid, rfam, l, r);

    if (l.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(l, 1.0, 0.0, 0.0);
        canvas->add(sc);

        std::stringstream ss;
        ss << "GPU Double Contact, left, fam = " << lfam;
        scroll->add(ss.str().c_str(), canvas, sc);
    }

    if (r.size() > 0){
        SegmentedCurve *sc = new SegmentedCurve(r, 0.0, 0.0, 1.0);
        canvas->add(sc);

        std::stringstream ss;
        ss << "GPU Double Contact, right, fam = " << lfam;
        scroll->add(ss.str().c_str(), canvas, sc);
    }

    }
    // GPU Double Contact

    return Fl::run();
}

