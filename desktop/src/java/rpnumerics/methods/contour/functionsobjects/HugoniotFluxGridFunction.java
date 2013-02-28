package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.FluxFunction;
import wave.util.*;

public class HugoniotFluxGridFunction extends GridFunction {

	private FluxFunction function;
	
	public HugoniotFluxGridFunction(int dimensionOfFunction, int numberOfFunctions,
			FluxFunction fluxFunction, HugoniotParams params) {
		super(dimensionOfFunction, numberOfFunctions, fluxFunction, params);
		
		this.function =  fluxFunction;
	}	
	
    @Override
	public RealMatrix2 deriv(PointNDimension point) throws CanNotPerformCalculations {
		return function.DF(point.toRealVector());
	}
	
	public final Object clone() {
		return null;
	}
	
}
