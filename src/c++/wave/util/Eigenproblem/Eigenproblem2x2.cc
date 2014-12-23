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
    std::vector<Eigenvalue> evs;
    find_eigenvalues(A, B, evs);

    eps.resize(2);
    for (int i = 0; i < 2; i++){
        eps[i].right_eigenvector.real.resize(2);
        eps[i].right_eigenvector.imaginary.resize(2);

        eps[i].left_eigenvector.real.resize(2);
        eps[i].left_eigenvector.imaginary.resize(2);

        eps[i].is_real = evs[i].is_real;
    }

    // http://www.math.harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/index.html
    //
    if (evs[0].is_real){
        // Real.
        //
        for (int i = 0; i < 2; i++){
            eps[i].eigenvalue = evs[i];

            // Zero imaginary part.
            //
            for (int j = 0; j < 2; j++){
                eps[i].right_eigenvector.imaginary(j) = 0.0;
                eps[i].left_eigenvector.imaginary(j)  = 0.0;
            }

            // Proceed...
            //
            double lambda = evs[i].real;
            DoubleMatrix C = A - lambda*B;

            if (C(1, 0) != 0.0){
                eps[i].right_eigenvector.real(0) = -C(1, 1);
                eps[i].right_eigenvector.real(1) =  C(1, 0);
                eps[i].right_eigenvector.real.normalize();

                eps[i].left_eigenvector.real(0) = -C(1, 0);
                eps[i].left_eigenvector.real(1) =  C(0, 0); // C(1, 1)
                eps[i].left_eigenvector.real.normalize();
            }
            else if (C(0, 1) != 0.0){
                eps[i].right_eigenvector.real(0) = -C(0, 1);
                eps[i].right_eigenvector.real(1) =  C(0, 0);
                eps[i].right_eigenvector.real.normalize();

                eps[i].left_eigenvector.real(0) = -C(0, 0);
                eps[i].left_eigenvector.real(1) =  C(1, 0); // C(0, 1)
                eps[i].left_eigenvector.real.normalize();
            }
            else { // A(0, 1) == A(1, 0) == 0.0
                eps[0].right_eigenvector.real(0) = 1.0;
                eps[0].right_eigenvector.real(1) = 0.0;

                eps[0].left_eigenvector.real(0) = 0.0;
                eps[0].left_eigenvector.real(1) = 1.0;



                eps[1].right_eigenvector.real(0) = 0.0;
                eps[1].right_eigenvector.real(1) = 1.0;

                eps[1].left_eigenvector.real(0) = 1.0;
                eps[1].left_eigenvector.real(1) = 0.0;

                if (C(0, 0) == C(1, 1)) return EIGENPROBLEM_MULTIPLICITY_2;
            }
        }
    }
    else{
        // Complex.
        //
        double lambdar = evs[0].real;
        double lambdai = evs[0].imaginary;

        DoubleMatrix Cr = A - lambdar*B;
        DoubleMatrix Ci = A - lambdai*B;

        if (Cr(1, 0) != 0.0 || Ci(1, 0) != 0.0){
            // Eigenvectors are pairs of complex conjugated.
            //
            eps[0].right_eigenvector.real(0)      = -Cr(1, 1);
            eps[0].right_eigenvector.real(1)      =  Cr(1, 0);
            eps[0].right_eigenvector.imaginary(0) = -Ci(1, 1);
            eps[0].right_eigenvector.imaginary(1) =  Ci(1, 0);

            eps[0].left_eigenvector.real(0)       = -Cr(1, 0);
            eps[0].left_eigenvector.real(1)       =  Cr(0, 0);
            eps[0].left_eigenvector.imaginary(0)  = -Ci(1, 0);
            eps[0].left_eigenvector.imaginary(1)  =  Ci(0, 0);

            eps[0].right_eigenvector.normalize();
            eps[0].left_eigenvector.normalize();

            // Copy the real part, invert the imaginary part.
            // 
            eps[1].right_eigenvector.real      = -eps[0].right_eigenvector.real;
            eps[1].left_eigenvector.real       = -eps[0].left_eigenvector.real;

            eps[1].right_eigenvector.imaginary = -eps[0].right_eigenvector.imaginary;
            eps[1].left_eigenvector.imaginary  = -eps[0].left_eigenvector.imaginary;
        }
        else if (Cr(0, 1) != 0.0 || Ci(0, 1) != 0.0){
            // Eigenvectors are pairs of complex conjugated.
            //
            eps[0].right_eigenvector.real(0)      = -Cr(0, 1);
            eps[0].right_eigenvector.real(1)      =  Cr(0, 0);
            eps[0].right_eigenvector.imaginary(0) = -Ci(0, 1);
            eps[0].right_eigenvector.imaginary(1) =  Ci(0, 0);

            eps[0].left_eigenvector.real(0)       = -Cr(0, 0);
            eps[0].left_eigenvector.real(1)       =  Cr(1, 0);
            eps[0].left_eigenvector.imaginary(0)  = -Ci(0, 0);
            eps[0].left_eigenvector.imaginary(1)  =  Ci(1, 0);

            eps[0].right_eigenvector.normalize();
            eps[0].left_eigenvector.normalize();

            // Copy the real part, invert the imaginary part.
            // 
            eps[1].right_eigenvector.real      = -eps[0].right_eigenvector.real;
            eps[1].left_eigenvector.real       = -eps[0].left_eigenvector.real;

            eps[1].right_eigenvector.imaginary = -eps[0].right_eigenvector.imaginary;
            eps[1].left_eigenvector.imaginary  = -eps[0].left_eigenvector.imaginary;
        }
        else { // A(0, 1) == A(1, 0) == 0.0
            // 
            eps[0].right_eigenvector.real(0)      = 1.0;
            eps[0].right_eigenvector.real(1)      = 0.0;
            eps[0].right_eigenvector.imaginary(0) = 0.0;
            eps[0].right_eigenvector.imaginary(1) = 0.0;


            eps[0].left_eigenvector.real(0)       = 0.0;
            eps[0].left_eigenvector.real(1)       = 1.0;
            eps[0].left_eigenvector.imaginary(0)  = 0.0;
            eps[0].left_eigenvector.imaginary(1)  = 0.0;

            // 
            eps[1].right_eigenvector.real(0)      = 0.0;
            eps[1].right_eigenvector.real(1)      = 1.0;
            eps[1].right_eigenvector.imaginary(0) = 0.0;
            eps[1].right_eigenvector.imaginary(1) = 0.0;

            eps[1].left_eigenvector.real(0)       = 1.0;
            eps[1].left_eigenvector.real(1)       = 0.0;
            eps[1].left_eigenvector.imaginary(0)  = 0.0;
            eps[1].left_eigenvector.imaginary(1)  = 0.0;

            if (Cr(0, 0) == Cr(1, 1)) return EIGENPROBLEM_MULTIPLICITY_2;
        }
    }

    return EIGENPROBLEM_MULTIPLICITY_1;
}

