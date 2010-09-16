/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyFluxFunction.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CoreyFluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

CoreyFluxFunction::CoreyFluxFunction(const CoreyParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams) : FluxFunction(FluxParams(params.params())) {

    capil_ = new Capillarity(capilParams);
    perm_ = new Permeability(permParams);
    viscParams_ = new ViscosityParams(viscParams.epsl());

}

CoreyFluxFunction::CoreyFluxFunction(const CoreyFluxFunction & copy) : FluxFunction(copy.fluxParams()) {

    capil_ = new Capillarity(copy.capil());
    perm_ = new Permeability(copy.perm());
    viscParams_ = new ViscosityParams(copy.visc());

}

RpFunction * CoreyFluxFunction::clone() const {
    return new CoreyFluxFunction(*this);
}

CoreyFluxFunction::~CoreyFluxFunction() {

    delete capil_;
    delete perm_;
    delete viscParams_;

}

int CoreyFluxFunction::jet(const WaveState &U, JetMatrix &M, int degree) const {

    double sw, so, sg, Grwg, Grwo, Grow, Grog, lambdaw, lambdao, lambdag, lambda;

    CoreyParams & coreyParams = (CoreyParams &) fluxParams();

    sw = U(0);
    so = U(1);
    sg = 1. - sw - so;
    // acrescentei as 4 definicoes abaixo, e mudei as ultimas duas linhas desta funcao. Fazer o mesmo
    // na DF. Isto nao eh por economia, mas por legibilidade das formulas.

    Grwg = coreyParams.grw() - coreyParams.grg();
    Grwo = coreyParams.grw() - coreyParams.gro();
    Grow = -1. * Grwo;
    Grog = coreyParams.gro() - coreyParams.grg();

    lambdaw = perm().kw(sw, so, sg) / coreyParams.muw();
    lambdao = perm().ko(sw, so, sg) / coreyParams.muo();
    lambdag = perm().kg(sw, so, sg) / coreyParams.mug();

    lambda = lambdaw + lambdao + lambdag;

    double v = coreyParams.vel();

    M(0, (lambdaw / lambda) * (v + lambdao * Grwo + lambdag * Grwg));
    M(1, (lambdao / lambda) * (v + lambdaw * Grow + lambdag * Grog));


    if (degree > 0) {

        double lambda2, dlambdawdsw, dlambdaodsw, dlambdagdsw, dlambdadsw;
        double dlambdawdso, dlambdaodso, dlambdagdso, dlambdadso;

        dlambdawdsw = perm().dkwdsw(sw, so, sg) / coreyParams.muw();
        dlambdaodsw = perm().dkodsw(sw, so, sg) / coreyParams.muo();
        dlambdagdsw = perm().dkgdsw(sw, so, sg) / coreyParams.mug();
        dlambdadsw = dlambdawdsw + dlambdaodsw + dlambdagdsw;
        dlambdawdso = perm().dkwdso(sw, so, sg) / coreyParams.muw();
        dlambdaodso = perm().dkodso(sw, so, sg) / coreyParams.muo();
        dlambdagdso = perm().dkgdso(sw, so, sg) / coreyParams.mug();
        dlambdadso = dlambdawdso + dlambdaodso + dlambdagdso;
        lambda2=pow(lambda,2.);

        M(0, 0, (lambdaw / lambda) * (dlambdaodsw * (Grwo) + dlambdagdsw *
                (Grwg)) + ((lambda * dlambdawdsw - lambdaw * dlambdadsw) / lambda2) *
                (v + lambdao * (Grwo) + lambdag * (Grwg)));

        M(0, 1, (lambdaw / lambda) * (dlambdaodso * (Grwo) + dlambdagdso *
                (Grwg)) + ((lambda * dlambdawdso - lambdaw * dlambdadso) / lambda2) *
                (v + lambdao * (Grwo) + lambdag * (Grwg)));


        M(1, 0, (lambdao / lambda) * (dlambdawdsw * (-Grwo) + dlambdagdsw *
                (Grog)) + ((lambda * dlambdaodsw - lambdao * dlambdadsw) / lambda2) *
                (v + lambdaw * (-Grwo) + lambdag * (Grog)));


        M(1, 1, (lambdao / lambda) * (dlambdawdso * (-Grwo) + dlambdagdso *
                (Grog)) + ((lambda * dlambdaodso - lambdao * dlambdadso) / lambda2) *
                (v + lambdaw * (-Grwo) + lambdag * (Grog)));

        
    }



    if (degree > 1) {

        int i, j, k;

        for (i = 0; i < M.n_comps(); i++) {

            for (j = 0; j < M.n_comps(); j++) {

                for (k = 0; k < M.n_comps(); k++) {

                    if (i == j)

                        M(i, j, k, 1);

                    else

                        M(i, j, k, 0);

                }
            }
        }



    }


    return 0;

}

void CoreyFluxFunction::viscosity(const RealVector & U, RealMatrix2 & viscosity) {

    RealMatrix2 capillarity_jacobian(2, 2);

    capil().jacobian(U, capillarity_jacobian);
    viscosity.mul(capillarity_jacobian);
    viscosity.scale(viscParams_->epsl());

}

void CoreyFluxFunction::balance(const RealVector & U, RealMatrix2 & balanceMatrix) {

    CoreyParams & coreyParams = (CoreyParams &) fluxParams();
    double sw = U(0);
    double so = U(1);
    double sg = 1. - sw - so;
    double lambdaw = perm().kw(sw, so, sg) / coreyParams.muw();
    double lambdao = perm().ko(sw, so, sg) / coreyParams.muo();
    double lambdag = perm().kg(sw, so, sg) / coreyParams.mug();
    double lambda = lambdaw + lambdao + lambdag;
    double fw = lambdaw / lambda;
    double fo = lambdao / lambda;

    balanceMatrix(0, 0, lambdaw * (1. - fw));
    balanceMatrix(0, 1, lambdaw * (-fo));
    balanceMatrix(1, 0, balanceMatrix(0, 1));
    balanceMatrix(1, 1, lambdao * (1. - fo));

}





