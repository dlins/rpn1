#ifndef _EIGENPROBLEM_
#define _EIGENPROBLEM_

#include <iostream>
#include "Eigenpair.h"
#include "JetMatrix.h"

#define EIGENPROBLEM_SOLVE_OK    0
#define EIGENPROBLEM_SOLVE_ERROR 1

#define EIGENPROBLEM_MULTIPLICITY_1 1
#define EIGENPROBLEM_MULTIPLICITY_2 2

// "Multiplicity" here denotes neither algebraic nor geometric multiplicity.
//
class Eigenproblem {
    private:
    protected:
        virtual int solve_system(const DoubleMatrix &A, const RealVector &b, RealVector &x);
    public:
        Eigenproblem();
        virtual ~Eigenproblem();

        // Standard problem. The return value codifies the multiplicity.
        //
        virtual int find_eigenpair(const DoubleMatrix &A, int index, Eigenpair &ep);
        virtual int find_eigenpairs(const DoubleMatrix &A, std::vector<Eigenpair> &eps);

        virtual int find_eigenvalue(const DoubleMatrix &A, int index, Eigenvalue &ev);
        virtual int find_eigenvalues(const DoubleMatrix &A, std::vector<Eigenvalue> &evs);

        // Generalized problem. The return value codifies the multiplicity.
        //
        virtual int find_eigenpair(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenpair &ep);
        virtual int find_eigenpairs(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenpair> &eps);

        virtual int find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenvalue &ev);

        virtual int find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, 
                                    const std::vector<DoubleMatrix> &dA, const std::vector<DoubleMatrix> &dB,
                                    int index, JetMatrix &ev);

        virtual int find_eigenvalues(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenvalue> &evs);

        // Incomplete.
        //
        virtual int find_eigenvector(const DoubleMatrix &A, const DoubleMatrix &B, 
                                     const std::vector<DoubleMatrix> &dA, const std::vector<DoubleMatrix> &dB,
                                     int index, JetMatrix &evector);
};

#endif // _EIGENPROBLEM_

