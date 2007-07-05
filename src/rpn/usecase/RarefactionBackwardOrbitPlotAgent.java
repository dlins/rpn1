package rpn.usecase;

import rpn.component.*;
import wave.util.*;
import rpnumerics.OrbitCalc;
import rpnumerics.OrbitPoint;

public class RarefactionBackwardOrbitPlotAgent extends RpModelPlotAgent {


    static public final String DESC_TEXT = "Rarefaction Backward Orbit";

    static private RarefactionBackwardOrbitPlotAgent instance_ = null;


    public RarefactionBackwardOrbitPlotAgent() {

        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_BWD);
    }


    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionOrbitFactory factory = new RarefactionOrbitFactory(new
                OrbitCalc(oPoint,
                          OrbitGeom.BACKWARD_DIR));
        return factory.geom();
    }


    static public RarefactionBackwardOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new RarefactionBackwardOrbitPlotAgent();
        }
        return instance_;

    }


}
