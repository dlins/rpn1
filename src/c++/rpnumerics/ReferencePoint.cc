#include "ReferencePoint.h"

ReferencePoint::ReferencePoint(){
    f = 0;
    g = 0;
    v = 0;
}

ReferencePoint::ReferencePoint(const RealVector &p,
                               const FluxFunction *ff, const AccumulationFunction *gg,
                               const Viscosity_Matrix *vv){

    point = p;
    f = ff;
    g = gg;
    v = vv;

    fill_point(point, f, g, v);
}

// Set the point. Then fill values at point.
//
void ReferencePoint::fill_point(const RealVector &p){
    point = p;

    fill_point(point, f, g, v);

    return;
}

// Fill values at point.
//
void ReferencePoint::fill_point(const RealVector &p,
                                const FluxFunction *ff, const AccumulationFunction *gg,
                                const Viscosity_Matrix *vv){

    int n = p.size();
    F.resize(n);
    G.resize(n);

    JF.resize(n, n);
    JG.resize(n, n);

    // Fill flux and accumulation at point
    RealVector temp(p);

    if (ff != 0) ff->fill_with_jet(p.size(), temp.components(), 1, F.components(), JF.data(), 0);
    if (gg != 0) gg->fill_with_jet(p.size(), temp.components(), 1, G.components(), JG.data(), 0);

    std::cout << "ReferencePoint::fill_point(): flux & accum" << std::endl;

    // Fill eigenpairs at point
    if (ff != 0 && gg != 0) Eigen::eig(p.size(), JF.data(), JG.data(), e);

    std::cout << "ReferencePoint::fill_point(): eigenpairs" << std::endl;

    // Fill viscosity matrix at point. Notice that this works only for degree = 0 so far. See Viscosity_Matrix::fill_viscous_matrix().
    if (vv != 0) vv->fill_viscous_matrix(p, B, 0);

    std::cout << "ReferencePoint::fill_point(): viscous matrix" << std::endl;

    return;
}

