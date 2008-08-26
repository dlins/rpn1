/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 */

package rpnumerics;

import wave.util.*;

/*
 * BlowUp Vector creation class
 *
 */

public class BlowUpLineFieldVector extends RealVector {

    //
    // Members
    //

    private int stateSpaceDim_;
    private int familyIndex_;
    private RealVector familyVector_;
    private int lambdaDir_;
    private FluxFunction flux_;

    //
    // Constructors
    //
    public BlowUpLineFieldVector(RealVector x,int familyIndex,FluxFunction flux) {

	// line field vector is a 2n + 2 vector
	super(2*x.getSize() + 2);
	stateSpaceDim_ = x.getSize();
	familyIndex_ = familyIndex;
	familyVector_ = new RealVector(x);
        flux_=flux;
	// line field vector is u,lambda,r,l
	build_u_Component(x);
	build_eigen_Components(x);
	build_l_Component(x);

    }

    //
    // Accessors/Mutators
    //
    public int getFamilyIndex(){return familyIndex_;}
    public RealVector getFamilyVector(){return familyVector_;}

    //
    // Methods
    //
    protected void build_u_Component(RealVector x) {

	/**
	 * u is the input vector itself
	 */
	for (int i=0;i < stateSpaceDim_;i++)
		setElement(i,x.getElement(i));
    }

    protected void build_eigen_Components(RealVector x) {

	/**
	 * lambda is the eigen value with family index
	 */
//	RealMatrix2 df = RPNUMERICS.fluxFunction().DF(x);

        RealMatrix2 df = flux_.DF(x);

	double[] eigenValR = new double[stateSpaceDim_];
	double[] eigenValI = new double[stateSpaceDim_];
	RealVector[] eigenVec = new RealVector[stateSpaceDim_];

	RealMatrix2 eigenCalcMatrix = new RealMatrix2(df);
	eigenCalcMatrix.fillEigenData(stateSpaceDim_,df,eigenValR,eigenValI,eigenVec);

	// getting eigenvalues and eigenvectors sorted
        // with increasing absolute value of real part
        RealVector.sortEigenData(stateSpaceDim_, eigenValR, eigenValI, eigenVec);

	setElement(stateSpaceDim_,eigenValR[familyIndex_]);

	for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) {
		setElement(i,eigenVec[familyIndex_].getElement(i - (stateSpaceDim_ + 1)));
	}

    }

    protected void build_eigen_Components_P(RealVector x) {

	/**
	 * lambda is the eigen value with family index
	 */
//	RealMatrix2 df = RPNUMERICS.fluxFunction().DF(x);
        
        RealMatrix2 df = flux_.DF(x);


	double[] eigenValR = new double[stateSpaceDim_];
	double[] eigenValI = new double[stateSpaceDim_];
	RealVector[] eigenVec = new RealVector[stateSpaceDim_];

	RealMatrix2 eigenCalcMatrix = new RealMatrix2(df);
	eigenCalcMatrix.fillEigenData(stateSpaceDim_,df,eigenValR,eigenValI,eigenVec);

	// getting eigenvalues and eigenvectors sorted
        // with increasing absolute value of real part
        RealVector.sortEigenData(stateSpaceDim_, eigenValR, eigenValI, eigenVec);
	RealVector eigenP0 = eigenVec[familyIndex_];

	// compares eigenvalues for u and (u + eps*eigenVec)
        double lambdaP0 = eigenValR[familyIndex_];
	RealVector scaledEigen = new RealVector(eigenP0);
	scaledEigen.scale(RPNUMERICS.errorControl().eps());
        RealVector P = new RealVector(x);
	P.add(scaledEigen);

//	df =RPNUMERICS.fluxFunction().DF(P);
        
        df = flux_.DF(P);
        
	eigenCalcMatrix.fillEigenData(stateSpaceDim_,df,eigenValR,eigenValI,eigenVec);
        double lambdaP = eigenValR[familyIndex_];

        if (lambdaP0 < lambdaP)
                if (lambdaDir_ == RPNUMERICS.INCREASING_LAMBDA) {
			setElement(stateSpaceDim_,lambdaP0);
			for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) {
				setElement(i,eigenP0.getElement(i - (stateSpaceDim_ + 1)));
			}
		}
                else {
                        eigenP0.negate();
			setElement(stateSpaceDim_,lambdaP0);
			for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) {
				setElement(i,eigenP0.getElement(i - (stateSpaceDim_ + 1)));
			}
		}
        else
                if (lambdaDir_ == RPNUMERICS.INCREASING_LAMBDA) {

                        eigenP0.negate();
			setElement(stateSpaceDim_,lambdaP0);
			for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) {
				setElement(i,eigenP0.getElement(i - (stateSpaceDim_ + 1)));
			}
		}
                else {
			setElement(stateSpaceDim_,lambdaP0);
			for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) {
				setElement(i,eigenP0.getElement(i - (stateSpaceDim_ + 1)));
			}
		}
     }

    protected void build_l_Component(RealVector x) {

	/**
	 * l is zero for now...
	 */
	setElement(2*stateSpaceDim_ + 1,0);
    }

}
