package rpn.usecase;

import rpn.component.*;
import rpnumerics.PhasePoint;
import rpnumerics.RarefactionOrbitCalc;
import wave.util.*;

public class RarefactionBackwardOrbitPlotAgent extends RpModelPlotAgent {
    
    
    static public final String DESC_TEXT = "Rarefaction Backward Orbit";
    
    static private RarefactionBackwardOrbitPlotAgent instance_ = null;
    
    
    public RarefactionBackwardOrbitPlotAgent() {
        
        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_BWD);
    }
    
    
    public RpGeometry createRpGeometry(RealVector[] input) {
        
        PhasePoint oPoint = new PhasePoint(input[input.length - 1]);
        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(new RarefactionOrbitCalc(oPoint,-1));
        return factory.geom();
    }
    
    
    static public RarefactionBackwardOrbitPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new RarefactionBackwardOrbitPlotAgent();
        }
        return instance_;
        
    }
    
    
}
