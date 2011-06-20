/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;
import wave.ode.ODESolver;

public class CompositeCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
    private int timeDirection_;
    private int increase_;
    private String methodName_;
    private int familyIndex_;
    private String flowName_;
    int xResolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;

    //
    // Constructors/Initializers
    //
    public CompositeCalc(PhasePoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }

    public CompositeCalc(String methodName, String flowName, PhasePoint point, int familyIndex, int timeDirection) {

        methodName_ = methodName;

        start_ = point;
        timeDirection_ = timeDirection;
        familyIndex_ = familyIndex;
        methodName_ = "ContinuationRarefactionMethod";//TODO Put the correct method name

    }

    public CompositeCalc(int xResolution, int yResolution, PhasePoint startPoint, int increase, int leftFamily, int rightFamily, int characteristicDomain) {

        this.xResolution_ = xResolution;
        this.yResolution_ = yResolution;
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;

        System.out.println("Curve Family:"+curveFamily_);
        System.out.println("Domain Family:" + domainFamily_);


        characteristicDomain_ = characteristicDomain;
        start_ = startPoint;
        increase_ = increase;
    }

    CompositeCalc(OrbitPoint orbitPoint, int timeDirection, ODESolver odeSolver, String methodName) {
        start_ = orbitPoint;
        timeDirection_ = timeDirection;
//        solver_ = odeSolver;
        methodName_ = methodName;

    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        RpSolution result = null;


        result = (CompositeCurve) nativeCalc(xResolution_, yResolution_, start_, increase_, curveFamily_, domainFamily_, characteristicDomain_);
        if (result == null) {
            throw new RpException("Error in native layer");
        }



        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, PhasePoint start, int increase, int leftFamily, int rightFamily, int characteristicDomain) throws RpException;
}
