/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import rpn.configuration.CurveConfiguration;

public class BuckleyLeverettinInflectionCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //
    public BuckleyLeverettinInflectionCurveCalc() {
        super(new ContourParams());

        CurveConfiguration curveConfig = (CurveConfiguration) RPNUMERICS.getConfiguration("inflectioncurve");
        configuration_ = curveConfig.clone();
        String[] resolution = {"resolution"};
        configuration_.keepParameters(resolution);




    }

    public BuckleyLeverettinInflectionCurveCalc(ContourParams params) {
        super(params);
        CurveConfiguration curveConfig = (CurveConfiguration) RPNUMERICS.getConfiguration("inflectioncurve");
        configuration_ = curveConfig.clone();
        String[] resolution = {"resolution"};
        configuration_.keepParameters(resolution);

    }

    public RpSolution calc() throws RpException {


        BuckleyLeverettInflectionCurve result = (BuckleyLeverettInflectionCurve) nativeCalc();

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
