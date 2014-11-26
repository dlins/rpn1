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
    Canvas       *canvas = (Canvas*)0;


int main(){
    // ************************* Physics ************************* //
    IonRatios ratio;
    IonPermeability permeability;
    flux = new IonFlux(&ratio, &permeability);

    IonAdsorption adsorption;
    accum = new IonAccumulation(&adsorption);
    // ************************* Physics ************************* //


    // Window
    int main_w  = 900;
    int main_h  = 900;

    canvaswin = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Ion");
    {
        double mirror[9] = {-1.0, 0.0, 0.0, 
                             0.0, 1.0, 0.0,
                             0.0, 0.0, 1.0};

        canvas = new Canvas(10, 10, main_w - 20, main_h - 20);
        canvas->xlabel("u");
        canvas->ylabel("Ion Flux, component 0");
    }
    canvaswin->end();
    canvaswin->resizable(canvaswin);

    // ************************* Draw Boundaries ************************* //
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

    canvas->add(b);
    canvas->nozoom();
    // ************************* Draw Boundaries ************************* //

    canvaswin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

