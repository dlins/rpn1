/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class TransitionalLineCalc extends OrbitCalc{

    //
    // Constants
    //
    //
    // Members
    //
    private String tLineName_;  

    // Constructors/Initializers
    //
    public TransitionalLineCalc(String tLineName) {

        super(new OrbitPoint(new RealVector(2)), 0);
        
        tLineName_=tLineName;

    }
    
    

    //
    // Accessors/Mutators
    //
   
    //
    // Methods
    //
    @Override
    public RpSolution recalc() throws RpException {
        return calc();

    }

    @Override
    public RpSolution calc() throws RpException {
        RpSolution result;

        result =calc(tLineName_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        
        return result;

    }

    

    private native RpSolution calc(String methodName) throws RpException;


}
