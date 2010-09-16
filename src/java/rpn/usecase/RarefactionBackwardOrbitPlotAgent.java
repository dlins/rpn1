package rpn.usecase;

import javax.swing.JToggleButton;
import rpn.component.RarefactionOrbitGeomFactory;
import rpn.component.RpGeometry;
import rpnumerics.OrbitPoint;
import wave.util.RealVector;
import rpnumerics.RPNUMERICS;

public class RarefactionBackwardOrbitPlotAgent extends RpModelPlotAgent {
    
    static public final String DESC_TEXT = "Rarefaction Backward Orbit";
    static private RarefactionBackwardOrbitPlotAgent instance_ = null;
    
    public RarefactionBackwardOrbitPlotAgent() {
        
        super(DESC_TEXT, rpn.RPnConfig.ORBIT_BWD,new JToggleButton());
    }
    
    public RpGeometry createRpGeometry(RealVector[] input) {
        
        OrbitPoint oPoint = new OrbitPoint(input[input.length - 1]);
        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(RPNUMERICS.createRarefactionCalc(oPoint));

        return factory.geom();
    }
    
    
    static public RarefactionBackwardOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new RarefactionBackwardOrbitPlotAgent();
        }
        return instance_;
    }
}
