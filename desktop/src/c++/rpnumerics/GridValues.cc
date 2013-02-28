#include "GridValues.h"
#include "Boundary.h"



GridValues::GridValues(const Boundary *b, 
                       const RealVector &pmin, const RealVector &pmax,
                       const std::vector<int> &number_of_cells){

    set_grid(b, pmin, pmax, number_of_cells);
}

// Set the grid.
//
void GridValues::set_grid(const Boundary *b, 
                          const RealVector &min, const RealVector &max,
                          const std::vector<int> &number_of_cells){

    grid_computed =
    functions_on_grid_computed = Jacobians_on_grid_computed =
    dd_computed = e_computed = false;

    fill_values_on_grid(b, min, max, number_of_cells);

    return;
}

// Fill the bare minimum of the grid values.
//
void GridValues::fill_values_on_grid(const Boundary *b, 
                                     const RealVector &pmin, const RealVector &pmax,
                                     const std::vector<int> &number_of_cells){
    if (!grid_computed){
        int dim = pmin.size();

        printf("Inside GridValues::fill_values_on_grid\n");

        grid_resolution.resize(dim);
        for (int i = 0; i < dim; i++) grid_resolution.component(i) = (fabs(pmax.component(i) - pmin.component(i))) / (double) (number_of_cells[i]);

        
        cout<<"Number of cells: "<<number_of_cells[0]<<" "<<number_of_cells[1]<<endl;
        
        grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);

        for (int i = 0; i <= number_of_cells[0]; i++) {
            for (int j = 0; j <= number_of_cells[1]; j++) {
                grid(i, j).resize(dim);

                grid(i, j).component(0) = pmin.component(0) + (double) i * grid_resolution.component(0);
                grid(i, j).component(1) = pmin.component(1) + (double) j * grid_resolution.component(1);

                if (dim==3) {
                    grid(i, j).component(2) = 1.0;
                }
            }
        }

        point_inside.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);

        for (int i = 0; i <= number_of_cells[0]; i++) {
            for (int j = 0; j <= number_of_cells[1]; j++) {
                point_inside(i, j) = b->inside(grid(i, j));
            }
        }

        cell_type.resize(number_of_cells[0], number_of_cells[1]);

        for (int i = 0; i < number_of_cells[0]; i++) {
            for (int j = 0; j < number_of_cells[1]; j++) {

                cell_type(i, j) = CELL_IS_INVALID;
                if (point_inside(i, j)){
                    if (point_inside(i + 1, j)){
                        if (point_inside(i, j + 1)){
                            if (point_inside(i + 1, j + 1)) cell_type(i, j) = CELL_IS_SQUARE;
                            else                            cell_type(i, j) = CELL_IS_TRIANGLE;
                        }
                    }
                }
            }
        }

        grid_computed = true;

        // Set to false the rest of the values on the grid.
        //
        functions_on_grid_computed = Jacobians_on_grid_computed = 
        dd_computed = e_computed = false; 
    }

    return;
}

void GridValues::fill_functions_on_grid(const FluxFunction *ff, const AccumulationFunction *aa){
    if (!functions_on_grid_computed){
//        fill_values_on_grid(GridValues &gv);

//        printf("Inside GridValues::fill_functions_on_grid\n");
        
  

        int rows = grid.rows(), cols = grid.cols(); 
        int n = rows*cols;
        if (n == 0) return;
        


        F_on_grid.resize(rows, cols);
        G_on_grid.resize(rows, cols);

        int dim = grid(0, 0).size();
        

        for (int i = 0; i < n; i++) {
            F_on_grid(i).resize(dim);
            G_on_grid(i).resize(dim);

            // We only compute the value of the function on points that are inside the physical domain
            // given by "boundary".
            if (point_inside(i)) {

                ff->fill_with_jet(dim, grid(i).components(), 0, F_on_grid(i).components(), 0, 0);
                aa->fill_with_jet(dim, grid(i).components(), 0, G_on_grid(i).components(), 0, 0);
            }
        }

        functions_on_grid_computed = true;
    }

    return;
}

