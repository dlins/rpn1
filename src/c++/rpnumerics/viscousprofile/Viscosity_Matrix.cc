#include "Viscosity_Matrix.h"

void Viscosity_Matrix::fill_viscous_matrix(const RealVector &p, Matrix<double> &m){
    m.resize(2, 2);

    m(0, 0) = m(1, 1) = 1.0;
    m(1, 0) = m(0, 1) = 0.0;

    return;
}

