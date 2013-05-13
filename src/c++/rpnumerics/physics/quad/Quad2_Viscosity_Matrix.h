/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2_Viscosity_Matrix.h
 */

#ifndef _Quad2_Viscosity_Matrix_H
#define _Quad2_Viscosity_Matrix_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Viscosity_Matrix.h"
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Quad2_Viscosity_Matrix : public Viscosity_Matrix {
private:

    double f1, g1, f2, g2;


public:
    Quad2_Viscosity_Matrix(const RealVector &);


    void fill_viscous_matrix(const RealVector &p, ViscosityJetMatrix &m, int degree);

    bool is_identity(void);

};

#endif //! _Quad2_Viscosity_Matrix_H
