package rpnumerics.methods.contour;

import rpnumerics.RpCurve;
import rpnumerics.methods.contour.exceptions.CanNotInitializeGrid;
import rpnumerics.methods.contour.exceptions.SolutionNotFound;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.nraphson.*;
import wave.util.exceptions.DimensionOutOfBounds;
import wave.util.*;

public class ContourHugoniot extends ContourNDVectorialFunctions {

	private PointNDimension initialPoint;
	private boolean pointSet = false;
	
	public ContourHugoniot(HugoniotFunction functionp, HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
													throws IllegalArgumentException {
		super(functionp, functionp.getFunctionDimension(), hyperCubeErrorTreatment);
	}

	public void setInitialPoint(PointNDimension point) {
		this.initialPoint = point;
		pointSet = true;
	}
	
	public RpCurve curvND(double[] rect,int[] res) throws CanNotPerformCalculations {
		if (pointSet) {
			RpCurve  curve = super.curvND(rect, res);
			pointSet = false;
			return curve;
		} else {
			throw new CanNotPerformCalculations();
		}			
	}
	
	protected GridGenerator initializeSolutionConstraints(int dimension, 
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
			return new FluxOnGrid(dimension, constraints, VectorFunctionArray, initialPoint, hyperCubeErrorTreatment);	
		} catch (CanNotPerformCalculations e) {
			throw new CanNotInitializeGrid();
		} 
	}	  
	
	/*(protected void initMainObjects() {   
    	this.setCFace(new CubeFace(dimension, numberOfEquations));
		this.setCSolver(new CubeSolverNRaphson(this.getCFace(), (MDVectorFunction) this.function[0]));
    }
		
	protected double[][] evaluateFunctions(GridGenerator solution, FunctionParameters parameters) throws SolutionNotFound, 
																										 DimensionOutOfBounds, 
																										 CanNotPerformCalculations {
		((CubeSolverNRaphson) this.getCSolver()).setParametersAndConstraints((FunctionParameters) parameters.clone(), solution);
		return super.evaluateFunctions(solution, parameters);
	}*/
	 
	public void resetInitialPoint() {
		this.initialPoint = null;
	}
	
	public PointNDimension getInitialPoint() {
		return this.initialPoint;
	}
}
