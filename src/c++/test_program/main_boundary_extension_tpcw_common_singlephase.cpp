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

#include "Rarefaction.h"
#include "Thermodynamics.h"
#include "Hugoniot_TP.h"
#include "RectBoundary.h"
#include "SinglePhaseBoundary.h"

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

// Input here...
Fl_Double_Window *win;
    Fl_Group *characteristic_where;
        Fl_Round_Button *characteristic_on_domain;
        Fl_Round_Button *characteristic_on_curve;

    Fl_Group *family;
        Fl_Round_Button *fam0;
        Fl_Round_Button *fam1;

    Fl_Group *border;
        Fl_Round_Button *s0;
        Fl_Round_Button *s1;

    Fl_Group *domain;
        Fl_Round_Button *vapor;
        Fl_Round_Button *liquid;

    Fl_Button *go;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;

// Output here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas       *canvas_tpcw = (Canvas*)0;

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
FluxFunction *fref;
AccumulationFunction *aref;

void go_cb(Fl_Widget*, void*){
    int fam = (fam0->value() == 1) ? 0 : 1;

    int characteristic = (characteristic_on_domain->value() == 1) ? CHARACTERISTIC_ON_DOMAIN : CHARACTERISTIC_ON_CURVE;

    int where_constant[2];
    FluxFunction* flux[2];
    AccumulationFunction* accum[2];
    Boundary* boundary[2];

    if (s0->value() == 1){
        where_constant[0] = RECT_BOUNDARY_SG_ZERO;
        flux[1] = liquid_flux;
        accum[1] = liquid_accum;
        boundary[1] = liquid_boundary;
    }
    else {
        where_constant[0] = RECT_BOUNDARY_SG_ONE;
        flux[1] = vapor_flux;
        accum[1] = vapor_accum;
        boundary[1] = vapor_boundary;
    }

    boundary[0] = tpcw_boundary;
    flux[0] = tpcw_flux;
    accum[0] = tpcw_accum;
    where_constant[1] = DOMAIN_EDGE_UPPER_COMPOSITION;

    double r[2] = {1.0, 0.0};
    double g[2] = {0.0, 0.0};
    double b[2] = {0.0, 1.0};

    for (int i = 0; i < 2; i++){
        std::vector<RealVector> c, d;

        boundary[i]->extension_curve(tpcw_flux, tpcw_accum,
                                     flux[i], accum[i],
                                     *tpcw_grid,
                                     where_constant[i], 256, false /*bool singular*/,
                                     fam, characteristic,
                                     c, d);

        std::cout << "c.size() = " << c.size() << ", d.size() = " << d.size() << std::endl;

        if (d.size() > 2){
            std::vector<Point2D> p;
            for (int j = 0; j < d.size()/2; j++){
                p.push_back(Point2D(d[2*j].component(0),     d[2*j].component(1)));
                p.push_back(Point2D(d[2*j + 1].component(0), d[2*j + 1].component(1)));
            }

            SegmentedCurve *curve2d = new SegmentedCurve(p, r[i], g[i], b[i]);
            canvas_tpcw->add(curve2d);

            char buf[100];
            sprintf(buf, "On domain. Size = %d, char. = %d, fam = %d, s = %d.", d.size()/2, characteristic, fam, where_constant);
            scroll->add(buf, canvas_tpcw, curve2d);
        }
    }

    return;
}

void no_close_cb(Fl_Widget*, void*){
    return;
}

void close_cb(Fl_Widget*, void*){
    Contour2p5_Method::deallocate_arrays();

    exit(0);

    return;
}

int main(){
    // Thermo
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

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
    fpp.component(5) = 0.0;
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
    number_of_cells[0] = number_of_cells[1] = 256;

    tpcw_boundary = new RectBoundary(pmin, pmax);

    // Vapor
    vapor_boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_VAPOR, &Thermodynamics::Theta2T);

    // Liquid
    liquid_boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_LIQUID, &Thermodynamics::Theta2T);
    // ************************* Boundaries ************************* //

    // ************************* GridValues ************************* //
    tpcw_grid = new GridValues(tpcw_boundary, pmin, pmax, number_of_cells);

    RealVector vapor_max(pmax);
//    vapor_max.component(0) = vapor_boundary->maximums().component(0); // Fix later
//    vapor_grid = new GridValues(vapor_boundary, pmin, vapor_max, number_of_cells);

    RealVector liquid_max(pmax);
