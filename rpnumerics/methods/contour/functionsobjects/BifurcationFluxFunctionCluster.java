package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.physics.FluxFunction;

public class BifurcationFluxFunctionCluster  {

	private BifurcationFluxFunction[] functions;
	private HugoniotParams params;
	
	public BifurcationFluxFunctionCluster(BifurcationFluxFunction[] function, 
										  HugoniotParams params) {
		super();
		
		this.functions = function; 		
		this.params = params;
	}

	public BifurcationFluxFunction getFunctionAt(int index) {
		return functions[index];
	}
		
	public HugoniotParams getHugoniotParams() {
		return this.params;
	}
}
