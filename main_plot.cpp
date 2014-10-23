#include <FL/Fl_Double_Window.H>

#include "canvas.h"
#include "segmentedcurve.h"

#include <fstream>
#include <iostream>

Fl_Double_Window *win;
Canvas *canvas;

int main(int argc, char *argv[]){
    if (argc != 2) return -1;

    // Read the data.
    //
    std::fstream in(argv[1], std::fstream::in);
    std::vector<RealVector> curve;

    RealVector p(2), q(2);
    while (in >> p(0) >> p(1) >> q(0) >> q(1)){
        curve.push_back(p);
        curve.push_back(q);
    }

    for (int i = 0; i < curve.size(); i++){
        std::cout << curve[i] << std::endl;
    }

    in.close();

    // Window.
    //
    win = new Fl_Double_Window(10, 10, 800, 800, "Plot");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
    }
    win->end();
    win->resizable(win);
    win->show();
    
    // Show the curve.
    //
    SegmentedCurve sc(curve, 0.0, 0.0, 0.0);
    canvas->add(&sc);
    canvas->nozoom();

    return Fl::run();
}

