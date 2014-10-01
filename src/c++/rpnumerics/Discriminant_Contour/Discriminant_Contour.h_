#ifndef _DISCRIMINANT_CONTOUR_
#define _DISCRIMINANT_CONTOUR_

#include "ImplicitFunction.h"
#include "ContourMethod.h"

#include <vector>
#include <deque>

class Discriminant_Contour : public ImplicitFunction {
    private:
    protected:
        double level;

        const FluxFunction *ff;
        const AccumulationFunction *aa;

        int (*fos)(Discriminant_Contour *obj, double *foncub, int i, int j);
        bool is_improvable;

        double discriminant(const DoubleMatrix &FJ, const DoubleMatrix &GJ);

        RealVector abc(const DoubleMatrix &FJ, const DoubleMatrix &GJ);

        static int discriminant_function_on_square(Discriminant_Contour *obj, double *foncub, int i, int j);
        static int derivative_discriminant_function_on_square(Discriminant_Contour *obj, double *foncub, int i, int j);
        int index_derivative_discriminant_variable;

        static double max_discriminant(Discriminant_Contour *obj, int i, int j);

//        int discriminant_function_on_square(double *foncub, int i, int j);
//        int derivative_discriminant_function_on_square(double *foncub, int i, int j);

        double localm_(const RealVector &xa, const RealVector &xb, double flip, double t, RealVector &xn);
    public:
        Discriminant_Contour();
        ~Discriminant_Contour();

        int function_on_square(double *foncub, int i, int j);

        int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, 
                  double level,
                  std::vector<RealVector> &discriminant_contour);

        void curve(const FluxFunction *f, const AccumulationFunction *a, 
                   GridValues &g, 
                   const std::vector<double> &level,
                   std::vector<std::vector<RealVector> > &discriminant_contour);

        int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, 
                  const RealVector &p,
                  std::vector<RealVector> &discriminant_contour, double &lev);

        // TODO:
        // The value of u should be selected internally and not by the user.
        //
        int derivative_curve(const FluxFunction *f, const AccumulationFunction *a, 
                             GridValues &g, int u,
                             std::vector<RealVector> &derivative_discriminant_contour, std::vector<double> &discriminant_on_segment);

        // Derivative of the discriminant with respect to the variable indexed by u.
        //
        double derivative_discriminant(const FluxFunction *f, const AccumulationFunction *a, const RealVector &p, int u);

        // This method will remain public.
        //
        double discriminant(const FluxFunction *f, const AccumulationFunction *a, const RealVector &p);

        bool improvable(void){return is_improvable;}
        int complete(const RealVector &p0, const RealVector &p1, const RealVector &p_init, RealVector &p_completed);
};

#endif // _DISCRIMINANT_CONTOUR_

