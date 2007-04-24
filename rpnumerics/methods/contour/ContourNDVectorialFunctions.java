package rpnumerics.methods.contour;

import rpnumerics.methods.contour.exceptions.CanNotInitializeGrid;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.exceptions.DimensionOutOfBounds;

public class ContourNDVectorialFunctions extends ContourND {

	private MDVectorFunction vectorialFunction;
	
	public ContourNDVectorialFunctions(MDVectorFunction functionp, 
									   int dimensionp, 
									   HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws IllegalArgumentException {
		super(Converter.fromVectorialToCube(functionp), dimensionp, hyperCubeErrorTreatment);
	
		this.vectorialFunction = functionp;
	}
	
	protected GridGenerator initializeSolutionConstraints(int dimension, 
														  Constraint[] constraints, 
														  CubeFunction[] function, 
														  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) 
																							  throws DimensionOutOfBounds,
																								     CanNotInitializeGrid {
													
		GridGeneratorUsingReducedMemory solutionp;
		
		MDVectorFunction[] tmp = new MDVectorFunction[function.length];
		
		for (int pont_function = 0; pont_function < function.length; pont_function++) {
			tmp[pont_function] = (MDVectorFunction) function[pont_function];
		}
		
		solutionp = new VectorialFunctionsOnGridUsingReducedMemory(dimension, 
																   constraints, 
																   tmp, 
																   hyperCubeErrorTreatment);
		
		return solutionp;
	}

	public MDVectorFunction getVectorialFunction() {
		return this.vectorialFunction;
	}
	
}
