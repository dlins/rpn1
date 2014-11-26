#include <iostream>
#include "SimpleFlux.h"

#include <FL/Fl_Double_Window.H>

#include "canvas.h"
#include "curve2d.h"

Fl_Double_Window *win;
Canvas *canvas;

int main(){
    RealVector coef(4);
    coef(0) =  0.0;
    coef(1) = -1.0;
    coef(2) =  0.0;
    coef(3) =  1.0;

    SimpleFlux sf(coef);

    win = new Fl_Double_Window(10, 10, 800, 800);
        canvas =  new Canvas(0, 0, win->w(), win->h());
    win->end();

    win->resizable(win);

    win->show();

    // Add plot
    int n = 100;

    double min = -2.0, max = 2.0;
    double delta = (max - min)/(double)(n - 1);

    std::vector<RealVector> f, df, d2f;

    for (int i = 0; i < n; i++){
        RealVector w(2);
        w(0) = min + (double)i*delta;
        w(1) = 0.0;

        JetMatrix jm;
        sf.jet(w, jm, 2);

        RealVector p(2);
        p(0) = w(0);
        
        p(1) = jm.get(0);
        f.push_back(p);

        p(1) = jm.get(0, 0);
        df.push_back(p);

        p(1) = jm.get(0, 0, 0);
        d2f.push_back(p);
    }

    Curve2D *cf = new Curve2D(f, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
    canvas->add(cf);

    Curve2D *cdf = new Curve2D(df, 0.0, 1.0, 0.0, CURVE2D_SOLID_LINE);
    canvas->add(cdf);

    Curve2D *cd2f = new Curve2D(d2f, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE);
    canvas->add(cd2f);

    return Fl::run();
}

