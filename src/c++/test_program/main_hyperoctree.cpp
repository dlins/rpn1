#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Box.H>
#include "RealVector.h"

#include "HyperOctree.h"
#include "canvas.h"
#include "curve2d.h"
#include <cmath>

class RV : public RealVector {
    public:
        RV(int n) : RealVector(n){
        }

        bool intersect(const BoxND &b){
            bool intersection = true;
            int i = 0;

            while (intersection && i < size_){
                intersection = (component(i) >= b.pmin(i)) && (component(i) <= b.pmax(i));
                i++;
            }

            return intersection;
        }
};

Fl_Double_Window *win;
    Canvas *canvas;

    std::vector<Curve2D*> on_move_box;

    HyperOctree<RV> *hpnd = 0;

// Test
Fl_Double_Window *output;
    Fl_Box       *ob = (Fl_Box*)0;

Curve2D* box_to_curve(const BoxND &b, double rr, double gg, double bb){
    RealVector temp(2);

    std::vector<RealVector> v;

    temp(0) = b.pmin(0);
    temp(1) = b.pmin(1);
    v.push_back(temp);

    temp(0) = b.pmax(0);
    temp(1) = b.pmin(1);
    v.push_back(temp);

    temp(0) = b.pmax(0);
    temp(1) = b.pmax(1);
    v.push_back(temp);

    temp(0) = b.pmin(0);
    temp(1) = b.pmax(1);
    v.push_back(temp);
    
    v.push_back(v[0]);

    return new Curve2D(v, rr, gg, bb, CURVE2D_SOLID_LINE);
}

void on_move(Fl_Widget*, void*){
    RV initial_point(2);
    canvas->getxy(initial_point(0), initial_point(1));

    if (hpnd != 0){
        std::vector<BoxND> b;
        hpnd->leaves_intersected_by_object(&initial_point, b);
        
        // Remove...
        //
        for (int i = 0; i < on_move_box.size(); i++) canvas->erase(on_move_box[i]);
        on_move_box.clear();

        // ...and add.
        //
        for (int i = 0; i < b.size(); i++){
            Curve2D *c = box_to_curve(b[i], 0.0, 1.0, 0.0);

            canvas->add(c);
            on_move_box.push_back(c);
        }

        // Print number of objects in the leaf
        std::vector<RV*> list;
        hpnd->within_leaves_intersected_by_object(&initial_point, list);

        std::stringstream out;
        out << list.size();

        ob->copy_label(out.str().c_str());
        output->position(Fl::event_x_root() + 20, Fl::event_y_root() + 20);
//        output->position(win->x() + win->w() + 20, win->y());
        output->show();

        Fl::check();
    }

    return;
}

void on_leave(Fl_Widget*, void*){
    for (int i = 0; i < on_move_box.size(); i++) canvas->erase(on_move_box[i]);
    on_move_box.clear();
    output->hide();

    return;
}

void show_leaves(){
    // Create function points.
    //
    std::vector<PointND> f;
    
    int n = 1000;

    double min = 0.0;
    double max = 2.0*M_PI;
    double delta = (max - min)/(double)(n - 1);

    RV *p;

    for (int i = 0; i < n; i++){
        double theta = min + ((double)i)*delta;

        p = new RV(2);
        p->component(0) = .9*std::cos(theta);
        p->component(1) = .9*std::sin(theta);

        hpnd->add(p);
    }

//    for (int i = 0; i < n; i++){
//        double theta = min + ((double)i)*delta;

//        p = new RV(2);
//        p->component(0) = 1.5;
//        p->component(1) = .9*std::sin(theta);

//        hpnd.add(p);
//    }

    std::vector<BoxND> bn, bl;
    hpnd->boxes(bn, bl);

//    for (int i = 0; i < bn.size(); i++){
//        Curve2D *c = box_to_curve(bn[i], 0.0, 0.0, 0.0);
//        canvas->add(c);
//    }

    for (int i = 0; i < bl.size(); i++){
        Curve2D *c = box_to_curve(bl[i], 1.0, 0.0, 0.0);
        canvas->add(c);
    }

    // Show points.
    //
    std::vector<RealVector> vrv;
    for (int i = 0; i < n; i++){
        double theta = min + ((double)i)*delta;

        RealVector p(2);
        p(0) = .9*std::cos(theta);
        p(1) = .9*std::sin(theta);

        vrv.push_back(p);
    }
    Curve2D *c = new Curve2D(vrv, 0.0, 0.0, 1.0, CURVE2D_MARKERS);
    canvas->add(c);

    canvas->nozoom();

    return;
}

void close_cb(Fl_Widget*, void*){
    exit(0);

    return;
}

int main(){
    // Output
    output = new Fl_Double_Window(10, 10, 50, 50, "Info");
    {
        ob = new Fl_Box(0, 0, output->w(), output->h(), "Info");
        ob->labelfont(FL_COURIER);
    }
    output->end();
    output->clear_border();

    win = new Fl_Double_Window(10, 10, 800, 800, "HyperOctree test");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());
        canvas->on_move(&on_move, canvas, 0);
        canvas->on_leave(&on_leave, canvas, 0);
    }
    win->end();
    win->callback(close_cb);

    win->resizable(win);
    win->show();

    // HyperOctree.
    //
    PointND pmin(2), pmax(2);
    pmin(0) = pmin(1) = -2.0;
    pmax(0) = pmax(1) =  4.0;

    BoxND box(pmin, pmax);

    hpnd = new HyperOctree<RV>(box, 15, 10);
    show_leaves();

    Fl::scheme("gtk+");

    return Fl::run();
}

