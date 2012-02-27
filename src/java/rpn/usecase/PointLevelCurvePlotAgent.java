/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.*;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class PointLevelCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Point Level Curve";

    //
    // Members
    //
    static private PointLevelCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected PointLevelCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        LevelCurveGeomFactory factory = new LevelCurveGeomFactory(RPNUMERICS.createPointLevelCurveCalc(input[0]));
        return factory.geom();
    }

    static public PointLevelCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new PointLevelCurvePlotAgent();
        }
        return instance_;
    }



    
}
