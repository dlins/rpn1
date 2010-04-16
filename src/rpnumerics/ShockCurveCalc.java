/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.ode.ODESolver;

public class ShockCurveCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
    private int timeDirection_;
    private String methodName_;
    private String flowName_;
    private int familyIndex_;

    //
    // Constructors/Initializers
    //
    public ShockCurveCalc(PhasePoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }

    public ShockCurveCalc(String methodName, String flowName, PhasePoint point, int familyIndex, int timeDirection) {

        methodName_ = methodName;

        start_ = point;
        timeDirection_ = timeDirection;
        familyIndex_ = familyIndex;
        methodName_ = "ContinuationShockMethod";//TODO Put the correct method name

    }

    //
    // Accessors/Mutators
    //
    public int tDirection() {
        return timeDirection_;
    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        if (timeDirection_ == 0) {

            ShockCurve resultForward = (ShockCurve) calc(methodName_, flowName_, start_, familyIndex_, 1);
            ShockCurve resultBackward = (ShockCurve) calc(methodName_, flowName_, start_, familyIndex_, -1);
            Orbit resultComplete = ShockCurve.concat(resultBackward, resultForward);
            ShockCurve completeCurve = new ShockCurve(resultComplete.getPoints(), resultComplete.getIntegrationFlag());

            return completeCurve;
        }

        RpSolution result = calc(methodName_, flowName_, start_, familyIndex_, timeDirection_);
        return result;
    }

    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;

    public String getCalcMethodName() {
        return methodName_;

    }
}
