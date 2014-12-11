#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Light_Button.H>
#include <FL/Fl.H>

#include "canvas.h"
#include "curve2d.h"

#include "SplineFlux.h"

#include <stdio.h>

Fl_Double_Window *win;
Canvas *canvas;

Fl_Double_Window *control;
Fl_Light_Button *add;

std::vector<RealVector> points;

Curve2D *spline_curve = 0;
Curve2D *temp_spline_curve = 0;
Curve2D *point_curve = 0;

double cmin, cmax;

void new_spline(const std::vector<RealVector> &p, Curve2D *curve){
    SplineFlux sf(p);

    std::vector<RealVector> spline_points;

    int maxnum = 100;
    double delta = (cmax - cmin)/(double)(maxnum - 1);

    for (int i = 0; i < maxnum; i++){
        RealVector x(2);
        x(0) = cmin + delta*(double)i;
        x(1) = 0.0;

        JetMatrix jm(2);
        sf.jet(x, jm, 0);

        x(1) = jm.get(0);

        spline_points.push_back(x);
    }

    std::cout << "New almost done." << std::endl;

    if (curve != 0) canvas->erase(curve);
    curve = new Curve2D(spline_points, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE);
    canvas->add(curve);
    Fl::check();

    std::cout << "New done." << std::endl;

    return;
}

void draw_points(){
    if (point_curve != 0) canvas->erase(point_curve);
    point_curve = new Curve2D(points, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
    canvas->add(point_curve);

    return;
}

void addpointcb(Fl_Widget*, void*){
    RealVector p(2);

    canvas->getxy(p(0), p(1));

    points.push_back(p);

    draw_points();
    new_spline(points, spline_curve);

    return;
}

void hoover(Fl_Widget*, void*){
    if (points.size() <= 2) return;

    RealVector p(2);

    canvas->getxy(p(0), p(1));

    std::vector<RealVector> temp_points;
    for (int i = 0; i < points.size(); i++) temp_points.push_back(points[i]);

    temp_points.push_back(p);

    new_spline(temp_points, temp_spline_curve);

    return;
}

void addcb(Fl_Widget*, void*){
    if (add->value() == 0) canvas->on_move(0, canvas, 0);
    else                   canvas->on_move(&hoover, canvas, 0);

    return;
}

void wincb(Fl_Widget*, void*){
    FILE *fid = fopen("spline.txt", "w");

    fprintf(fid, "%d\n", points.size());
    for (int i = 0; i < points.size(); i++) fprintf(fid, "%g %g\n", points[i](0), points[i](1));

    fclose(fid);

    exit(0);
    return;
}

int main(int argc, char *argv[]){
    cmin = -1.0;
    cmax =  1.0;

    win = new Fl_Double_Window(10, 10, 800, 800, "SplineFlux");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());

        // Create a boundary here.
        RealVector p(2);
        std::vector<RealVector> b;

        // 0
        p(0) = p(1) = cmin;
        b.push_back(p);
        
        // 1
        p(0) = cmax; p(1) = cmin;
        b.push_back(p);

        // 2
        p(0) = cmax; p(1) = cmax;
        b.push_back(p);    

        // 3
        p(0) = cmin;
        b.push_back(p);

        // Close.
        p(0) = cmin; p(1) = cmin;
        b.push_back(p);  

        Curve2D *boundary = new Curve2D(b, 0.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
        canvas->add(boundary);

        canvas->nozoom();
        canvas->show_grid();

        canvas->setextfunc(&addpointcb, canvas, 0);
        //canvas->on_move(&hoover, canvas, 0);
    }
    win->end();

    win->resizable(win);
    win->show();
    win->callback(wincb);

    control = new Fl_Double_Window(win->x() + win->w(), win->y(), 200, 50, "Control");
    {   
        add = new Fl_Light_Button(10, 10, control->w() - 20, control->h() - 20, "&On hoover");
        add->callback(addcb);

        add->value(1);

        add->do_callback();
    }
    control->end();

    control->show();

    if (argc > 1){
        FILE *fid = fopen(argv[1], "r");

        if (fid){
            int n;
            fscanf(fid, "%d", &n);

            for (int i = 0; i < n; i++){
                RealVector p(2);
                fscanf(fid, "%lg %lg", &p(0), &p(1));

                std::cout << "Spline point: " << p << std::endl;

                points.push_back(p);
            }

            fclose(fid);

            new_spline(points, spline_curve);
            draw_points();

            std::cout << "Should continue." << std::endl;
        }
    }

    return Fl::run();
}

