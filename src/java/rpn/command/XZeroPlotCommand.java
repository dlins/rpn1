/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import javax.swing.JToggleButton;
import rpn.RPnConfig;
import rpn.component.*;
import wave.util.RealVector;

public class XZeroPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Stationary Point";

    //
    // Members
    //
    static private XZeroPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected XZeroPlotCommand() {
        super(DESC_TEXT, RPnConfig.STATPOINT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
//        return  new XZeroGeomFactory(new StationaryPointCalc(new PhasePoint(input[input.length - 1]),(ShockFlow)RPNUMERICS.createShockFlow())).geom();
        return null;
    }

    static public XZeroPlotCommand instance() {
        if (instance_ == null)
            instance_ = new XZeroPlotCommand();
        return instance_;
    }
}
