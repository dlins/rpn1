/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class RarefactionOrbitCalc extends WaveCurveOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    // Constructors/Initializers
    //
    public RarefactionOrbitCalc(PhasePoint point, int familyIndex,int timeDirection) {

        super(new OrbitPoint(point), familyIndex,timeDirection);
    }

    //
    // Accessors/Mutators
    //
   
    //
    // Methods
    //
    public RpSolution recalc() throws RpException {
        return calc();

    }


    public RpSolution calc() throws RpException {

        RarefactionOrbit result;

        result = (RarefactionOrbit) calc("methodName_", "flowName_", getStart(), getFamilyIndex(), getDirection());
        

        if (result == null) {
            throw new RpException("Error in native layer");
        }
      

        return result;

    }

    private native RpSolution calc(String methodName, String flowName, PhasePoint initialpoint, int familyIndex, int timeDirection) throws RpException;


}
