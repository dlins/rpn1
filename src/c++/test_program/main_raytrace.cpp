#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"

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
#include "WaveCurvePlot.h"
#include "GridValuesPlot.h"

// Input here...
Fl_Double_Window   *canvaswin = (Fl_Double_Window*)0;
    Canvas     *canvas = (Canvas*)0;

// Test
Fl_Double_Window *output;
    Fl_Box       *ob = (Fl_Box*)0;

// Flux objects
StonePermParams   *stonepermparams = (StonePermParams*)0;
StoneParams       *stoneparams     = (StoneParams*)0;
StoneFluxFunction *stoneflux       = (StoneFluxFunction*)0;
StoneAccumulation *stoneaccum      = (StoneAccumulation*)0;

FluxFunction *flux;
AccumulationFunction *accum;
Boundary *boundary;

GridValues *grid;

int step;
RealVector p0, p1;

void select(Fl_Widget *w, void*){
    RealVector *p;

    if (step == 0) p = &p0;
    else           p = &p1;

    canvas->getxy((*p)(0), (*p)(1));

    Curve2D *c = new Curve2D(*p, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
    canvas->add(c);

    if (p == &p1){
        std::vector<RealVector> vr;
        vr.push_back(p0);
        vr.push_back(p1);
        Curve2D *cc = new Curve2D(vr, 0.0, 0.0, 0.0);
        canvas->add(cc);

        Curve curve;
        curve.disable_intersecting_cells(p0, p1, grid);

        GridValuesPlot *gvp = new GridValuesPlot(grid);
        canvas->add(gvp);
       
    }

    step++;
    if (step == 2) step = 0;

    return;
}

void on_move_cell(Fl_Widget*, void*){
    RealVector point(2);
    canvas->getxy(point(0), point(1));

    int i, j;
    grid->cell(point, i, j);

    std::stringstream ss;
    ss << i << ", " << j << ", enabled = " << grid->cell_type(i, j);


    ob->copy_label(ss.str().c_str());
    output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
    output->show();

    return;
}

void wincb(Fl_Widget *w, void*){
    if ((Fl_Double_Window*)w == canvaswin) exit(0);

    return;
}

int main(){
    // Test seg-seg intersection.
    {
        RealVector p0(2), p1(2), q0(2), q1(2);
        p0(0) = -1.0;
        p0(1) = -1.0;

        p1 = -p0;

        q0(0) =  0.5;
        q0(1) = -1.0;

        q1(0) =  0.5;
        q1(1) =  1.0;

        RealVector r;
        double alpha, beta;
        bool test = segment_segment_intersection(p0, p1, q0, q1, r, alpha, beta);

        std::cout << "p0 = " << p0 << ", p1 = " << p1 << std::endl;
        std::cout << "q0 = " << q0 << ", q1 = " << q1 << std::endl;
        std::cout << "    found = " << test << ", r = " << r << ", alpha = " << alpha << ", beta = " << beta << std::endl;
    }

    step = 0;
    p0.resize(2);
    p1.resize(2);

    // Output
    output = new Fl_Double_Window(10, 10, 300, 50, "Info");
    {
        ob = new Fl_Box(0, 0, output->w(), output->h(), "Info");
        ob->labelfont(FL_COURIER);
        ob->box(FL_THIN_UP_BOX);
    }
    output->end();
    output->clear_border();

    // Create fluxFunction
    double expw, expg, expo; expw = expg = 2.0; expo = 2.0;
    double expow, expog;     expow = expog = 2.0;
    double cnw, cng, cno;    cnw = cng = cno = 0.0;
    double lw, lg;           lw = lg = 0.0;
    double low, log;         low = log = 0.0;
    double epsl = 0.0;

    StonePermParams *stonepermparams  = new StonePermParams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);

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

    flux = stoneflux;
    accum = stoneaccum;
    boundary = new Three_Phase_Boundary();
    std::cout << boundary->max_distance() << std::endl;

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 10;

    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //
    
    // Window
    int main_w  = 900;
    int main_h  = main_w;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Liu\'s wavecurve");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        canvas = new Canvas(0, 0, canvaswin->w(), canvaswin->h());
        canvas->xlabel("sw");
        canvas->ylabel("so");
        canvas->number_of_horizontal_ticks(5);
        canvas->number_of_vertical_ticks(5);

        canvas->setextfunc(&select, canvas, 0);
        canvas->on_move(&on_move_cell, canvas, 0);

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);
    canvaswin->callback(wincb);
    canvaswin->show();


    // ************************* Draw Boundaries ************************* //
    std::vector<RealVector> side;
    boundary->physical_boundary(side);
    side.push_back(side[0]);

    Curve2D side_curve(side, 0, 0, 0, CURVE2D_SOLID_LINE);
    canvas->add(&side_curve);
    canvas->nozoom();

    // Draw the cells
    for (int i = 0; i < grid->cell_type.rows(); i++){
        std::vector<RealVector> v;

        RealVector p(2);

        p(0) = 0.0;
        p(1) = grid->grid_resolution(0)*(double)i;
        v.push_back(p);

        p(0) = 1.0;
        v.push_back(p);

        Curve2D *c = new Curve2D(v, 0.0, 0.0, 0.0);

        canvas->add(c);
    }

    for (int j = 0; j < grid->cell_type.cols(); j++){
        std::vector<RealVector> v;

        RealVector p(2);

        p(0) = grid->grid_resolution(1)*(double)j;
        p(1) = 0.0;
        v.push_back(p);

        p(1) = 1.0;
        v.push_back(p);

        Curve2D *c = new Curve2D(v, 0.0, 0.0, 0.0);

        canvas->add(c);
    }

    {
        GridValuesPlot *gvp = new GridValuesPlot(grid);
        canvas->add(gvp);
//        scroll->add("Disabled cells", canvas, gvp);
    }

    // Draw the cells

    

    return Fl::run();
}

