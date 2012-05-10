/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public abstract class WaveCurveOrbitCalc extends OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //

    private int familyIndex_;


    //
    // Constructors/Initializers
    //
    public WaveCurveOrbitCalc(PhasePoint point, int familyIndex,int timeDirection) {

        super(new OrbitPoint(point), timeDirection);
        familyIndex_=familyIndex;
       
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

    public int getFamilyIndex(){
        return familyIndex_;
    }

   

}
