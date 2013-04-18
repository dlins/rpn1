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

#include "lsode.h"

#ifndef ORBIT_FORWARD
#define ORBIT_FORWARD 1
#endif

#ifndef ORBIT_BACKWARD
#define ORBIT_BACKWARD (-1)
#endif

#ifndef ORBIT_REACHED_SEGMENT
#define ORBIT_REACHED_SEGMENT 10
#endif

#ifndef ORBIT_STAGNANT
#define ORBIT_STAGNANT 20
#endif

class Viscous_Profile {
    private:
    protected:
        static const FluxFunction         *f;
        static const AccumulationFunction *a;

        static Viscosity_Matrix *vmf;

        static double distance(const RealVector &p, const RealVector &q){
            double d = 0.0;

            for (int i = 0; i < p.size(); i++) d += (p.component(i) - q.component(i))*(p.component(i) - q.component(i));

            return sqrt(d);
        }

        static bool segment_intersection(double *p1, double *p2, double *q1, double *q2, double *r);

        static void Newton_improvement(const FluxFunction *ff, const AccumulationFunction *aa,
                                  double sigma, const RealVector &p,  RealVector &ref, RealVector &out);
    public:
        // Critical points
        static void critical_points_linearization(const FluxFunction *ff, const AccumulationFunction *aa, 
                                                  Viscosity_Matrix *v,
                                                  double speed,  RealVector &cp,  RealVector &ref, std::vector<eigenpair>  &ep);

        // Orbit
        static int orbit(const FluxFunction *ff, const AccumulationFunction *aa, 
                         Viscosity_Matrix *v,
                         const Boundary *boundary,
                         const RealVector &init, const RealVector &ref, double speed, 
                         double deltaxi,
                         int orbit_direction,
                         std::vector<RealVector> &out,
                         std::vector<RealVector> *segment = 0);

        static int orbit_flux(int *neq, double *xi, double *in, double *out, int *nparam, double *param); // For LSODE

        static void viscous_field(const FluxFunction *f, const AccumulationFunction *a,
                                 RealVector &ref,double speed,
                                  const RealVector &pmin, const RealVector &pmax, 
                                  const std::vector<int> &noc, 
                                  std::vector<RealVector> &grid, std::vector<RealVector> &dir);
};

#endif // _VISCOUS_PROFILE_

