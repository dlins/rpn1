/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CommandConfiguration;

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
        
        configuration_= RPNUMERICS.getConfiguration("fundamentalcurve");
        
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
