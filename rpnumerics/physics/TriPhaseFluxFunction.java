/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.multid.map.*;
import wave.multid.Multid;
import wave.multid.Space;
import wave.util.HessianMatrix;
import wave.util.MatrixVector;
import wave.util.RealMatrix2;
import wave.util.IsoTriang2DBoundary;
import wave.util.Boundary;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class TriPhaseFluxFunction implements FluxFunction {


	
   //
    // Members
    //
    private TriPhaseFluxParams params_;
    private Capillarity capil_;
    private Permeability perm_;
    private ViscosityParams viscParams_;

	//
	// Constructors
	//
	public TriPhaseFluxFunction(TriPhaseFluxParams params,PermParams permParams,CapilParams capilParams,ViscosityParams viscParams) {

 	      params_ = params;
  	      capil_ = new Capillarity(capilParams);
   	     perm_ = new Permeability(permParams);
    	    viscParams_ = new ViscosityParams(viscParams.epsl());

	}


	// 
	// Accessors/Mutators
	//
   public FluxParams fluxParams() { return params_; }
    public Permeability perm() { return perm_; }
    public Capillarity capil() { return capil_; }

    //
    // Methods
    //
 
    public RealVector F(RealVector U) {
        double sw = U.getElement(0);
        double so = U.getElement(1);
        double sg = 1. - sw - so;
        double lambdaw = perm().kw(sw, so, sg) / params_.muw();
        double lambdao = perm().ko(sw, so, sg) / params_.muo();
        double lambdag = perm().kg(sw, so, sg) / params_.mug();
        double lambda = lambdaw + lambdao + lambdag;
        double[] F = new double[2];
        F[0] = (lambdaw / lambda) * (params_.vel() + lambdao * (params_.grw() - params_.gro()) + lambdag *
            (params_.grw() - params_.grg()));
        F[1] = (lambdao / lambda) * (params_.vel() + lambdaw * (params_.gro() - params_.grw()) + lambdag *
            (params_.gro() - params_.grg()));
        return new RealVector(F);
    }

    public RealMatrix2 DF(RealVector U) {
        double sw = U.getElement(0);
        double so = U.getElement(1);
        double sg = 1. - sw - so;
        double lambdaw = perm().kw(sw, so, sg) / params_.muw();
        double lambdao = perm().ko(sw, so, sg) / params_.muo();
        double lambdag = perm().kg(sw, so, sg) / params_.mug();
        double lambda = lambdaw + lambdao + lambdag;
        double lambda2 = Math.pow(lambda, 2);
        double dlambdawdsw = perm().dkwdsw(sw, so, sg) / params_.muw();
        double dlambdaodsw = perm().dkodsw(sw, so, sg) / params_.muo();
        double dlambdagdsw = perm().dkgdsw(sw, so, sg) / params_.mug();
        double dlambdadsw = dlambdawdsw + dlambdaodsw + dlambdagdsw;
        double dlambdawdso = perm().dkwdso(sw, so, sg) / params_.muw();
        double dlambdaodso = perm().dkodso(sw, so, sg) / params_.muo();
        double dlambdagdso = perm().dkgdso(sw, so, sg) / params_.mug();
        double dlambdadso = dlambdawdso + dlambdaodso + dlambdagdso;
        RealMatrix2 A = new RealMatrix2(2, 2);
        A.setElement(0, 0, (lambdaw / lambda) * (dlambdaodsw * (params_.grw() - params_.gro()) + dlambdagdsw *
            (params_.grw() - params_.grg())) + ((lambda * dlambdawdsw - lambdaw * dlambdadsw) / lambda2) *
            (params_.vel() + lambdao * (params_.grw() - params_.gro()) + lambdag * (params_.grw() - params_.grg())));
        A.setElement(0, 1, (lambdaw / lambda) * (dlambdaodso * (params_.grw() - params_.gro()) + dlambdagdso *
            (params_.grw() - params_.grg())) + ((lambda * dlambdawdso - lambdaw * dlambdadso) / lambda2) *
            (params_.vel() + lambdao * (params_.grw() - params_.gro()) + lambdag * (params_.grw() - params_.grg())));
        A.setElement(1, 0, (lambdao / lambda) * (dlambdawdsw * (params_.gro() - params_.grw()) + dlambdagdsw *
            (params_.gro() - params_.grg())) + ((lambda * dlambdaodsw - lambdao * dlambdadsw) / lambda2) *
            (params_.vel() + lambdaw * (params_.gro() - params_.grw()) + lambdag * (params_.gro() - params_.grg())));
        A.setElement(1, 1, (lambdao / lambda) * (dlambdawdso * (params_.gro() - params_.grw()) + dlambdagdso *
            (params_.gro() - params_.grg())) + ((lambda * dlambdaodso - lambdao * dlambdadso) / lambda2) *
            (params_.vel() + lambdaw * (params_.gro() - params_.grw()) + lambdag * (params_.gro() - params_.grg())));
        return A;
    }

    public HessianMatrix D2F(RealVector U) {
        HessianMatrix j = new HessianMatrix(3);
        return j;
    }
        public  RealMatrix2 viscosity(RealVector U) {
          RealMatrix2 balance = balance(U);
          double sw = U.getElement(0);
          double so = U.getElement(1);
          double sg = 1. - sw - so;
        RealMatrix2 capillarity_jacobian = capil().jacobian(U);
        balance.mul(capillarity_jacobian);
        balance.scale(viscParams_.epsl());
        return balance;
    }

    public  RealMatrix2 balance(RealVector U) {

        double sw = U.getElement(0);
        double so = U.getElement(1);
        double sg = 1. - sw - so;
        double lambdaw = perm().kw(sw, so, sg) / params_.muw();
        double lambdao = perm().ko(sw, so, sg) / params_.muo();
        double lambdag = perm().kg(sw, so, sg) / params_.mug();
        double lambda = lambdaw + lambdao + lambdag;
        double fw = lambdaw / lambda;
        double fo = lambdao / lambda;
        RealMatrix2 balanceMatrix = new RealMatrix2(2, 2);
        balanceMatrix.setElement(0, 0, lambdaw * (1. - fw));
        balanceMatrix.setElement(0, 1, lambdaw * (-fo));
        balanceMatrix.setElement(1, 0, balanceMatrix.getElement(0, 1));
        balanceMatrix.setElement(1, 1, lambdao * (1. - fo));
        return balanceMatrix;
    }	


}
