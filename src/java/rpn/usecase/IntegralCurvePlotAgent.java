package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.IntegralOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class IntegralCurvePlotAgent extends RpModelPlotAgent {

    static public final String DESC_TEXT = "Integral Curve";
    static private IntegralCurvePlotAgent instance_ = null;

    public IntegralCurvePlotAgent() {

        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        IntegralOrbitGeomFactory factory = new IntegralOrbitGeomFactory(RPNUMERICS.createIntegralCurveCalc(oPoint));

        return factory.geom();

     }

    static public IntegralCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new IntegralCurvePlotAgent();
        }
        return instance_;
    }
}
