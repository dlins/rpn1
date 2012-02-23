#ifndef _BIFURCATION_CURVE_
#define _BIFURCATION_CURVE_

#include <math.h>
#include "eigen.h"
#include "RealVector.h"
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "Boundary.h"

//#include "RealEigenvalueCell.h"

// TODO: A class Cell is needed, such that it has three or four vertices defined a Cell.
//       Pointers to vertices? How to index them?

// TODO: number_of_cells should be number_of_grid_points throughout.

#ifndef CHARACTERISTIC_ON_CURVE
#define CHARACTERISTIC_ON_CURVE  0
#endif

#ifndef CHARACTERISTIC_ON_DOMAIN
#define CHARACTERISTIC_ON_DOMAIN 1
#endif

class Bifurcation_Curve {
protected:
    double epsilon;

    // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
    void create_grid(const RealVector &pmin, const RealVector &pmax, const int *number_of_cells, Matrix<RealVector> &p);

    // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
    void fill_values_on_grid(const RealVector &pmin, const RealVector &pmax,
            const FluxFunction *ff, const AccumulationFunction *aa,
            const int *number_of_cells,
            Matrix<RealVector> &grid,
            Matrix<RealVector> &ffv, Matrix<RealVector> &aav,
            Matrix< vector<eigenpair> > &e, Matrix< vector<bool> > &eig_is_real);

    // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
    //
    // This function is a mere wrapper for the function above. Perhaps that one should be eliminated.
    void fill_values_on_grid(const RealVector &pmin, const RealVector &pmax,
            const FluxFunction *ff, const AccumulationFunction *aa,
            const int *number_of_grid_pnts,
            Matrix<RealVector> &grid,
            Matrix<RealVector> &ffv, Matrix<RealVector> &aav,
            Matrix< vector<double> > &e, Matrix< vector<bool> > &eig_is_real);

    // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
    //
    // This function is a mere wrapper for the function above. Perhaps that one should be eliminated.
    void fill_values_on_grid(const RealVector &pmin, const RealVector &pmax,
            const FluxFunction *ff, const AccumulationFunction *aa,
            const int *number_of_grid_pnts,
            Matrix<RealVector> &grid,
            Matrix<RealVector> &ffv, Matrix<RealVector> &aav,
            Matrix< vector<eigenpair> > &e, Matrix< vector<bool> > &eig_is_real,
            const Boundary *b, Matrix<bool> &is_inside);

    // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
    //
    // This function is a mere wrapper for the function above. Perhaps that one should be eliminated.
    void fill_values_on_grid(const RealVector &pmin, const RealVector &pmax,
            const FluxFunction *ff, const AccumulationFunction *aa,
            const int *number_of_grid_pnts,
            Matrix<RealVector> &grid,
            Matrix<RealVector> &ffv, Matrix<RealVector> &aav,
            Matrix< vector<double> > &e, Matrix< vector<bool> > &eig_is_real,
            const Boundary *b, Matrix<bool> &is_inside);

    void fill_with_jet(RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H);

    // TODO: The grid is not always rectangular. Cells outside a triangular grid
    // should be marked somehow. Cells lying ON the hypotenuse are triangles, elsewhere are rectangles.
    //        void create_cells(int *number_of_cells, Matrix<RealEigenvalueCell> &mc, Matrix< std::vector<bool> > *eigenvalues_on_the_grid);

    //        void validate_cells(int family, int *number_of_cells, Matrix<RealEigenvalueCell> &mc, Matrix<bool> &mb);
    void validate_cells(int family, bool type_of_cells, Matrix< std::vector<bool> > &original, Matrix<bool> &mb_is_complex, Matrix<bool> &mb_is_inside);

    //TODO: Esta era a original, esta faltando modificar Extension_Curve. Despois este metodo deve sair (Pablo 13/Jan/12)
    void validate_cells(int family, bool type_of_cells, Matrix< std::vector<bool> > &original, Matrix<bool> &mb_is_complex);

    void prepare_cell(int i, int j, int family, Matrix< std::vector<double> > &eigen, Matrix<RealVector> &flux_values, Matrix<RealVector> &accum_values, double *lambda, Matrix<double> &flux, Matrix<double> &accum);

    bool prepare_segment(int i, int family, int where_is_characteristic,
            const std::vector< std::vector<double> > &eigen,
            const std::vector<RealVector> &flux_values,
            const std::vector<RealVector> &accum_values,
            const std::vector<std::vector<bool> > &eig_is_real,
            double *lambda,
            Matrix<double> &flux,
            Matrix<double> &accum);

    void fill_values_on_segments(const FluxFunction *ff, const AccumulationFunction *aa, const std::vector<RealVector> &input,
            std::vector<RealVector> &vff, std::vector<RealVector> &vaa,
            std::vector<std::vector<double> > &vee, std::vector< std::vector<bool> > &eig_is_real);

    template <typename T> void initialize_matrix(int n, int m, T *matrix, T value) {
        for (int i = 0; i < n * m; i++) matrix[i] = value;
        return;
    }

public:
    Bifurcation_Curve();
    ~Bifurcation_Curve();
};

#endif // _BIFURCATION_CURVE_

