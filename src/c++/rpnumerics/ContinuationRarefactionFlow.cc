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

//ContinuationRarefactionFlow::ContinuationRarefactionFlow(const int familyIndex, const int direction, const FluxFunction & fluxFunction) : RarefactionFlow(familyIndex, direction, fluxFunction) {
//}

ContinuationRarefactionFlow::~ContinuationRarefactionFlow() {
}

ContinuationRarefactionFlow::ContinuationRarefactionFlow(const RealVector & referenceVector, const int familyIndex, const int direction, const FluxFunction & fluxFunction) : RarefactionFlow(referenceVector, familyIndex, direction, fluxFunction) {

    //    cout << "here" << endl;

}

ContinuationRarefactionFlow::ContinuationRarefactionFlow(const ContinuationRarefactionFlow &copy) : RarefactionFlow(copy.getReferenceVector(), copy.getFamilyIndex(), copy.direction(), copy.fluxFunction()) {

}

int ContinuationRarefactionFlow::flux(const RealVector & input, RealVector &output) {

    double in[input.size()];

    double out[input.size()];

    int dimension = input.size();

    for (int i = 0; i < input.size(); i++) {
        in[i] = input(i);
    }

    double param [input.size() + 2 ];

    param[1] = (int) getFamilyIndex();
    param[0] = _GENERAL_ACCUMULATION_;

    for (int i = 0; i < input.size(); i++) {
        param[i + 2] = getReferenceVectorComponent(i);
    }
    int nparam = input.size() + 2;

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

//int ContinuationRarefactionFlow::flux(int n, int family, double *in, double *lambda, double *out) {
//    if (out != 0) {
//
//
//        // Fill the Jacobianflux
//        double J[n][n];
//
//        const FluxFunction & fluxF = fluxFunction();
//
//        fill_with_jet(fluxF, n, in, 1, 0, &J[0][0], 0);
//
//        // Find J's eigencouples and sort them.
//
//        vector<eigenpair> e;
//        Eigen::eig(n, &J[0][0], e);
//
//
//
//        if (e[family].i != 0) {
//
//            return COMPLEX_EIGENVALUE;
//        } else {
//
//            *lambda = e[family].r;
//
//
//            for (int i = 0; i < n; i++) {
//                out[i] = e[family].vrr[i];
//
//            }
//            return SUCCESSFUL_PROCEDURE;
//        }
//
//    }
//    return SUCCESSFUL_PROCEDURE;
//
//}

int ContinuationRarefactionFlow::rarefaction(int *neq, double *xi, double *in, double *out, int *nparam, double *param) {

    const FluxFunction & flux_object = fluxFunction();
    const AccumulationFunction & accumulation_flux_object = RpNumerics::getPhysics().accumulation(); //TODO REMOVE !!
    // The dimension of the problem:
    int n = *neq;

    /* NEW BELOW */
    // The family:
    int family = getFamilyIndex(); //(int)param[1];
    //    int family = (int)param[1];

    // The reference eigenvector:
    double rev[n];
    int ii;
    for (ii = 0; ii < n; ii++) rev[ii] = getReferenceVectorComponent(ii); //param[2 + ii];

    // Some parameters
    int i, j, info;
    vector<eigenpair> e;
    int type = RpNumerics::getPhysics().getPhysicsVector().at(0)->type();
    //    cout <<"Valor de type: "<<type<<endl;
    param[0] = type; // _SIMPLE_ACCUMULATION_; //TODO REMOVE !!
    //    param[0] = _GENERAL_ACCUMULATION_; //TODO REMOVE !!

    // Find the eigenpair
    if ((int) param[0] == _SIMPLE_ACCUMULATION_) {
        double J[n][n];
        fill_with_jet(flux_object, n, in, 1, 0, &J[0][0], 0);
        info = Eigen::eig(n, &J[0][0], e);
    } else {
        double A[n][n], B[n][n];
        fill_with_jet(flux_object, n, in, 1, 0, &A[0][0], 0);
        fill_with_jet(accumulation_flux_object, n, in, 1, 0, &B[0][0], 0);
        info = Eigen::eig(n, &A[0][0], &B[0][0], e);
    }
    /* NEW ABOVE */

    // Check for stop criteria
    // TODO This section is to be tuned.
    if (info == 0) { // NEW HERE. I don't know where SUCCESSFUL_PROCEDURE & ABORTED_PROCEDURE are defined within the librpn distribution,
        // so I had to make do with the value that LAPACK returns as info.
        // The eigenvalue of this family must be real
        //for (i = 0; i < n; i++){
        if (fabs(e[family].i) > 1e-10) { // TODO Set a good value for this
#ifdef TEST_RAREFACTION
            printf("At rarefaction(): Imaginary eigenvalue!\n");
            printf("Eigenvalue %d = % g %+g*i.\n", i, e[i].r, e[i].i);
#endif
            return COMPLEX_EIGENVALUE;
        }
        //}

        // All eigenvalues must be different.
        // This test can be performed thus because the eigenpairs are ordered
        // according to the real part of the eigenvalue. Thus, if two eigenvalues
        // are equal, one will come after the other.
        for (i = 0; i < n - 1; i++) {
            if (e[i].r == e[i + 1].r) {
#ifdef TEST_RAREFACTION
                printf("At rarefaction(): Eigenvalues are equal!\n");
                printf("Eigenvalue %d = % f %+f*i.\n", i, e[i].r, e[i].i);
                printf("Eigenvalue %d = % f %+f*i.\n", i + 1, e[i + 1].r, e[i + 1].i);
#endif
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

    return SUCCESSFUL_PROCEDURE;

    //    // The dimension of the problem:
    //
    //    int n = *neq;
    //
    //    // The family:
    //
    //    int family = getFamilyIndex();
    //
    //    // The reference eigenvector:
    //    double rev[n];
    //    int ii;
    //
    //    for (ii = 0; ii < n; ii++) rev[ii] = getReferenceVectorComponent(ii); //param[ 1+ii];
    //
    //    // Fill the Jacobian
    //    double J[n][n];
    //
    //    double H[n][n][n];
    //
    //    const FluxFunction & flux = fluxFunction();
    //
    //    fill_with_jet(flux, n, in, 2, 0, &J[0][0], &H[0][0][0]);
    //
    //
    //    // Find J's eigencouples and sort them.
    //    int i, info;
    //    vector<eigencouple> e;
    //    info = Eigen::eig(n, &J[0][0], e);
    //    for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
    //
    //    // Check for stop criteria
    //    // TODO This section is to be tuned.
    //    if (info == SUCCESSFUL_PROCEDURE) {
    //        // All eigenvalues must be real
    //        for (i = 0; i < n; i++) {
    //            if (fabs(e[i].i) > 0) {
    //                return COMPLEX_EIGENVALUE;
    //            }
    //        }
    //
    //        // All eigenvalues must be different.
    //        // This test can be performed thus because the eigencouples are ordered
    //        // according to the real part of the eigenvalue. Thus, if two eigenvalues
    //        // are equal, one will come after the other.
    //        for (i = 0; i < n - 1; i++) {
    //            if (e[i].r == e[i + 1].r) {
    //                return ABORTED_PROCEDURE;
    //            }
    //        }
    //
    //    } else return ABORTED_PROCEDURE;
    //
    //    // The eigenvector to be returned is the one whose inner product with the
    //    // reference vector is positive.
    //    if (prodint(n, &(e[family].vrr[0]), &rev[0]) > 0) {
    //        for (i = 0; i < n; i++) out[i] = e[family].vrr[i];
    //    } else {
    //        for (i = 0; i < n; i++) out[i] = -e[family].vrr[i];
    //    }
    //
    //    // STOP CRITERION:
    //    // The identity in Proposition 10.11 of
    //    // "An Introduction to Conservation Laws:
    //    // Theory and Applications to Multi-Phase Flow" must not change
    //    // sign (that is, the rarefaction is monotonous).
    //
    //    double res[n];
    //
    //    applyH(n, &(e[family].vrr[0]), &H[0][0][0], &(e[family].vrr[0]), &res[0]);
    //    double dlambda_dtk = prodint(n, &res[0], &(e[family].vlr[0])) /
    //            prodint(n, &(e[family].vlr[0]), &(e[family].vrr[0]));
    //    if (dlambda_dtk * ref_speed < 0) {
    //        return ABORTED_PROCEDURE;
    //    }
    //
    //    // Set the value of the last viable eigenvalue (the shock speed):
    //    lasteigenvalue = e[family].r;
    //
    //    // Update the value of the reference eigenvector:
    //    for (i = 0; i < n; i++) {
    //        //        re[i] = out[i]; <<-Teste
    //        setReferenceVectorComponent(i, out[i]);
    //    }
    //    return SUCCESSFUL_PROCEDURE;
}

void ContinuationRarefactionFlow::fill_with_jet(const RpFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);

    //    cout<<"Tamanho em fill: "<<n<<endl;
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];
    //    cout << "Entrada em fill: " << r << endl;
    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);

    JetMatrix c_jet(n);
    //    cout <<"Depois da linha 296"<<c_jet.size()<<endl;
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
    //    cout <<"Dentro de fill with jet cont rare flow"<<endl;
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

int ContinuationRarefactionFlow::flux(int n, int family, const FluxFunction &ff, const AccumulationFunction &gg, int type, double *in, double *lambda, double *out) { /* NEW HERE */
    /* NEW BELOW */
    int info;
    vector<eigenpair> e;
    if (type == _SIMPLE_ACCUMULATION_) {
        double J[n][n];
        fill_with_jet(ff, n, in, 1, 0, &J[0][0], 0);
        info = Eigen::eig(n, &J[0][0], e);
    } else {
        //        cout<<"Rare nao e simple"<<endl;
        double A[n][n], B[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) A[i][j] = B[i][j] = 0.0;
        }

        fill_with_jet(ff, n, in, 1, 0, &A[0][0], 0);
        //        printf("here1\n");
        fill_with_jet(gg, n, in, 1, 0, &B[0][0], 0);
        //        printf("here2\n");

        //        printf("in = \n");
        //        printf("[");
        //        for (int j = 0; j < n; j++) printf("%e ", in[j]);
        //        printf("]\n");
        //
        //        printf("A = \n");
        //        for (int i = 0; i < n; i++) {
        //            printf("[");
        //            for (int j = 0; j < n; j++) printf("%e ", A[i][j]);
        //            printf("]\n");
        //        }
        //
        //        printf("B = \n");
        //        for (int i = 0; i < n; i++) {
        //            printf("[");
        //            for (int j = 0; j < n; j++) printf("%e ", B[i][j]);
        //            printf("]\n");
        //        }

        info = Eigen::eig(n, &A[0][0], &B[0][0], e);



        //        printf("after eig: info = %d, e.size() = %d\n", info, e.size());
    }
    /* NEW ABOVE */

    //    Eigen::print_eigen(e);
    //    printf("in the middle: e.size() = %d\n", e.size());
    //    printf("here3.5\n");
    if (fabs(e[family].i) > 1e-5) {
        //        printf("Complex eigenvalue\n"); printf("here4\n");
        return COMPLEX_EIGENVALUE;
    } else {
        *lambda = e[family].r;
        if (out != 0) for (int i = 0; i < n; i++) {
                out[i] = e[family].vrr[i];

                //                cout << "out referencia" << out[i] << endl;
                //                printf("here5\n");

            }
        return SUCCESSFUL_PROCEDURE;
    }
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

