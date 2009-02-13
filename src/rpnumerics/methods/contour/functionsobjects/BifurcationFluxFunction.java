package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.*;
import rpnumerics.FluxFunction;
import wave.util.*;

public class BifurcationFluxFunction extends GridFunction {

	private MatrixFunctionsOnGrid JacobianGrid = null;
	
	private int dimensionOfFunction;
	private int numberOfFunctions;
	private FluxFunction fluxFunction;
	private HugoniotParams params;
		
	public BifurcationFluxFunction(int dimensionOfFunction, int numberOfFunctions,
			FluxFunction fluxFunction, HugoniotParams params) {
		super(dimensionOfFunction, numberOfFunctions, fluxFunction, params);
		this.fluxFunction = fluxFunction;
		this.dimensionOfFunction = dimensionOfFunction;
		this.numberOfFunctions = numberOfFunctions;
	}

	public RealMatrix2 deriv(PointNDimension point) throws CanNotPerformCalculations {
		return fluxFunction.DF(point.toRealVector());
	}
	
	public RealMatrix2 deriv(FunctionParameters coordinates) throws CanNotPerformCalculations {
		if (JacobianGrid == null) {
			throw new CanNotPerformCalculations(); 
		}
		
		double[][] solution = new double[numberOfFunctions][numberOfFunctions];
		
		solution = JacobianGrid.getSolutionAt(coordinates);
		
		return RealMatrix2.convert(dimensionOfFunction, dimensionOfFunction, solution);
	}
	
	public HessianMatrix deriv2(PointNDimension point) throws CanNotPerformCalculations {
		return fluxFunction.D2F(point.toRealVector());
	}
	
	public HessianMatrix deriv2(FunctionParameters coordinates) throws CanNotPerformCalculations {
		return null; // implementar futuramente com ponto de grade...
	}
		
	public void initJacobianGrid(MatrixFunctionsOnGrid JacobianGrid){
		this.JacobianGrid = JacobianGrid;
	}

	public Object clone() {
		return new BifurcationFluxFunction(dimensionOfFunction, numberOfFunctions, fluxFunction, params);
	}
		
}
