/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationRarefactionFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ContinuationRarefactionFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

ContinuationRarefactionFlow::ContinuationRarefactionFlow(const int familyIndex, const int direction, const FluxFunction & fluxFunction) : RarefactionFlow(familyIndex, direction, fluxFunction) {
}

ContinuationRarefactionFlow::~ContinuationRarefactionFlow() {
}

ContinuationRarefactionFlow::ContinuationRarefactionFlow(const RealVector referenceVector,const int familyIndex, const int direction, const FluxFunction & fluxFunction):RarefactionFlow(referenceVector,familyIndex, direction, fluxFunction){
    
}


int ContinuationRarefactionFlow::flux(const RealVector & input, RealVector &output) {

    double in[input.size()];

    double out[input.size()];

    int dimension = input.size();

    for (int i = 0; i < input.size(); i++) {
        in[i] = input(i);
    }

    double param [input.size() + 1];

    param[0] = (int) getFamilyIndex();

    for (int i = 1; i < input.size() + 1; i++) {
        param[i] = getReferenceVectorComponent(i);
    }
    int nparam = 3;

    double xi = 0;

    rarefaction(&dimension, &xi, in, out, &nparam, &param[0]);

    for (int i = 0; i < input.size(); i++) {
        output(i) = out[i];
    }

}

int ContinuationRarefactionFlow::fluxDeriv(const RealVector & input, JacobianMatrix & output) {

}

int ContinuationRarefactionFlow::fluxDeriv2(const RealVector & input, HessianMatrix & output) {

}

WaveFlow * ContinuationRarefactionFlow::clone()const {

    return new ContinuationRarefactionFlow(*this);
}

void ContinuationRarefactionFlow::setReferenceVectorComponent(const int index, const double value) {

    re[index] = value;
}

double ContinuationRarefactionFlow::getReferenceVectorComponent(const int index)const {
    return re[index];
}

int ContinuationRarefactionFlow::flux(int n, int family, double *in, double *lambda, double *out) {

    // Fill the Jacobianflux
    double J[n][n];

    const FluxFunction & flux = fluxFunction();
    fill_with_jet(flux, n, in, 1, 0, &J[0][0], 0);

    // Find J's eigencouples and sort them.

    vector<eigencouple> e;
    Eigen::eig(n, &J[0][0], e);

    if (e[family].i != 0) {

        return COMPLEX_EIGENVALUE;
    } else {

        *lambda = e[family].r;

        int i;
        for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
        return SUCCESSFUL_PROCEDURE;
    }

}

