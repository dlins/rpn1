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

int Eigenproblem::find_eigenvalues(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenvalue> &evs){
    return 1;
}

