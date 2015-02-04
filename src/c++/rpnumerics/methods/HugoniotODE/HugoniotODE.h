#ifndef _HUGONIOTODE_
#define _HUGONIOTODE_

#include "HugoniotCurve.h"
#include "ODE_Solver.h"
#include "SubPhysics.h"

#define HUGONIOTODE_GENERIC_POINT 0

class HugoniotODE: public HugoniotCurve {
    private:
    protected:
        ODE_Solver *odesolver;
        HugoniotContinuation *hug;

        RealVector refvec;

        void add_point_to_curve(const RealVector &p, Curve &curve);
        void curve_engine(const RealVector &initial_point, const RealVector &rv, Curve &c);
    public:
        HugoniotODE(SubPhysics *s, ODE_Solver *ode);
        virtual ~HugoniotODE();

        virtual void list_of_reference_points(std::vector<int> &type, std::vector<std::string> &name) const;

        void set_reference_vector(const RealVector &v){
            refvec = v;
            return;
        }

        static int field(int *neq, double *xi, double *in, double *out, int *obj, double* /* Not used */);

        virtual void curve(const ReferencePoint &ref, int type, std::vector<Curve> &c);
};

#endif // _HUGONIOTODE_

