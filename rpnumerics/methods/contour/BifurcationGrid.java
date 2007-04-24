
package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.exceptions.DimensionOutOfBounds;

public class BifurcationGrid extends MPGridFunctionOnGridEvaluator {

	public BifurcationGrid(int dimension, 
					       Constraint[] constraint,
					       MDVectorFunction[] functionsp,
					       HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws DimensionOutOfBounds,
					       																		CanNotPerformCalculations{
		super(dimension, constraint, functionsp, hyperCubeErrorTreatment);
		
		Constraint[] plusConstraints = this.getPlusConstraints();
		Constraint[] minusConstraints = this.getMinusConstraints();
		
		GridFunction[] plusGridFunctions = this.getPlusFunctions();
		GridFunction[] minusGridFunctions = this.getMinusFunctions();
		
		BifurcationFluxFunction[] plusFunctions = new BifurcationFluxFunction[plusGridFunctions.length];
		BifurcationFluxFunction[] minusFunctions = new BifurcationFluxFunction[minusGridFunctions.length];
		
		for(int pont_function = 0; pont_function < plusGridFunctions.length; pont_function++) {
			plusFunctions[pont_function] = (BifurcationFluxFunction) plusGridFunctions[pont_function]; 
			minusFunctions[pont_function] = (BifurcationFluxFunction) minusGridFunctions[pont_function]; 
		}
			
		int reducedDimension = (dimension / 2);
		
		for (int pont_solution = 0; pont_solution < plusFunctions.length; pont_solution++) {
						
			MatrixFunctionsOnGrid positiveSolution = 
								getMatrixFunctionEvaluator(reducedDimension, 
														 plusConstraints, 
														 new JacobianFromBifurcationFunction(reducedDimension, 
																							 plusFunctions[pont_solution]),
														 hyperCubeErrorTreatment);
			plusFunctions[pont_solution].initJacobianGrid(positiveSolution);
			
			MatrixFunctionsOnGrid negativeSolution = 
								getMatrixFunctionEvaluator(reducedDimension, 
														  minusConstraints, 
														  new JacobianFromBifurcationFunction(reducedDimension, 
															 								  minusFunctions[pont_solution]),
														  hyperCubeErrorTreatment);
			minusFunctions[pont_solution].initJacobianGrid(negativeSolution);			
		
		}
	}
	
	protected MatrixFunctionsOnGrid getMatrixFunctionEvaluator(int dimension,
																Constraint[] constraint, 
																MatrixFunction matrixFunction,
																HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)  
														throws DimensionOutOfBounds, 
																CanNotPerformCalculations{
		
		return new MatrixFunctionsOnGrid(dimension, 
										 constraint, 
										 matrixFunction,
										 hyperCubeErrorTreatment);
		
	}

}
