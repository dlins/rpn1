#ifndef _JETTESTER2D_
#define _JETTESTER2D_

#include "JetTester.h"

class JetTester2D: public JetTester {
    private:
    protected:
    public:
        JetTester2D();
        virtual ~JetTester2D();

        virtual void populate_F(void *obj, int (*f)(void *obj, const RealVector &state, int degree, JetMatrix &jm),
                        const RealVector &pmin, const RealVector &pmax,
                        const std::vector<unsigned long int> &subdivision,
                        MultiArray<RealVector> &F, MultiArray<DoubleMatrix> &JF);

        virtual void populate_JF(void *obj, int (*f)(void *obj, const RealVector &state, int degree, JetMatrix &jm),
                         const RealVector &pmin, const RealVector &pmax,
                         const std::vector<unsigned long int> &subdivision,
                         MultiArray<DoubleMatrix> &JF, MultiArray<std::vector<DoubleMatrix> > &HF);

        virtual void numerical_Jacobian(const MultiArray<RealVector>    &F, 
                                const MultiArray<DoubleMatrix> &JF,
                                int rows, int cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                                unsigned long int intdelta,
                                const RealVector &pmin, const RealVector &pmax,
                                const std::vector<unsigned long int> &subdivision,
                                /*MultiArray<DoubleMatrix> &numerical_analytic_deviation*/
                                DoubleMatrix &numerical_analytic_abs_deviation_sup,
                                double &synthetic_deviation,
                                double &max_abs_F);

        virtual void numerical_Hessian(const MultiArray<DoubleMatrix>               &JF, 
                                       const MultiArray<std::vector<DoubleMatrix> > &HF,
                                       int rows, int cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                                       const RealVector &pmin, const RealVector &pmax,
                                       const std::vector<unsigned long int> &subdivision,
                                       /*MultiArray<DoubleMatrix> &numerical_analytic_deviation*/
                                       std::vector<DoubleMatrix> &numerical_analytic_abs_deviation_sup,
                                       RealVector &max_vec,
                                       double &synthetic_deviation);
};

#endif // _JETTESTER2D_

