/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 */

package rpnumerics;

import wave.util.*;

/*
 * B Matrix creation class
 *
 */

public class BlowUpLineFieldMatrix extends RealMatrix2 {

    //
    // Members
    //
    private int stateSpaceDim_;


    //
    // Constructors
    //
    public BlowUpLineFieldMatrix(int stateSpaceDim
				,RealMatrix2 df
				,HessianMatrix d2f
				,double familyEigenVal,
				RealVector familyEigenVec) {

	// B is a (2n + 1) x (2n + 2) matrix
	super(2*stateSpaceDim + 1,2*stateSpaceDim + 2);

	// stores the N dimension value
	stateSpaceDim_ = stateSpaceDim;

	buildZeroBlocks();
	build_d2f_Block(d2f,familyEigenVec);
	build_df_Block(df,familyEigenVal);
	build_eigenVec_Blocks(familyEigenVec);
	buildIdentityBlock();
    }

    //
    // Methods
    //
    protected void buildZeroBlocks() {

	/**
	 * sets the zero terms first
	 */
	setZero();
    }

    protected void build_d2f_Block(HessianMatrix d2f,RealVector familyEigenVec) {

	/**
	 * E dA/dui ri (i := 1,n) will be built from d2f Hessian Matrix
	 * resulting term is the sum of sub matrices scaling.
	 */
	RealMatrix2 dAdUrMatrix = new RealMatrix2(stateSpaceDim_,stateSpaceDim_);
	RealMatrix2 swapMatrix = new RealMatrix2(stateSpaceDim_,stateSpaceDim_);
	for (int k=0;k < stateSpaceDim_;k++) {
		for (int i=0;i < stateSpaceDim_;i++)
			for (int j=0;j < stateSpaceDim_;j++)
				swapMatrix.setElement(i,j,d2f.getElement(i,j,k));

		swapMatrix.scale(familyEigenVec.getElement(k));

		if (k == 0)
			dAdUrMatrix.set(swapMatrix);
		else
			dAdUrMatrix.add(swapMatrix);
			
	}

	// fills B with the first term
	dAdUrMatrix.copySubMatrix(0,0,stateSpaceDim_,stateSpaceDim_,0,0,this);
    }

    protected void build_df_Block(RealMatrix2 df,double familyEigenVal) {

	/**
	 * A - lamda*I
	 */
	RealMatrix2 AMinusLambdaI = new RealMatrix2(df);
	RealMatrix2 identityMatrix1 = new RealMatrix2(stateSpaceDim_,stateSpaceDim_);
	identityMatrix1.scale(familyEigenVal);
	AMinusLambdaI.sub(identityMatrix1);

	// fills B with the third term
	AMinusLambdaI.copySubMatrix(0,0,stateSpaceDim_,stateSpaceDim_,0,stateSpaceDim_ + 1,this);
    }

    protected void build_eigenVec_Blocks(RealVector familyEigenVec) {

	/**
	 * negates the family eigen vector
	 */
	for (int i=0;i < stateSpaceDim_;i++) 
		setElement(i,stateSpaceDim_,-1*familyEigenVec.getElement(i));
	/**
	 * scales the family eigen vector on 2
	 */
	RealVector rT = new RealVector(familyEigenVec);
	rT.scale(2);
	for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) 
		setElement(stateSpaceDim_,i,rT.getElement(i - (stateSpaceDim_ + 1)));
	/**
	 * negates the family eigen vector 
	 */
	for (int i=stateSpaceDim_ + 1;i < 2*stateSpaceDim_ + 1;i++) 
		setElement(i,2*stateSpaceDim_ + 1,-1*familyEigenVec.getElement(i - (stateSpaceDim_ + 1)));
    }
	
    protected void buildIdentityBlock() {

	/**
	 * Identity matrix
	 */
	RealMatrix2 identityMatrix2 = new RealMatrix2(stateSpaceDim_,stateSpaceDim_);
	identityMatrix2.copySubMatrix(0,0,stateSpaceDim_,stateSpaceDim_,stateSpaceDim_ + 1,0,this);
    }		
}
