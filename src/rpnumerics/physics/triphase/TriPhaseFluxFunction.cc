/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TriphaseFluxFunction.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "TriPhaseFluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

TriPhaseFluxFunction::TriPhaseFluxFunction(const TriPhaseParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams):FluxFunction(FluxParams(params.params())) {
    
    capil_ = new Capillarity(capilParams);
    perm_ = new Permeability(permParams);
    viscParams_ = new ViscosityParams(viscParams.epsl());
    
}

TriPhaseFluxFunction::TriPhaseFluxFunction(const TriPhaseFluxFunction & copy ):FluxFunction(copy.fluxParams()){
    
    capil_= new Capillarity(copy.capil());
    perm_= new Permeability(copy.perm());
    viscParams_= new ViscosityParams(copy.visc());
    
}

RpFunction * TriPhaseFluxFunction::clone() const {return new TriPhaseFluxFunction(*this);}

TriPhaseFluxFunction::~TriPhaseFluxFunction(){
    
    delete capil_;
    delete perm_;
    delete viscParams_;
    
}


int TriPhaseFluxFunction::F(const WaveState &U, JetMatrix &M)const {
    
    
    double sw, so, sg , lambdaw , lambdao, lambdag, lambda;
    
    TriPhaseParams & triPhaseParams = (TriPhaseParams &) fluxParams();
    
    
    sw = U(0);
    so = U(1);
    sg = 1. - sw - so;
    lambdaw = perm().kw(sw, so, sg) / triPhaseParams.muw();
    lambdao = perm().ko(sw, so, sg) / triPhaseParams.muo();
    lambdag = perm().kg(sw, so, sg) / triPhaseParams.mug();
    lambda = lambdaw + lambdao + lambdag;
    
    M(0, (lambdaw / lambda) * (triPhaseParams.vel() + lambdao * (triPhaseParams.grw() - triPhaseParams.gro()) + lambdag *
            (triPhaseParams.grw() - triPhaseParams.grg())));
    M(1, (lambdao / lambda) * (triPhaseParams.vel() + lambdaw * (triPhaseParams.gro() - triPhaseParams.grw()) + lambdag *
            (triPhaseParams.gro() - triPhaseParams.grg())));
    
    
}
int TriPhaseFluxFunction::DF(const WaveState &U, JetMatrix &M)const {
    
    
    double sw, so, sg , lambdaw , lambdao, lambdag, lambda;
    double   lambda2, dlambdawdsw , dlambdaodsw, dlambdagdsw, dlambdadsw ;
    double  dlambdawdso, dlambdaodso, dlambdagdso, dlambdadso;
    
    TriPhaseParams & triPhaseParams = (TriPhaseParams &) fluxParams();
    
    
    lambdaw = perm().kw(sw, so, sg) / triPhaseParams.muw();
    lambdao = perm().ko(sw, so, sg) / triPhaseParams.muo();
    lambdag = perm().kg(sw, so, sg) / triPhaseParams.mug();
    lambda = lambdaw + lambdao + lambdag;
    lambda2 = pow(lambda, 2);
    dlambdawdsw = perm().dkwdsw(sw, so, sg) / triPhaseParams.muw();
    dlambdaodsw = perm().dkodsw(sw, so, sg) / triPhaseParams.muo();
    dlambdagdsw = perm().dkgdsw(sw, so, sg) / triPhaseParams.mug();
    dlambdadsw = dlambdawdsw + dlambdaodsw + dlambdagdsw;
    dlambdawdso = perm().dkwdso(sw, so, sg) / triPhaseParams.muw();
    dlambdaodso = perm().dkodso(sw, so, sg) / triPhaseParams.muo();
    dlambdagdso = perm().dkgdso(sw, so, sg) / triPhaseParams.mug();
    dlambdadso = dlambdawdso + dlambdaodso + dlambdagdso;
    
    M(0, 0, (lambdaw / lambda) * (dlambdaodsw * (triPhaseParams.grw() - triPhaseParams.gro()) + dlambdagdsw *
            (triPhaseParams.grw() - triPhaseParams.grg())) + ((lambda * dlambdawdsw - lambdaw * dlambdadsw) / lambda2) *
            (triPhaseParams.vel() + lambdao * (triPhaseParams.grw() - triPhaseParams.gro()) + lambdag * (triPhaseParams.grw() - triPhaseParams.grg())));
    
    M(0, 1, (lambdaw / lambda) * (dlambdaodso * (triPhaseParams.grw() - triPhaseParams.gro()) + dlambdagdso *
            (triPhaseParams.grw() - triPhaseParams.grg())) + ((lambda * dlambdawdso - lambdaw * dlambdadso) / lambda2) *
            (triPhaseParams.vel() + lambdao * (triPhaseParams.grw() - triPhaseParams.gro()) + lambdag * (triPhaseParams.grw() - triPhaseParams.grg())));
    
    
    M(1, 0, (lambdao / lambda) * (dlambdawdsw * (triPhaseParams.gro() - triPhaseParams.grw()) + dlambdagdsw *
            (triPhaseParams.gro() - triPhaseParams.grg())) + ((lambda * dlambdaodsw - lambdao * dlambdadsw) / lambda2) *
            (triPhaseParams.vel() + lambdaw * (triPhaseParams.gro() - triPhaseParams.grw()) + lambdag * (triPhaseParams.gro() - triPhaseParams.grg())));
    
    
    M(1, 1, (lambdao / lambda) * (dlambdawdso * (triPhaseParams.gro() - triPhaseParams.grw()) + dlambdagdso *
            (triPhaseParams.gro() - triPhaseParams.grg())) + ((lambda * dlambdaodso - lambdao * dlambdadso) / lambda2) *
            (triPhaseParams.vel() + lambdaw * (triPhaseParams.gro() - triPhaseParams.grw()) + lambdag * (triPhaseParams.gro() - triPhaseParams.grg())));
    
    
}
int TriPhaseFluxFunction::D2F(const WaveState &U, JetMatrix &M)const {
    
    int i, j, k;
    
    for (i=0; i < M.n_comps();i++){
        
        for (j=0; j < M.n_comps();j++){
            
            for (k=0;k < M.n_comps();k++){
                
                if (i==j)
                    
                    M(i, j, k, 1);
                
                else
                    
                    M(i, j, k, 0);
                
            }
        }
    }
   
    
}

