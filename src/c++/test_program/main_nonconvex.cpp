#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Round_Button.H>
#include "canvas.h"
#include "curve2d.h"
#include "RealVector.h"

Fl_Double_Window *win;
    Canvas *canvas;

Fl_Double_Window *commandwin;
    Fl_Round_Button *polygon, *point;
    Fl_Button *clear;

std::vector<RealVector> polygonvector;
RealVector pointfortest;
Curve2D *polygoncurve;
Curve2D *pointcurve;

void wincb(Fl_Widget *o, void*){
    if ((Fl_Double_Window*)o == win) exit(0);

    return;
}

void clearcb(Fl_Widget*, void*){
    polygonvector.clear();

    if (polygoncurve != 0) canvas->erase(polygoncurve);
    if (pointcurve != 0) canvas->erase(pointcurve);

    return;
}

void addpointcb(Fl_Widget*, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    polygonvector.push_back(p);
    
    if (polygoncurve != 0) canvas->erase(polygoncurve);

    std::vector<RealVector> temp(polygonvector);
    temp.push_back(temp.front());

    polygoncurve = new Curve2D(temp, 0.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE);
    canvas->add(polygoncurve);

    return;
}

void testpointcb(Fl_Widget*, void*){
    canvas->getxy(pointfortest(0), pointfortest(1));

    double r, g, b;
    r = g = b = 0.0;

    if (pointcurve != 0) canvas->erase(pointcurve);

    if (inside_non_convex_polygon(polygonvector, pointfortest)) g = 1.0;
    else                                                        r = 1.0;

    pointcurve = new Curve2D(pointfortest, r, g, b, CURVE2D_MARKERS);
    canvas->add(pointcurve);

    return;
}

void optioncb(Fl_Widget*, void*){
    if (polygon->value() == 1) canvas->setextfunc(&addpointcb, canvas, 0);
    else                       canvas->setextfunc(&testpointcb, canvas, 0);

    return;
}

int main(){
    polygoncurve = 0;
    pointcurve = 0;
    pointfortest.resize(2);

    win = new Fl_Double_Window(10, 10, 800, 800, "Non-convex polygon test");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
        canvas->xlabel("x");
        canvas->ylabel("y");

        canvas->axis(-2.0, 2.0, -2.0, 2.0);
    }
    win->end();
    win->resizable(win);
    win->callback(wincb);
    win->show();

    commandwin = new Fl_Double_Window(win->x() + win->w() + 1, win->y(), 300, 80, "Command window");
    {
        polygon = new Fl_Round_Button(10, 10, (commandwin->w() - 30)/2, 25, "Add to polygon");
        polygon->type(FL_RADIO_BUTTON);
        polygon->value(1);
        polygon->callback(optioncb);

        point   = new Fl_Round_Button(polygon->x() + polygon->w() + 10, polygon->y(), polygon->w(), polygon->h(), "Test point");
        point->type(FL_RADIO_BUTTON);
        point->value(0);
        point->callback(optioncb);

        clear = new Fl_Button(10, polygon->y() + polygon->h() + 10, commandwin->w() - 20, 25, "Clear polygon");
        clear->callback(clearcb);

        polygon->do_callback();
    }
    commandwin->end();
    commandwin->callback(wincb);
    commandwin->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

