/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import rpnumerics.RpException;

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
    public native RpSolution recalc() throws RpException;

    public native  RpSolution calc() throws RpException ;
}
