/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import rpn.configuration.CurveConfiguration;

public class CoincidenceCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //
    public CoincidenceCurveCalc() {
        super(new ContourParams());
        CurveConfiguration curveConfig = (CurveConfiguration) RPNUMERICS.getConfiguration("inflectioncurve");
        configuration_ = curveConfig.clone();
        String[] resolution = {"resolution"};
        configuration_.keepParameters(resolution);



    }

    public CoincidenceCurveCalc(ContourParams params) {
        super(params);
        CurveConfiguration curveConfig = (CurveConfiguration) RPNUMERICS.getConfiguration("inflectioncurve");
        configuration_ = curveConfig.clone();
        String[] resolution = {"resolution"};
        configuration_.keepParameters(resolution);
    }
    
    
    

    @Override
    public RpSolution calc() throws RpException {


        CoincidenceCurve result = (CoincidenceCurve) nativeCalc();

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;
}
