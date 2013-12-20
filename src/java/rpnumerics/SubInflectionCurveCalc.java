/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CommandConfiguration;
import rpn.configuration.CurveConfiguration;

public class SubInflectionCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //
    public SubInflectionCurveCalc() {
        super(new ContourParams());
        
        CurveConfiguration curveConfig = (CurveConfiguration) RPNUMERICS.getConfiguration("inflectioncurve");
        
        configuration_ = curveConfig.clone();
        String[] resolution = {"resolution"};
        configuration_.keepParameters(resolution);

    }

    
    public SubInflectionCurveCalc(ContourParams params) {
        super(params);
        
        CurveConfiguration curveConfig = (CurveConfiguration) RPNUMERICS.getConfiguration("inflectioncurve");
        
        configuration_ = curveConfig.clone();
        String[] resolution = {"resolution"};
        configuration_.keepParameters(resolution);

    }

    
    
   

    public RpSolution calc() throws RpException {


        SubInflectionCurve result = (SubInflectionCurve) nativeCalc();

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;
}
