/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.*;
import wave.util.RealVector;

public class HugoniotPoint extends RealVector {
    //
    // Constants
    //
    //
    // Members
    //
    private RealVector eigenValRLeft_;
    private RealVector eigenValRRight_;
    private HugoniotPointType type_;
    private ShockFlow myFlow_;
    //
    // Constructors
    //
    public HugoniotPoint(PhasePoint xZero, RealVector x, double sigma) {
        super(x);
        ShockFlowParams params = new ShockFlowParams(xZero, sigma);
        myFlow_ = RPNUMERICS.createShockFlow(params);
        initType();
    }

    public HugoniotPoint(PhasePoint xZero, RealVector x) {
        this(xZero, x, ShockFlow.sigmaCalc(xZero.getCoords(), x));
    }

    protected void initType() {

        RealMatrix2 dfLeft = new RealMatrix2(myFlow_.getXZero().getSize(), myFlow_.getXZero().getSize());
        JacobianMatrix jMatrix = myFlow_.fluxDeriv(myFlow_.getXZero().getCoords());

        for (int i = 0; i < dfLeft.getNumRow(); i++) {
            for (int j = 0; j < dfLeft.getNumCol(); j++) {
                dfLeft.setElement(i, j, jMatrix.getElement(i, j));
            }
        }
        RealMatrix2 dfRight = new RealMatrix2(myFlow_.getXZero().getSize(), myFlow_.getXZero().getSize());

        jMatrix = myFlow_.fluxDeriv(this);

        for (int i = 0; i < dfRight.getNumRow(); i++) {
            for (int j = 0; j < dfRight.getNumCol(); j++) {
                dfRight.setElement(i, j, jMatrix.getElement(i, j));
            }
        }

        int stateSpaceDim = dfLeft.getNumRow();
        double[] eigenValRLeft = new double[stateSpaceDim];
        double[] eigenValRRight = new double[stateSpaceDim];
        double[] eigenValI = new double[stateSpaceDim];
        dfRight.fillEigenVals(dfLeft, eigenValRLeft, eigenValI);
        dfRight.fillEigenVals(dfRight, eigenValRRight, eigenValI);
        eigenValRLeft_ = new RealVector(eigenValRLeft);
        eigenValRLeft_.sort();
        eigenValRRight_ = new RealVector(eigenValRRight);
        eigenValRRight_.sort();
        type_ = new HugoniotPointType(eigenValRLeft, eigenValRRight);
    }

    //
    // Accessors/Mutators
    //
    public HugoniotPointType type() {
        return type_;
    }

    public double sigma() {
        return myFlow_.getSigma();
    }

    public RealVector eigenValRLeft() {
        return eigenValRLeft_;
    }

    public RealVector eigenValRRight() {
        return eigenValRRight_;
    }
}
