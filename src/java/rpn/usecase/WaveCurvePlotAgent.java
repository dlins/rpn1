package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class WaveCurvePlotAgent extends RpModelPlotAgent {

    static public final String DESC_TEXT = "Wave Curve";
    static private WaveCurvePlotAgent instance_ = null;

    public WaveCurvePlotAgent() {

        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        WaveCurveGeomFactory factory = new WaveCurveGeomFactory(RPNUMERICS.createWaveCurveCalc(oPoint));

        return factory.geom();

     }

    static public WaveCurvePlotAgent instance() {
        if (instance_ == null) {
            instance_ = new WaveCurvePlotAgent();
        }
        return instance_;
    }
}
