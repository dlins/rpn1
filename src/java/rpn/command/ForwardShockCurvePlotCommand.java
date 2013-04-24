/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.*;
import wave.util.RealVector;

public class ForwardShockCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "ForwardShockCurve";

    //
    // Members
    //
    static private ForwardShockCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ForwardShockCurvePlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Calling ForwardShockCurvePlotAgent");
//        RPNUMERICS.getShockProfile().setXZero(new PhasePoint(input[0]));
//
//        HugoniotCurveCalc hugoniotCurveCalc = RPNUMERICS.createHugoniotCalc();
//        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(hugoniotCurveCalc);
        return null;

    }

    static public ForwardShockCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ForwardShockCurvePlotCommand();
        }
        return instance_;
    }
}
