package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RarefactionCurveGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class RarefactionOrbitPlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Rarefaction Orbit";
    static private RarefactionOrbitPlotCommand instance_ = null;

    public RarefactionOrbitPlotCommand() {

        super(DESC_TEXT, null,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionCurveGeomFactory factory = new RarefactionCurveGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));

        return factory.geom();

     }

    static public RarefactionOrbitPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new RarefactionOrbitPlotCommand();
        }
        return instance_;
    }
}
