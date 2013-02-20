/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

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
    
    
      @Override
    public RpSolution recalc(List<Area> areaListToRefine) throws RpException {
        return nativeCalc(areaListToRefine);
    }

    private native RpSolution nativeCalc() throws RpException;
    private native RpSolution nativeCalc(List<Area> areasToRefine) throws RpException;
 
}
