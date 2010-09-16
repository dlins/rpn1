package rpnumerics.methods.contour.functionsobjects;

import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDVectorFunctionDecorator extends MDVectorFunction {
	
	private MDVectorFunction function;
	private int index;
	
	public MDVectorFunctionDecorator(MDVectorFunction function, int index) {
		super(function.getFunctionDimension(), function.getResultComponentNumber(), function.getFluxFunction(), function.getParams());
		this.function = function;
		this.index = index;
	}
	
	public int getFunctionDimension() {
	    return this.function.getFunctionDimension();
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public RealVector value(RealVector u) {
		return this.function.value(u);
	}

	public RealMatrix2 deriv(RealVector u) {		
		return this.function.deriv(u);
	}
	
	public RealVector value(PointNDimension u) throws CanNotPerformCalculations {
		return this.function.value(u);
	}

	public RealMatrix2 deriv(PointNDimension u) throws CanNotPerformCalculations {		
		return this.function.deriv(u);
	}
	
	public int getParameterComponentNumber() {
	    return this.function.getParameterComponentNumber();
	}
	
	public int getResultComponentNumber() {
	    return this.function.getResultComponentNumber();
	}	
	
	public MDVectorFunction getFunction() {
		return this.function;
	}
}
