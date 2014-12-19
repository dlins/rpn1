#include "Eigenproblem.h"

Eigenproblem::Eigenproblem(){
}

Eigenproblem::~Eigenproblem(){
}

// Standard problem.
//
int Eigenproblem::find_eigenpair(const DoubleMatrix &A, int index, Eigenpair &ep){
    return 1;
}

int Eigenproblem::find_eigenpairs(const DoubleMatrix &A, std::vector<Eigenpair> &eps){
    return 1;
}

int Eigenproblem::find_eigenvalue(const DoubleMatrix &A, int index, Eigenvalue &ev){
    return 1;
}

int Eigenproblem::find_eigenvalues(const DoubleMatrix &A, std::vector<Eigenvalue> &evs){
    return 1;
}

// Generalized problem.
//
int Eigenproblem::find_eigenpair(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenpair &ep){
    return 1;
}

int Eigenproblem::find_eigenpairs(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenpair> &eps){
    return 1;
}

int Eigenproblem::find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenvalue &ev){
    return 1;
}

int Eigenproblem::find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, 
                                  const std::vector<DoubleMatrix> &dA, const std::vector<DoubleMatrix> &dB,
                                  int index, JetMatrix &ev){
    return 1;
}

int Eigenproblem::find_eigenvalues(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenvalue> &evs){
    return 1;
}

int Eigenproblem::solve_system(const DoubleMatrix &A, const RealVector &b, RealVector &x){
    int n = A.rows();
    DoubleMatrix B(n, 1), X(n, 1);

    int info = solve(A, B, X);

    x.resize(n);
    for (int i = 0; i < n; i++) x(i) = X(0, i)	;

    if (info == 0) return EIGENPROBLEM_SOLVE_OK;
    else           return EIGENPROBLEM_SOLVE_ERROR;
}

// Find the index-th eigenvector and its derivative (which is not output yet).
// The derivative is used by stiff-integrators, since the eigenvector itself
// is the field to be integrated. Be careful with normalizations!
//
int Eigenproblem::find_eigenvector(const DoubleMatrix &A, const DoubleMatrix &B, 
                                      const std::vector<DoubleMatrix> &dA, const std::vector<DoubleMatrix> &dB,
                                      int index, JetMatrix &evector){

    JetMatrix ev; // To store the eigenvalue and its derivatives.

    int var = A.cols();
    ev.resize(var, 1);

    std::vector<Eigenpair> eps;
    int info = find_eigenpairs(A, B, eps);

    // Set the eigenvalue.
    //
    double lambda = eps[index].eigenvalue.real;
    ev.set(0, lambda);

    RealVector l = eps[index].left_eigenvector.real;  // Watch out for the normalization!!! Can't be always to size one.
    RealVector r = eps[index].right_eigenvector.real;
    double inv_norm_squared_r = 1.0/norm2_squared(r);

    RealVector dlambda(var);

    double inv_den = 1.0/(l*(B*r));

    for (int i = 0; i < dlambda.size(); i++) dlambda(i) = inv_den*(  l*(dA[i] - lambda*dB[i])*r  );

    for (int i = 0; i < var; i++) ev.set(0, i, dlambda(i));

    DoubleMatrix E = A + outer_product(l, l)*B - lambda*B;

    DoubleMatrix dr(var, var);
    for (int i = 0; i < var; i++){
        RealVector rr = (dlambda(i)*B - dA[i] + lambda*dB[i])*r;

        // One column at a time.
        //
        RealVector dr_di(2);
        int info = solve_system(E, rr, dr_di);

        // Correction: (dr/du_i) maps to (dr/du_i) + c*r, where
        //
        //     c = -<r, dr/du_i>/<r, r>.
        //
        double c = -(r*dr_di)*inv_norm_squared_r;
        RealVector dr_di_corrected = dr_di + c*r;

        for (int j = 0; j < var; j++) dr(j, i) = dr_di_corrected(j); // Transposed with respect to Fortran!
    }

    // Copy (ony the right-eigenvector and its first-degree derivative).
    //
    evector.resize(var, var);
    for (int i = 0; i < var; i++){
        evector.set(i, r(i));
        for (int j = 0; j < var; j++){
            evector.set(i, j, dr(i, j));
        }
    }

    return info;
}


