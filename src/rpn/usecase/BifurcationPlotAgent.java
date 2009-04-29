package rpn.usecase;

import rpn.component.*;
import rpnumerics.*;
import wave.util.*;

public class BifurcationPlotAgent extends RpModelPlotAgent {

	static public final String DESC_TEXT = "Bifurcation Curve";
    
    static private BifurcationPlotAgent instance_ = null;
	
	public BifurcationPlotAgent() {
		super(DESC_TEXT, rpn.RPnConfig.HUGONIOT);
	}

	public RpGeometry createRpGeometry(RealVector[] coords) {
		BifurcationCurveCalc bifurcationCurveCalc = RPNUMERICS.createBifurcationCalc();
		
		BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(bifurcationCurveCalc);
		
	    return factory.geom();
	}
	
	 static public BifurcationPlotAgent instance() {
	        if (instance_ == null) {
	            instance_ = new BifurcationPlotAgent();
	        }
	        
	        return instance_;
	 }

}
