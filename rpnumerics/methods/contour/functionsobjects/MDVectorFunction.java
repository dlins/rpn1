package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDVectorFunction extends CubeFunction {

	private int dimension;
	private int m;
	
	private FluxFunction fluxFunction;
	private HugoniotParams params;
	
	public MDVectorFunction(int dimension, int m, FluxFunction fluxFunction, HugoniotParams params) {
		super(fluxFunction, params);
		this.dimension = dimension;
		this.m = m;
		
		this.fluxFunction = fluxFunction;
		this.params = params;
	}

	public FluxFunction getFluxFunction() {
		return this.fluxFunction;
	}
	
	public HugoniotParams getParams() {
		return params;
	}
	
	public int getFunctionDimension() {
	    return this.dimension;
	}
	
	public RealVector value(RealVector u) {
		return null;
	}

	public RealMatrix2 deriv(RealVector u)  {		
		return null;
	}
	
	public RealVector value(PointNDimension u) throws CanNotPerformCalculations {
		return null;
	}

	public RealMatrix2 deriv(PointNDimension u) throws CanNotPerformCalculations {		
		return null;
	}
	
	public int getParameterComponentNumber() {
	    return this.dimension;
	}
	
	public int getResultComponentNumber() {
	    return this.m;
	}	
	
}
