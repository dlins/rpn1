/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class PointLevelCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Point Level Curve";

    //
    // Members
    //
    static private PointLevelCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected PointLevelCurvePlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createPointLevelCurveCalc(input[0]));
        return factory.geom();
    }

    static public PointLevelCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new PointLevelCurvePlotCommand();
        }
        return instance_;
    }



    
}
