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
#include "ThreePhaseFlowEquationFunctionLevelCurve.h"

#include <map>
#include <iostream>

ThreePhaseFlowSubPhysics *subphysics;
ThreePhaseFlowPermeabilityLevelCurve *permlevelcurve;
ThreePhaseFlowPermeability *permeability;
ThreePhaseFlowEquationFunctionLevelCurve *flowlevel;

void wincb(Fl_Widget*, void*){
    exit(0);

    return;
}

class PermWindow: public Fl_Double_Window {
    private:
    protected:
    public:
        Canvas *canvas;
        Fl_Round_Button *rndmove, *rndclick;

        SegmentedCurve *sc;
        Text *text;

        int type;

        PermWindow(int x, int y, int w, int h, const char *l): Fl_Double_Window(x, y, w, h, l){
            sc = 0;
            text = 0;

            canvas = new Canvas(0, 0, w, h - 10 - 25 - 10);

            // Add some stuff to the canvas.
            //
            {
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
            }

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

            // Move & click.
            //
            rndmove = new Fl_Round_Button(10, canvas->y() + canvas->h() + 10, (w - 30)/2, 25, "Follow mouse");
            rndmove->type(FL_RADIO_BUTTON);
            rndmove->value(1);

            rndclick = new Fl_Round_Button(rndmove->x() + rndmove->w() + 10, rndmove->y(), rndmove->w(), rndmove->h(), "Compute on click");
            rndclick->type(FL_RADIO_BUTTON);

            // Finish.
            //
            end();
            callback(wincb);
        }

        virtual ~PermWindow(){
        }
};

std::vector<PermWindow*> permwindow;

//void on_move_flow(Fl_Widget *obj, void*){
//    Canvas *canvas = (Canvas*)obj;
//    int canvas_index = permtype[canvas];

//    RealVector point(2);
//    canvas->getxy(point(0), point(1));

//    if (!subphysics->boundary()->inside(point)) return;

//    std::vector<RealVector> c;

//    EquationFunctionLevelCurve eflc(subphysics->flux(), subphysics->gridvalues());
//    eflc.curve(point, 1, c);

//    if (c.size() == 0) return;

//    if (sc[canvas_index] != 0) canvas->erase(sc[canvas_index]);
//    sc[canvas_index] = new SegmentedCurve(c, 0.0, 0.0, 1.0);
//    canvas->add(sc[canvas_index]);

//    if (text[canvas_index] != 0) canvas->erase(text[canvas_index]);

//    // Add the level.
//    //
//    RealVector shift(2);
//    shift(0) = -10.0;
//    shift(1) = -10.0;
//    std::stringstream ss;
//    ss.precision(2);
//    ss << permlevelcurve->level(point, type[canvas_index]); // TODO: Use only two digits.

//    text[canvas_index] = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
//    canvas->add(text[canvas_index]);

//    Fl::check();

//    return;
//}

void on_move_perm(Fl_Widget *obj, void*){
    Canvas *canvas = (Canvas*)obj;

    PermWindow *pw = (PermWindow*)canvas->window();

    RealVector point(2);
    pw->canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
    permlevelcurve->curve(point, pw->type, c);

    if (c.size() == 0) return;

    if (pw->sc != 0) pw->canvas->erase(pw->sc);
    pw->sc = new SegmentedCurve(c, 0.0, 0.0, 1.0);
    pw->canvas->add(pw->sc);

    if (pw->text != 0) pw->canvas->erase(pw->text);

    // Add the level.
    //
    RealVector shift(2);
    shift(0) = -10.0;
    shift(1) = -10.0;
    std::stringstream ss;
    ss.precision(2);
    ss << permlevelcurve->level(point, pw->type); // TODO: Use only two digits.

    pw->text = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
    pw->canvas->add(pw->text);

    Fl::check();

    return;
}

void on_click_perm(Fl_Widget *obj, void*){
    PermWindow *pw = (PermWindow*)obj;
    Canvas *canvas = pw->canvas;

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
    permlevelcurve->curve(point, pw->type, c);

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
    ss.precision(2);
    ss << permlevelcurve->level(point, pw->type); // TODO: Use only two digits.

    Text *t = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
    canvas->add(t);

    Fl::check();

    return;
}

void rmcb(Fl_Widget*, void *obj){
    PermWindow *pw = (PermWindow*)obj;
    pw->canvas->setextfunc(0, pw, 0);
    pw->canvas->on_move(&on_move_perm, pw, 0);

    return;
}

void rccb(Fl_Widget*, void *obj){
    PermWindow *pw = (PermWindow*)obj;

    // Connect this callback...
    //
    pw->canvas->setextfunc(on_click_perm, pw, 0);

    // ...and disconnect this one.
    //
    pw->canvas->on_move(0, pw, 0);

    // Delete the curve that changes as the mouse moves, if it exists.
    //
    if (pw->sc != 0){
        pw->canvas->erase(pw->sc);
        pw->sc = 0;
    }

    if (pw->text != 0){
        pw->canvas->erase(pw->text);
        pw->text = 0;
    }
    
    return;
}

int main(){
//    subphysics = new Brooks_CoreySubPhysics;
    subphysics = new CoreyQuadSubPhysics;
    permeability = subphysics->permeability();

    permeability = new SorbiePermeability(0); // TODO Change this, create SorbieSubPhysics.

    // Size.
    //
    int w = 500;
    int h = w + 10 + 25 + 10;

    // Permeabilities.
    //   
    {
        permlevelcurve = new ThreePhaseFlowPermeabilityLevelCurve(subphysics, permeability);

        std::vector<std::string>       name;
        std::vector<int>               type;
        permlevelcurve->list_of_types(type, name); 

        for (int j = 0; j < name.size(); j++){
            PermWindow *pw = new PermWindow(10 + w*j, 10, w, h, name[j].c_str());
            pw->show();

            // Set type.
            //
            pw->type = type[j];
            
            pw->rndmove->callback(rmcb, pw);
            pw->rndclick->callback(rccb, pw);

            pw->rndmove->do_callback();
        }
    }

    // Flows.
    //
    {
        flowlevel = new ThreePhaseFlowEquationFunctionLevelCurve(subphysics->flux(), subphysics->gridvalues());

        std::vector<std::string>       name;
        std::vector<int>               type;
        flowlevel->list_of_components(type, name); 

        for (int j = 0; j < name.size(); j++){
            PermWindow *pw = new PermWindow(10 + w*j, 10 + h, w, h, name[j].c_str());
            pw->show();

            // Set type.
            //
            pw->type = type[j];
            
//            pw->rndmove->callback(rmcb, pw);
//            pw->rndclick->callback(rccb, pw);

//            pw->rndmove->do_callback();
        }
    }

    return Fl::run();
}

