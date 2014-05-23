/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class StoneExplicitSecondaryBifurcationCurveCalc extends SecondaryBifurcationCurveCalc {
    
    private  int opositeSide_;
    private  int numberOfSteps_;
  
     //
    // Constructors/Initializers
    //
    
    public StoneExplicitSecondaryBifurcationCurveCalc(ContourParams params,int opositeSide, int numberOfSteps) {
        super(params);
        
        opositeSide_=opositeSide;
        numberOfSteps_=numberOfSteps;
      
    }

    @Override
    public RpSolution calc() throws RpException {
        RpSolution result = null;

        result = (SecondaryBifurcationCurve) nativeCalc(opositeSide_,numberOfSteps_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }
 

        return result;
    }
    
    
      @Override
    public RpSolution recalc(List<Area> areaListToRefine) throws RpException {
        return nativeCalc(areaListToRefine);
    }

    private native RpSolution nativeCalc(int opositeSide,int numberOfSteps) throws RpException;
    private native RpSolution nativeCalc(List<Area> areasToRefine) throws RpException;
 
}
