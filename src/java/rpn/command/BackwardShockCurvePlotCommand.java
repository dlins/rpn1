/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import wave.util.RealVector;

public class BackwardShockCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "BackwardShockCurve";

    //
    // Members
    //
    static private BackwardShockCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BackwardShockCurvePlotCommand() {
        super(DESC_TEXT, null,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Calling BackwardShockCurvePlotAgent");
//        RPNUMERICS.getShockProfile().setXZero(new PhasePoint(input[0]));
//
//        HugoniotCurveCalc hugoniotCurveCalc = RPNUMERICS.createHugoniotCalc();
//        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(hugoniotCurveCalc);
        return null;

    }

    static public BackwardShockCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new BackwardShockCurvePlotCommand();
        }
        return instance_;
    }
}
