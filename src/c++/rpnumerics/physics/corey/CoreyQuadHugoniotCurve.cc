#include "CoreyQuadHugoniotCurve.h"

CoreyQuadHugoniotCurve::CoreyQuadHugoniotCurve(const CoreyQuad *ff, const AccumulationFunction *aa, Stone_Explicit_Bifurcation_Curves *s, const Boundary *b) : HugoniotCurve((FluxFunction*)ff, aa, b){
    G_vertex.resize(2);
    G_vertex(0) = 0.0;
    G_vertex(1) = 0.0;

    W_vertex.resize(2);
    W_vertex(0) = 1.0;
    W_vertex(1) = 0.0;

    O_vertex.resize(2);
    O_vertex(0) = 0.0;
    O_vertex(1) = 1.0;

    flux = ff;
    sebc = s;
}

CoreyQuadHugoniotCurve::~CoreyQuadHugoniotCurve(){
}

// This method will move upwards to ThreePhaseFlowPhysics.
//
RealVector CoreyQuadHugoniotCurve::project(const RealVector &p, int type){
    // Decide what the case is.
    //
    RealVector flux_params = flux->fluxParams().params();

    RealVector mu(3);
    mu(0) = flux_params(3); // muw
    mu(2) = flux_params(4); // mug
    mu(1) = flux_params(5); // muo

    // This will be used later to form the reference point.
    //
    RealVector proj(2);

    switch (type){
        // Purely geometric case, which seems to be perfectly well here.
        //
        case COREYQUADHUGONIOTCURVE_G_VERTEX:
        {
            proj = G_vertex;

            break;
        }
        // Purely geometric case, which seems to be perfectly well here.
        //
        case COREYQUADHUGONIOTCURVE_W_VERTEX:
        {
            proj = W_vertex;

            break;
        }
        // Purely geometric case, which seems to be perfectly well here.
        //
        case COREYQUADHUGONIOTCURVE_O_VERTEX:
        {
            proj = O_vertex;

            break;
        }
        // Purely geometric case, which seems to be perfectly well here.
        //
        case COREYQUADHUGONIOTCURVE_GW_SIDE:
        {
            proj = project_point_onto_line_2D(p, G_vertex, W_vertex);

            break;
        }
        // Purely geometric case, which seems to be perfectly well here.
        //
        case COREYQUADHUGONIOTCURVE_WO_SIDE:
        {
            proj = project_point_onto_line_2D(p, W_vertex, O_vertex);

            break;
        }
        // Purely geometric case, which seems to be perfectly well here.
        //
        case COREYQUADHUGONIOTCURVE_GO_SIDE:
        {
            proj = project_point_onto_line_2D(p, G_vertex, O_vertex);

            break;
        }
        // This case may need to move elsewhere. Perhaps closer to the ExplicitHugoniot
        // or to the specific Physics, and not in the superphysics. This applies to all
        // the bifurcations, as well as the umbilic, and as a by-product, to the generic point.
        //
        case COREYQUADHUGONIOTCURVE_G_BIFURCATION:
        {
            RealVector vertex, point_on_side;

            sebc->vertex_and_side(THREE_PHASE_BOUNDARY_SG_ZERO, mu, vertex, point_on_side);
            proj = project_point_onto_line_2D(p, vertex, point_on_side);

            break;
        }
        // This case may need to move elsewhere. Perhaps closer to the ExplicitHugoniot
        // or to the specific Physics, and not in the superphysics. This applies to all
        // the bifurcations, as well as the umbilic, and as a by-product, to the generic point.
        //
        case COREYQUADHUGONIOTCURVE_W_BIFURCATION:
        {
            RealVector vertex, point_on_side;

            sebc->vertex_and_side(THREE_PHASE_BOUNDARY_SW_ZERO, mu, vertex, point_on_side);
            proj = project_point_onto_line_2D(p, vertex, point_on_side);

            break;
        }
        // This case may need to move elsewhere. Perhaps closer to the ExplicitHugoniot
        // or to the specific Physics, and not in the superphysics. This applies to all
        // the bifurcations, as well as the umbilic, and as a by-product, to the generic point.
        //
        case COREYQUADHUGONIOTCURVE_O_BIFURCATION:
        {
            RealVector vertex, point_on_side;

            sebc->vertex_and_side(THREE_PHASE_BOUNDARY_SO_ZERO, mu, vertex, point_on_side);
            proj = project_point_onto_line_2D(p, vertex, point_on_side);

            break;
        }
        // This case may need to move elsewhere. Perhaps closer to the ExplicitHugoniot
        // or to the specific Physics, and not in the superphysics. This applies to all
        // the bifurcations, as well as the umbilic, and as a by-product, to the generic point.
        //
        case COREYQUADHUGONIOTCURVE_UMBILIC_POINT:
        {
            double inv_sum = 1.0/sum(mu);

            proj(0) = mu(0)*inv_sum;
            proj(1) = mu(1)*inv_sum;

            break;
        }
        // This case may need to move elsewhere. Perhaps closer to the ExplicitHugoniot
        // or to the specific Physics, and not in the superphysics. This applies to all
        // the bifurcations, as well as the umbilic, and as a by-product, to the generic point.
        //
        default: // COREYQUADHUGONIOTCURVE_GENERIC_POINT
        {
            proj = p;

            break;
        }
    }

    return proj;
}


