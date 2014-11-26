#include "Brooks_CoreySubPhysics.h"
#include "CoreyQuadSubPhysics.h"
#include "SorbiePermeability.h"

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include "canvas.h"
#include "MultiColoredCurve.h"
#include "WaveCurvePlot.h"
#include "LSODE.h"
#include "Text.h"
#include "ThreePhaseFlowPermeabilityLevelCurve.h"
#include "segmentedcurve.h"

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

ThreePhaseFlowSubPhysics *subphysics;
MultiColoredCurve *mcc = 0;
WaveCurvePlot *wcp = 0;

SegmentedCurve *scw = 0;
SegmentedCurve *sco = 0;
SegmentedCurve *scg = 0;

ThreePhaseFlowPermeability *permeability;

void on_move_Hugoniot(Fl_Widget*, void*){
    timer tm;
    tm.reset();

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

    if (mcc != 0) canvas->erase(mcc);

    ReferencePoint ref(point, subphysics->flux(), subphysics->accumulation(), 0);

    std::vector<HugoniotCurve*> Hugoniot_methods;
    subphysics->list_of_Hugoniot_methods(Hugoniot_methods);

    std::vector<HugoniotPolyLine> classified_curve;
    Hugoniot_methods[0]->curve(ref, 0, classified_curve);

    if (classified_curve.size() > 0){
        mcc = new MultiColoredCurve(classified_curve, -100.0, 100.0, 100);
        canvas->add(mcc);
    }

    double elapsed_time = tm.elapsed();
    total_time += elapsed_time;
    n++;

    std::stringstream ss;
    ss << "Hugoniot, elapsed time = " << elapsed_time << ", mean time = " << total_time/(double)n;
    win->copy_label(ss.str().c_str());

    Fl::check();

    std::cout << "Done" << std::endl;

    return;
}

void on_move_rarefaction(Fl_Widget*, void*){
    timer tm;
    tm.reset();

    RarefactionCurve *rarefaction_curve = subphysics->rarefaction_curve();
    if (rarefaction_curve == 0) return;
    
    int increase = RAREFACTION_SPEED_SHOULD_INCREASE;
    
    int family   = 0;
    
    int dimension = subphysics->boundary()->minimums().size();

    RealVector initial_point(dimension);
    canvas->getxy(initial_point(0), initial_point(1));

    if (dimension == 3) initial_point(2) = 1.0;

    LSODE lsode;
    ODE_Solver *odesolver = &lsode;

    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    RealVector final_direction;

    int edge;

    int info_rar = rarefaction_curve->curve(initial_point,
                                            family,
                                            increase,
                                            INTEGRAL_CURVE,
                                            RAREFACTION_INITIALIZE,
                                            0,
                                            odesolver,
                                            deltaxi,
                                            rarcurve,
                                            inflection_point,
                                            final_direction,
                                            rar_stopped_because,
                                            edge);
                                            
    if (rarcurve.curve.size() > 0){
        WaveCurve w;
        w.wavecurve.push_back(rarcurve);

        if (wcp != 0) canvas->erase(wcp);
        wcp = new WaveCurvePlot(w, CURVE2D_SOLID_LINE | CURVE2D_ARROWS, 0.0, 2.0, 10);

        canvas->add(wcp);
    }

    double elapsed_time = tm.elapsed();
    total_time += elapsed_time;
    n++;

    std::stringstream ss;
    ss << "Hugoniot, elapsed time = " << elapsed_time << ", mean time = " << total_time/(double)n;
    win->copy_label(ss.str().c_str());

    return;
}

void on_move_perm(Fl_Widget*, void*){
    timer tm;
    tm.reset();

    RealVector point(2);
    canvas->getxy(point(0), point(1));

    if (!subphysics->boundary()->inside(point)) return;

//    ThreePhaseFlowPermeabilityLevelCurve p(subphysics);
    ThreePhaseFlowPermeabilityLevelCurve p(subphysics, permeability);

    {
    std::vector<RealVector> c;
    p.curve(point, WATER_PERMEABILITY_CURVE, c);

    if (c.size() == 0) return;

    if (scw != 0) canvas->erase(scw);
    scw = new SegmentedCurve(c, 0.0, 0.0, 1.0);
    canvas->add(scw);
    }

    {
    std::vector<RealVector> c;
    p.curve(point, OIL_PERMEABILITY_CURVE, c);

    if (c.size() == 0) return;

    if (sco != 0) canvas->erase(sco);
    sco = new SegmentedCurve(c, 1.0, 0.0, 0.0);
    canvas->add(sco);
    }

    double elapsed_time = tm.elapsed();
    total_time += elapsed_time;
    n++;

    std::stringstream ss;
    ss << "Permeability, elapsed time = " << elapsed_time << ", mean time = " << total_time/(double)n;
    win->copy_label(ss.str().c_str());

    Fl::check();

    return;
}

int main(){
    subphysics = new Brooks_CoreySubPhysics;
//    subphysics = new CoreyQuadSubPhysics;
    permeability = subphysics->permeability();

    permeability = new SorbiePermeability(0); // TODO Change this, create SorbieSubPhysics.

    win = new Fl_Double_Window(10, 10, 800, 800, "Follow");
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

        canvas->on_move(&on_move_Hugoniot, canvas, 0);
//        canvas->on_move(&on_move_rarefaction, canvas, 0);
        canvas->on_move(&on_move_perm, canvas, 0);

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
    }
    win->end();
    win->show();

    return Fl::run();
}

