#ifndef _INFLECTION_CURVE_
#define _INFLECTION_CURVE_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"
#include <vector>
#include "eigen.h"
#include "ImplicitFunction.h"
#include "ContourMethod.h"

class Inflection_Curve : public ImplicitFunction {
    private:
        const FluxFunction         *ff;
        const AccumulationFunction *aa;
        Boundary * boundary_;
        // For the grid proper.
        RealVector pmin, pmax;
        int *number_of_cells;
        
        // Values on the grid.
        Matrix<RealVector>            grid;
        Matrix< std::vector<double> > dd;
        Matrix< vector<eigenpair> >   e;
        Matrix< vector<bool> >        eig_is_real;

        int family;

        void fill_values_on_grid(void);

        void fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H);
    protected:
    public:
        Inflection_Curve(const FluxFunction *f, const AccumulationFunction *a, Boundary * b,
                         const RealVector &min, const RealVector &max, 
                         const int *cells);
        ~Inflection_Curve();

//        int SquareFunction(double *foncub, int i, int j, int is_square);
        int function_on_square(double *foncub, int i, int j, int is_square);

        int consistency(double *v1, double *v2, int &orient);

        int curve(int fam, std::vector<RealVector> &inflection_curve);

        // ISTO EH NOVO!!
        void matrixmult(int m, int p, int n, double *A, double *B, double *C);
        double ddot(int n, double *x, double *y);
        double dirdrv(int n, const RealVector &p, int family);
        double dirdrv(int n, double *p, int family);

};

#endif // _INFLECTION_CURVE_