int Eigenproblem2x2::find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenvalue &ev){
}

// Only for real eigenpairs!
//
int Eigenproblem2x2::find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, 
                                     const std::vector<DoubleMatrix> &dA, const std::vector<DoubleMatrix> &dB,
                                     int index, JetMatrix &ev){
    // Number of variables == columns of A or B, interpreted as Jacobians of a certain function.
    //
    int var = A.cols();
    ev.resize(var, 1);

    std::vector<Eigenpair> eps;
    int info = find_eigenpairs(A, B, eps);

    // Set the eigenvalue.
    //
    double lambda = eps[index].eigenvalue.real;
    ev.set(0, lambda);

    RealVector l = eps[index].left_eigenvector.real;
    RealVector r = eps[index].right_eigenvector.real;

    RealVector dlambda(var);

    double inv_den = 1.0/(l*(B*r));

    for (int i = 0; i < dlambda.size(); i++) dlambda(i) = inv_den*(  l*(dA[i] - lambda*dB[i])*r  );

    for (int i = 0; i < var; i++) ev.set(0, i, dlambda(i));

    return info;
}

// Assume x.size() == 2.
//
int Eigenproblem2x2::solve_system(const DoubleMatrix &A, const RealVector &b, RealVector &x){
    double Delta = A(0, 0)*A(1, 1) - A(0, 1)*A(1, 0);
    
    if (abs(Delta) <= 1e-20) return EIGENPROBLEM_SOLVE_ERROR;

    double invDelta = 1.0/Delta;

    x(0) = invDelta*(b(0)*A(1, 1) - b(1)*A(0, 1));
    x(1) = invDelta*(b(1)*A(0, 0) - b(0)*A(1, 0));

    return EIGENPROBLEM_SOLVE_OK;
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