int ContinuationRarefactionFlow::rarefaction(int *neq, double *xi, double *in, double *out, int *nparam, double *param) {
    // The dimension of the problem:
    int n = *neq;

    // The family:
    //    int family = (int)param[0];

    int family = getFamilyIndex();

    // The reference eigenvector:
    double rev[n];
    int ii;
    //    for (ii = 0; ii < n; ii++) rev[ii] = param[1 + ii];

    for (ii = 0; ii < n; ii++) rev[ii] = getReferenceVectorComponent(ii); //param[ 1+ii];

    // Fill the Jacobian
    double J[n][n];

    double H[n][n][n];

    const FluxFunction & flux = fluxFunction();

    fill_with_jet(flux, n, in, 2, 0, &J[0][0], &H[0][0][0]);


    // Find J's eigencouples and sort them.
    int i, info;
    vector<eigencouple> e;
    info = Eigen::eig(n, &J[0][0], e);
    for (i = 0; i < n; i++) out[i] = e[family].vrr[i];

    // Check for stop criteria
    // TODO This section is to be tuned.
    if (info == SUCCESSFUL_PROCEDURE) {
        // All eigenvalues must be real
        for (i = 0; i < n; i++) {
            if (fabs(e[i].i) > 0) {
                return COMPLEX_EIGENVALUE;
            }
        }

        // All eigenvalues must be different.
        // This test can be performed thus because the eigencouples are ordered
        // according to the real part of the eigenvalue. Thus, if two eigenvalues
        // are equal, one will come after the other.
        for (i = 0; i < n - 1; i++) {
            if (e[i].r == e[i + 1].r) {
                return ABORTED_PROCEDURE;
            }
        }

    } else return ABORTED_PROCEDURE;

    // The eigenvector to be returned is the one whose inner product with the
    // reference vector is positive.
    if (prodint(n, &(e[family].vrr[0]), &rev[0]) > 0) {
        for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
    } else {
        for (i = 0; i < n; i++) out[i] = -e[family].vrr[i];
    }

    // STOP CRITERION:
    // The identity in Proposition 10.11 of 
    // "An Introduction to Conservation Laws: 
    // Theory and Applications to Multi-Phase Flow" must not change
    // sign (that is, the rarefaction is monotonous).

    double res[EIG_MAX];

    applyH(EIG_MAX, &(e[family].vrr[0]), &H[0][0][0], &(e[family].vrr[0]), &res[0]);
    double dlambda_dtk = prodint(EIG_MAX, &res[0], &(e[family].vlr[0])) /
            prodint(EIG_MAX, &(e[family].vlr[0]), &(e[family].vrr[0]));
    if (dlambda_dtk * ref_speed < 0) {
        return ABORTED_PROCEDURE;
    }

    // Set the value of the last viable eigenvalue (the shock speed):
    lasteigenvalue = e[family].r;

    // Update the value of the reference eigenvector:
    for (i = 0; i < n; i++) {
        re[i] = out[i];
    }
    return SUCCESSFUL_PROCEDURE;
}


void ContinuationRarefactionFlow::fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];

    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);

    flux_object.jet(state_c, c_jet, degree);

    // Fill F
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet(i);

    // Fill J
    if (J != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i * n + j] = c_jet(i, j);
            }
        }
    }

    // Fill H
    if (H != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    H[(i * n + j) * n + k] = c_jet(i, j, k); // Check this!!!!!!!!
                }
            }
        }
    }

    return;
}

double ContinuationRarefactionFlow::prodint(int n, double *a, double *b)const {
    int incx = 1, incy = 1;
    return ddot_(&n, a, &incx, b, &incy);
}

void ContinuationRarefactionFlow::applyH(int n, double *xi, double *H, double *eta, double *out)const {
    int i, j, k;

    // Temporary matrix
    double Htemp[n][n];

    // Temporary vector
    double vtemp[n];

    for (k = 0; k < n; k++) {
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                // Fill the temporary matrix with the k-th layer of H,
                // i.e., with H[k][:][:]
                Htemp[i][j] = H[(k * n + i) * n + j];
                // vtemp = transpose(xi)*H[k][:][:]
                matrixmult(1, n, n, xi, &Htemp[0][0], &vtemp[0]);
                // out[k] = vtemp*eta
                matrixmult(1, n, 1, eta, &vtemp[0], &out[k]);
            }
        }
    }

    return;
}

void ContinuationRarefactionFlow::matrixmult(int m, int p, int n, double *A, double *B, double *C)const {
    extern double ddot_(int *, double *, int *, double *, int *);
    int i, j, k, incx = 1, incy = 1, pp = p;
    double sum;

    double arow[p], bcol[p];

    for (i = 0; i < m; i++) {
        for (j = 0; j < n; j++) {
            sum = 0;
            for (k = 0; k < p; k++) sum += A[i * p + k] * B[k * n + j];
            //C[i*n + j] = sum;

            // Alternate
            for (k = 0; k < p; k++) {
                arow[k] = A[i * p + k];
                bcol[k] = B[k * n + j];
            }
            C[i * n + j] = ddot_(&pp, &arow[0], &incx, &bcol[0], &incy);

            //printf("C[%d][%d]: Conventional = % f; ddot_ = % f\n", i, j, sum, C[i*n + j]);

        }
    }

    return;
}

