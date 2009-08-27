/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.ode.ODESolver;

public class RarefactionOrbitCalc implements RpCalculation {
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
    private ODESolver solver_;

    //
    // Constructors/Initializers
    //
    public RarefactionOrbitCalc(PhasePoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }

    public RarefactionOrbitCalc(String methodName, String flowName, PhasePoint point, int familyIndex, int timeDirection) {

        methodName_ = methodName;

        start_ = point;
        timeDirection_ = timeDirection;
        familyIndex_ = familyIndex;
        methodName_ = "ContinuationRarefactionMethod";//TODO Put the correct method name

    }

    RarefactionOrbitCalc(OrbitPoint orbitPoint, int timeDirection, ODESolver odeSolver, String methodName) {
        start_ = orbitPoint;
        timeDirection_ = timeDirection;
        solver_ = odeSolver;
        methodName_ = methodName;

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
//        solver_.getProfile().setFunction(new FlowVectorField(RPNUMERICS.createRarefactionFlow()));
        return calc();

    }

    public RpSolution calc() throws RpException {
        RpSolution    result = calc(methodName_, flowName_, start_, familyIndex_, timeDirection_);
        return result;
    }

    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;

    public String getCalcMethodName() {
        return methodName_;

    }

  
}
