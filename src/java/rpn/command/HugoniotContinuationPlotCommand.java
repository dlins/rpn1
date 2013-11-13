/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.HugoniotContinuationCurveCalc;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.ShockCurveCalc;

public class HugoniotContinuationPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Hugoniot Continuation";
    //
    // Members
    //
    static private HugoniotContinuationPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected HugoniotContinuationPlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        HugoniotContinuationCurveCalc  shockCalc =  new HugoniotContinuationCurveCalc(oPoint, 0);
        WaveCurveGeomFactory factory = new WaveCurveGeomFactory(shockCalc);
        return factory.geom();
    }

    static public HugoniotContinuationPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new HugoniotContinuationPlotCommand();
        }
        return instance_;
    }
}
