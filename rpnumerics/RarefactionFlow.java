/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.*;

public class RarefactionFlow implements WaveFlow {

    public static int FIRST = 1;

    //
    // Members
    //
    private PhasePoint referenceVector_;
    private int familyIndex_ = 1;

    //
    // Constructors
    //
    public RarefactionFlow(PhasePoint xZero, int familyIndex) {

        referenceVector_ = new PhasePoint(xZero);
        familyIndex_ = familyIndex;
    }

    //
    // Accessors/Mutators
    //
    public PhasePoint getReferenceVector() {
        return referenceVector_;
    }

    //
    // Methods
    //
    public RealVector flux(RealVector u) {

        //	System.out.println("rarefaction flux u = " + u);

        RealMatrix2 df = RPNUMERICS.fluxFunction().DF(u);
        int stateSpaceDim = u.getSize();

        double[] eigenValR = new double[stateSpaceDim];
        double[] eigenValI = new double[stateSpaceDim];
        RealVector[] eigenVec = new RealVector[stateSpaceDim];

        RealMatrix2 eigenCalcMatrix = new RealMatrix2(df);
        eigenCalcMatrix.fillEigenData(stateSpaceDim, df, eigenValR, eigenValI,
                                      eigenVec);

        // getting eigenvalues and eigenvectors sorted
        // with increasing absolute value of real part
        RealVector.sortEigenData(stateSpaceDim, eigenValR, eigenValI, eigenVec);

        RealVector rarefactionVector = eigenVec[familyIndex_];
        //	System.out.println("rarefaction flux rarefactionVector  = " + rarefactionVector);

        if (rarefactionVector.dot(referenceVector_.getCoords()) < 0.) {
            rarefactionVector.negate();
        }

        referenceVector_ = new PhasePoint(rarefactionVector);
        //	System.out.println("rarefaction flux new referenceVector  = " + referenceVector_);
        return rarefactionVector;
    }

    public RealMatrix2 fluxDeriv(RealVector u) {

        /** @todo  not implemented yet...*/
        int stateSpaceDim = u.getSize();
        return new RealMatrix2(stateSpaceDim, stateSpaceDim);
    }

    public HessianMatrix fluxDeriv2(RealVector u) {
        /** @todo  not implemented yet...*/
        int stateSpaceDim = u.getSize();
        return new HessianMatrix(stateSpaceDim);
    }

    public PhasePoint getXZero() {
        return referenceVector_;
    }


    public void setXZero(PhasePoint xzero) {

        referenceVector_ = xzero;
    }
}
