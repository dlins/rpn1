/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public  class WaveCurveOrbitCalc extends OrbitCalc  {
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
   

    public int getFamilyIndex(){
        return familyIndex_;
    }

   

}
