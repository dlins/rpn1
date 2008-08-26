/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.ode.ODESolution;
import wave.ode.ODESolver;

public class OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    private OrbitPoint start_;
    private int timeDirection_;
    private ODESolver odeSolver_;
    private String calcMethodName_;
    private WaveFlow flow_;

    //
    // Constructors/Initializers
    //
    public OrbitCalc(OrbitPoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
        calcMethodName_ = "default";//TODO Put the correct method name
    }

    OrbitCalc(OrbitPoint orbitPoint, int timeDirection, ODESolver odeSolver) {
        start_ = orbitPoint;
        timeDirection_ = timeDirection;
        odeSolver_ = odeSolver;
        calcMethodName_ = "default";//TODO Put the correct method name


    }
    
    
     OrbitCalc(OrbitPoint orbitPoint, int timeDirection, ODESolver odeSolver,WaveFlow flow) {
        start_ = orbitPoint;
        timeDirection_ = timeDirection;
        odeSolver_ = odeSolver;
        calcMethodName_ = "default";//TODO Put the correct method name
        flow_=flow;


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

//        ODESolution odeSol = AdvanceCurve.calc(start_.getCoords(), timeDirection_);

        ODESolution odeSol = odeSolver_.solve(getStart(),timeDirection_);
        return new Orbit(odeSol.getWavePoints(), odeSol.getTimes(), odeSol.getFlag());
    }

    public String getCalcMethodName() {
        return calcMethodName_;

    }

    public OrbitPoint getStart() {
        return start_;
    }

    public WaveFlow getFlow() {

        return flow_;

    }
}
