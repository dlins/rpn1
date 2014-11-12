#include "Brooks_CoreySubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "SorbiePermeability.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include "canvas.h"
#include "Text.h"
#include "ThreePhaseFlowPermeabilityLevelCurve.h"
#include "segmentedcurve.h"

#include <map>

std::vector<Fl_Double_Window*> winvec;
std::vector<Canvas*>           canvasvec;
std::vector<Fl_Round_Button*>  rndclick; 
std::vector<Fl_Round_Button*>  rndmove;
std::vector<std::string>       name;
std::vector<int>               type;
std::vector<SegmentedCurve*>   sc;

std::map<Canvas*, int> permtype;

ThreePhaseFlowSubPhysics *subphysics;
ThreePhaseFlowPermeabilityLevelCurve *permlevelcurve;

ThreePhaseFlowPermeability *permeability;

void on_move_perm(Fl_Widget *obj, void*){
    Canvas *canvas = (Canvas*)obj;
    int canvas_index = permtype[canvas];

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
    permlevelcurve->curve(point, type[canvas_index], c);

    if (c.size() == 0) return;

    if (sc[canvas_index] != 0) canvas->erase(sc[canvas_index]);
    sc[canvas_index] = new SegmentedCurve(c, 0.0, 0.0, 1.0);
    canvas->add(sc[canvas_index]);

    Fl::check();

    return;
}

void on_click_perm(Fl_Widget *obj, void*){
    Canvas *canvas = (Canvas*)obj;
    int canvas_index = permtype[canvas];

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
    permlevelcurve->curve(point, type[canvas_index], c);

    if (c.size() == 0) return;

    // Add the curve.
    //
    SegmentedCurve *sc = new SegmentedCurve(c, 0.0, 0.0, 1.0);
    canvas->add(sc);

    // Add the level.
    //
    RealVector shift(2);
    shift(0) = -10.0;
    shift(1) = -10.0;
    std::stringstream ss;
    ss << permlevelcurve->level(point, type[canvas_index]); // TODO: Use only two digits.

    Text *t = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
    canvas->add(t);

    Fl::check();

    return;
}

void rmcb(Fl_Widget*, void *obj){
    Canvas *canvas = (Canvas*)obj;

    canvas->setextfunc(0, canvas, 0);
    canvas->on_move(&on_move_perm, canvas, 0);

    return;
}

void rccb(Fl_Widget*, void *obj){
    Canvas *canvas = (Canvas*)obj;

    // Connect this callback...
    //
    canvas->setextfunc(on_click_perm, canvas, 0);

    // ...and disconnect this one.
    //
    canvas->on_move(0, canvas, 0);

    // Delete the curve that changes as the mouse moves, if it exists.
    //
    int canvas_index = permtype[canvas];

    if (sc[canvas_index] != 0){
        canvas->erase(sc[canvas_index]);
        sc[canvas_index] = 0;
    }
    
    return;
}

void wincb(Fl_Widget*, void*){
    exit(0);

    return;
}

int main(){
    // Use one or several canvases.

    subphysics = new Brooks_CoreySubPhysics;
//    subphysics = new CoreyQuadSubPhysics;
    permeability = subphysics->permeability();

    permeability = new SorbiePermeability(0); // TODO Change this, create SorbieSubPhysics.

    permlevelcurve = new ThreePhaseFlowPermeabilityLevelCurve(subphysics, permeability);
    permlevelcurve->list_of_types(type, name);
    
    for (int j = 0; j < name.size(); j++){
        int w = 800;
        int h = 800 + 10 + 25 + 10;

        Fl_Double_Window *win = new Fl_Double_Window(10 + w*winvec.size(), 10, w, h, name[j].c_str());
        {
            Canvas *canvas = new Canvas(0, 0, win->w(), win->h() - 10 - 25 - 10);

            std::vector<std::vector<RealVector> > b;
            subphysics->boundary()->physical_boundary(b);

            for (int i = 0; i < b.size(); i++){
                if (b[i].size() > 1){
                    Curve2D *c = new Curve2D(b[i], 0.0, 0.0, 0.0);

                    canvas->add(c);
                }
            }

            canvas->set_transform_matrix(subphysics->transformation_matrix().data());
            canvas->nozoom();

            canvas->xlabel(subphysics->xlabel().c_str());
            canvas->ylabel(subphysics->ylabel().c_str());

            canvasvec.push_back(canvas);

            {
                RealVector pos(2);
                pos(0) = 0.0;
                pos(1) = 1.0;

                RealVector shift(2);
                shift(0) = 0.0;
                shift(1) = -10.0;

                Text *t = new Text(std::string("G"), pos, shift, 1.0, 0.0, 0.0);
                canvas->add(t);
            }

            {
                RealVector pos(2);
                pos(0) = 1.0;
                pos(1) = 0.0;

                RealVector shift(2);
                shift(0) = 10.0;
                shift(1) = 10.0;

                Text *t = new Text(std::string("W"), pos, shift, 1.0, 0.0, 0.0);
                canvas->add(t);
            }

            {
                RealVector pos(2);
                pos(0) = 0.0;
                pos(1) = 0.0;

                RealVector shift(2);
                shift(0) = -10.0;
                shift(1) =  10.0;

                Text *t = new Text(std::string("O"), pos, shift, 1.0, 0.0, 0.0);
                canvas->add(t);
            }

            canvas->on_move(&on_move_perm, canvas, 0);

            permtype[canvas] = j;

            sc.push_back(0); // Or else...

            {
                Fl_Round_Button *rm = new Fl_Round_Button(10, canvas->y() + canvas->h() + 10, (win->w() - 30)/2, 25, "Move");
                rm->type(FL_RADIO_BUTTON);
                rm->value(1);
                rm->callback(rmcb, canvas);

                rndmove.push_back(rm);

                Fl_Round_Button *rc = new Fl_Round_Button(rm->x() + rm->w() + 10, rm->y(), rm->w(), rm->h(), "Click");
                rc->type(FL_RADIO_BUTTON);
                rc->value(0);
                rc->callback(rccb, canvas);

                rndclick.push_back(rc);
            }
        }
        win->end();
        win->callback(wincb);
        win->show();

        winvec.push_back(win);
    }

    return Fl::run();
}

