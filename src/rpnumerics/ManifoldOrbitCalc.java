/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.*;
import org.netlib.lapack.DGEES;
import org.netlib.lapack.DTRSYL;
import org.netlib.util.intW;
import org.netlib.util.doubleW;
import rpnumerics.PhasePoint;
import rpnumerics.RpNumerics;
import rpnumerics.RpException;
import wave.util.RealVector;
import wave.util.RealMatrix2;

/*
  Computations:
  a)evaluates the second order approximation of the manifold, see Section 4.2
  b)finds value of h = UuT(firstPoint - stationaryPoint)
  c)corrects value of h using formula (4.27), where T  is chosen such that the inequalities ||x(T) - xapp(T)||  eps and ||x(T) - x(-)||  prefDeltaNorm are satisfied (the bisection method is used to find the value of T  such that one of the inequalities is satisfied as an equality).
  d)numerical integrations from the point x(T) = x0 + Uuh,  where we set T = 0.
*/

public class ManifoldOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    private StationaryPoint stationaryPoint_;
    private PhasePoint firstPoint_;
    private int timeDirection_;

    //
    // Constructors
    //
    public ManifoldOrbitCalc(StationaryPoint stationaryPoint, PhasePoint firstPoint, int timeDirection) {
        timeDirection_ = timeDirection;
        stationaryPoint_ = stationaryPoint;
        firstPoint_ = firstPoint;
    }

    //
    // Accessors/Mutators
    //
    // TODO Alexei, is this really the first point ?


    public int tDirection() { return timeDirection_; }

    //
    // Methods
    //
    public native RpSolution recalc() throws RpException;
    
    public  native RpSolution calc() throws RpException;



}
