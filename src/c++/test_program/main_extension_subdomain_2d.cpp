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
#include "Extension_Curve.h"
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
// TEST

// Input here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas     *canvas = (Canvas*)0;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves;

Fl_Double_Window *subdomainwin;
    Fl_Round_Button *use_subdomain;
    Fl_Round_Button *dont_use_subdomain;
    Fl_Round_Button *use_domain;

Fl_Double_Window *actionwin;
    Fl_Round_Button *set_polygon;
    Fl_Round_Button *compute_extension;

Three_Phase_Boundary *Boundary = (Three_Phase_Boundary*)0;

// Flux objects
StonePermParams   *stonepermparams = (StonePermParams*)0;
StoneParams       *stoneparams     = (StoneParams*)0;
StoneFluxFunction *stoneflux       = (StoneFluxFunction*)0;
StoneAccumulation *stoneaccum      = (StoneAccumulation*)0;

GridValues *grid;

std::vector<RealVector> polygon;

Curve2D *convex_hull_curve = 0;

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

    c->getxy(initial.component(0), initial.component(1));

    // Adquire the polygon
    if (set_polygon->value() == 1){
        polygon.push_back(initial);

        if (polygon.size() > 2){
            std::vector<RealVector> convex_hull_points;

            convex_hull(polygon, convex_hull_points);

            if (convex_hull_curve != 0) canvas->erase(convex_hull_curve);

            convex_hull_curve = new Curve2D(convex_hull_points, 0.0, 0.0, 1.0, CURVE2D_MARKERS /*| CURVE2D_INDICES*/ | CURVE2D_SOLID_LINE);
            canvas->add(convex_hull_curve);
        }

        return;
    }

    initial(0) = 0.514449;
    initial(1) = 0.196842;

    std::vector<RealVector> rarcurve, inflection_points;

    int increase = RAREFACTION_SPEED_INCREASE;
    int family = 0;

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

        Curve2D *test = new Curve2D(rarcurve, 255.0/255.0, 0.0/255.0, 0.0/255.0, lambda/*, CURVE2D_MARKERS | CURVE2D_SOLID_LINE  | CURVE2D_INDICES*/);
        c->add(test);

        std::stringstream buf;
        buf << "Rarefaction. Init. = " << initial << ", size = " << rarcurve.size();
        scroll->add(buf.str().c_str(), c, test);

        //for (int i = 0; i < rarcurve.size(); i++) std::cout << "Rarefaction[" << i << "] = " << rarcurve[i] << std::endl;

        // Strip the rarefaction points of their last component (lambda)
        for (int i = 0; i < rarcurve.size(); i++) rarcurve[i].resize(2);

        Extension_Curve ec;
        std::vector<RealVector> extension_on_curve, extension_on_domain;

        if (use_domain->value() == 1){
//            ec.curve(stoneflux, stoneaccum, 
//                     *grid, CHARACTERISTIC_ON_DOMAIN,
//                     true /*bool is_singular*/, family,
//                     rarcurve,
//                     extension_on_curve,
//                     extension_on_domain);

            ec.extension_of_curve(stoneflux, stoneaccum,stoneflux, stoneaccum,  
                     *grid, CHARACTERISTIC_ON_DOMAIN,
                     true /*bool is_singular*/, family,
                     rarcurve,
                     extension_on_curve,
                     extension_on_domain);

        }
        else if (use_subdomain->value() == 1) {
            ec.curve_in_subdomain(stoneflux, stoneaccum,
                     *grid, polygon, CHARACTERISTIC_ON_DOMAIN,
                     true /*bool is_singular*/, family,
                     rarcurve,
                     extension_on_curve,
                     extension_on_domain);
        }
        else {
            ec.curve_out_of_subdomain(stoneflux, stoneaccum,
                     *grid, polygon, CHARACTERISTIC_ON_DOMAIN,
                     true /*bool is_singular*/, family,
                     rarcurve,
                     extension_on_curve,
                     extension_on_domain);
        }

        if (extension_on_domain.size() > 0){
            for (int i = 0; i < extension_on_domain.size(); i++) std::cout << "Ext. " << i << " = " << extension_on_domain[i] << std::endl;

            SegmentedCurve *sc = new SegmentedCurve(extension_on_domain, 0.0, 1.0, 0.0);
            canvas->add(sc);

            std::stringstream extbuf;
            extbuf << "Rarefaction extension, size = " << extension_on_domain.size();
            scroll->add(extbuf.str().c_str(), c, sc);
        } 
        else std::cout << "No extension found!" << std::endl;

    }

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

    double grw = 1.5; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    double vel = 0.0; // 0.0

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

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    //scrollwin->callback(no_close_cb);
    scrollwin->set_non_modal();

    subdomainwin = new Fl_Double_Window(scrollwin->x() + scrollwin->w() + 10, scrollwin->y(), 500, 120, "Subdomain");
    {
        use_subdomain = new Fl_Round_Button(10, 10, subdomainwin->w() - 20, (subdomainwin->h() - 40)/3, "Use only subdomain");
        dont_use_subdomain = new Fl_Round_Button(use_subdomain->x(), use_subdomain->y() + use_subdomain->h() + 10, use_subdomain->w(), use_subdomain->h(), "Don\'t use subdomain");
        use_domain    = new Fl_Round_Button(dont_use_subdomain->x(), dont_use_subdomain->y() + dont_use_subdomain->h() + 10, dont_use_subdomain->w(), dont_use_subdomain->h(), "Use domain");

        use_subdomain->value(1);

        use_subdomain->type(FL_RADIO_BUTTON);
        dont_use_subdomain->type(FL_RADIO_BUTTON);
        use_domain->type(FL_RADIO_BUTTON);
    }
    subdomainwin->end();

    actionwin = new Fl_Double_Window(subdomainwin->x() + subdomainwin->w() + 10, subdomainwin->y(), 500, 80, "Action");
    {
        set_polygon = new Fl_Round_Button(10, 10, actionwin->w() - 20, (actionwin->h() - 30)/2, "Set polygon");
        compute_extension    = new Fl_Round_Button(set_polygon->x(), set_polygon->y() + set_polygon->h() + 10, set_polygon->w(), set_polygon->h(), "Compute extension");

        set_polygon->value(1);

        set_polygon->type(FL_RADIO_BUTTON);
        compute_extension->type(FL_RADIO_BUTTON);
    }
    actionwin->end();

    Fl_Round_Button *set_polygon;
    Fl_Round_Button *compute_extension;

    canvaswin->show();

    scrollwin->show();

    subdomainwin->show();

    actionwin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

