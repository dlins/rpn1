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
#include "Hysteresis.h"

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
    Fl_Group *family;
        Fl_Round_Button *fam0;
        Fl_Round_Button *fam1;

    Fl_Group *singular;
        Fl_Round_Button *singular0;
        Fl_Round_Button *singular1;

    Fl_Button *go;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;

// Output here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas       *canvas_tpcw = (Canvas*)0;
    Canvas      *canvas_vapor = (Canvas*)0;
    Canvas     *canvas_liquid = (Canvas*)0;

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

RealVector ref(3);

void go_cb(Fl_Widget*, void*){
    int fam = (fam0->value() == 1) ? 0 : 1;

    bool sng = (singular0->value() == 1) ? false : true;

    // Compute the Hysteresis curve.
    std::vector<Canvas*>               canvas(3);
    std::vector<FluxFunction*>         flux(3);
    std::vector<AccumulationFunction*> accum(3);
    std::vector<GridValues*>           grid(3);
    std::vector<std::string>           domain_name(3);

    canvas[0] = canvas_vapor; 
    canvas[1] = canvas_tpcw;
    canvas[2] = canvas_liquid;

    flux[0] = vapor_flux; 
    flux[1] = tpcw_flux;
    flux[2] = liquid_flux;

    accum[0] = vapor_accum; 
    accum[1] = tpcw_accum;
    accum[2] = liquid_accum;

    grid[0] = vapor_grid; 
    grid[1] = tpcw_grid;
    grid[2] = liquid_grid;

    domain_name[0] = std::string("Vapor"); 
    domain_name[1] = std::string("TPCW"); 
    domain_name[2] = std::string("Liquid"); 

    for (int i = 0; i < flux.size(); i++){
        std::vector<RealVector> curve_segments, domain_segments;

        Hysteresis::curve(flux[i],
                          accum[i],
                          *(grid[i]),
                          CHARACTERISTIC_ON_CURVE,
                          fam,
                          fam,
                          tpcw_flux,
                          tpcw_accum,
                          sng,
                          curve_segments,
                          domain_segments);

        std::cout << "Hysteresis over " << domain_name[i] << " completed. curve_segments.size() = " << curve_segments.size() << std::endl;

        if (curve_segments.size() > 2){
            SegmentedCurve *curve2d = new SegmentedCurve(curve_segments, 0.0, 0.0, 1.0);
            canvas[i]->add(curve2d);

            char buf[100];
            sprintf(buf, "Inflection over %s. Size = %d, fam = %d.", domain_name[i].c_str(), curve_segments.size()/2, fam);
            scroll->add(buf, canvas[i], curve2d);    
        }

        if (domain_segments.size() > 2){
            SegmentedCurve *curve2d = new SegmentedCurve(domain_segments, 1.0, 0.0, 0.0);
            canvas[i]->add(curve2d);

            char buf[100];
            sprintf(buf, "Hysteresis over %s. Size = %d, fam = %d.", domain_name[i].c_str(), domain_segments.size()/2, fam);
            scroll->add(buf, canvas[i], curve2d);    
        }
    }

    return;
}

