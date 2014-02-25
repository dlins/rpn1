/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.ShockCurveCalc;

public class ShockCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Shock Curve";
    //
    // Members
    //
    static private ShockCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ShockCurvePlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        ShockCurveCalc shockCalc = RPNUMERICS.createShockCurveCalc(oPoint);
        ShockCurveGeomFactory factory = new ShockCurveGeomFactory(shockCalc);
        return factory.geom();
    }

    static public ShockCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new ShockCurvePlotCommand();
        }
        return instance_;
    }
}
