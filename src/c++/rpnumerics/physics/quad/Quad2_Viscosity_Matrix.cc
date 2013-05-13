/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2_Viscosity_Matrix.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Quad2_Viscosity_Matrix.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


Quad2_Viscosity_Matrix::Quad2_Viscosity_Matrix(const RealVector & viscosityElements) :
f1(viscosityElements(0)), g1(viscosityElements(1)),
f2(viscosityElements(2)), g2(viscosityElements(3)) {

}



void Quad2_Viscosity_Matrix::fill_viscous_matrix(const RealVector &p, ViscosityJetMatrix &m, int degree) {
    if (degree >= 0) {
        std::cout<<"Preenchendo em quad2 v matrix"<<std::endl;
        m.M()(0, 0) = f1;
        m.M()(0, 1) = g1;
        m.M()(1, 0) = f2;
        m.M()(1, 1) = g2;

        if (degree >= 1) {
            printf("Viscosity_Matrix::fill_viscous_matrix(): This method is not prepared yet to deal with degrees higher than 0!\n");
            exit(0);

            if (degree >= 2) {
            }
        }
    }

    return;
}

bool Quad2_Viscosity_Matrix::is_identity(void) {
    return false;
}