//    liquid_max.component(0) = liquid_boundary->maximums().component(0); // Fix later
//    liquid_grid = new GridValues(liquid_boundary, pmin, liquid_max, number_of_cells);
    // ************************* GridValues ************************* //
    // Window
    int main_w  = .9*Fl::w();
    int main_h  = .9*Fl::h();

    canvaswin = new Fl_Double_Window(min(main_w, main_h), min(main_w, main_h), "SinglePhases\' Boundary Extension on TPCW");
    {
        // TPCW at the middle
        canvas_tpcw = new Canvas(10, 10, canvaswin->w() - 20, canvaswin->h() - 20);
        canvas_tpcw->xlabel("s");
        canvas_tpcw->ylabel("Theta");
//        canvas_tpcw->setextfunc(&clickcb, canvas_tpcw, 0);

    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(no_close_cb);

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
    // ************************* Draw Boundaries ************************* //


    
    // List of curves
    scrollwin = new Fl_Double_Window(500, 500, "Extensions");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30, "Extensions");
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(no_close_cb);
    scrollwin->set_non_modal();

    win = new Fl_Double_Window(10, 10, 300, 500, "TPCW\'s Boundary Extension");
    {
        characteristic_where = new Fl_Group(10, 10, win->w() - 20, 10 + 25 + 10 + 25 + 10);
        {
            characteristic_on_domain = new Fl_Round_Button(characteristic_where->x() + 10, characteristic_where->y() + 10, characteristic_where->w() - 20, 25, "Characteristic on domain");
            characteristic_on_domain->value(1);
            characteristic_on_domain->type(FL_RADIO_BUTTON);

            characteristic_on_curve = new Fl_Round_Button(characteristic_on_domain->x(), 
                                                          characteristic_on_domain->y() + characteristic_on_domain->h() + 10, 
                                                          characteristic_on_domain->w(), 
                                                          characteristic_on_domain->h(), 
                                                          "Characteristic on curve");
            characteristic_on_curve->type(FL_RADIO_BUTTON);
        }
        characteristic_where->end();
        characteristic_where->box(FL_FRAME);

        family = new Fl_Group(characteristic_where->x(), characteristic_where->y() + characteristic_where->h() + 10, win->w() - 20, 10 + 25 + 10 + 25 + 10);
        {
            fam0 = new Fl_Round_Button(family->x() + 10, family->y() + 10, family->w() - 20, 25, "Family 0");
            fam0->value(1);
            fam0->type(FL_RADIO_BUTTON);
            fam1 = new Fl_Round_Button(fam0->x(), 
                                       fam0->y() + fam0->h() + 10, 
                                       fam0->w(), 
                                       fam0->h(), 
                                       "Family 1");
            fam1->type(FL_RADIO_BUTTON);
        }
        family->end();
        family->box(FL_FRAME);

        border = new Fl_Group(family->x(), family->y() + family->h() + 10, win->w() - 20, 10 + 25 + 10 + 25 + 10);
        {
            s0 = new Fl_Round_Button(border->x() + 10, border->y() + 10, border->w() - 20, 25, "s = 0 (Upper Liquid)");
            s0->value(1);
            s0->type(FL_RADIO_BUTTON);

            s1 = new Fl_Round_Button(s0->x(), 
                                     s0->y() + s0->h() + 10, 
                                     s0->w(), 
                                     s0->h(), 
                                     "s = 1 (Upper Vapor)");
            s1->type(FL_RADIO_BUTTON);
        }
        border->end();
        border->box(FL_FRAME);

//        domain = new Fl_Group(border->x(), border->y() + border->h() + 10, win->w() - 20, 10 + 25 + 10 + 25 + 10);
//        {
//            vapor = new Fl_Round_Button(domain->x() + 10, domain->y() + 10, domain->w() - 20, 25, "Lower edge of Vapor");
//            vapor->value(1);
//            vapor->type(FL_RADIO_BUTTON);

//            liquid = new Fl_Round_Button(vapor->x(), 
//                                         vapor->y() + s0->h() + 10, 
//                                         vapor->w(), 
//                                         vapor->h(), 
//                                         "Lower edge of Liquid");
//            liquid->type(FL_RADIO_BUTTON);
//        }
//        domain->end();
//        domain->box(FL_FRAME);

        go = new Fl_Button(10, border->y() + border->h() + 10, win->w() - 20, win->h() - (border->y() + border->h() + 10 + 10), "Go!");
        go->callback(go_cb);

    }
    win->end();
    win->callback(close_cb);

    canvaswin->position(win->x() + win->w(), win->y());
    scrollwin->position(canvaswin->x() + canvaswin->w(), canvaswin->y());

    win->set_non_modal();

    canvaswin->show();
    scrollwin->show();
    win->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

