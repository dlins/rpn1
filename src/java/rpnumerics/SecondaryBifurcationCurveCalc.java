/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class SecondaryBifurcationCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //

    int xResolution_;
    int yResolution_;

    public SecondaryBifurcationCurveCalc(ContourParams params) {
        super(params);
        
      
    }

    @Override
    public RpSolution calc() throws RpException {
        RpSolution result = null;

        result = (SecondaryBifurcationCurve) nativeCalc();

        if (result == null) {
            throw new RpException("Error in native layer");
        }
 

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;
 
}
