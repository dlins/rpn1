package rpn.usecase;

import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeometry;
import wave.util.RealVector;


import rpnumerics.PhasePoint;
import rpnumerics.RarefactionOrbitCalc;

public class RarefactionForwardOrbitPlotAgent extends RpModelPlotAgent {


    static public final String DESC_TEXT = "Rarefaction Forward Orbit";

    static private RarefactionForwardOrbitPlotAgent instance_ = null;


    public RarefactionForwardOrbitPlotAgent() {

        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        PhasePoint oPoint = new PhasePoint(input[input.length - 1]);
        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(new RarefactionOrbitCalc(oPoint,1));
        return factory.geom();

    }

    static public RarefactionForwardOrbitPlotAgent instance() {
        if (instance_ == null)
            instance_ = new RarefactionForwardOrbitPlotAgent();
        return instance_;

    }





}
