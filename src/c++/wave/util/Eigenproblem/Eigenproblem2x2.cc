#include "Eigenproblem2x2.h"

Eigenproblem2x2::Eigenproblem2x2(): Eigenproblem(){
}

Eigenproblem2x2::~Eigenproblem2x2(){
}

// Standard problem.
//
int Eigenproblem2x2::find_eigenpair(const DoubleMatrix &A, int index, Eigenpair &ep){
    return EIGENPROBLEM_MULTIPLICITY_1;
}

int Eigenproblem2x2::find_eigenpairs(const DoubleMatrix &A, std::vector<Eigenpair> &eps){
    eps.resize(2);

    std::vector<Eigenvalue> evs;
    find_eigenvalues(A, evs);

    // http://www.math.harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/index.html
    //
    if (A(1, 0) != 0.0){
        for (int i = 0; i < 2; i++){
            eps[i].right_eigenvector.real.resize(2);
            eps[i].right_eigenvector.real(0) = evs[i].real - A(1, 1);
            eps[i].right_eigenvector.real(1) = A(1, 0);

            eps[i].right_eigenvector.real.normalize();
        }
    }
    else if (A(0, 1) != 0.0){
        for (int i = 0; i < 2; i++){
            eps[i].right_eigenvector.real.resize(2);
            eps[i].right_eigenvector.real(0) = A(0, 1);
            eps[i].right_eigenvector.real(1) = evs[i].real - A(0, 0);

            eps[i].right_eigenvector.real.normalize();
        }
    }
    else { // A(0, 1) == A(1, 0) == 0.0
        for (int i = 0; i < 2; i++) eps[i].right_eigenvector.real.resize(2);

        eps[0].right_eigenvector.real(0) = 1.0;
        eps[0].right_eigenvector.real(1) = 0.0;

        eps[1].right_eigenvector.real(0) = 0.0;
        eps[1].right_eigenvector.real(1) = 1.0;
    }

    return EIGENPROBLEM_MULTIPLICITY_1;
}

int Eigenproblem2x2::find_eigenvalue(const DoubleMatrix &A, int index, Eigenvalue &ev){
    double a = 1.0;
    double b = -(A(0, 0) + A(1, 1));
    double c = A(0, 0)*A(1, 1) - A(1, 0)*A(0, 1);

    if (b >= 0.0){
    }
    else {
    }

    return EIGENPROBLEM_MULTIPLICITY_1;
}

int Eigenproblem2x2::find_eigenvalues(const DoubleMatrix &A, std::vector<Eigenvalue> &evs){
    evs.resize(2);

    double a = 1.0;
    double b = -(A(0, 0) + A(1, 1));
    double c = A(0, 0)*A(1, 1) - A(1, 0)*A(0, 1);

    double Delta = b*b - 4.0*a*c;
    double sqrtDelta = sqrt(std::abs(Delta));

    double inv2a = 1.0/(2.0*a);

    if (Delta > 0){
        for (int i = 0; i < 2; i++){
            evs[i].is_real   = true;
            evs[i].imaginary = 0.0;
        }

        if (b >= 0.0){
            evs[0].real = (-b - sqrtDelta)*inv2a;
            evs[1].real = -2.0*c/(b + sqrtDelta);
        }
        else {
            evs[0].real = 2.0*c/(-b + sqrtDelta);
            evs[1].real = (-b + sqrtDelta)*inv2a;
        }

    } 
    else {
        evs[0].is_real = evs[1].is_real = false;

        evs[0].real = evs[1].real = -b*inv2a;

        evs[1].imaginary = sqrtDelta*inv2a;
        evs[0].imaginary = -evs[1].imaginary;
    }

    return EIGENPROBLEM_MULTIPLICITY_1;
}

// Generalized problem.
//
int Eigenproblem2x2::find_eigenpair(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenpair &ep){
}

int Eigenproblem2x2::find_eigenpairs(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenpair> &eps){
    eps.resize(2);

    std::vector<Eigenvalue> evs;
    find_eigenvalues(A, evs);

    // http://www.math.harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/index.html
    //
    if (evs[0].is_real){
        // Real.
        //
        for (int i = 0; i < 2; i++){
            double lambda = evs[i].real;
            DoubleMatrix C = A - lambda*B;

            if (C(1, 0) != 0.0){
                eps[i].right_eigenvector.real.resize(2);
                eps[i].right_eigenvector.real(0) = -C(1, 1);
                eps[i].right_eigenvector.real(1) =  C(1, 0);

                eps[i].right_eigenvector.real.normalize();
            }
            else if (C(0, 1) != 0.0){
                eps[i].right_eigenvector.real.resize(2);
                eps[i].right_eigenvector.real(0) = -C(0, 1);
                eps[i].right_eigenvector.real(1) =  C(0, 0);

                eps[i].right_eigenvector.real.normalize();
            }
            else { // A(0, 1) == A(1, 0) == 0.0
                if (C(0, 0) == C(1, 1)) return EIGENPROBLEM_MULTIPLICITY_2;
             
                eps[0].right_eigenvector.real(0) = 1.0;
                eps[0].right_eigenvector.real(1) = 0.0;

                eps[1].right_eigenvector.real(0) = 0.0;
                eps[1].right_eigenvector.real(1) = 1.0;
            }
        }
    }
    else{
        // Complex.
        //
    }

    return EIGENPROBLEM_MULTIPLICITY_1;
}

int Eigenproblem2x2::find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenvalue &ev){
}

int Eigenproblem2x2::find_eigenvalues(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenvalue> &evs){
    evs.resize(2);

    double a = B(0, 0)*B(1, 1) - B(0, 1)*B(1, 0);
    double b = A(0, 1)*B(1, 0) + A(1, 0)*B(0, 1) - (A(0, 0) + A(1, 1));
    double c = A(0, 0)*A(1, 1) - A(1, 0)*A(0, 1);

    double Delta = b*b - 4.0*a*c;
    double sqrtDelta = sqrt(std::abs(Delta));

    double inv2a = 1.0/(2.0*a);

    if (Delta > 0){
        for (int i = 0; i < 2; i++){
            evs[i].is_real   = true;
            evs[i].imaginary = 0.0;
        }

        if (b >= 0.0){
            evs[0].real = (-b - sqrtDelta)*inv2a;
            evs[1].real = -2.0*c/(b + sqrtDelta);
        }
        else {
            evs[0].real = 2.0*c/(-b + sqrtDelta);
            evs[1].real = (-b + sqrtDelta)*inv2a;
        }

    } 
    else {
        evs[0].is_real = evs[1].is_real = false;

        evs[0].real = evs[1].real = -b*inv2a;

        evs[1].imaginary = sqrtDelta*inv2a;
        evs[0].imaginary = -evs[1].imaginary;
    }

    return EIGENPROBLEM_MULTIPLICITY_1;
}

