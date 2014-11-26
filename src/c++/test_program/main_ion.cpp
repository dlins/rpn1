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
#include "Hugoniot_Curve.h"
#include "RectBoundary.h"

#include "ReferencePoint.h"
#include "ViscosityJetMatrix.h"
#include "ColorCurve.h"

// TEST
#include "HugoniotContinuation2D2D.h"
#include <string>
// TEST

// Vitor's functions here:
#include "IonFlux.h" // Flux
#include "IonAccumulation.h" // Accum

IonFlux *flux;
IonAccumulation *accum;
GridValues *grid;
RectBoundary *boundary;

// Input here...
Fl_Double_Window *canvaswin = (Fl_Double_Window*)0;
    Canvas       *canvas           = (Canvas*)0;
    Canvas       *flux_test_canvas = (Canvas*)0;

Fl_Double_Window *scrollwin;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_button;

// Callbacks ahead!
//
void no_close_cb(Fl_Widget*, void*){
    return;
}

void close_cb(Fl_Widget*, void*){
    ContourMethod::deallocate_arrays();

    exit(0);

    return;
}

void Hugoniot_cb(Fl_Widget *w, void*){
    Canvas *c = (Canvas*)w;

    RealVector ref(2);

    c->getxy(ref.component(0), ref.component(1));

    std::cout << "Reference point = " << ref << std::endl; 

    // Reference point
    //Viscosity_Matrix v;
    ReferencePoint referencepoint(ref, flux, accum, 0);

    // Hugoniot (search)

    std::vector<HugoniotPolyLine> hugoniot_curve;
    std::vector<bool>             circular;

    Hugoniot_Curve htp; 
    htp.classified_curve(flux, accum, *grid, referencepoint, 
                         hugoniot_curve,
                         circular);

    std::cout << "hugoniot_curve.size() = " << hugoniot_curve.size() << std::endl;

    if (hugoniot_curve.size() > 0){
        MultiColoredCurve *mcc = new MultiColoredCurve(hugoniot_curve);
        canvas->add(mcc);

        std::stringstream s;
        s << "Hugoniot (search). Ref. = " << referencepoint.point << ". Size = " << hugoniot_curve.size();

        scroll->add(s.str().c_str(), canvas, mcc);

        // Add the test Dan suggested. 
        for (int i = 0; i < hugoniot_curve.size(); i++){
            for (int j = 0; j < hugoniot_curve[i].point.size(); j++){
                WaveState w(2);
                w(0) = hugoniot_curve[i].point[j](0);
                w(1) = 0.0;

                JetMatrix m(2);

                flux->jet(w, m, 0);

                hugoniot_curve[i].point[j](1) = m.get(0);
            }
        }

        mcc = new MultiColoredCurve(hugoniot_curve);
        flux_test_canvas->add(mcc);
        flux_test_canvas->axis(0.0, 1.0, 0.0, 1.0);

        scroll->add("Test", flux_test_canvas, mcc);

        // Add the test Dan suggested.
    }

    // Hugoniot (search)

//    // Hugoniot (continuation)

//    HugoniotContinuation2D2D hug(flux, accum, boundary);
//    hug.set_reference_point(referencepoint);

//    std::vector< std::vector<RealVector> > curve;
//    int info = hug.curve(curve);
//    
//    std::cout << "All done. Info = " << info << std::endl;

//    for (int i = 0; i < curve.size(); i++){
//        if (curve[i].size() > 0){
//            Curve2D *shock_test = new Curve2D(curve[i], 255.0/255.0, 0.0/255.0, 0.0/255.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE /* | CURVE2D_INDICES*/);
//            canvas->add(shock_test);

//            std::stringstream s;
//            s << "Hugoniot (cont.). Ref. = " << referencepoint.point << ". Size = " << curve[i].size();

//            scroll->add(s.str().c_str(), canvas, shock_test);
//        }
//    }

//    // Hugoniot (continuation)

    return;
}

void clear_buttoncb(Fl_Widget*, void*){
    scroll->clear_all_graphics();

    return;
}

int main(){
    // ************************* Physics ************************* //
    IonRatios ratio;
    IonPermeability permeability;
    flux = new IonFlux(&ratio, &permeability);

    IonAdsorption adsorption;
    accum = new IonAccumulation(&adsorption);
    // ************************* Physics ************************* //


    // ************************* Boundaries ************************* //

    RealVector pmin(2), pmax(2);

    pmin(0) = 0.0;
    pmin(1) = 0.0;

    pmax(0) = 1.0;
    pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 128;

    boundary = new RectBoundary(pmin, pmax);

    // ************************* GridValues ************************* //
    grid = new GridValues(boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //

    // Window
    int main_w  = 1800;
    int main_h  = 900;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Ion");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        canvas = new Canvas(10, 10, (main_w - 30)/2, main_h - 20);
        canvas->xlabel("u");
        canvas->ylabel("v");
        canvas->setextfunc(&Hugoniot_cb, canvas, 0);

//        canvas->set_transform_matrix(mirror);

        flux_test_canvas = new Canvas(canvas->x() + canvas->w() + 10, canvas->y(), canvas->w(), canvas->h());
        flux_test_canvas->xlabel("u");
        flux_test_canvas->ylabel("Flux and other stuff");
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

    // ************************* Draw Boundaries ************************* //
    std::vector<RealVector> edges;
    std::vector<Point2D> edges2d;
    Curve2D *b;

    edges2d.push_back(Point2D(pmin.component(0), pmin.component(1)));
    edges2d.push_back(Point2D(pmax.component(0), pmin.component(1)));
    edges2d.push_back(Point2D(pmax.component(0), pmax.component(1)));
    edges2d.push_back(Point2D(pmin.component(0), pmax.component(1)));
    edges2d.push_back(edges2d[0]);
    b = new Curve2D(edges2d, 0, 0, 0, CURVE2D_SOLID_LINE);

    canvas->add(b);
    canvas->nozoom();
    // ************************* Draw Boundaries ************************* //

    // List of curves
    scrollwin = new Fl_Double_Window(canvaswin->x() + canvaswin->w() + 10, canvaswin->y(), 500, 500, "Curves");
    {
        scroll = new CanvasMenuScroll(10, 20, scrollwin->w() - 20, scrollwin->h() - 30 - 20 - 25, "Curves");
        clear_button = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, scroll->w(), 25, "Clear all curves");
        clear_button->callback(clear_buttoncb);
    }
    scrollwin->end();
    scrollwin->resizable(scrollwin);
    //scrollwin->callback(no_close_cb);
    scrollwin->set_non_modal();

    // Add the flux(u) to flux_test_canvas
    {
        int n = 1000;

        double umin = 0.0;
        double umax = 1.0;

        double delta = (umax - umin)/(double)(n - 1);

        std::vector<Point2D> edges2d;

        for (int i = 0; i < n; i++){
            double u = umin + (double)i*delta;

            WaveState w(2);
            w(0) = u;
            w(1) = 0.0;

            JetMatrix m(2);

            flux->jet(w, m, 0);

            double flux_u = m.get(0);

            edges2d.push_back(Point2D(u, flux_u));
        }

        Curve2D *b;

        b = new Curve2D(edges2d, 0, 0, 0, CURVE2D_SOLID_LINE);

        flux_test_canvas->add(b);
        flux_test_canvas->nozoom();
        scroll->add("Flux", flux_test_canvas, b);
    }
    // Add the flux(u) to flux_test_canvas

    canvaswin->show();

    scrollwin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}


