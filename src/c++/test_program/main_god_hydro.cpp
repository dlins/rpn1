#include <iostream>
#include "StoneSubPhysics.h"
#include "GasVolatileDeadSubPhysics.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Choice.H>
#include <FL/Fl_Tile.H>
#include <FL/Fl_Float_Input.H>
#include <FL/Fl_Scroll.H>
#include "canvas.h"
#include "canvasmenuscroll.h"
#include "curve2d.h"
#include "MultiColoredCurve.h"

SubPhysics *tpfsp;

Fl_Double_Window *win;
    Fl_Tile *tile;
    Canvas *canvas;

    Fl_Group *scroll_grp;
    CanvasMenuScroll *scroll;
    Fl_Button        *clear_all_curves, *nozoom;

void wincb(Fl_Widget *w, void*){
    Fl_Double_Window *thiswindow = (Fl_Double_Window*)w;

    if (thiswindow == win) exit(0);

    return;
}

void clear_curves(Fl_Widget*, void*){
    scroll->clear_all_graphics();

    return;
}

void nozoomcb(Fl_Widget*, void*){
    canvas->nozoom();

    return;
}

int main(){
    // Create the subphysics.
    //
//    tpfsp = new StoneSubPhysics;
    tpfsp = new GasVolatileDeadSubPhysics;

    // Main window.
    //
    int scrollwidth = 300;
    win = new Fl_Double_Window(10, 10, 800 + scrollwidth + 20, 800);
    {
        // Canvas.
        //
        canvas = new Canvas(0, 0, win->w() - 20 - scrollwidth, win->h());

        canvas->xlabel("s");
        canvas->ylabel("hydro(s)");

        canvas->axis(0.0, 1.0, -10.0, 10.0);
        canvas->show_grid();

        // Scroll.
        //
        scroll_grp = new Fl_Group(canvas->x() + canvas->w() + 1, canvas->y(), scrollwidth, canvas->h());
        scroll = new CanvasMenuScroll(canvas->x() + canvas->w() + 10, 20, scrollwidth, win->h() - 40 - 25, "Curves");

        clear_all_curves = new Fl_Button(scroll->x(), scroll->y() + scroll->h() + 10, (scroll->w() - 10)/2, 25, "Clear all curves");
        clear_all_curves->callback(clear_curves);

        nozoom = new Fl_Button(clear_all_curves->x() + clear_all_curves->w() + 10, 
                               clear_all_curves->y(),
                               clear_all_curves->w(),
                               clear_all_curves->h(),
                               "No zoom");
        nozoom->callback(nozoomcb);

        scroll_grp->end();

        // Populate the canvas
        GasVolatileDeadHydrodynamics hydro;

        std::vector<double> r;
        r.push_back(1.0);
        r.push_back(2.0);
        r.push_back(3.0);
        r.push_back(4.0);
        r.push_back(5.0);

        for (int i = 0; i < r.size(); i++){
            int n = 100;
            double smin = 0.0;
            double smax = 1.0;
            double delta = (smax - smin)/(double)(n - 1);

            std::vector<RealVector> f, df_ds, df_dr, d2f_ds2;

            for (int j = 0; j < n; j++){
                double s = smin + delta*(double)j;
                JetMatrix fo;
                hydro.fractional_flow(2, s, r[i], fo);

                RealVector p(2);
                p(0) = s;

                // f.
                //
                p(1) = fo.get(0);
                f.push_back(p);

                // df_ds.
                //
                p(1) = fo.get(0, 0);
                df_ds.push_back(p);

                // df_dr.
                //
                p(1) = fo.get(0, 1);
                df_dr.push_back(p);

                // d2f_ds2.
                //
                p(1) = fo.get(0, 0, 0);
                d2f_ds2.push_back(p);
            }

            {
                Curve2D *cf = new Curve2D(f, 0.0, 0.0, 0.0);
                canvas->add(cf);

                std::stringstream ss;
                ss << "f, r = " << r[i];
                scroll->add(ss.str().c_str(), canvas, cf);
            }

            {
                Curve2D *cf = new Curve2D(df_ds, 1.0, 0.0, 0.0);
                canvas->add(cf);

                std::stringstream ss;
                ss << "df_ds, r = " << r[i];
                scroll->add(ss.str().c_str(), canvas, cf);
            }

            {
                Curve2D *cf = new Curve2D(df_dr, 0.0, 0.0, 1.0);
                canvas->add(cf);

                std::stringstream ss;
                ss << "df_dr, r = " << r[i];
                scroll->add(ss.str().c_str(), canvas, cf);
            }

            {
                Curve2D *cf = new Curve2D(d2f_ds2, 0.0, 1.0, 1.0);
                canvas->add(cf);

                std::stringstream ss;
                ss << "d2f_ds2, r = " << r[i];
                scroll->add(ss.str().c_str(), canvas, cf);
            }
            
        }

        canvas->nozoom();

    }
    win->end();
    win->copy_label("Hydro");
    win->resizable(win);
    win->callback(wincb);
    win->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

