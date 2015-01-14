#include "Brooks_CoreySubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "FoamSubPhysics.h"
#include "KovalSubPhysics.h"
#include "SorbieSubPhysics.h"
#include "StoneSubPhysics.h"

//#include "SorbiePermeability.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Float_Input.H>
#include "canvas.h"
#include "Text.h"
#include "ThreePhaseFlowPermeabilityLevelCurve.h"
#include "ThreePhaseFlowMobilityLevelCurve.h"
#include "segmentedcurve.h"
#include "ThreePhaseFlowEquationFunctionLevelCurve.h"

#include <map>
#include <iostream>
#include <typeinfo>

ThreePhaseFlowSubPhysics *subphysics;
ThreePhaseFlowPermeabilityLevelCurve *permlevelcurve;
ThreePhaseFlowMobilityLevelCurve *moblevelcurve;
ThreePhaseFlowPermeability *permeability;
ThreePhaseFlowEquationFunctionLevelCurve *flowlevel;

Fl_Double_Window *paramwin;
    Fl_Button *paramapply, *paramcancel;
    std::vector<Fl_Float_Input*> paraminput;
    std::vector<double> parameter_value;
    std::vector<Parameter*> parameters;

void wincb(Fl_Widget *obj, void*){
    if ((Fl_Double_Window*)obj != paramwin) exit(0);

    return;
}

// Add some stuff to the canvas.
//
void prepare_triangle(Canvas *canvas){
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

                Text *t = new Text(std::string("O"), pos, shift, 1.0, 0.0, 0.0);
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

                Text *t = new Text(std::string("G"), pos, shift, 1.0, 0.0, 0.0);
                canvas->add(t);
            }

    return;
}

class MapOutWindow: public Fl_Double_Window {
    private:
    protected:
    public:
        Canvas *canvas;
        Curve2D *point;

        MapOutWindow(int x, int y, int w, int h, const char *l): Fl_Double_Window(x, y, w, h, l){
            point = 0;

            canvas = new Canvas(0, 0, w, h);
            prepare_triangle(canvas);

            end();

            resizable(canvas);
            callback(wincb);
        }

        virtual ~MapOutWindow(){
        }
};

MapOutWindow *map_out;

class MapInWindow: public Fl_Double_Window {
    private:
    protected:
        static void on_click_map(Fl_Widget *obj, void*){
            MapInWindow *local_map_in = (MapInWindow*)obj;

            RealVector temp(2);
            local_map_in->canvas->getxy(temp(0), temp(1));

            if (!subphysics->boundary()->inside(temp)) return;

            double sw = temp(0);
            double so = temp(1);

            int n = 50;

            RealVector pmin(2), pmax(2);

            if      (local_map_in->swrnd->value() == 1){
                pmin(0) = pmax(0) = sw;

                pmin(1) = 0.0;
                pmax(1) = 1.0 - sw;
            }
            else if (local_map_in->sornd->value() == 1){
                pmin(1) = pmax(1) = so;

                pmin(0) = 0.0;
                pmax(0) = 1.0 - so;
            }
            else if (local_map_in->sgrnd->value() == 1){
                double sg = 1.0 - sw - so;

                pmin(0) = 0.0;
                pmin(1) = 1.0 - sg;

                pmax(0) = 1.0 - sg;
                pmax(1) = 0.0;
            }

            Curve curve;
            Utilities::regularly_sampled_segment(pmin, pmax, n, curve);

            std::vector<RealVector> line, map;
            for (int i = 0; i < curve.curve.size(); i++){
                line.push_back(curve.curve[i]);

                JetMatrix jm;
                subphysics->flux()->jet(line.back(), jm, 0);

                map.push_back(jm.function());
            }

            {
                Curve2D *c = new Curve2D(line, 1.0, 0.0, 0.0);
                local_map_in->canvas->add(c);
            }
            {
                Curve2D *c = new Curve2D(map, 1.0, 0.0, 0.0);
                map_out->canvas->add(c);
            }


            return;
        }

