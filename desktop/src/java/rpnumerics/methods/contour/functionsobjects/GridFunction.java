package rpnumerics.methods.contour.functionsobjects;


import rpnumerics.methods.contour.*;
import rpnumerics.*;
import wave.util.*;

public abstract class GridFunction extends MDVectorFunction {
	
	private VectorialFunctionsOnMatrixGrid FluxGrid = null;
	
	protected FluxFunction fluxFunction;
	protected HugoniotParams params;
	
	private int dimensionOfFunction; 
	private int numberOfFunctions; 
	
	public GridFunction(int dimensionOfFunction, int numberOfFunctions, FluxFunction fluxFunction, HugoniotParams params) {
		super(dimensionOfFunction, numberOfFunctions, fluxFunction, params);
		
		this.fluxFunction = fluxFunction;
		this.params = params;
		
		this.numberOfFunctions = numberOfFunctions;
		this.dimensionOfFunction = dimensionOfFunction;
		
	}
		
	public FluxFunction getFluxFunction() {
		return fluxFunction;
	}
	
	public RealVector value(PointNDimension point) throws CanNotPerformCalculations {		
		return fluxFunction.F(point.toRealVector());
	}
		
	protected RealVector value(FunctionParameters coordinates) throws CanNotPerformCalculations {
		if (FluxGrid == null) {
			throw new CanNotPerformCalculations(); 
		}
		
		double[] solution = new double[numberOfFunctions];
		
		for(int pont_solution = 0; pont_solution < numberOfFunctions; pont_solution++) {
			try {
				
				EvaluationFromGridDouble evaluation = (EvaluationFromGridDouble) FluxGrid.getSolutionAt(coordinates, pont_solution);
				solution[pont_solution] = evaluation.getEvaluation();
			} catch (CanNotPerformCalculations e) {
				throw new CanNotPerformCalculations();
			}
		}
		
		return new RealVector(solution);
	}
		
	/*protected RealVector getSolutionAt(FunctionParameters coordinates) throws CanNotPerformCalculations {
		if (FluxGrid == null) {
			throw new CanNotPerformCalculations();
		}
		
		double[] solution = new double[numberOfFunctions];
		
		for(int pont_solution = 0; pont_solution < numberOfFunctions; pont_solution++) {
			try {
				solution[pont_solution] = FluxGrid.getSolutionAt(coordinates, pont_solution);
			} catch (CanNotPerformCalculations e) {
				throw new CanNotPerformCalculations();
				
			}
		}
		
		return new RealVector(solution);
	}*/
	
	protected RealVector getPointCoordinates(FunctionParameters coordinates) {		
		try {
			return (FluxGrid.getPointCoordinates(coordinates.toIntArray())).toRealVector();
		} catch (Exception e) {
			return null;
		}
	}
		
	public abstract Object clone(); 
	
	public void initFluxGrid(VectorialFunctionsOnMatrixGrid FluxGrid){
		this.FluxGrid = FluxGrid;
	}
		
	public int getFunctionDimension() {
	   return this.dimensionOfFunction;
    }

}

