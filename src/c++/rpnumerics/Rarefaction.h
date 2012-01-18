#ifndef _RAREFACTION_
#define _RAREFACTION_

#define RAREFACTION_INIT_OK        0
#define RAREFACTION_INIT_FAILURE   1
#define RAREFACTION_NOT_MONOTONOUS 2

#define RAREFACTION_SIMPLE_ACCUMULATION  10
#define RAREFACTION_GENERAL_ACCUMULATION 11

#define RAREFACTION_SPEED_INCREASE 20
#define RAREFACTION_SPEED_NEUTRAL  21
#define RAREFACTION_SPEED_DECREASE 22

#define RAREFACTION_INITIALIZE_YES 30
#define RAREFACTION_INITIALIZE_NO  31

#define CHECK_RAREFACTION_MONOTONY_TRUE 40
#define CHECK_RAREFACTION_MONOTONY_FALSE 41

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "eigen.h"
#include "Boundary.h"
#include <vector>

extern "C" {
    int lsode_(int (*)(int *, double *, double *, double *, int *, double *), int *, double *, double *, double *,
            int *, double *, double *, int *, int *, int *, double *, int *,
            int *, int *, int(*)(int *, double *, double *, int *, int *, double *, int *), int *, int*, double*);
}

class Rarefaction {
private:
    static FluxFunction *fluxfunction;
    static AccumulationFunction *accumulationfunction;
    static int type;
    static int family;

    static double ddot(int n, double *x, double *y);
    static void matrixmult(int m, int p, int n, double *A, double *B, double *C);

    static void fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H);

    static int flux(int *neq, double *xi, double *in, double *out, int *nparam, double *param);

    static int compute_last_point(const RealVector &previous_point, const RealVector &new_point, RealVector &last_point);

    static void compute_eigenpair(int n, const RealVector &in, double &lambda, RealVector &eigenvector);

    static double compute_lambda(int n, const RealVector &in);

    static int init(const RealVector &initial_point, int increase, double deltaxi, RealVector &second_point);

    static double dirdrv(int n, const RealVector &p);

    static int rar_last_point(int n, const RealVector &p0, const RealVector &p1, RealVector &out);
protected:
public:
    static int curve(const RealVector &initial_point,
            int initialize,
            const RealVector *initial_direction,
            int curve_family,
            int increase,
            int check_monotony,
            double deltaxi,
            const FluxFunction *ff, const AccumulationFunction *aa,
            int type_of_accumulation,
            Boundary *boundary,
            std::vector<RealVector> &rarcurve);
};

#endif // _RAREFACTION_

