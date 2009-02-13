package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import wave.util.exceptions.*;

public class ContourNDNoDiagonal extends ContourNDExcludeHyperCube {
	
	public ContourNDNoDiagonal(ContourND contour)
	throws IllegalArgumentException {
		super(contour);
	}
	
	protected boolean jumpTest(FunctionParameters parameters, CubeFunction[] function) {
    	
		boolean result = false;
		
		if ((parameters.myDimensionIs() % 2) == 0) {		
			result = testLoop(parameters);				
		}
		
		return result;
    }
	
	private boolean testLoop(FunctionParameters parameters) {
		boolean test = true;	
		int dimension = parameters.myDimensionIs();
		
		int half = dimension / 2;
		
		for (int pont_dimension = 1; (pont_dimension <= half) && test; pont_dimension++) {
			try {
				
				if (Math.abs(parameters.getIndex(pont_dimension) - parameters.getIndex(pont_dimension + half)) > 1) {
					test = false;
				}
				//test = test && (parameters.getIndex(pont_dimension) == parameters.getIndex(pont_dimension + half));
			} catch (DimensionOutOfBounds e) {
				e.printStackTrace();
			}
		}
		
		return test;
	}
		
}