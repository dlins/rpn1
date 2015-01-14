#include <string>
#include <fstream>

#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"
#include "Secondary_Bifurcation.h"

int main(int argc, char *argv[]){
    // Boundary.
    //
    Three_Phase_Boundary boundary;

    // ************************* GridValues ************************* //
    RealVector pmin(2); pmin(0) = pmin(1) = 0.0;
    RealVector pmax(2); pmax(0) = pmax(1) = 1.0;

    std::vector<int> number_of_cells(2);
    number_of_cells[0] = number_of_cells[1] = 256;

    GridValues grid(&boundary, pmin, pmax, number_of_cells);
    // ************************* GridValues ************************* //

    // Flux parameters and flux proper.
    //
    // Create fluxFunction
    double expw, expg, expo; expw = expg = 2.0; expo = 2.0;
    double expow, expog;     expow = expog = 2.0;
    double cnw, cng, cno;    cnw = cng = cno = 0.0;
    double lw, lg;           lw = lg = 0.0;
    double low, log;         low = log = 0.0;
    double epsl = 0.0;

    StonePermParams stonepermparams(expw, expg, expo, expow, expog, cnw, cng, cno, lw, lg, low, log, epsl);

    double grw = 1.0; // 1.5 
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    muw = 1.0;
    mug = 0.5;
    muo = 2.0;

    double vel = 1.0; // 0.0

    RealVector p(7);
    p.component(0) = grw;
    p.component(1) = grg;
    p.component(2) = gro;
    p.component(3) = muw;
    p.component(4) = mug;
    p.component(5) = muo;
    p.component(6) = vel;

    StoneParams stoneparams(p);

    StoneFluxFunction flux(stoneparams, stonepermparams);

    // Trivial accumulation
    StoneAccumulation accum;

    // Secondary bifurcation proper.
    //
    Secondary_Bifurcation sb;

    std::vector<RealVector> left_curve; std::vector<RealVector> right_curve;

    sb.curve(&flux, &accum, grid,
             &flux, &accum, grid,
             left_curve, right_curve);

    if (left_curve.size() > 0){
        std::ofstream out("secondary_bifurcation_left.txt", std::ifstream::out);

        out << left_curve.size() << std::endl;
        for (int i = 0; i < left_curve.size(); i++) out << left_curve[i] << std::endl;

        out.close();
    }

    if (right_curve.size() > 0){
        std::ofstream out("secondary_bifurcation_right.txt", std::ifstream::out);

        out << right_curve.size() << std::endl;
        for (int i = 0; i < right_curve.size(); i++) out << right_curve[i] << std::endl;

        out.close();
    }

    return 0;
}

