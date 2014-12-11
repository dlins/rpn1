#include <string>
#include <fstream>

#include <FL/Fl_Double_Window.H>
#include "canvas.h"
#include "segmentedcurve.h"

#include "Double_Contact.h"
#include "CoreyQuad.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"
#include "Extension_Curve.h"
#include "Utilities.h"

Fl_Double_Window *win;
    Canvas *canvas;

int main(int argc, char *argv[]){
    // Boundary.
    //
    Three_Phase_Boundary boundary;

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 128;

    GridValues grid(&boundary, pmin, pmax, number_of_cells);
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
    CoreyQuad flux(params);

    // Trivial accumulation
    StoneAccumulation accum;

    // Window
    win = new Fl_Double_Window(10, 10, 800, 800, "Extension of the so = 0 side on Corey");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());

        double m[9] = {1.0, .5, 0.0, 0.0, sqrt(3)/2, 0.0, 0.0, 0.0, 1.0};
        canvas->set_transform_matrix(m);
        canvas->xlabel("sw");
        canvas->ylabel("so");

        RealVector A(2), B(2), C(2);
        A(0) = 0.0;
        A(1) = 0.0;

        B(0) = 1.0;
        B(1) = 0.0;

        C(0) = 0.0;
        C(1) = 1.0;

        std::vector<RealVector> triangle;
        triangle.push_back(A);
        triangle.push_back(B);
        triangle.push_back(C);
        triangle.push_back(A);

        Curve2D *triangle_curve = new Curve2D(triangle, 0.0, 0.0, 0.0);
        canvas->add(triangle_curve);
    }
    win->end();

    win->resizable(win);
    win->show();

    // Boundary extension.
    //
    {
        Curve curve;
        RealVector p(2), q(2);
        p(0) = 0.0;
        p(1) = 0.0;

        q(0) = 1.0;
        q(1) = 0.0;
        
        Utilities::regularly_sampled_segment(p, q, 50, curve);

        std::vector<RealVector> extension_on_curve;
        std::vector<RealVector> extension_on_domain;

        int where_is_characteristic = CHARACTERISTIC_ON_DOMAIN;
        bool is_singular = false;
        int fam = 0;

        Extension_Curve ec;
        ec.curve(&flux, &accum, 
                 grid, where_is_characteristic,
                 is_singular, fam,
                 curve.curve,
                 extension_on_curve,
                 extension_on_domain);

        if (extension_on_domain.size() > 0){
            SegmentedCurve *sc = new SegmentedCurve(extension_on_domain, 1.0, 0.0, 0.0);
            canvas->add(sc);
        }
    }

    canvas->nozoom();

    // Double Contact


    return Fl::run();
}

