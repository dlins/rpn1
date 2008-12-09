/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.*;

public class RarefactionFlow extends WaveFlow {

    public static int FIRST = 1;    //
    // Members
    //
    private PhasePoint referenceVector_;
    private int familyIndex_ = 1;
    private FluxFunction flux_;
    //
    // Constructors
    //
    public RarefactionFlow(PhasePoint xZero, int familyIndex, FluxFunction flux) {

        referenceVector_ = new PhasePoint(xZero);
        familyIndex_ = familyIndex;
        flux_ = flux;
    }

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    public WavePoint flux(RealVector x) {

        //	System.out.println("rarefaction flux u = " + u);

        WaveState input = new WaveState(new PhasePoint(x));
        JetMatrix output = new JetMatrix(x.getSize());
        getFlux().jet(input, output, 1);

        RealMatrix2 df = new RealMatrix2(output.n_comps(), output.n_comps()); //TODO Replace for JacobianMatrix

        for (int i = 0; i < output.n_comps(); i++) {
            for (int j = 0; j < output.n_comps(); j++) {
                df.setElement(i, j, output.getElement(i, j));
            }
        }

//        RealMatrix2 df = getFlux().DF(u);
        int stateSpaceDim = x.getSize();

        double[] eigenValR = new double[stateSpaceDim];
        double[] eigenValI = new double[stateSpaceDim];
        RealVector[] eigenVec = new RealVector[stateSpaceDim];

        RealMatrix2 eigenCalcMatrix = new RealMatrix2(df);
        eigenCalcMatrix.fillEigenData(stateSpaceDim, df, eigenValR, eigenValI,
                eigenVec);

        // getting eigenvalues and eigenvectors sorted
        // with increasing absolute value of real part
        RealVector.sortEigenData(stateSpaceDim, eigenValR, eigenValI, eigenVec);

        RealVector rarefactionVector = eigenVec[getFamily()];
        //	System.out.println("rarefaction flux rarefactionVector  = " + rarefactionVector);

        if (rarefactionVector.dot(getReferenceVector().getCoords()) < 0.) {
            rarefactionVector.negate();
        }

        setReferenceVector(new PhasePoint(rarefactionVector));
        //	System.out.println("rarefaction flux new referenceVector  = " + referenceVector_);


        WavePoint returned = new WavePoint(rarefactionVector, eigenValR[getFamily()]);

        return returned;

//        return rarefactionVector;
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

    public PhasePoint getReferenceVector() {
        return referenceVector_;
    }

    public PhasePoint getXZero() {
        return getReferenceVector();
    }

    public void setXZero(PhasePoint xzero) {

        setReferenceVector(xzero);
    }

    public String getName() {
        return "Rarefaction Flow";

    }

    public FluxFunction getFlux() {
        return flux_;
    }

    public void setReferenceVector(PhasePoint referenceVector_) {
        this.referenceVector_ = referenceVector_;
    }

    public int getFamily() {
        return familyIndex_;
    }

    public void setFamily(int familyIndex_) {
        this.familyIndex_ = familyIndex_;
    }
}