        static void on_move_map(Fl_Widget *obj, void*){
            Canvas *canvas = (Canvas*)obj;
            MapInWindow *local_map_in = (MapInWindow*)canvas->window();

            RealVector point(2);
            local_map_in->canvas->getxy(point(0), point(1));

            JetMatrix jm;
            subphysics->flux()->jet(point, jm, 0);

            RealVector out = jm.function();

            if (local_map_in->point != 0) local_map_in->canvas->erase(local_map_in->point);
            {
                local_map_in->point = new Curve2D(point, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
                local_map_in->canvas->add(local_map_in->point);
            }

            if (map_out->point != 0) map_out->canvas->erase(map_out->point);
            {
                map_out->point = new Curve2D(out, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
                map_out->canvas->add(map_out->point);
            }

            Fl::check();

            return;
        }

        static void actioncb(Fl_Widget*, void *obj){
            MapInWindow *local_map_in = (MapInWindow*)obj;
            std::cout << "local_map_in = " << (void*)local_map_in << std::endl;

            if (local_map_in->mvrnd->value() == 1){
                local_map_in->canvas->on_move(&MapInWindow::on_move_map, local_map_in, 0);
                local_map_in->canvas->setextfunc(0, local_map_in, 0);
            }
            else {
                if (local_map_in->point != 0) local_map_in->canvas->erase(local_map_in->point);
                local_map_in->canvas->on_move(0, local_map_in, 0);
                local_map_in->canvas->setextfunc(&MapInWindow::on_click_map, local_map_in, 0);
            }
        }

    public:
        Canvas *canvas;
        Curve2D *point;

        Fl_Group *componentgrp;
            Fl_Round_Button *swrnd, *sornd, *sgrnd;

        Fl_Group *actiongrp;
            Fl_Round_Button *mvrnd, *clickrnd;

        MapInWindow(int x, int y, int w, int h, const char *l): Fl_Double_Window(x, y, w, h, l){
            std::cout << "Ctor: " << (void*)this << std::endl;
            point = 0;

            canvas = new Canvas(0, 0, w, h - 80);
            prepare_triangle(canvas);

            componentgrp = new Fl_Group(canvas->x(), canvas->y() + canvas->h() + 10, canvas->w(), 25);
            {
                int rndw = (componentgrp->w() - 40)/3;

                swrnd = new Fl_Round_Button(componentgrp->x() + 10, componentgrp->y(), rndw, 25, "Constant sw");
                swrnd->type(FL_RADIO_BUTTON);
                swrnd->value(1);

                sornd = new Fl_Round_Button(swrnd->x() + rndw + 10, swrnd->y(), rndw, 25, "Constant so");
                sornd->type(FL_RADIO_BUTTON);

                sgrnd = new Fl_Round_Button(sornd->x() + rndw + 10, sornd->y(), rndw, 25, "Constant sg");
                sgrnd->type(FL_RADIO_BUTTON);

            }
            componentgrp->end();
            componentgrp->box(FL_BORDER_BOX);

            actiongrp = new Fl_Group(componentgrp->x(), componentgrp->y() + componentgrp->h() + 10, componentgrp->w(), 25);
            {
                int rndw = (actiongrp->w() - 30)/2;

                mvrnd = new Fl_Round_Button(actiongrp->x() + 10, actiongrp->y(), rndw, 25, "Follow mouse");
                mvrnd->type(FL_RADIO_BUTTON);
                mvrnd->value(1);
                mvrnd->callback(actioncb, this);

                clickrnd = new Fl_Round_Button(mvrnd->x() + mvrnd->w() + 10, mvrnd->y(), rndw, 25, "Compute on click");
                clickrnd->type(FL_RADIO_BUTTON);
                clickrnd->callback(actioncb, this);

                mvrnd->do_callback();
            }
            actiongrp->end();
            actiongrp->box(FL_BORDER_BOX);

            end();

            resizable(canvas);
            callback(wincb);
        }

        virtual ~MapInWindow(){
        }
};

MapInWindow *map_in;

class PermWindow: public Fl_Double_Window {
    private:
    protected:
    public:
        Canvas *canvas;
        Fl_Round_Button *rndmove, *rndclick;

        SegmentedCurve *sc;
        Text *text;

        int type;

        std::vector<double> level;
        std::vector<GraphicObject*> graphicobject;

        PermWindow(int x, int y, int w, int h, const char *l): Fl_Double_Window(x, y, w, h){
            copy_label(l);
            sc = 0;
            text = 0;

            canvas = new Canvas(0, 0, w, h - 10 - 25 - 10);
            prepare_triangle(canvas);

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
            resizable(canvas);
            callback(wincb);
        }

        virtual ~PermWindow(){
        }
};

std::vector<PermWindow*> permwindow;
std::vector<PermWindow*> flowwindow;

class ViscosityInputWindow: public Fl_Double_Window {
    private:
    protected:
        ThreePhaseFlowSubPhysics *subphysics;

        Canvas *canvas;
        Curve2D *viscpoint;
        Text *visctext;

        static void on_move_visc(Fl_Widget *obj, void*){
            Canvas *canvas = (Canvas*)obj;
            ViscosityInputWindow *win = (ViscosityInputWindow*)canvas->window();

            RealVector p(2);
            canvas->getxy(p(0), p(1));

            ThreePhaseFlowSubPhysics *subphysics = win->subphysics;

            if (!subphysics->boundary()->inside(p)) return;

            double muw, muo, mug;
            muw = p(0);            
            muo = p(1);
            mug = 1.0 - muw - muo;

            subphysics->muw()->value(muw);
            subphysics->muo()->value(muo);
            subphysics->mug()->value(mug);

            for (int i = 0; i < parameters.size(); i++){
                parameter_value[i] = parameters[i]->value();
                
                std::stringstream ss;
                ss << parameters[i]->value();
                paraminput[i]->value(ss.str().c_str());
            }

            paramapply->do_callback();

            return;
        }

        static void wincb(Fl_Widget*, void*){return;}
    public:
        ViscosityInputWindow(int x, int y, int w, int h, const char *l, ThreePhaseFlowSubPhysics *s): Fl_Double_Window(x, y, w, h, l), subphysics(s){
            viscpoint = 0;

            canvas = new Canvas(0, 0, w, h);
            canvas->on_move(&ViscosityInputWindow::on_move_visc, this, 0);

            prepare_triangle(canvas);

            end();

            resizable(canvas);
            callback(ViscosityInputWindow::wincb);
        }

        virtual ~ViscosityInputWindow(){
        }
};

void on_move_perm(Fl_Widget *obj, void*){
    Canvas *canvas = (Canvas*)obj;

    PermWindow *pw = (PermWindow*)canvas->window();

    RealVector point(2);
    pw->canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
//    permlevelcurve->curve(point, pw->type, c);
    moblevelcurve->curve(point, pw->type, c);

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
//    ss << permlevelcurve->level(point, pw->type); // TODO: Use only two digits.
    ss << moblevelcurve->level(point, pw->type); // TODO: Use only two digits.

    pw->text = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
    pw->canvas->add(pw->text);

    Fl::check();

    return;
}

void on_move_flow(Fl_Widget *obj, void*){
    Canvas *canvas = (Canvas*)obj;

    PermWindow *pw = (PermWindow*)canvas->window();

    RealVector point(2);
    pw->canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
    flowlevel->curve(point, pw->type, c);

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
    ss << flowlevel->level(point, pw->type); // TODO: Use only two digits.

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
//    permlevelcurve->curve(point, pw->type, c);
    moblevelcurve->curve(point, pw->type, c);

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
//    ss << permlevelcurve->level(point, pw->type); // TODO: Use only two digits.
    ss << moblevelcurve->level(point, pw->type); // TODO: Use only two digits.

    Text *t = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
    canvas->add(t);

//    pw->level.push_back(permlevelcurve->level(point, pw->type));
    pw->level.push_back(moblevelcurve->level(point, pw->type));

    std::cout << "Stored new level: " << pw->level.back() << std::endl;

    pw->graphicobject.push_back(sc);
    pw->graphicobject.push_back(t);

    Fl::check();

    return;
}

void on_click_flow(Fl_Widget *obj, void*){
    PermWindow *pw = (PermWindow*)obj;
    Canvas *canvas = pw->canvas;

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    std::vector<RealVector> c;
    flowlevel->curve(point, pw->type, c);

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
    ss << flowlevel->level(point, pw->type); // TODO: Use only two digits.

    Text *t = new Text(ss.str(), point, shift, 1.0, 0.0, 0.0);
    canvas->add(t);

    pw->level.push_back(flowlevel->level(point, pw->type));

    std::cout << "Stored new level: " << pw->level.back() << std::endl;

    pw->graphicobject.push_back(sc);
    pw->graphicobject.push_back(t);

    Fl::check();

    return;
}

void rmcb(Fl_Widget*, void *obj){
    PermWindow *pw = (PermWindow*)obj;
    pw->canvas->setextfunc(0, pw, 0);
    pw->canvas->on_move(&on_move_perm, pw, 0);

    return;
}

void rmflowcb(Fl_Widget*, void *obj){
    PermWindow *pw = (PermWindow*)obj;
    pw->canvas->setextfunc(0, pw, 0);
    pw->canvas->on_move(&on_move_flow, pw, 0);

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

void rcflowcb(Fl_Widget*, void *obj){
    PermWindow *pw = (PermWindow*)obj;

    // Connect this callback...
    //
    pw->canvas->setextfunc(on_click_flow, pw, 0);

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

void applycb(Fl_Widget*, void*){
    for (int i = 0; i < paraminput.size(); i++){
        std::stringstream ss;
        ss << paraminput[i]->value();

        ss >> parameter_value[i];
        parameters[i]->value(parameter_value[i]);
    }

    // Update the mobilities components.
    //
    for (int i = 0; i < permwindow.size(); i++){
        for (int j = 0; j < permwindow[i]->graphicobject.size(); j++){
            permwindow[i]->canvas->erase(permwindow[i]->graphicobject[j]);
        }
        permwindow[i]->graphicobject.clear();

        for (int j = 0; j < permwindow[i]->level.size(); j++){
            std::vector<RealVector> c;

            moblevelcurve->prepare_grid();
            moblevelcurve->curve(permwindow[i]->level[j], permwindow[i]->type, c);

            if (c.size() == 0) return;

            // Add the curve.
            //
            SegmentedCurve *sc = new SegmentedCurve(c, 0.0, 0.0, 1.0);
            permwindow[i]->canvas->add(sc);
            permwindow[i]->graphicobject.push_back(sc);
        }
    }

    // Update the flow components.
    //
    for (int i = 0; i < flowwindow.size(); i++){
        for (int j = 0; j < flowwindow[i]->graphicobject.size(); j++){
            flowwindow[i]->canvas->erase(flowwindow[i]->graphicobject[j]);
        }
        flowwindow[i]->graphicobject.clear();

        for (int j = 0; j < flowwindow[i]->level.size(); j++){
            std::vector<RealVector> c;

            flowlevel->curve(flowwindow[i]->level[j], flowwindow[i]->type, c);

            if (c.size() == 0) return;

            // Add the curve.
            //
            SegmentedCurve *sc = new SegmentedCurve(c, 0.0, 0.0, 1.0);
            flowwindow[i]->canvas->add(sc);
            flowwindow[i]->graphicobject.push_back(sc);
        }
    }

    return;
}

void cancelcb(Fl_Widget*, void*){
    for (int i = 0; i < paraminput.size(); i++){
        std::stringstream ss;
        ss << parameter_value[i];

        paraminput[i]->value(ss.str().c_str());
    }

    return;
}

int main(){
    std::cout << "Select the subphysics: " << std::endl << std::endl;
    std::cout << "    1. Brooks-Corey." << std::endl;
    std::cout << "    2. CoreyQuad." << std::endl;
    std::cout << "    3. Foam." << std::endl;
    std::cout << "    4. Koval." << std::endl;
    std::cout << "    5. Sorbie." << std::endl;
    std::cout << "    6. Stone." << std::endl;

    int choice;
    while (true){
        std::cin >> choice;

        if (choice >= 1 && choice <= 6) break;
    }

    if      (choice == 1) subphysics = new Brooks_CoreySubPhysics();
    else if (choice == 2) subphysics = new CoreyQuadSubPhysics();
    else if (choice == 3) subphysics = new FoamSubPhysics();
    else if (choice == 4) subphysics = new KovalSubPhysics();
    else if (choice == 5) subphysics = new SorbieSubPhysics();
    else if (choice == 6) subphysics = new StoneSubPhysics();

    permeability = subphysics->permeability();

    // Size.
    //
    int w = 450;
    int h = w + 10 + 25 + 10;

    // Permeabilities.
    //   
    {
        permlevelcurve = new ThreePhaseFlowPermeabilityLevelCurve(subphysics, permeability);
        moblevelcurve = new ThreePhaseFlowMobilityLevelCurve(subphysics);

        std::vector<std::string>       name;
        std::vector<int>               type;
        moblevelcurve->list_of_types(type, name); 

        for (int j = 0; j < name.size(); j++){
            PermWindow *pw = new PermWindow(10 + w*j, 10, w, h, name[j].c_str());
            pw->show();

            // Set type.
            //
            pw->type = type[j];
            
            pw->rndmove->callback(rmcb, pw);
            pw->rndclick->callback(rccb, pw);

            pw->rndmove->do_callback();

            permwindow.push_back(pw);
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
            
            pw->rndmove->callback(rmflowcb, pw);
            pw->rndclick->callback(rcflowcb, pw);

            pw->rndmove->do_callback();

            flowwindow.push_back(pw); //
        }
    }

    // Mapping.
    //
    map_in = new MapInWindow(0, 0, 800, 800, "Map input");
    map_in->show();

    map_out = new MapOutWindow(0, 0, 800, 800, "Map output");
    map_out->show();

    // Parameters.
    //
    {
        subphysics->equation_parameter(parameters);
        
        std::vector<AuxiliaryFunction*> vaf;
        subphysics->auxiliary_functions(vaf);
        for (int i = 0; i < vaf.size(); i++){
            std::vector<Parameter*> vp;
            vaf[i]->parameter(vp);
            
            for (int j = 0; j < vp.size(); j++) parameters.push_back(vp[j]);
        }

        for (int i = 0; i < parameters.size(); i++) parameter_value.push_back(parameters[i]->value());        

        int paramwinh = 10 + parameters.size()*(25 + 10) + 25 + 10;
        int paramwinw = 500;
        paramwin = new Fl_Double_Window(10, 10, paramwinw, paramwinh, "Parameters");
        int px = 50;

        for (int i = 0; i < parameters.size(); i++){
            Fl_Float_Input *input = new Fl_Float_Input(px, 10 + (25 + 10)*i, paramwinw - 10 - px, 25, parameters[i]->name().c_str());
            std::stringstream ss;
            ss << parameters[i]->value();
            input->value(ss.str().c_str());

            paraminput.push_back(input);
        }

        paramapply  = new Fl_Button(10, 35*parameters.size() + 10, (paramwinw - 30)/2, 25, "Apply");
        paramapply->callback(applycb);

        paramcancel = new Fl_Button(paramapply->x() + paramapply->w() + 10, paramapply->y(), paramapply->w(), paramapply->h(), "Cancel");
        paramcancel->callback(cancelcb);

        paramwin->end();
        paramwin->callback(wincb);
        paramwin->show();

        // Viscosity Input.
        //
        ViscosityInputWindow *vw = new ViscosityInputWindow(10, 10, 300, 300, "Viscosity input", subphysics);
        vw->show();
    }

    Fl::scheme("gtk+");

    return Fl::run();
}

