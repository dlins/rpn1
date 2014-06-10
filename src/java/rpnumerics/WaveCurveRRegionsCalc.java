/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class WaveCurveRRegionsCalc extends WaveCurveOrbitCalc {

    //
    // Constants
    //
    // Constructors/Initializers
    //
   
    
    int waveCurveID_;

    public WaveCurveRRegionsCalc(int waveCurveID) {
        super(new OrbitPoint(new RealVector(2)),0,0);



        waveCurveID_=waveCurveID;
       
    }
    
    
//    public WaveCurveRRegionsCalc(PhasePoint input, CurveConfiguration waveCurveConfiguration) {
//
//        super(new OrbitPoint(input), Integer.parseInt(waveCurveConfiguration.getParam("family")),Integer.parseInt(waveCurveConfiguration.getParam("direction")));
//
//        configuration_ = waveCurveConfiguration.clone();
//
//    }

    

    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpSolution calc() throws RpException {
        WaveCurveRRegions result = null;
      
            result = (WaveCurveRRegions) nativeCalc(waveCurveID_);
       
        if (result == null) {
            throw new RpException("Error in native layer");
        }



        return result;
    }



    private native RpSolution nativeCalc(int waveCurveID);

   
}
