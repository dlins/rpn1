#ifndef _BIFURCATION_CURVETPCW_
#define _BIFURCATION_CURVETPCW_

#include <math.h>
#include "eigen.h"
#include "RealVector.h"
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
//#include "RealEigenvalueCell.h"

// TODO: A class Cell is needed, such that it has three or four vertices defined a Cell.
//       Pointers to vertices? How to index them?

// TODO: number_of_cells should be number_of_grid_points throughout.

class Bifurcation_CurveTPCW {
    protected:
        double epsilon;

        // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
        void create_grid(const RealVector &pmin, const RealVector &pmax, const int *number_of_cells, Matrix<RealVector> &p);

        // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
        void fill_values_on_grid(const RealVector &pmin, const RealVector &pmax, 
                                 const FluxFunction *ff, const AccumulationFunction *aa, 
                                 const FluxFunction *Redff, const AccumulationFunction *Redaa, 
                                 const int *number_of_cells,
                                 Matrix<RealVector> &grid,
                                 Matrix<RealVector> &ffv, Matrix<RealVector> &aav, 
                                 Matrix< vector<eigenpair> > &e, Matrix< vector<bool> > &eig_is_real);

        // TODO: Domains could be triangles. In that case it is necessary to understand what to do with pmin & pmax
        // 
        // This function is a mere wrapper for the function above. Perhaps that one should be eliminated.
        void fill_values_on_grid(const RealVector &pmin, const RealVector &pmax, 
                                 const FluxFunction *ff, const AccumulationFunction *aa, 
                                 const FluxFunction *Redff, const AccumulationFunction *Redaa, 
                                 const int *number_of_grid_pnts,
                                 Matrix<RealVector> &grid,
                                 Matrix<RealVector> &ffv, Matrix<RealVector> &aav, 
                                 Matrix< vector<double> > &e, Matrix< vector<bool> > &eig_is_real);

        void fill_with_jet(RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H);

        // TODO: The grid is not always rectangular. Cells outside a triangular grid
        // should be marked somehow. Cells lying ON the hypotenuse are triangles, elsewhere are rectangles.
//        void create_cells(int *number_of_cells, Matrix<RealEigenvalueCell> &mc, Matrix< std::vector<bool> > *eigenvalues_on_the_grid);

//        void validate_cells(int family, int *number_of_cells, Matrix<RealEigenvalueCell> &mc, Matrix<bool> &mb);
        void validate_cells(int family, bool type_of_cells, Matrix< std::vector<bool> > &original, Matrix<bool> &mb_is_complex);

        void prepare_cell(int i, int j, int family, Matrix< std::vector<double> > &eigen, Matrix<RealVector> &flux_values, Matrix<RealVector> &accum_values, double *lambda, Matrix<double> &flux, Matrix<double> &accum);

        void fill_values_on_curve(const FluxFunction *ff, const AccumulationFunction *aa, const std::vector<RealVector> &input, 
                                  std::vector<RealVector> &vff, std::vector<RealVector> &vaa, 
                                  std::vector<std::vector<double> > &vee, std::vector< std::vector<bool> > &eig_is_real);

    public:
        Bifurcation_CurveTPCW();
        ~Bifurcation_CurveTPCW();
};

#endif // _BIFURCATION_CURVETPCW_

