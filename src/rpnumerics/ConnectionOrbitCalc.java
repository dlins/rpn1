/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.RpException;
import wave.util.RealMatrix2;
import wave.util.RealVector;

/*
Creates a connecting orbit using Newton s (or gradient) method for manifoldObitA and manifoldObitB.
Spep in the method is based on equations (5.3), (5.4).
New reference points are obtained using equation (5.1) and corrected by
deleting a component proportional to f(x(t)).
A gradient method is used is ||xPa - xPb|| > maxPSectionStepLength or |Ds| > maxSigmaStepLength.
 */

public class ConnectionOrbitCalc implements RpCalculation {
    // bound for changing approximated part
    static double APPROXIMATION_CHANGE_RELATIVE_MAXIMUM = 0.5;
    //
    // Members
    //
    private ManifoldOrbit manifoldOrbitA_;
    private ManifoldOrbit manifoldOrbitB_;
    private int flag_;
    private int iterationNumber_;
    private double sigmaAccuracy_;

    //
    // Constructor
    //
    public ConnectionOrbitCalc(ManifoldOrbit manifoldOrbitA, ManifoldOrbit manifoldOrbitB) {

    }

    //
    // Accessors/Mutators
    //
    public ManifoldOrbit getManifoldOrbitA() { return manifoldOrbitA_; }

    public ManifoldOrbit getManifoldOrbitB() { return manifoldOrbitB_; }

    public int getIterationNumber() { return iterationNumber_; }

    public double getSigmaAccuracy() { return sigmaAccuracy_; }

    //
    // Methods
    //
    public native RpSolution recalc() throws RpException;
    
    public native RpSolution calc() throws RpException;
}
