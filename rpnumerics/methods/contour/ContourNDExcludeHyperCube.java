package rpnumerics.methods.contour;

import rpnumerics.methods.contour.exceptions.SolutionNotFound;
import rpnumerics.methods.contour.functionsobjects.*;
import wave.exceptions.DimensionOutOfBounds;

public class ContourNDExcludeHyperCube extends ContourNDDecorator {

	public ContourNDExcludeHyperCube(ContourND contour)
			throws IllegalArgumentException {
		super(contour);
		// TODO Auto-generated constructor stub
	}
	
	protected double[][] evaluateFunctions(GridGenerator solution, FunctionParameters parameters) throws SolutionNotFound,
																										 DimensionOutOfBounds,
																										 CanNotPerformCalculations {
    	
    	double[][] foncub_ = new double[numberOfEquations][numberOfVertices];
    	
		int numberOfVertices = solution.getNumberOfVertices();
		
		double[] feasibleSolution = new double[numberOfVertices];						
		
		if (!jumpTest(parameters, function)) {
			
			for (int function_pointer = 1; function_pointer <= numberOfEquations; function_pointer++) {
				
				feasibleSolution = solution.solveHyperCube(parameters, function[function_pointer - 1]);
				
				if (feasibleSolution == null) {
					throw new SolutionNotFound();
				} else {		    							
					foncub_[function_pointer - 1] = feasibleSolution;
				}
			}
		} else {
			throw new SolutionNotFound();
		}
		
		return foncub_;
    } 
	
	protected boolean jumpTest(FunctionParameters parameters, CubeFunction[] function) {		
   		return false;
   	}

}
