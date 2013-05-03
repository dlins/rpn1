#ifndef _QUAD2_EXPLICIT_
#define _QUAD2_EXPLICIT_

#include "Quad2FluxFunction.h"
#include "Viscosity_Matrix.h"
#include "RectBoundary.h"

// BASE CLASS
class Quad2_Explicit {
    private:
    protected:
        const Quad2FluxFunction *f;
        const Viscosity_Matrix  *vm;
        const RectBoundary      *rect;

        // These parameters below were all grouped in *common* blocks in Fortran.
        double a1, b1, c1, d1, e1;
        double a2, b2, c2, d2, e2;

        double alpha0, beta0, gamma0;
        double alpha1, beta1, gamma1;
        double alpha2, beta2, gamma2;

        double alpht0, betat0, gammt0;
        double alpht1, betat1, gammt1;
        double alpht2, betat2, gammt2;
        // These parameters above were all grouped in *common* blocks in Fortran.

        static void qcoef(double &alpha, double &beta, double &gamma, 
                          Quad2_Explicit *obj, double c2phi, double s2phi);

        static void qcoeft(double &alphat, double &betat, double &gammat, 
                           Quad2_Explicit *obj, double c2phi, double s2phi);

        static void qcoefp(double &alphap, double &betap, double &gammap, 
                           Quad2_Explicit *obj, double c2phi, double s2phi);

        void update_data();

        Quad2_Explicit(const Quad2FluxFunction *ff, const Viscosity_Matrix *vvm,
                       const RectBoundary *b);
    public:
        ~Quad2_Explicit();
};

#endif // _QUAD2_EXPLICIT_

