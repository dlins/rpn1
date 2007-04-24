/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.ode.ODESolution;

public class OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    private OrbitPoint start_;
    private int timeDirection_;

    //
    // Constructors/Initializers
    //
    public OrbitCalc(OrbitPoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }

    //
    // Accessors/Mutators
    //
    public int tDirection() { return timeDirection_; }

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();
    }

    public RpSolution calc() throws RpException {

        ODESolution odeSol = AdvanceCurve.calc(start_.getCoords(), timeDirection_);
        return new Orbit(odeSol.getCoordinates(), odeSol.getTimes(), odeSol.getFlag());
    }
}
