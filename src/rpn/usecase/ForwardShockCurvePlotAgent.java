/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;

public class ForwardShockCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "ForwardShockCurve";

    //
    // Members
    //
    static private ForwardShockCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ForwardShockCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Calling ForwardShockCurvePlotAgent");
//        RPNUMERICS.getShockProfile().setXZero(new PhasePoint(input[0]));
//
//        HugoniotCurveCalc hugoniotCurveCalc = RPNUMERICS.createHugoniotCalc();
//        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(hugoniotCurveCalc);
        return null;

    }

    static public ForwardShockCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new ForwardShockCurvePlotAgent();
        }
        return instance_;
    }
}
