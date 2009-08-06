/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.component.*;
import wave.util.RealVector;

public class BackwardShockCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "BackwardShockCurve";

    //
    // Members
    //
    static private BackwardShockCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BackwardShockCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Calling BackwardShockCurvePlotAgent");
//        RPNUMERICS.getShockProfile().setXZero(new PhasePoint(input[0]));
//
//        HugoniotCurveCalc hugoniotCurveCalc = RPNUMERICS.createHugoniotCalc();
//        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(hugoniotCurveCalc);
        return null;

    }

    static public BackwardShockCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new BackwardShockCurvePlotAgent();
        }
        return instance_;
    }
}
