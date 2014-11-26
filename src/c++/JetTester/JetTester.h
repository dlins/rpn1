#ifndef _JETTESTER_
#define _JETTESTER_

#include "JetMatrix.h"
#include "MultiArray.h"
#include <vector>
#include <iostream>
#include <limits>

class JetTester{
    private:
    protected:
    public:
        JetTester();
        virtual ~JetTester();

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
                                Matrix<std::vector<unsigned int> > &sup_pos,
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

        // TODO: Ideally, we could obtain pmin, pmax and subdivision merely by querying GridValues.
        //       
        //
        virtual void test(void *obj, int (*f)(void *obj, const RealVector &state, int degree, JetMatrix &jm), 
                          const std::vector<int> &variables,
                          const RealVector &pmin, const RealVector &pmax,
                          const std::vector<unsigned long int> &subdivision,
                          std::vector<double> &min_error, 
                          std::vector<double> &max_error,
                          std::vector<double> &mean_value);


};

#endif // _JETTESTER_

