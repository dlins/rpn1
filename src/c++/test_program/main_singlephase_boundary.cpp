#include <FL/Fl_Double_Window.H>
#include "canvas.h"
#include "curve2d.h" 

#include "MolarDensity.h"
#include "Thermodynamics.h"
#include "VLE_Flash_TPCW.h"
#include "SinglePhaseBoundary.h"

int main(){
    Fl_Double_Window win(0, 0, 800, 800, "SinglePhase Boundary");
    Canvas canvas(0, 0, win.w(), win.h());
    canvas.xlabel("x");
    canvas.ylabel("y");

    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");
    tc->set_flash(flash);

    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    SinglePhaseBoundary *vapor_boundary = new SinglePhaseBoundary(flash, Theta_min, Theta_max, DOMAIN_IS_VAPOR, &Thermodynamics::Theta2T);

    std::vector<std::vector<RealVector> > pb;
    vapor_boundary->physical_boundary(pb);

    std::cout << pb.size() << std::endl;

    Curve2D *c;
    for (int i = 0; i < pb.size(); i++){
        std::cout << "    " << pb[i].size() << std::endl;
        c = new Curve2D(pb[i], 0.0, 0.0, 0.0);
        canvas.add(c);
    }
    canvas.nozoom();

    win.end();
    win.resizable(&win);
    win.show();

    return Fl::run();
}

