#include "Stone_Explicit_Bifurcation_Curves.h"

Stone_Explicit_Bifurcation_Curves::Stone_Explicit_Bifurcation_Curves(StoneFluxFunction *ff){
    f = ff;
}

Stone_Explicit_Bifurcation_Curves::~Stone_Explicit_Bifurcation_Curves(){
}

void Stone_Explicit_Bifurcation_Curves::line(const RealVector &p, const RealVector &q, int nos, std::vector<RealVector> &v){
    v.clear();

    RealVector delta = (q - p)/((double)nos);

    // Add first point (p).
    //
    v.push_back(p);

    // Not starting a 0!!!
    //
    for (int i = 1; i <= nos; i++){
        RealVector temp = p + delta*((double)i);

        // Add two copies of the new point...
        //
        v.push_back(temp);
        v.push_back(temp);
    }

    // ...and remove the second copy of the last point.
    //
    v.pop_back();

    return;
}

void Stone_Explicit_Bifurcation_Curves::expl_sec_bif_crv(int side_opposite_vertex, int nos, 
                                                         std::vector<RealVector> &vertex_to_umbilic, 
                                                         std::vector<RealVector> &umbilic_to_side){
    vertex_to_umbilic.clear();
    umbilic_to_side.clear();

    RealVector permeability_params = f->perm().params().params();

    double expw = permeability_params(0);
    double expg = permeability_params(1);
    double expo = permeability_params(2);

    double expow = permeability_params(3);
    double expog = permeability_params(4);

    double cnw = permeability_params(5);
    double cng = permeability_params(6);
    double cno = permeability_params(7);

    double lw = permeability_params(8);
    double lg = permeability_params(9);

    double low = permeability_params(10);
    double log = permeability_params(11);

    double epsl = permeability_params(12);

    // Only compute the curves in this case.
    //
    if (expw  == 2.0 && expg  == 2.0 && expo == 2.0 &&
        expow == 2.0 && expog == 2.0 &&
        cnw   == 0.0 && cng   == 0.0 && cno == 0.0 &&
        low   == 0.0 && log   == 0.0 &&
        epsl  == 0.0 ){

        // muw, muo, mug
        RealVector flux_params = f->fluxParams().params();

        double muw = flux_params(3);
        double mug = flux_params(4);
        double muo = flux_params(5);

        std::cout << "muw = " << muw << ", muo = " << muo << ", mug = " << mug << std::endl;

        double sum_mu = muw + muo + mug;

        // Umbilic point.
        //
        RealVector umbp(2);
        umbp(0) = muw/sum_mu;
        umbp(1) = muo/sum_mu;

        // Just in case...
        //
        nos = std::max(2, nos);

        // Endpoints
        RealVector vertex(2), point_on_side(2);

        if (side_opposite_vertex == THREE_PHASE_BOUNDARY_SW_ZERO){
            // From the vertex to the umbilic
            vertex(0) = 1.0;
            vertex(1) = 0.0;

            // From the umbilic to the side.
            //
            point_on_side(0) = 0.0;
            point_on_side(1) = muo/(mug + muo);
        }
        else if (side_opposite_vertex == THREE_PHASE_BOUNDARY_SO_ZERO){
            // From the vertex to the umbilic
            vertex(0) = 0.0;
            vertex(1) = 1.0;

            // From the umbilic to the side.
            //
            point_on_side(0) = muw/(muw + mug);
            point_on_side(1) = 0.0;
        }
        else {
            // From the vertex to the umbilic
            vertex(0) = 0.0;
            vertex(1) = 0.0;

            // From the umbilic to the side.
            //
            point_on_side(0) = muw/(muw + muo);
            point_on_side(1) = muo/(muw + muo);
        }

        line(vertex, umbp, nos, vertex_to_umbilic);
        line(umbp, point_on_side, nos, umbilic_to_side);
    }

    return;
}

