#include <FL/Fl.H>
#include <FL/x.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Group.H>
#include <FL/Fl_Tabs.H>
#include <FL/Fl_Spinner.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Round_Button.H>
#include <FL/Fl_Float_Input.H>
#include <FL/fl_ask.H>

#include <vector>
#include <deque>
#include <sstream>
using namespace std;

#include <stdio.h>
#include <stdlib.h>

#include "canvasmenuscroll.h"
#include "canvas.h"
#include "point2d.h"
#include "curve2d.h"
#include "quiverplot.h"
#include "segmentedcurve.h"
#include "WaveCurvePlot.h"

#include "Rarefaction.h"
#include "Hugoniot_TP.h"

#include "RectBoundary.h"
#include "SinglePhaseBoundary.h"
#include "Thermodynamics_Common.h"

#include "FluxSinglePhaseVaporAdimensionalized_Params.h"
#include "FluxSinglePhaseVaporAdimensionalized.h"

#include "AccumulationSinglePhaseVaporAdimensionalized_Params.h"
#include "AccumulationSinglePhaseVaporAdimensionalized.h"

#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

int main(int argc, char *argv[]){
    // Flash
    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);


    // Thermo
    double mc = 0.044, mw = 0.018;
    Thermodynamics_Common *thermo = new Thermodynamics_Common(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");
    thermo->set_flash(flash);

    double abs_perm = 3e-12; 
    double sin_beta = 0.0;
    double const_gravity = 9.8;
    bool has_gravity = false;
    bool has_horizontal = true;

    RealVector fpp(12);
    fpp.component(0) = abs_perm;
    fpp.component(1) = sin_beta;
    fpp.component(2) = (double)has_gravity;
    fpp.component(3) = (double)has_horizontal;
    
    fpp.component(4) = 0.0;
    fpp.component(5) = 0.0;
    fpp.component(6) = 2.0;
    fpp.component(7) = 2.0;
    fpp.component(8) = 0.38;
    fpp.component(9) = 304.63;
    fpp.component(10) = 998.2;
    fpp.component(11) = 4.22e-3;

    Flux2Comp2PhasesAdimensionalized_Params fp(fpp, thermo);
    Flux2Comp2PhasesAdimensionalized flux(fp); printf("Flux at: %p\n", &flux);

    double phi = 0.38;

    Accum2Comp2PhasesAdimensionalized_Params ap(thermo, phi);
    Accum2Comp2PhasesAdimensionalized accum(ap); printf("Accum at: %p\n", &accum);

    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector min(3), max(3);

    min.component(0) = 0.0;
    min.component(1) = Theta_min;
    min.component(2) = 0.0;

    max.component(0) = 1.0;
    max.component(1) = Theta_max;
    max.component(2) = 2.0;

    RealVector F(3), A(3);

    std::cout << "*** will compute ***" << std::endl;

    flux.fill_with_jet(min.size(), min.components(), 0, F.components(), 0, 0);
    std::cout << "F = " << F << std::endl;

    accum.fill_with_jet(min.size(), min.components(), 0, A.components(), 0, 0);
    std::cout << "A = " << A << std::endl;

    return 0;
}

