/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydisperseHugoniotFunction.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "PolydisperseHugoniotFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



PolydisperseHugoniotFunction::PolydisperseHugoniotFunction(const RealVector& U, const Polydisperse & stoneFluxFunction) : HugoniotFunctionClass(stoneFluxFunction) {

    int n = U.size();


    const FluxFunction &fluxFunction = getFluxFunction();
    Uref.resize(n);
    for (int i = 0; i < n; i++) Uref.component(i) = U.component(i);


    // TODO: The flux object must be initialized somehow (be it created here or outside, etc.)
    UrefJetMatrix.resize(n);
    WaveState u(Uref); // TODO: Check this.
    //    stone->jet(u, UrefJetMatrix, 1);

    fluxFunction.jet(u, UrefJetMatrix, 1);

    // Find the eigenpairs of the Jacobian of the jet at Uref
    double A[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            A[i][j] = UrefJetMatrix(i, j);
        }
    }
    Eigen::eig(n, &A[0][0], ve_uref);

    double epsilon = 1.0e-6; // TODO: Tolerance must be created within a class which will be used by everyone. Say, "class Tolerance", or something like that.

    if (fabs(ve_uref[0].i) < epsilon) Uref_is_elliptic = false;
    else Uref_is_elliptic = true;
}

PolydisperseHugoniotFunction::~PolydisperseHugoniotFunction() {
}

void PolydisperseHugoniotFunction::setReferenceVector(const RealVector & refVec) {
    Uref = refVec;
    const FluxFunction & fluxFunction = getFluxFunction();
    // TODO: The flux object must be initialized somehow (be it created here or outside, etc.)
    UrefJetMatrix.resize(refVec.size());
    int n = refVec.size();
    WaveState u(refVec); // TODO: Check this.
    //    stone->jet(u, UrefJetMatrix, 1);

    fluxFunction.jet(u, UrefJetMatrix, 1);

    // Find the eigenpairs of the Jacobian of the jet at Uref
    double A[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            A[i][j] = UrefJetMatrix(i, j);
        }
    }


    Eigen::eig(n, &A[0][0], ve_uref);

    double epsilon = 1.0e-6; // TODO: Tolerance must be created within a class which will be used by everyone. Say, "class Tolerance", or something like that.

    if (fabs(ve_uref[0].i) < epsilon) Uref_is_elliptic = false;
    else Uref_is_elliptic = true;

    HugoniotFunctionClass::setReferenceVector(refVec);
}

double PolydisperseHugoniotFunction::HugoniotFunction(const RealVector & u) {
    //        Uref = getReferenceVector();
    const FluxFunction & fluxFunction = getFluxFunction();
    double sw = u(0);
    double swz = Uref(0);

    double so = u(1);
    double soz = Uref(1);

    double dsw = sw - swz;
    double dso = so - soz;

    double epsilon = 1.0e-6; // TODO: Tolerance must be created within a class which will be used by everyone. Say, "class Tolerance", or something like that.

    // Find F at the point
    double dhw, dho;
    double dfw, dfo;

    WaveState wu(u); // TODO: Is this correct?
    JetMatrix UJetMatrix(u.size());

    if (fabs(dsw) + fabs(dso) >= epsilon) {
        dhw = sw - swz;
        dho = so - soz;

        //        stone->jet(wu, UJetMatrix, 0);
        fluxFunction.jet(wu, UJetMatrix, 0);

        dfw = UJetMatrix(0) - UrefJetMatrix(0);
        dfo = UJetMatrix(1) - UrefJetMatrix(1);
    } else {
        //            stone->jet(wu, UJetMatrix, 1);
        fluxFunction.jet(wu, UJetMatrix, 0);
        dhw = dsw;
        dho = dso;
        dfw = UrefJetMatrix(0, 0) * dsw + UrefJetMatrix(0, 1) * dso;
        dfo = UrefJetMatrix(1, 0) * dsw + UrefJetMatrix(1, 1) * dso;
    }

    double hugont = dho * dfw - dhw*dfo;

    if (fabs(hugont) <= epsilon * (fabs(dho * dfw) + fabs(dhw * dfo))) hugont = 0.0;

    return hugont;
}
