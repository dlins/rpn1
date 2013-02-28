package rpnumerics.methods.contour;

import java.awt.Color;

import rpnumerics.*;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.exceptions.*;
import wave.multid.view.ViewingAttr;
import wave.util.exceptions.*;

public class ContourBifurcation extends ContourNDVectorialFunctions {

	private int family;
	
	public ContourBifurcation(MDVectorFunction functionp, int family, HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws IllegalArgumentException {
		super(functionp, functionp.getFunctionDimension(), hyperCubeErrorTreatment);
		
		this.family = family;
	}
			
    @Override
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
	
    @Override
	protected RPnCurve setRPnCurve(ContourCurve curve) {
    	
		BifurcationCurve rpncurve = null;
		
		if (curve.numberOfSegments() != 0 ) {
			rpncurve = new BifurcationCurve(family, curve, new ViewingAttr(Color.yellow));
		}		
    		
    	return rpncurve;
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
