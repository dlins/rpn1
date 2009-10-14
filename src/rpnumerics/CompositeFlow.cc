/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CompositeFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CompositeFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


CompositeFlow::CompositeFlow(int family, const RarefactionFlow & rarefactionFlow, const ShockFlow & shockFlow, const FluxFunction & fluxFunction) : WaveFlow(fluxFunction), rarefactionFlow_((RarefactionFlow *)rarefactionFlow.clone()),shockFlow_((ShockFlow *)shockFlow.clone()),family_(family) {
}

CompositeFlow::CompositeFlow(const CompositeFlow & copy) : WaveFlow(copy.fluxFunction()), family_(copy.getFamily()), rarefactionFlow_((RarefactionFlow *)copy.getRarefactionFlow().clone()),shockFlow_((ShockFlow *)copy.getShockFlow().clone()) {

}

int CompositeFlow::flux(const RealVector &, RealVector &) {
    return 2;
}

int CompositeFlow::fluxDeriv(const RealVector &, JacobianMatrix &) {
    return 2;
}

int CompositeFlow::fluxDeriv2(const RealVector &, HessianMatrix &) {
    return 2;
}

WaveFlow * CompositeFlow::clone()const {

    return new CompositeFlow(*this);
}

const RarefactionFlow & CompositeFlow::getRarefactionFlow() const{
    return *rarefactionFlow_;
}

const ShockFlow & CompositeFlow::getShockFlow() const{
    return *shockFlow_;
}

