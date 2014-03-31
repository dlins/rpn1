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
    private final String methodName_;
    private final int edge_;

    public SecondaryBifurcationCurveCalc(ContourParams params, String methodName,int edge) {
        super(params);
        methodName_=methodName;
        edge_=edge;
      
    }

    @Override
    public RpSolution calc() throws RpException {
        RpSolution result = null;

        result = (SecondaryBifurcationCurve) nativeCalc(methodName_,edge_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }
        return result;
    }
    
    
      @Override
    public RpSolution recalc(List<Area> areaListToRefine) throws RpException {
        return nativeCalc(areaListToRefine);
    }

    private native RpSolution nativeCalc(String methodName,int edge) throws RpException;
    private native RpSolution nativeCalc(List<Area> areasToRefine) throws RpException;
    
    
    public enum SecondaryBifurcationMethods {
        
        IMPLICIT,
        STONE;
        
    }
 
}
