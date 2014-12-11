#ifndef _CANVAS_
#define _CANVAS_

#include <FL/Fl.H>
#include <FL/Fl_Widget.H>
#include <FL/fl_draw.H>
#include <FL/x.H>
#include <FL/Fl_Box.H>
#include <vector>
#include <string>
#include <sstream>
#include <fstream>
#include <iostream>
#include "graphicobject.h"
//#include "pnm.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "DoubleMatrix.h"

using namespace std;

extern "C" {void dgetri_(int*, double*, int*, int*, double*, int*, int*);}

typedef void(*ExternalFunctionSignature)(Fl_Widget*, void*);

class Canvas : public Fl_Widget {
    protected:
        int sl, sr, su, sd;
        double xmax, xmin, ymax, ymin;
        double xviewmax, xviewmin, yviewmax, yviewmin;
        double xviewmax_old, xviewmin_old, yviewmax_old, yviewmin_old;
        double temp_x_factor, temp_y_factor;
        double mx, my, bx, by;
        double deltax, deltay;

        Point2D pb, pe; // Begin and end points when dragging.
        bool draw_zoom;
        vector<GraphicObject*> vgr;

        // Text
        int n_hor, n_ver; // Number of horizontal and vertical ticks
        bool show_text_;
        bool show_ticks_;

        void draw();
        int handle(int);

        // External function
        void (*extfunc)(Fl_Widget*, void*);
        Fl_Widget *extwidget;
        void *extvoid;

        // Labels
        char *xlabel_, *ylabel_;
        uchar *ylabelbuf;

        // Grid
        bool show_grid_;

        // Transformation matrix and its inverse
        double *tm, *itm;

        // Other external functions
        void (*on_move_function)(Fl_Widget*, void*);
        void (*on_enter_function)(Fl_Widget*, void*);
        void (*on_leave_function)(Fl_Widget*, void*);

    public:
        Canvas(int, int, int, int);
        ~Canvas();
        void add(GraphicObject*);
        void clear();
        void erase(int);
        void erase(GraphicObject*);
        int size();
        string num2str(double);
        void pstricks(const char*);
//        void canvas2pnm(const char*);
        void show_text(void);
        void hide_text(void);
        void show_ticks(void);
        void hide_ticks(void);
        void nozoom(void);
        void getxy(double &, double &);
        void setextfunc(void(*)(Fl_Widget*, void*), Fl_Widget*, void*);

//        typedef void(*ExternalFunctionSignature)(Fl_Widget*, void*);
        ExternalFunctionSignature getextfunc(){return extfunc;}

        void replace(GraphicObject*, GraphicObject*);

        void xlabel(const char*);
        void ylabel(const char*);

        void show_grid(void);
        void hide_grid(void);

        void number_of_horizontal_ticks(int);
        int number_of_horizontal_ticks(void);

        void number_of_vertical_ticks(int);
        int number_of_vertical_ticks(void);

        void axis(double minx, double maxx, double miny, double maxy);

        // Do something with this 3x3 matrix of the form:
        //
        //     [a b c; d e f; 0 0 1]
        //
        // that implements a chain of transformations in
        // homogeneous coordinates.
        //
        int set_transform_matrix(const double *m);

        int set_transform_matrix(const DoubleMatrix &m){
            return set_transform_matrix(m.data());;
        }

        // Set some external functions
        void on_move(void(*f)(Fl_Widget*, void*), Fl_Widget*, void*) {on_move_function = f; return;}
        void on_enter(void(*f)(Fl_Widget*, void*), Fl_Widget*, void*){on_enter_function = f; return;}
        void on_leave(void(*f)(Fl_Widget*, void*), Fl_Widget*, void*){on_leave_function = f; return;}

        // Get the transformation matrix.
        //
        DoubleMatrix get_transform_matrix(){
            bx = x() + sl - mx*xviewmin;
            by = y() + su - my*yviewmax;

            DoubleMatrix T = DoubleMatrix::eye(3);
            T(0, 2) = bx;
            T(1, 2) = by;

            mx = (w() - sr - sl)/(xviewmax - xviewmin);
            my = (su + sd - h())/(yviewmax - yviewmin);

            DoubleMatrix S = DoubleMatrix::eye(3);
            S(0, 0) = mx;
            S(1, 1) = my;

            return T*S;
        }
};

#endif // _CANVAS_


