/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;


//
// finds a stationary point of the vector field f
// using Newton method with the starting point x0
//
// fills the local information of eigenvalues, eigenvectors, schur decompositions
// the point initial_ is taken as an initial guess.
//
import wave.util.*;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.PhasePoint;
import rpnumerics.*;
public class StationaryPointCalc implements RpCalculation {
    //
    // Constants
    //

    //
    // Members
    //
    private PhasePoint initial_;

    //
    // Constructors
    //
    public StationaryPointCalc(PhasePoint initial) {
        initial_ = initial;
    }

    //
    // Accessors/Mutators
    //
    public PhasePoint getInitPoint() { return initial_; }

    public void setInitPoint(PhasePoint initial) { initial_ = initial; }

    //
    // Methods
    //
    public  native RpSolution recalc() throws RpException;

    public native  RpSolution calc() throws RpException;

}
