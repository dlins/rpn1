package rpnumerics.methods.contour;

import rpnumerics.methods.contour.exceptions.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.exceptions.DimensionOutOfBounds;

public class ContourFixedGridDuringCalculations extends ContourNDDecorator {

	private GridGenerator grid;
	private boolean GridAlreadyCalculated = false;
	
	public ContourFixedGridDuringCalculations(ContourND contour)
			throws IllegalArgumentException {
		super(contour);
	}

	protected GridGenerator initializeSolutionConstraints(int dimension, 
			  Constraint[] constraints, 
			  rpnumerics.methods.contour.functionsobjects.CubeFunction[] function,
			  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) 
												throws DimensionOutOfBounds,
													   CanNotInitializeGrid {

		if(!GridAlreadyCalculated) {
			grid = super.initializeSolutionConstraints(dimension, constraints, function, hyperCubeErrorTreatment);
			GridAlreadyCalculated = true;
		} 	
		
		return grid;		
	}
}
