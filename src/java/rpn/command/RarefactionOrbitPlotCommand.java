package rpn.command;

import javax.swing.JToggleButton;
import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class RarefactionOrbitPlotCommand extends RpModelPlotCommand {

    static public final String DESC_TEXT = "Rarefaction Orbit";
    static private RarefactionOrbitPlotCommand instance_ = null;

    public RarefactionOrbitPlotCommand() {

        super(DESC_TEXT, rpn.configuration.RPnConfig.ORBIT_FWD,new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));

        return factory.geom();

     }

    static public RarefactionOrbitPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new RarefactionOrbitPlotCommand();
        }
        return instance_;
    }
}