int CompositeFlow::composite(int *neq, double *xi, double *in, double *out, int *nparam, double *param) {

    int n = (*neq) / 2;

    const FluxFunction & cmp_flux_object = fluxFunction();

    // Family
    int family = (int) param[0];

    // Reference vector for the rarefaction
    double rar_ref[n];
    for (int i = 0; i < n; i++) rar_ref[i] = param[1 + i];

    // Reference vector for the shock
    double shk_ref[n];
    for (int i = 0; i < n; i++) shk_ref[i] = param[1 + n + i];

    // Double to compute inner products
    double p;

    // Compute the first part of the field: rarefaction (BEGIN)

    //int rar = rarefaction(neq, xi, in, out, nparam, param); // TODO: See if this is ok!!! param!!!

    // Eigenvalue and eigenvector at U-
    // inm = U-
    double inm[n];
    for (int i = 0; i < n; i++) inm[i] = in[i]; // This is redundant. Accessing directly the first n elements of in would suffice.

    double Jm[n][n];
    fill_with_jet(cmp_flux_object, n, inm, 1, 0, &Jm[0][0], 0);

    double lambda;
    double rm[n];
    struct eigen e[n];
    int eig_info = cdgeev(n, &Jm[0][0], &e[0]); // TODO: The 2x2 case should be done by hand

    lambda = e[family].r;
    for (int i = 0; i < n; i++) rm[i] = e[family].vrr[i];

    // Verify that the eigenvector points in the correct direction.
    // Since the eigenvector will be used by the shock, it must be corrected if need be.
    p = 0;
    for (int i = 0; i < n; i++) p += rm[i] * rar_ref[i];
    if (p > 0) {
        for (int i = 0; i < n; i++) out[i] = rm[i];
    } else {
        for (int i = 0; i < n; i++) out[i] = rm[i] = -rm[i];
    }

    //for (int i = 0; i < (*neq); i++) out[i] = 0; // ADDED FOR TESTING PURPOSES

    // Compute the first part of the field: rarefaction (END)


    // Compute the second part of the field: shock (BEGIN)
    // TODO: Generalize to n > 2

    // A lot of ingredients must be arranged to compute M and c:
    //
    // DeltaU[0] = [u] = u+ - u-; DeltaU[1] = [v] = v+ - v-.
    double DeltaU[n];
    for (int i = 0; i < n; i++) DeltaU[i] = in[i + n] - in[i];

    // DeltaUsqr = [u]^2 + [v]^2.
    double DeltaUsqr = 0;
    for (int i = 0; i < n; i++) DeltaUsqr += DeltaU[i] * DeltaU[i];

    // inp = U+
    double inp[n];
    for (int i = 0; i < n; i++) inp[i] = in[i + n];

    // Jacobian at U+
    double Jp[n][n];
    fill_with_jet(cmp_flux_object, n, inp, 1, 0, &Jp[0][0], 0);

    // Jacobian at U-
    //fill_with_jet(cmp_flux_object, (*neq), inm, 1, 0, &Jm[0][0], 0); // Here, inm[0:(*neq) - 1] = in[0:(*neq) - 1]: in could be used directly.

    // Finally, after all the ingredients are ready:
    //
    // Matrix M
    double M[n][n];
    M[0][0] = DeltaUsqr * (lambda - Jp[0][0]) +
            (1.0 - 2.0 * lambda) * DeltaU[0] * DeltaU[0] +
            (Jp[0][0] * DeltaU[0] + Jp[1][0] * DeltaU[1]) * DeltaU[0];

    M[0][1] = DeltaUsqr * (lambda - Jp[0][1]) +
            (1.0 - 2.0 * lambda) * DeltaU[1] * DeltaU[0] +
            (Jp[0][1] * DeltaU[0] + Jp[1][1] * DeltaU[1]) * DeltaU[0];

    M[1][0] = DeltaUsqr * (lambda - Jp[1][0]) +
            (1.0 - 2.0 * lambda) * DeltaU[0] * DeltaU[1] +
            (Jp[0][0] * DeltaU[0] + Jp[1][0] * DeltaU[1]) * DeltaU[1];

    M[1][1] = DeltaUsqr * (lambda - Jp[1][1]) +
            (1.0 - 2.0 * lambda) * DeltaU[1] * DeltaU[1] +
            (Jp[0][1] * DeltaU[0] + Jp[1][1] * DeltaU[1]) * DeltaU[1];

    // Vector c
    double c[n];
    c[0] = DeltaUsqr * (lambda * rm[0] - (Jm[0][0] * rm[0] + Jm[0][1] * rm[1])) +
            (1.0 - 2.0 * lambda)*(rm[0] * DeltaU[0] + rm[1] * DeltaU[1]) * DeltaU[0] +
            ((Jm[0][0] * rm[0] + Jm[0][1] * rm[1]) * DeltaU[0] +
            (Jm[1][0] * rm[0] + Jm[1][1] * rm[1]) * DeltaU[1]) * DeltaU[0];

    c[1] = DeltaUsqr * (lambda * rm[0] - (Jm[0][0] * rm[0] + Jm[0][1] * rm[1])) +
            (1.0 - 2.0 * lambda)*(rm[0] * DeltaU[0] + rm[1] * DeltaU[1]) * DeltaU[1] +
            ((Jm[0][0] * rm[0] + Jm[0][1] * rm[1]) * DeltaU[0] +
            (Jm[1][0] * rm[0] + Jm[1][1] * rm[1]) * DeltaU[1]) * DeltaU[1];

    // Solve the system M*(d U+/d xi) = -c.
    double minus_c[n];
    for (int i = 0; i < n; i++) minus_c[i] = -c[i];
    double dU_dxi[n];
    int info_cdgesv = cdgesv(n, &M[0][0], minus_c, dU_dxi); // TODO: The 2x2 case should be done by hand
    if (info_cdgesv != SUCCESSFUL_PROCEDURE) {
        printf("Cmp eng.: cdgesv returned with error.\n\n");

        // in
        printf("\n");
        for (int i = 0; i < 2 * n; i++) printf("in[%d] = %f\n", i, in[i]);
        printf("\n");

        // DeltaU
        printf("\n");
        for (int i = 0; i < 2 * n; i++) printf("DeltaU[%d] = %f\n", i, DeltaU[i]);
        printf("\n");


        for (int i = 0; i < n; i++) {
            printf("M[%d, :] = [", i);
            for (int j = 0; j < n; j++) {
                printf(" %f ", M[i][j]);
            }
            printf("]\n");
        }

        printf("\n");
        for (int i = 0; i < n; i++) printf("c[%d] = [%f]\n", i, c[i]);
        printf("\n");

        return info_cdgesv;
    }

    for (int i = 0; i < n; i++) out[i + n] = dU_dxi[i];
    //for (int i = 0; i < (*neq); i++) out[i + (*neq)] = 0; // ADDED FOR TESTING PURPOSES

    // Check that dU_dxi points in the right direction.
    // (This is probably redundant. If so, the param vector could be shortened.)
    /*
        p = 0;
        for (int i = 0; i < (*neq); i++) p += dU_dxi[i]*shk_ref[i];
        if (p > 0) for (int i = 0; i < (*neq); i++) out[(*neq) + i] = dU_dxi[i];
        else       for (int i = 0; i < (*neq); i++) out[(*neq) + i] = -dU_dxi[i];
     */
    // Compute the second part of the field: shock (END)

    //if (eig_info != SUCCESSFUL_PROCEDURE) return eig_info;
    return SUCCESSFUL_PROCEDURE;


}

