package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RpGeometry;
import rpn.component.WaveCurveGeomFactory;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class WaveCurvePlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Wave Curve";
    static private WaveCurvePlotCommand instance_ = null;

    public WaveCurvePlotCommand() {

        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        WaveCurveGeomFactory factory = new WaveCurveGeomFactory(RPNUMERICS.createWaveCurveCalc(oPoint));

        return factory.geom();

     }

    static public WaveCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new WaveCurvePlotCommand();
        }
        return instance_;
    }
}
