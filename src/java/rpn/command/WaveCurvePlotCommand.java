package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;
import rpnumerics.WaveCurveCalc;

public class WaveCurvePlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Wave Curve";
    static private WaveCurvePlotCommand instance_ = null;

    public WaveCurvePlotCommand() {

        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        WaveCurveCalc waveCurveCalc = RPNUMERICS.createWaveCurveCalc(oPoint);
        WaveCurveGeomFactory factory = new WaveCurveGeomFactory(waveCurveCalc);

        return factory.geom();

     }

    static public WaveCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurvePlotCommand();
        }
        return instance_;
    }
}
