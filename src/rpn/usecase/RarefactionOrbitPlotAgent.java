package rpn.usecase;

import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class RarefactionOrbitPlotAgent extends RpModelPlotAgent {

    static public final String DESC_TEXT = "Rarefaction Orbit";
    static private RarefactionOrbitPlotAgent instance_ = null;

    public RarefactionOrbitPlotAgent() {

        super(DESC_TEXT, rpn.RPnConfig.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));

        return factory.geom();

     }

    static public RarefactionOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new RarefactionOrbitPlotAgent();
        }
        return instance_;
    }
}
