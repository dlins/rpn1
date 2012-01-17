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
    private int familyIndex_;

    //
    // Constructors/Initializers
    //
    public OrbitCalc(OrbitPoint point, int familyIndex, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
        familyIndex_ = familyIndex;

    }

    public OrbitCalc(OrbitPoint orbitPoint, int timeDirection, ODESolver odeSolver) {
        start_ = orbitPoint;
        timeDirection_ = timeDirection;
        odeSolver_ = odeSolver;


    }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        FlowVectorField flowVectorField = (FlowVectorField) odeSolver_.getProfile().getFunction();
//        flowVectorField.setWaveFlow(RPNUMERICS.createShockFlow()); //Updating flow parameters
        if (timeDirection_ == 0) {

            ODESolution odeSolForward = odeSolver_.solve(getStart(), 1);
            ODESolution odeSolBackward = odeSolver_.solve(getStart(), -1);
            Orbit forwardOrbit = new Orbit(odeSolForward.getWavePoints(), odeSolForward.getTimes(), odeSolForward.getFlag());
            Orbit backwardOrbit = new Orbit(odeSolBackward.getWavePoints(), odeSolBackward.getTimes(), odeSolBackward.getFlag());
            Orbit complete = Orbit.concat(backwardOrbit, forwardOrbit,1);

            return complete;


        }

        ODESolution odeSol = odeSolver_.solve(getStart(), timeDirection_);

        return new Orbit(odeSol.getWavePoints(), odeSol.getTimes(), odeSol.getFlag());
    }

    public int getFamilyIndex() {
        return familyIndex_;
    }

    public OrbitPoint getStart() {
        return start_;
    }

    public int getDirection() {
        return timeDirection_;
    }
}
