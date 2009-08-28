/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class ShockCurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Shock Curve";

    //
    // Members
    //
    static private ShockCurvePlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected ShockCurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        ShockCurveGeomFactory factory = new ShockCurveGeomFactory(RPNUMERICS.createShockCurveCalc(oPoint));
        return factory.geom();
    }

    static public ShockCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new ShockCurvePlotAgent();
        }
        return instance_;
    }
}
