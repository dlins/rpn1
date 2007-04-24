package rpnumerics.methods.contour;

import rpnumerics.methods.contour.exceptions.CanNotInitializeGrid;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.exceptions.DimensionOutOfBounds;

public class ContourNDCompositeFunctions extends ContourNDVectorialFunctions {

	public ContourNDCompositeFunctions(MDVectorFunction functionp, 
									   int dimensionp, 
									   HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
																throws IllegalArgumentException {
		super(functionp, dimensionp, hyperCubeErrorTreatment);
	}
	
    protected GridGenerator initializeSolutionConstraints(int dimension, 
    													  Constraint[] constraints, 
    													  CubeFunction[] function, 
														  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) 
																							throws DimensionOutOfBounds,
																							       CanNotInitializeGrid {
    	
    	GridGenerator solutionp = null;
    	    	
    	MDVectorFunction[] mdVectorFunctions = new MDVectorFunction[function.length];
    	
    	for(int pont_func = 0; pont_func < function.length; pont_func++) {
    		mdVectorFunctions[pont_func] = (MDVectorFunction) function[pont_func];
    	}
    	
    	try {
    		solutionp = new MPGridFunctionOnGridEvaluator(dimension, constraints, mdVectorFunctions, hyperCubeErrorTreatment);
    	} catch (DimensionOutOfBounds e) {
    		throw e;
    	} catch (CanNotPerformCalculations e) {
    		throw new CanNotInitializeGrid();
    	}
    	
    	return solutionp;
    }

}
