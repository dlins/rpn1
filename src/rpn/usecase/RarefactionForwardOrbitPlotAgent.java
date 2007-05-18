package rpn.usecase;

import rpn.component.RpGeometry;
import wave.util.RealVector;
import rpnumerics.OrbitPoint;
import rpnumerics.OrbitCalc;
import rpn.component.OrbitGeom;

import rpn.component.RarefactionOrbitFactory;

public class RarefactionForwardOrbitPlotAgent extends RpModelPlotAgent {


    static public final String DESC_TEXT = "Rarefaction Forward Orbit";

    static private RarefactionForwardOrbitPlotAgent instance_ = null;


    public RarefactionForwardOrbitPlotAgent() {

        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_FWD);
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionOrbitFactory factory = new RarefactionOrbitFactory(new OrbitCalc(oPoint,
                OrbitGeom.FORWARD_DIR));
        return factory.geom();

    }

    static public RarefactionForwardOrbitPlotAgent instance() {
        if (instance_ == null)
            instance_ = new RarefactionForwardOrbitPlotAgent();
        return instance_;

    }





}
