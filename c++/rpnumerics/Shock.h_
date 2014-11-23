#ifndef _SHOCK_
#define _SHOCK_

#include <vector>
#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Boundary.h"
#include "ShockMethod.h"

#ifndef SHOCK_OK
#define SHOCK_OK 0
#endif

#ifndef SHOCK_ERROR
#define SHOCK_ERROR 1
#endif

#ifndef SHOCK_REACHED_BOUNDARY
#define SHOCK_REACHED_BOUNDARY 2
#endif

#ifndef SHOCK_AS_ENGINE_FOR_COMPOSITE 
#define SHOCK_AS_ENGINE_FOR_COMPOSITE 4
#endif

#ifndef SHOCK_FOR_ITSELF 
#define SHOCK_FOR_ITSELF 5
#endif

#ifndef SHOCK_MAX_NUMBER_STEPS
#define SHOCK_MAX_NUMBER_STEPS 10
#endif

#ifndef WAVE_FORWARD
#define WAVE_FORWARD 10
#endif

#ifndef WAVE_BACKWARD
#define WAVE_BACKWARD 11
#endif

#ifndef LAX_SHOCK
#define LAX_SHOCK 20
#endif

#ifndef NON_LAX_SHOCK
#define NON_LAX_SHOCK 21
#endif

#ifndef SHOCK_REACHED_DOUBLE_CONTACT
#define SHOCK_REACHED_DOUBLE_CONTACT 30
#endif 


#define NON_LAX_AFTER_BECOMING_RIGHT_CHARACTERISTIC_WITH_CURRENT_FAMILY 40
#define NON_LAX_FOR_OTHER_REASON                                        41

extern "C" int dgels_(char*, int*, int*, int*, double*, int*, double*, int*, double*, int*, int*);

extern"C" {
//    void dgeev_(char *, char *, int *, double *, int*, double *, double *,
//            double *, int *, double *, int *, double *, int *,
//            int *);

    void dgesv_(int *, int *, double *, int *, int *, double *, int *, int *);

}

class Shock :public ShockMethod{
    public:
        static int reason;
        Shock();
    protected:
        static void matrixmult(int m, int n, int p, double *A, double *B, double *C);
        static int sgn(double x);
        static double dotprod(int n, double x[], double y[]);
        
        static double euclidean_norm(int n, double x[]);
        static void fill_with_jet(RpFunction * flux_object, int n, double *in, int degree, double *F, double *J, double *H);
        static double shockspeed(int n, double Um[], double Up[], FluxFunction *ff, AccumulationFunction *aa);
        static int shock_step(const RealVector &p, const RealVector &Uref, const RealVector &tangent, const RealVector &normal, FluxFunction *ff, AccumulationFunction *aa, double &epsilon, RealVector &pnext);
        static void bisection(const RealVector &pp, double pp_sgn, const RealVector &pm, double pm_sgn, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G, RealVector &pnext);
        static int curve_constructor(Boundary *boundary, bool local_shock, int type_of_shock, int family, int increase, RealVector &ref_point, RealVector &pnext, 
                             RealVector &tangent, RealVector &normal, int number_ignore_doub_contact, 
                             FluxFunction *ff, AccumulationFunction *aa, 
                             double lambda, double lambda_0, double lambda_1, 
                             double sigma, double sign_composite, double sign_shock,
                             std::vector<RealVector> &shockcurve/*,
                             RealVector &pnext_after_double_contact*/);
        static double hugoniot(const RealVector &U, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G);
        static int verify_lax_shock(RealVector &ref, double lambda_0_ref, double lambda_1_ref, RealVector &p, int increase, int family, FluxFunction *ff, AccumulationFunction *aa);

        //static void lambda_on_point(RealVector &p, FluxFunction *ff, AccumulationFunction *aa, std::vector<double> &lambda);

        static void normalize_vector(int n, double *v);
    public:
        static double find_initial_epsilon(std::vector<RealVector> &right_eigenvector, std::vector<RealVector> &r_dot, double cos_alpha);
    public:
        static int compute_r_dot(int n, int fam, const RealVector &p, FluxFunction *fluxfunction, AccumulationFunction *accumulationfunction, 
                                 std::vector<RealVector> &right_eigenvector, std::vector<RealVector> &r_dot, double &cos_alpha);
        static int cdgels(double *AA, double *BB, double *x);
        static int cdgesv(int n, double *A, double *b, double *x);
        static double ddot(int n, double *x, double *y);
    public:
        static void normalize(int n, double x[]);
        static void hugoniot_der(const RealVector &U, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G, double *tangent, double *normal);
        static void hugoniot_der2(const RealVector &U, const RealVector &Uref, FluxFunction *F, AccumulationFunction *G, double *dRH2, double &curvature);
        
    public:
        static void curve(const RealVector &ref, bool local_shock, const RealVector &in, int increase, int family, int type_of_shock, const RealVector *orig_direction, int number_ignore_doub_contact, FluxFunction *ff, AccumulationFunction *aa, Boundary *boundary,
                         std::vector<RealVector> &shockcurve, int &info_shockcurve, std::vector<RealVector> &shockcurve_alt, int &info_shockcurve_alt);

        
        void curveCalc(const RealVector &ref, bool local_shock, const RealVector &in, int increase, int family, int type_of_shock, const RealVector *orig_direction, int number_ignore_doub_contact, FluxFunction *ff, AccumulationFunction *aa, Boundary *boundary,
            std::vector<RealVector> &shockcurve, int &info_shockcurve, std::vector<RealVector> &shockcurve_alt, int &info_shockcurve_alt,double newtonTolerance) ;
        
        static void add_sigma(const RealVector &ref, const FluxFunction *ff, const AccumulationFunction *aa, std::vector<RealVector> &curve);
};

#endif // _SHOCK_

