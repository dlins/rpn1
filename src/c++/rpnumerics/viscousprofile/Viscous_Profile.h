#ifndef _VISCOUS_PROFILE_
#define _VISCOUS_PROFILE_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "eigen.h"
#include "Matrix.h"
#include <vector>
#include "RealVector.h"
#include "Boundary.h"
#include "Viscosity_Matrix.h"

extern "C"{
    int lsode_(int (*)(int *, double *, double *, double *, int *, double *), int *, double *, double *, double *,
            int *, double *, double *, int *, int *, int *, double *, int *,
            int *, int *, int(*)(int *, double *, double *, int *, int *, double *, int *), int *, int*, double*);
}

#ifndef ORBIT_FORWARD
#define ORBIT_FORWARD 1
#endif

#ifndef ORBIT_BACKWARD
#define ORBIT_BACKWARD (-1)
#endif

class Viscous_Profile {
    private:
    protected:
        static const FluxFunction         *f;
        static const AccumulationFunction *a;

        static Viscosity_Matrix *vmf;
    public:
        // Critical points
        static void critical_points_linearization(const FluxFunction *ff, const AccumulationFunction *aa, 
                                                  Viscosity_Matrix *v,
                                                  double speed, const std::vector<RealVector> &cp, std::vector< std::vector<eigenpair> > &ep);

        // Orbit
        static int orbit(const FluxFunction *ff, const AccumulationFunction *aa, 
                         Viscosity_Matrix *v,
                         const Boundary *boundary,
                         const RealVector &init, const RealVector &ref, double speed, 
                         double deltaxi,
                         int orbit_direction,
                         std::vector<RealVector> &out);

        static int orbit_flux(int *neq, double *xi, double *in, double *out, int *nparam, double *param); // For LSODE
};

#endif // _VISCOUS_PROFILE_