void GridValues::fill_Jacobians_on_grid(const FluxFunction *ff, const AccumulationFunction *aa){
    
    if (!Jacobians_on_grid_computed){
        fill_functions_on_grid(ff, aa);

        int rows = grid.rows(), cols = grid.cols(); 
        int n = rows*cols;
        if (n == 0) return; 

        int dim = grid(0, 0).size();

        JF_on_grid.resize(rows, cols);
        JG_on_grid.resize(rows, cols);

        for (int i = 0; i < n; i++){
            
            if (point_inside(i)){
                
                JF_on_grid(i).resize(dim, dim);
                JG_on_grid(i).resize(dim, dim);//printf("GridValues::fill_Jacobians_on_grid. point inside, i = %d\n", i);

                if (functions_on_grid_computed){
                    //printf("ff = %p\n", ff);
                    ff->fill_with_jet(dim, grid(i).components(), 1, 0, JF_on_grid(i).data(), 0); 
                    aa->fill_with_jet(dim, grid(i).components(), 1, 0, JG_on_grid(i).data(), 0);
                }
                else {
                    F_on_grid(i).resize(dim);
                    G_on_grid(i).resize(dim);

                    ff->fill_with_jet(dim, grid(i).components(), 1, F_on_grid(i).components(), JF_on_grid(i).data(), 0);
                    aa->fill_with_jet(dim, grid(i).components(), 1, G_on_grid(i).components(), JG_on_grid(i).data(), 0);
                }
            }
        }

        Jacobians_on_grid_computed = true; //functions_on_grid_computed = true;
    }

    return;
}

void GridValues::fill_eigenpairs_on_grid(const FluxFunction *ff, const AccumulationFunction *aa){
    if (!e_computed){
        fill_Jacobians_on_grid(ff, aa);
        printf("Inside GridValues::fill_eigenpairs_on_grid\n");

        int rows = grid.rows(), cols = grid.cols(); 
        int n = rows*cols;
        if (n == 0) return; 

        int dim = grid(0, 0).size();
        double epsilon = 1e-10;

        e.resize(rows, cols);
        eig_is_real.resize(rows, cols);

        for (int i = 0; i < n; i++){
            if (point_inside(i)){
                // Find the eigenpairs
                std::vector<eigenpair> etemp;
                Eigen::eig(dim, JF_on_grid(i).data(), JG_on_grid(i).data(), etemp);

                //e(i).clear();
//                e(i).resize(etemp.size());
//            
//                for (int j = 0; j < etemp.size(); j++) e(i)[j] = etemp[j];
//
//                // Decide if the eigenvalues are real or complex
//                //eig_is_real(i).clear();
//                eig_is_real(i).resize(etemp.size());
//                for (int j = 0; j < etemp.size(); j++) {
//                    if (fabs(etemp[j].i) < epsilon) eig_is_real(i)[j] = true; // TODO: Comparacoes devem ser feitas com valores relativos, nao absolutos
//                    else eig_is_real(i)[j] = false;
//                }
//            }
            
            
            
            
            
             e(i).resize(etemp.size());
            
                for (int j = 0; j < etemp.size(); j++) e(i)[j] = etemp[j];

                // Decide if the eigenvalues are real or complex
                //eig_is_real(i).clear();
                eig_is_real(i).resize(2);
                for (int j = 0; j < 2; j++) {
                    if (fabs(etemp[j].i) < epsilon) eig_is_real(i)[j] = true; // TODO: Comparacoes devem ser feitas com valores relativos, nao absolutos
                    else eig_is_real(i)[j] = false;
                }
            }
            
            
            
            
            
            
            
            
            
            
            
            
            
            
        }

//        cell_is_real.resize(number_of_cells[0], number_of_cells[1]);
        cell_is_real.resize(rows - 1, cols - 1);

//        for (int i = 0; i < number_of_cells[0]; i++) {
//            for (int j = 0; j < number_of_cells[1]; j++) {
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols - 1; j++) {

                if ( cell_type(i, j) != CELL_IS_INVALID ) {
                    cell_is_real(i, j) = false;
                    // TODO: Notice. Only for cases where dim = 2.
                    // Therefore if one eigenvalue is complex, the other one is complex as well.
	                if (eig_is_real(i, j)[0]){
	                    if (eig_is_real(i + 1, j)[0]){
	                        if (eig_is_real(i, j + 1)[0]){
	                            if ( cell_type(i, j) == CELL_IS_TRIANGLE ) cell_is_real(i, j) = true;
	                            else if ( eig_is_real(i + 1, j + 1)[0] )   cell_is_real(i, j) = true;
	                        }
	                    }
	                }
                }
            }
        }

        e_computed = true;
    }

    return;
}