int CompositeFlow::cdgeev(int n, double *A, struct eigen *e)const {

    int lda = n, lwork = 5 * n, ldvr = n, ldvl = n;
    int i, j, info;
    double vr[n][n], vl[n][n];
    double work[5 * n], wi[n], wr[n];

    if (n == 1) {
        double Delta = (A[0] - A[3])*(A[0] - A[3]) + 4 * A[1] * A[2];
        if (Delta >= 0) {
            // Eigenvalues and eigenvectors are real.

            // Eigenvalues
            double sqrtDelta = sqrt(Delta);
            double bminus = A[0] + A[3];

            if (bminus > 0) {
                wr[0] = .5 * (bminus + sqrtDelta);
                wr[1] = .5 * (bminus * bminus - Delta) / (bminus + sqrtDelta);
            } else {
                wr[0] = .5 * (bminus - sqrtDelta);
                wr[1] = .5 * (bminus * bminus - Delta) / (bminus - sqrtDelta);
            }

            //wr[0] = (A[0] + A[3] - sqrtDelta)/2;
            //wr[1] = (A[0] + A[3] + sqrtDelta)/2;

            wi[0] = 0;
            wi[1] = 0;

            // First right-eigenvector
            if (A[0] == wr[0]) {
                vr[0][0] = 1;
                vr[0][1] = 0;
            } else {
                vr[0][0] = A[1] / (wr[0] - A[0]);
                vr[0][1] = 1;
            }
            // Second right-eigenvector
            if (A[3] == wr[1]) {
                vr[1][0] = 0;
                vr[1][1] = 1;
            } else {
                vr[1][0] = 1;
                vr[1][1] = A[2] / (wr[1] - A[3]);
            }

            // First left-eigenvector
            if (A[0] == wr[0]) {
                vl[0][0] = 1;
                vl[0][1] = 0;
            } else {
                vl[0][0] = A[2] / (wr[0] - A[0]);
                vl[0][1] = 1;
            }
            // Second left-eigenvector
            if (A[3] == wr[1]) {
                vl[1][0] = 0;
                vl[1][1] = 1;
            } else {
                vl[1][0] = 1;
                vl[1][1] = A[1] / (wr[1] - A[3]);
            }

            // Normalize
            for (i = 0; i < 2; i++) {
                double sqrtlength;

                // Right-eigenvectors
                sqrtlength = sqrt(vr[i][0] * vr[i][0] + vr[i][1] * vr[i][1]);
                if (sqrtlength != 0) {
                    vr[i][0] = vr[i][0] / sqrtlength;
                    vr[i][1] = vr[i][1] / sqrtlength;
                }

                // Left-eigenvectors
                sqrtlength = sqrt(vl[i][0] * vl[i][0] + vl[i][1] * vl[i][1]);
                if (sqrtlength != 0) {
                    vl[i][0] = vl[i][0] / sqrtlength;
                    vl[i][1] = vl[i][1] / sqrtlength;
                }
            }
        } else {
            // Eigenvalues and eigenvectors are complex.
            wr[0] = (A[0] + A[3]) / 2;
            wr[1] = wr[0];

            wi[0] = fabs(-sqrt(-Delta) / 2);
            wi[1] = -wi[0];

            // Eigenvectors will not be computed because
            // they will not be used in this case anyway.
        }
        info = 0;
    } else {
        // Create a transposed copy of A to be used by LAPACK's dgeev:
        double B[n][n];
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) B[j][i] = A[i * n + j];
        }

        dgeev_("V", "V", &n, &B[0][0], &lda, &wr[0], &wi[0],
                &vl[0][0], &ldvl, &vr[0][0], &ldvr, &work[0], &lwork,
                &info);
    }

    // Process the results
    if (info != 0) return ABORTED_PROCEDURE;
    else {
        transpose(&vl[0][0], n); // ...or else...
        transpose(&vr[0][0], n); // ...or else...
        fill_eigen(e, &wr[0], &wi[0], &vl[0][0], &vr[0][0]);
        sort_eigen(e);
        return SUCCESSFUL_PROCEDURE;
    }
}

int CompositeFlow::cdgesv(int n, double *A, double *b, double *x) {
    int i, j;
    int dim = n;
    int nrhs = 1;
    int lda = n;
    int ipiv[n];
    int ldb = n;
    int info;

    // Create a transposed copy of A to be used by LAPACK's dgesv:
    double B[n][n];
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) B[j][i] = A[i * n + j];
    }

    // Create a copy of b to be used by LAPACK's dgesv:
    double bb[n];
    for (i = 0; i < n; i++) bb[i] = b[i];

    dgesv_(&dim, &nrhs, &B[0][0], &lda, &ipiv[0], &bb[0], &ldb, &info);

    if (info == 0) {
        for (i = 0; i < n; i++) x[i] = bb[i];
        return SUCCESSFUL_PROCEDURE;
    } else return ABORTED_PROCEDURE;
}

void CompositeFlow::fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
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

