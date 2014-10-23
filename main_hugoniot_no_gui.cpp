#include <string>
#include <fstream>

#include "Hugoniot_Curve.h"
#include "CoreyQuad.h"
#include "StoneAccumulation.h"
#include "IsoTriang2DBoundary.h"

int main(int argc, char *argv[]){
    std::ifstream in;
    std::ofstream out;

    if (argc == 1){
        in.open("hugoniot_reference_point.txt", std::ifstream::in);
        out.open("hugoniot_curve.txt", std::ifstream::out);
    }
    else if (argc == 3){
        in.open(argv[1], std::ifstream::in);
        out.open(argv[2], std::ifstream::out);
    }
    else {
        std::cout << "Wrong number of parameters. Aborting..." << std::endl;
        return 1;
    }

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
    double grw = 1.0;
    double grg = 1.0;
    double gro = 1.0;

    double muw = 1.0;
    double mug = 1.0;
    double muo = 1.0;

    double vel = 1.0;

    RealVector p(7);
    p.component(0) = grw;
    p.component(1) = grg;
    p.component(2) = gro;
    p.component(3) = muw;
    p.component(4) = mug;
    p.component(5) = muo;
    p.component(6) = vel;

    CoreyQuad_Params params(p);
    CoreyQuad flux(params);

    // Trivial accumulation
    StoneAccumulation accum;

    // Initial point that is transformed into a...
    //
    RealVector initial_point(2);
    in >> initial_point(0);
    in >> initial_point(1);

    // ...ReferencePoint.
    //
    ReferencePoint referencepoint(initial_point, &flux, &accum, 0);

    // Hugoniot proper.
    //
    Hugoniot_Curve hc;

    std::vector<RealVector> hugoniot_curve;
    std::vector< std::deque <RealVector> > curves;
    std::vector <bool> is_circular;
    int method = SEGMENTATION_METHOD;

    hc.curve(&flux, &accum, grid, referencepoint, 
             hugoniot_curve,
             curves, is_circular,
             method);

    // Output.
    //
    if (hugoniot_curve.size() > 0){
        out << hugoniot_curve.size() << std::endl;
        for (int i = 0; i < hugoniot_curve.size(); i++) out << hugoniot_curve[i] << std::endl;
    }

    // Finish.
    //
    in.close();
    out.close();

    return 0;
}

