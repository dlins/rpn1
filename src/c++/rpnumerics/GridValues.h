#ifndef _GRIDVALUES_
#define _GRIDVALUES_

#include <stdio.h>
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "RealVector.h"
#include "eigen.h"
//#include "Boundary.h"

class Boundary;

#include "Matrix.h"

// The definitions below will help the Contour to decide whether a cell is to be
// processed (and if so, how) or not.
//
#ifndef CELL_IS_INVALID
#define CELL_IS_INVALID 0
#endif

#ifndef CELL_IS_TRIANGLE    // Lower triangle
#define CELL_IS_TRIANGLE 1
#endif

#ifndef CELL_IS_SQUARE
#define CELL_IS_SQUARE 2
#endif

class GridValues {
    private:
    protected:
        // Fill the bare minimum of the grid values.
        //
        void fill_values_on_grid(const Boundary *b, 
                                 const RealVector &min, const RealVector &max,
                                 const std::vector<int> &number_of_cells);
    public:
        // Values on the grid. Every value MUST have a boolean companion that can be used
        // to know if the values are already computed or not.

        Matrix<RealVector>               grid;                       // Grid proper
        Matrix<bool>                     point_inside;               // The point belongs to the domain (verified via Boundary)
        Matrix<int>                      cell_type;                  // One of CELL_IS_INVALID, CELL_IS_TRIANGLE (x)or CELL_IS_SQUARE.
        bool                             grid_computed;              // Already computed?

        Matrix<RealVector>               F_on_grid;                  // Flux
        Matrix<RealVector>               G_on_grid;                  // Accumulation
        bool                             functions_on_grid_computed; // Already computed?

        Matrix< Matrix<double> >         JF_on_grid;                 // Jacobians of the flux
        Matrix< Matrix<double> >         JG_on_grid;                 // Jacobians of the accumulation
        bool                             Jacobians_on_grid_computed; // Already computed?

        Matrix< std::vector<double> >    dd;                         // Directional derivatives
        bool                             dd_computed;                // Already computed?

        Matrix< std::vector<eigenpair> > e;                          // Eigenpairs
        Matrix< std::vector<bool> >      eig_is_real;                // Are the eigenvalue real or complex?
        Matrix<bool>                     cell_is_real;               // Is the whole cell real or complex?
        bool                             e_computed;                 // Already computed?


        RealVector grid_resolution;                                  //Number of cells

        // Here the future programmers will add the values that will prove necessary later.

        // Constructor. Fills the spatial grid.
        //
        GridValues(const Boundary *b, 
                   const RealVector &pmin, const RealVector &pmax,
                   const std::vector<int> &number_of_cells);

        // Destructor. Does nothing so far.
        //
        ~GridValues(){}

        // Set the grid. This function could be merged with fill_values_on_grid().
        //
        void set_grid(const Boundary *b, 
                      const RealVector &min, const RealVector &max,
                      const std::vector<int> &number_of_cells);

        // Fill F and G.
        // 
        void fill_functions_on_grid(const FluxFunction *ff, const AccumulationFunction *aa);

        // Fill JF and JG.
        // 
        void fill_Jacobians_on_grid(const FluxFunction *ff, const AccumulationFunction *aa);

        // Fill eigenpairs.
        //
        void fill_eigenpairs_on_grid(const FluxFunction *ff, const AccumulationFunction *aa);

        // Fill the directional derivatives.
        //
        void fill_dirdrv_on_grid(const FluxFunction *ff, const AccumulationFunction *aa);
};

#endif // _GRIDVALUES_

