#include "CoreyQuadSubPhysics.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include "canvas.h"
#include "MultiColoredCurve.h"
#include "WaveCurvePlot.h"
#include "LSODE.h"
#include "Text.h"

#include <ctime>
#include <sys/time.h>

int n = 0;
double total_time = 0.0;

class timer {
public:
    timer() { 
        reset(); 
    }

    void reset(void) { 
        m_reset = time(); 
    }

    double elapsed(void) { 
        return time() - m_reset; 
    }

    double time(void) {
        struct timeval v;
        gettimeofday(&v, (struct timezone *) NULL);
        return v.tv_sec + v.tv_usec/1.0e6;
    }
private:
    double m_reset;
};


Fl_Double_Window *win;
    Canvas *canvas;

SubPhysics *subphysics;
MultiColoredCurve *mcc = 0;
WaveCurvePlot *wcp = 0;

void wccb(Fl_Widget*, void*){
//    timer tm;
//    tm.reset();

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    WaveCurveFactory *w = subphysics->wavecurvefactory();

    WaveCurve hwc;

    int family = 0;
    int increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    int wavecurve_stopped_because, edge;

    w->wavecurve(COREYQUADWAVECURVEFACTORY_WO_SIDE, point, family, increase, subphysics->Hugoniot_continuation(), hwc, 
                 wavecurve_stopped_because, edge);

    if (hwc.wavecurve.size() > 0){
        WaveCurvePlot *wcp = new WaveCurvePlot(hwc, point);
        canvas->add(wcp);
    }

    return;
}

int main(){
    subphysics = new CoreyQuadSubPhysics;

    win = new Fl_Double_Window(10, 10, 800, 800, "CoreyQuadWaveCurveFactory");
    {
        canvas = new Canvas(0, 0, win->w(), win->h());

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

        canvas->setextfunc(&wccb, canvas, 0);

        {
            RealVector pos(2);
            pos(0) = 0.0;
            pos(1) = 0.0;

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
            pos(1) = 1.0;

            RealVector shift(2);
            shift(0) = -10.0;
            shift(1) =  10.0;

            Text *t = new Text(std::string("O"), pos, shift, 1.0, 0.0, 0.0);
            canvas->add(t);
        }
    }
    win->end();
    win->show();

//    {
//        WaveCurveFactory *w = subphysics->wavecurvefactory();

//        WaveCurve hwc;

//        int family = 0;
//        int increase = RAREFACTION_SPEED_SHOULD_INCREASE;
//        int wavecurve_stopped_because, edge;

//        RealVector point(2);

//        w->wavecurve(COREYQUADWAVECURVEFACTORY_GW_SIDE, point, family, increase, subphysics->Hugoniot_continuation(), hwc, 
//                     wavecurve_stopped_because, edge);

//        if (hwc.wavecurve.size() > 0){
//            WaveCurvePlot *wcp = new WaveCurvePlot(hwc, point);
//            canvas->add(wcp);
//        }
//    }

    {
        WaveCurveFactory *w = subphysics->wavecurvefactory();

        std::vector<int> type;
        std::vector<std::string> name;

        w->list_of_initial_points(type, name);
        for (int i = 0; i < type.size(); i++){
            std::cout << "type[" << i << "] = " << type[i] << ", name[" << i << "] = \"" << name[i] << "\"" << std::endl;
        }
    }

    return Fl::run();
}

