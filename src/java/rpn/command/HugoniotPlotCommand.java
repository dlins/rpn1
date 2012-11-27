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

public class HugoniotPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Hugoniot Curve";

    //
    // Members
    //
    static private HugoniotPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected HugoniotPlotCommand() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(RPNUMERICS.createHugoniotCalc(input[0]));
        return factory.geom();
    }

    static public HugoniotPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new HugoniotPlotCommand();
        }
        return instance_;
    }

   
}