int TriPhaseFluxFunction::jet(const WaveState &U, JetMatrix &M, int degree) const{
    
    switch (degree){
        
        case 0:
            
            F(U, M);
            
            break;
            
        case 1:
            
            F(U, M);
            DF(U, M);
            
            break;
            
        case 2:
            F(U, M);
            DF(U, M);
            D2F(U, M);
            
            break;
    }
    return 0;
    
}



void TriPhaseFluxFunction::viscosity(const RealVector & U, RealMatrix2 & viscosity) {
    
    RealMatrix2 capillarity_jacobian(2, 2);
    
    capil().jacobian(U, capillarity_jacobian);
    viscosity.mul(capillarity_jacobian);
    viscosity.scale(viscParams_->epsl());
    
}

void TriPhaseFluxFunction::balance(const RealVector & U, RealMatrix2 & balanceMatrix) {
    
    TriPhaseParams & triPhaseParams = (TriPhaseParams &) fluxParams();
    double sw = U(0);
    double so = U(1);
    double sg = 1. - sw - so;
    double lambdaw = perm().kw(sw, so, sg) / triPhaseParams.muw();
    double lambdao = perm().ko(sw, so, sg) / triPhaseParams.muo();
    double lambdag = perm().kg(sw, so, sg) / triPhaseParams.mug();
    double lambda = lambdaw + lambdao + lambdag;
    double fw = lambdaw / lambda;
    double fo = lambdao / lambda;
    
    balanceMatrix(0, 0, lambdaw * (1. - fw));
    balanceMatrix(0, 1, lambdaw * (-fo));
    balanceMatrix(1, 0, balanceMatrix(0, 1));
    balanceMatrix(1, 1, lambdao * (1. - fo));
    
}





