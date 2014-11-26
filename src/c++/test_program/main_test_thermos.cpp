#include <stdio.h>

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Choice.H>
#include "canvas.h"
#include "curve2d.h"

#include "Thermodynamics.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

// FLTK's
Fl_Double_Window *win;
    Fl_Choice    *choice;
    Canvas       *canvas0, *canvas1, *canvas2;

// Thermos
Thermodynamics                                *tc;
Thermodynamics_SuperCO2_WaterAdimensionalized *th;

void choicecb(Fl_Widget*, void*){
    canvas0->clear();
    canvas1->clear();
    canvas2->clear();

    int val = choice->value();

    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    double n = 500;
    double delta = (Theta_max - Theta_min)/(double)n;

    std::vector<Point2D> tc_p0, tc_p1, tc_p2;
    std::vector<Point2D> th_p0, th_p1, th_p2;

    JetMatrix j(1);
    double g0, g1, g2;

    for (int i = 0; i < n; i++){
        double Theta = Theta_min + delta*(double)i;

        if (val == 0){
            tc->RockEnthalpyVol_jet(Theta, 2, j);
            th->Diff_RockEnthalpyVol(Theta, g0, g1, g2);
        }
        else if (val == 1){
            tc->SuperCriticEnthalpyVol_jet(Theta, 2, j);
            th->Diff_SuperCriticEnthalpyVol(Theta, g0, g1, g2);
        }
        else if (val == 2){
            tc->AqueousEnthalpyVol_jet(Theta, 2, j);
            th->Diff_AqueousEnthalpyVol(Theta, g0, g1, g2);
        }
        else if (val == 3){
            tc->Rhosic_jet(Theta, 2, j);
            th->Diff_Rhosic(Theta, g0, g1, g2);
        }
        else if (val == 4){
            tc->Rhosiw_jet(Theta, 2, j);
            th->Diff_Rhosiw(Theta, g0, g1, g2);
        }
        else if (val == 5){
            tc->Rhoac_jet(Theta, 2, j);
            th->Diff_Rhoac(Theta, g0, g1, g2);
        }
        else if (val == 6){
            tc->Rhoaw_jet(Theta, 2, j);
            th->Diff_Rhoaw(Theta, g0, g1, g2);
        }

        tc_p0.push_back(Point2D(Theta, j.get(0)));
        tc_p1.push_back(Point2D(Theta, j.get(0, 0)));
        tc_p2.push_back(Point2D(Theta, j.get(0, 0, 0)));

        th_p0.push_back(Point2D(Theta, g0));
        th_p1.push_back(Point2D(Theta, g1));
        th_p2.push_back(Point2D(Theta, g2));
    }

    // Thermodynamics_Common
    Curve2D *tc_c0 = new Curve2D(tc_p0, 1, 0, 0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS);
    canvas0->add(tc_c0);

    Curve2D *tc_c1 = new Curve2D(tc_p1, 1, 0, 0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS);
    canvas1->add(tc_c1);

    Curve2D *tc_c2 = new Curve2D(tc_p2, 1, 0, 0, CURVE2D_SOLID_LINE | CURVE2D_MARKERS);
    canvas2->add(tc_c2);

    // Thermodynamics_SuperCO2_WaterAdimensionalized
    Curve2D *th_c0 = new Curve2D(th_p0, 0, 0, 1);
    canvas0->add(th_c0);

    Curve2D *th_c1 = new Curve2D(th_p1, 0, 0, 1);
    canvas1->add(th_c1);

    Curve2D *th_c2 = new Curve2D(th_p2, 0, 0, 1);
    canvas2->add(th_c2);

    return;
}

int main(){
    // Thermodynamics_Common
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // Thermodynamics_SuperCO2_WaterAdimensionalized
    th = new Thermodynamics_SuperCO2_WaterAdimensionalized(std::string("./c++/rpnumerics/physics/tpcw/"), 
                                                           Thermodynamics::T_typical(), 
                                                           Thermodynamics::Rho_typical(), 
                                                           Thermodynamics::U_typical());

    int info_th = th->status_after_init();
    if (info_th != SPLINE_OK){
        printf("Thermodynamics_SuperCO2_WaterAdimensionalized: There was a problem when creating the splines.\n");
        exit(0);
    }

    int main_w  = Fl::w();
    int main_h  = Fl::h();

    win = new Fl_Double_Window((Fl::w() - main_w)/2, (Fl::h() - main_h)/2, main_w, main_h, "Test Thermos");
    {
        int canvas_w = (main_w - 40)/3;
        int canvas_h = main_h - 10 - 10 - 25 - 10;

        canvas0 = new Canvas(10, 10, canvas_w, canvas_h);
        canvas0->color(FL_DARK_CYAN);
        canvas0->labelcolor(FL_WHITE);

        canvas1 = new Canvas(canvas0->x() + canvas0->w() + 10, canvas0->y(), canvas0->w(), canvas0->h());
        canvas1->color(FL_DARK_CYAN);
        canvas1->labelcolor(FL_WHITE);

        canvas2 = new Canvas(canvas1->x() + canvas1->w() + 10, canvas1->y(), canvas1->w(), canvas1->h());
        canvas2->color(FL_DARK_CYAN);
        canvas2->labelcolor(FL_WHITE);

        choice = new Fl_Choice(10 + 40, canvas0->y() + canvas0->h() + 10, main_w - 20 - 40, 25, "Jet:");
        choice->add("RockEnthalpyVol");
        choice->add("SuperCriticEnthalpyVol");
        choice->add("AqueousEnthalpyVol");
        choice->add("Rhosic");
        choice->add("Rhosiw");
        choice->add("Rhoac");
        choice->add("Rhoaw");

        choice->callback(choicecb);
    }
    win->end();
    win->resizable(win);

    win->show();

    Fl::scheme("gtk+");

    return Fl::run();
}

