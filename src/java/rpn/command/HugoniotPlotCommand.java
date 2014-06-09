/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpn.configuration.CurveConfiguration;
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
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        
        
        HugoniotCurveCalcND hugoniotCurveCalcND = new HugoniotCurveCalcND(new PhasePoint(input[0]),(CurveConfiguration) RPNUMERICS.getConfiguration("hugoniotcurve"));
        
        HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(hugoniotCurveCalcND);
        return factory.geom();
    }

    static public HugoniotPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new HugoniotPlotCommand();
        }
        return instance_;
    }

   
}
