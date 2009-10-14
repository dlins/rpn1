package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.exceptions.*;
import rpnumerics.methods.contour.nraphson.*;
import wave.util.exceptions.*;

public class ContourBifurcation extends ContourNDVectorialFunctions {

	public ContourBifurcation(MDVectorFunction functionp, HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws IllegalArgumentException {
		super(functionp, functionp.getFunctionDimension(), hyperCubeErrorTreatment);
	}
	
	protected GridGenerator initializeSolutionConstraints(int dimension, 
                                                              Constraint[] constraints,
                                                              rpnumerics.methods.contour.functionsobjects.CubeFunction[] function,
                                                              HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws DimensionOutOfBounds, CanNotInitializeGrid {
		
		MDVectorFunction[] VectorFunctionArray = new MDVectorFunction[function.length];
		
		for (int pont_func = 0; pont_func < function.length; pont_func++) {
			VectorFunctionArray[pont_func] = (MDVectorFunction) function[pont_func];
		}
		
		try {
			return new BifurcationGrid(dimension, constraints, VectorFunctionArray, hyperCubeErrorTreatment);
		} catch (CanNotPerformCalculations e) {
			throw new CanNotInitializeGrid();
		}
	}	  
	
	/*protected void initMainObjects() {   
    	this.setCFace(new CubeFace(dimension, numberOfEquations));
		this.setCSolver(new CubeSolverNRaphson(this.getCFace(), (MDVectorFunction) this.function[0]));
    }
		
	protected double[][] evaluateFunctions(GridGenerator solution, FunctionParameters parameters) throws SolutionNotFound,
	 																									 DimensionOutOfBounds,
	 																									 CanNotPerformCalculations {
		((CubeSolverNRaphson) this.getCSolver()).setParametersAndConstraints((FunctionParameters) parameters.clone(), solution);
		return super.evaluateFunctions(solution, parameters);
	}*/
	
}
