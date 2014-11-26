#include "StoneSubPhysics.h"


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
#include "segmentedcurve.h"
#include "MultiColoredCurve.h"

Fl_Double_Window *win;
    Canvas *canvas;

Fl_Double_Window *optionwin;
    Fl_Round_Button *hug, *eql;

SubPhysics *subphysics;

std::vector<std::vector<Curve> > hugoniots;
std::vector<ReferencePoint>      reference;

void hugcb(Fl_Widget*, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    ReferencePoint ref(p, subphysics->flux(), subphysics->accumulation(), 0);

    std::vector<HugoniotCurve*> Hugoniot_methods;
    std::vector<int>            shock_cases;
    std::vector<std::string>    names;
    subphysics->list_of_Hugoniot_methods(Hugoniot_methods);
    subphysics->shock_cases(shock_cases, names);

    HugoniotCurve *hugoniot = Hugoniot_methods[0];
    int type = shock_cases[0];

//    std::vector<HugoniotPolyLine> classified_curve;
//    hugoniot->curve(ref, type, classified_curve);

    std::vector<Curve> c;
    hugoniot->curve(ref, type, c);

    if (c.size() > 0){
        hugoniots.push_back(c);
        reference.push_back(ref);
        for (int i = 0; i < c.size(); i++){
            SegmentedCurve *mcc = new SegmentedCurve(c[i].curve, 0.0, 0.0, 1.0);
            canvas->add(mcc);
        }
    }

    return;
}

void eqlcb(Fl_Widget*, void*){
    RealVector p(2);
    canvas->getxy(p(0), p(1));

    RealVector closest_point;
    ReferencePoint ref;
    int pos;

    if (hugoniots.size() > 0){
        int index_p0, index_p1;

        for (int i = 0; i < hugoniots.size(); i++){
            ref = reference[i];
            pos = i;

            for (int j = 0; j < hugoniots[i].size(); j++)
            Utilities::pick_point_from_segmented_curve(hugoniots[i][j].curve, p, closest_point, index_p0, index_p1);
        }
    }

    std::vector<HugoniotCurve*> Hugoniot_methods;
    subphysics->list_of_Hugoniot_methods(Hugoniot_methods);
    std::vector<RealVector> ep;
    Hugoniot_methods[0]->equilibria(hugoniots[pos], ref, closest_point, ep);

    if (ep.size() > 0){
        ep.push_back(p);
        Curve2D *c = new Curve2D(ep, 1.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvas->add(c);
    }

    return;
}

void ocb(Fl_Widget*, void*){
    if (hug->value() == 1) canvas->setextfunc(hugcb, canvas, 0);
    else                   canvas->setextfunc(eqlcb, canvas, 0);

    return;
}

int main(){
    subphysics = new StoneSubPhysics;

    win = new Fl_Double_Window(10, 10, 800, 800, "Equilibria");
        canvas = new Canvas(0, 0, win->w(), win->h());

        std::vector<std::vector<RealVector> > b;
        subphysics->boundary()->physical_boundary(b);

        for (int i = 0; i < b.size(); i++){
            if (b[i].size() > 1){
                Curve2D *c = new Curve2D(b[i], 0.0, 0.0, 0.0);

                canvas->add(c);
            }
        }

        //canvas->set_transform_matrix(subphysics->transformation_matrix().data());
        canvas->nozoom();

        canvas->xlabel(subphysics->xlabel().c_str());
        canvas->ylabel(subphysics->ylabel().c_str());

//        
    win->end();
    win->show();

    optionwin = new Fl_Double_Window(win->x() + win->w() + 1, win->y(), 200, 80, "Options");
    {
        hug = new Fl_Round_Button(10, 10, optionwin->w() - 20, 25, "Hugoniot");
        hug->type(FL_RADIO_BUTTON);
        hug->value(1);
        hug->callback(ocb);

        eql = new Fl_Round_Button(hug->x(), hug->y() + hug->h() + 10, hug->w(), hug->h(), "Equilibria");
        eql->type(FL_RADIO_BUTTON);
        eql->value(0);
        eql->callback(ocb);

        ocb(0, 0);
    }
    optionwin->end();
    optionwin->show();

    return Fl::run();
}

