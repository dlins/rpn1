/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.*;
import wave.util.RealVector;
import org.netlib.lapack.DGEES;
import org.netlib.lapack.DGEEV;
import org.netlib.util.intW;

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
    private ConservationShockFlow myFlow_;


    //
    // Constructors
    //
    public HugoniotPoint(PhasePoint xZero, RealVector x, double sigma) {
        super(x);
       
//      myFlow_ = (ConservationShockFlow) RPNUMERICS.createShockFlow();//new ConservationShockFlow(xZero, sigma);
        ShockFlowParams params = new ShockFlowParams(xZero, sigma);
        myFlow_ = (ConservationShockFlow) RPNUMERICS.createShockFlow(params);
        initType();
    }

    public HugoniotPoint(PhasePoint xZero, RealVector x) {
        this(xZero, x,((ShockFlow) RPNUMERICS.createShockFlow(new ShockFlowParams(xZero))).sigmaCalc(xZero.getCoords(), x));
    }

     

    
    
    protected void initType() {
        RealMatrix2 dfLeft = myFlow_.fluxDeriv(myFlow_.getXZero().getCoords());
        RealMatrix2 dfRight = myFlow_.fluxDeriv(this);
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
