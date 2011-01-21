/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

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
    private String methodName_;
    private int familyIndex_;
    private String flowName_;

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
        RpSolution    result = calc(methodName_, start_);

        return result;
    }

    private native RpSolution calc(String methodName, PhasePoint initialpoint) throws RpException;

    public String getCalcMethodName() {
        return methodName_;

    }

  
}