void Hugoniot_cb(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector ref(3);
    c->getxy(ref.component(0), ref.component(1));
    ref.component(2) = 1.0;

    FluxFunction         *fref;
    AccumulationFunction *aref;
    std::string where_ref;

    if (c == canvas_tpcw){
        fref = tpcw_flux;
        aref = tpcw_accum;
        where_ref = std::string("TPCW");
    }
    else if (c == canvas_vapor){
        fref = vapor_flux;
        aref = vapor_accum;
        where_ref = std::string("Vapor");
    }
    else {
        fref = liquid_flux;
        aref = liquid_accum;
        where_ref = std::string("Liquid");
    }

    ReferencePoint referencepoint(ref, fref, aref, 0);

    Hugoniot_TP htp;

    std::vector<FluxFunction*> flux(3);
    std::vector<AccumulationFunction*> accum(3);
    std::vector<Canvas*> canvas(3);
    std::vector<GridValues*> grid(3);
    std::vector<std::string> name(3);

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

    name[0] = std::string("Vapor");
    name[1] = std::string("TPCW");
    name[2] = std::string("Liquid");

    for (int k = 0; k < flux.size(); k++){
        std::vector<RealVector> hugoniot_curve;
 
        htp.curve(flux[k], accum[k], 
                  *(grid[k]), 
                  referencepoint, 
                  hugoniot_curve);

        std::cout << "Hugoniot " << k << " has " << hugoniot_curve.size()/2 << " points." << std::endl;

        if (hugoniot_curve.size() > 2){
            std::vector<Point2D> p;
            for (int i = 0; i < hugoniot_curve.size()/2; i++){
                p.push_back(Point2D(hugoniot_curve[2*i].component(0),     hugoniot_curve[2*i].component(1)));
                p.push_back(Point2D(hugoniot_curve[2*i + 1].component(0), hugoniot_curve[2*i + 1].component(1)));
            }

            SegmentedCurve *curve2d = new SegmentedCurve(p, 0, 0, 1);

            // Classified curve
            ColorCurve cc(*(flux[k]), *(accum[k]));    
            std::vector<HugoniotPolyLine> classified_curve;
            std::vector<RealVector> transition_list;

            cc.classify_segmented_curve(hugoniot_curve, referencepoint,
                                        classified_curve,
                                        transition_list);

            // Color
            MultiColoredCurve *mcc = new MultiColoredCurve(classified_curve);
            canvas[k]->add(mcc);

            char buf[100];
            sprintf(buf, "Hugoniot over %s with reference on %s. Size = %d.", name[k].c_str(), where_ref.c_str(), hugoniot_curve.size()/2);
            scroll->add(buf, canvas[k], mcc);
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
//    ReferencePoint::print();

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
    int main_w  = Fl::w();
    int main_h  = Fl::h();

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Hugoniot between physics");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        // vapor to the left
        canvas_vapor = new Canvas(10, 10, (main_w - 20)/3, main_h - 20);
        canvas_vapor->xlabel("yw");
        canvas_vapor->ylabel("Theta");
        canvas_vapor->setextfunc(&Hugoniot_cb, canvas_vapor, 0);

        // TPCW at the middle
        canvas_tpcw = new Canvas(canvas_vapor->x() + canvas_vapor->w(), canvas_vapor->y(), canvas_vapor->w(), canvas_vapor->h());
        canvas_tpcw->xlabel("s");
        canvas_tpcw->ylabel("Theta");
        canvas_tpcw->set_transform_matrix(mirror);
        canvas_tpcw->setextfunc(&Hugoniot_cb, canvas_tpcw, 0);

        // liquid to the right, mirrored.
        canvas_liquid = new Canvas(canvas_tpcw->x() + canvas_tpcw->w(), canvas_vapor->y(), canvas_vapor->w(), canvas_vapor->h());
        canvas_liquid->xlabel("xc");
        canvas_liquid->ylabel("Theta");
        canvas_liquid->set_transform_matrix(mirror);
        canvas_liquid->setextfunc(&Hugoniot_cb, canvas_liquid, 0);
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

    // List of curves
    scrollwin = new Fl_Double_Window(500, 500, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30, "Hysteresis");
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    scrollwin->callback(no_close_cb);
    scrollwin->set_non_modal();

    win = new Fl_Double_Window(10, 10, 300, 500, "Hysteresis");
    {
        family = new Fl_Group(10, 10, win->w() - 20, 10 + 25 + 10 + 25 + 10);
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

        singular = new Fl_Group(family->x(), family->y() + family->h() + 10, win->w() - 20, 10 + 25 + 10 + 25 + 10);
        {
            singular0 = new Fl_Round_Button(singular->x() + 10, singular->y() + 10, singular->w() - 20, 25, "Not singular");
            singular0->type(FL_RADIO_BUTTON);

            singular1 = new Fl_Round_Button(singular0->x(), 
                                            singular0->y() + singular0->h() + 10, 
                                            singular0->w(), 
                                            singular0->h(), 
                                            "Singular");
            singular1->value(1);
            singular1->type(FL_RADIO_BUTTON);
        }
        singular->end();
        singular->box(FL_FRAME);


        go = new Fl_Button(10, singular->y() + singular->h() + 10, win->w() - 20, win->h() - (singular->y() + singular->h() + 10 + 10), "Go!");
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

