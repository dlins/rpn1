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
        
        String className = getClass().getSimpleName().toLowerCase();
        
        String curveName = className.replace("calc", "");
        
        configuration_= new CommandConfiguration(curveName);
        
        configuration_.setParamValue("family", String.valueOf(familyIndex));
        configuration_.setParamValue("direction", String.valueOf(timeDirection));
        configuration_.setParamValue("inputpoint", point.toString());
        
        
        

       
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
