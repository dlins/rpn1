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

public class DiscriminantPointLevelCurvePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Discriminant Point Level";

    //
    // Members
    //
    static private DiscriminantPointLevelCurvePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected DiscriminantPointLevelCurvePlotCommand() {
        super(DESC_TEXT, null,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createDiscriminantPointLevelCurveCalc(input[0]));
        return factory.geom();
    }

    static public DiscriminantPointLevelCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new DiscriminantPointLevelCurvePlotCommand();
        }
        return instance_;
    }



    
}
