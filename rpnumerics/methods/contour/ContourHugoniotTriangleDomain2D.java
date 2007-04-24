package rpnumerics.methods.contour;

import rpnumerics.methods.contour.exceptions.CanNotInitializeGrid;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.HugoniotFunction;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.exceptions.DimensionOutOfBounds;
import wave.util.*;

public class ContourHugoniotTriangleDomain2D extends ContourHugoniot {

	public ContourHugoniotTriangleDomain2D(HugoniotFunction functionp,
			HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws IllegalArgumentException {
		super(functionp, hyperCubeErrorTreatment);
	}
	
	/*protected GridGenerator initializeSolutionConstraints(int dimension, 
			  Constraint[] constraints, 
			  rpnumerics.methods.contour.functionsobjects.CubeFunction[] function, 
			  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) 
														throws DimensionOutOfBounds,
														       CanNotInitializeGrid {

		MDVectorFunction[] VectorFunctionArray = new MDVectorFunction[function.length];
	
		for (int pont_func = 0; pont_func < function.length; pont_func++) {
			VectorFunctionArray[pont_func] = (MDVectorFunction) function[pont_func];
		}
		
		try{	
			return new FluxOnGridTriangleDomain2D(dimension, constraints, VectorFunctionArray, this.getInitialPoint(), hyperCubeErrorTreatment);	
		}  catch (CanNotPerformCalculations e) {
			throw new CanNotInitializeGrid();
		}  catch (Exception e) {
			e.printStackTrace();
			throw new CanNotInitializeGrid();
		}
	}*/	  
	
	protected MultipleLoop initializeLoop(int[] res) {    	
		return new TriangleLoop2DIntervalBeginInOne(res[0]);
    }
}
