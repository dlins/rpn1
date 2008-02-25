/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import rpnumerics.RpException;

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
