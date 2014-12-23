#ifndef _EIGENPROBLEM2X2_
#define _EIGENPROBLEM2X2_

#include "Eigenproblem.h"

class Eigenproblem2x2: public Eigenproblem {
    private:
    protected:
        virtual int solve_system(const DoubleMatrix &A, const RealVector &b, RealVector &x);
    public:
        Eigenproblem2x2();
        virtual ~Eigenproblem2x2();

        // Standard problem.
        //
        virtual int find_eigenpair(const DoubleMatrix &A, int index, Eigenpair &ep);
        virtual int find_eigenpairs(const DoubleMatrix &A, std::vector<Eigenpair> &eps);

        virtual int find_eigenvalue(const DoubleMatrix &A, int index, Eigenvalue &ev);
        virtual int find_eigenvalues(const DoubleMatrix &A, std::vector<Eigenvalue> &evs);

        // Generalized problem.
        //
        virtual int find_eigenpair(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenpair &ep);
        virtual int find_eigenpairs(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenpair> &eps);

        virtual int find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, int index, Eigenvalue &ev);
        virtual int find_eigenvalue(const DoubleMatrix &A, const DoubleMatrix &B, 
                                    const std::vector<DoubleMatrix> &dA, const std::vector<DoubleMatrix> &dB,
                                    int index, JetMatrix &ev);

        virtual int find_eigenvalues(const DoubleMatrix &A, const DoubleMatrix &B, std::vector<Eigenvalue> &evs);
};

#endif // _EIGENPROBLEM2X2_

