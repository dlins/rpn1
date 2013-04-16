package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RarefactionCurveGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class RarefactionCurvePlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Rarefaction Curve";
    static private RarefactionCurvePlotCommand instance_ = null;

    public RarefactionCurvePlotCommand() {

        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionCurveGeomFactory factory = new RarefactionCurveGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));

        return factory.geom();
     }

    static public RarefactionCurvePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new RarefactionCurvePlotCommand();
        }
        return instance_;
    }
}
