#include "Viscosity_Matrix.h"

void Viscosity_Matrix::fill_viscous_matrix(const RealVector &p, ViscosityJetMatrix &m){
    fill_viscous_matrix(p, m, 0);
    return;
}

void Viscosity_Matrix::fill_viscous_matrix(const RealVector &p, ViscosityJetMatrix &m, int degree){
    if (degree >= 0){
        m.M()(0, 0) = 1.0;
        m.M()(0, 1) = 0.0;
        m.M()(1, 0) = 0.0;
        m.M()(1, 1) = 1.0;

        if (degree >= 1){
            IF_DEBUG
                printf("Viscosity_Matrix::fill_viscous_matrix(): This method is not prepared yet to deal with degrees higher than 0!\n");
            END_DEBUG
            exit(0);

            if (degree >= 2){
            }
        }
    }

    return;
}
    Viscosity_Matrix::Viscosity_Matrix(){}
    Viscosity_Matrix::~Viscosity_Matrix(){}