void GridValues::fill_dirdrv_on_grid(const FluxFunction *ff, const AccumulationFunction *aa){
    if (!dd_computed){
        fill_eigenpairs_on_grid(ff, aa);

        int rows = grid.rows(), cols = grid.cols(); 
        int n = rows*cols;
        if (n == 0) return; 

        double epsilon = 1e-10;

        int dim = grid(0, 0).size();

        dd.resize(rows, cols);

        for (int i = 0; i < n; i++){
            //printf("GridValues::fill_dirdrv_on_grid(). i = %d\n", i);
            if (point_inside(i)){
                //printf("    i = %d\n", i);
                dd(i).resize(dim);

                double H[dim][dim][dim];
                double M[dim][dim][dim];
                aa->fill_with_jet(dim, grid(i).components(), 2, 0, 0, &M[0][0][0]);
                ff->fill_with_jet(dim, grid(i).components(), 2, 0, 0, &H[0][0][0]); //printf("    i = %d, e(i).size() = %d\n", i, e(i).size());

                for (int fam = 0; fam < e(i).size(); fam++){
                    double norm = 0.0;
                    double dirdrv = 0.0;

                    // Extract lambda.
                    // The i-th eigenvalue must be real.
                    // The eigenvalues must be chosen carefully in the n-dimensional case.
                    // ALL eigenvalues must be real. Extend this by using a for cycle.
                    //
                    double lambda;

                    if (fabs(e(i)[fam].i) > epsilon) {
                        printf("Inside dirdrv(): Init step, eigenvalue %d is complex: % f %+f.\n", fam, e(i)[fam].r, e(i)[fam].i);
                        continue;
                    } 
                    else lambda = e(i)[fam].r;

                    double SubH[dim][dim];
                    double SubM[dim][dim];

                    // Nested loops, constructing SubH, SubM as H, M times r and the norm.
                    for (int k = 0; k < dim; k++) {
                        for (int m = 0; m < dim; m++) {
                            SubH[k][m] = 0.0;
                            SubM[k][m] = 0.0;
                            norm += e(i)[fam].vlr[k] * JG_on_grid(i)(k, m) * e(i)[fam].vrr[m]; // JG_on_grid(k, m) = B[k][m]
                            for (int j = 0; j < dim; j++) {
                                SubH[k][m] += H[k][m][j] * e(i)[fam].vrr[j];
                                SubM[k][m] += M[k][m][j] * e(i)[fam].vrr[j];
                            }
                            // For trivial accumulation, the directional derivative may be simplified as
                            //     dirdrv += e(i)[fam].vlr[k]*SubH[k][m]*e(i)[fam].vrr[m];
                            dirdrv += e(i)[fam].vlr[k]*(SubH[k][m] * e(i)[fam].vrr[m] - lambda * SubM[k][m] * e(i)[fam].vrr[m]);
                        }
                    }

                    dd(i)[fam] = dirdrv / norm;

                } // End fam
                //printf("    ****\n");
            }
        } // End grid

        dd_computed = true;
    }

    return;
}

