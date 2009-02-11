package rpn.usecase;

import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;


import rpnumerics.RPNUMERICS;

public class RarefactionForwardOrbitPlotAgent extends RpModelPlotAgent {

    static public final String DESC_TEXT = "Rarefaction Forward Orbit";
    static private RarefactionForwardOrbitPlotAgent instance_ = null;

    public RarefactionForwardOrbitPlotAgent() {

        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);

        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint, 1));//timeDirection)new RarefactionOrbitCalc(oPoint,-1));

        return factory.geom();

    }

    static public RarefactionForwardOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new RarefactionForwardOrbitPlotAgent();
        }
        return instance_;

    }
}
