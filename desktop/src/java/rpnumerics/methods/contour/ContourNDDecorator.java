package rpnumerics.methods.contour;


import java.util.*;

import rpnumerics.methods.contour.exceptions.CanNotInitializeGrid;
import rpnumerics.methods.contour.exceptions.SolutionNotFound;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorFound;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;

import wave.util.*;
import wave.util.exceptions.*;

public class ContourNDDecorator extends ContourND {

	// tem que modificar multi polygon para ser compativel...O nome do metodo eh mkedges tambem...
	
	private ContourND contour;
	
	public ContourNDDecorator(ContourND contour)
			throws IllegalArgumentException {
		super(contour.function, contour.dimension, contour.getHyperCubeErrorTreatment());
		
		this.contour = contour;
		
		// test if the class overloads the curveND method.
		// The class decorated can not overload such method.
		
		/*Class  c = contour.getClass();
		
		Method[] theMethods = c.getMethods();
		for (int i = 0; i < theMethods.length; i++) {
			String[] signature = (theMethods[i].toString()).split(" ");
			
		}*/		
		
		this.setCFace(contour.getCFace());
		this.setCSolver(contour.getCSolver());
	}
		
	protected ContourPolyline[] copyEdges(GridGenerator solution, int nedges, FunctionParameters parameters, double [][] sol_, int [][] edges_) throws DimensionOutOfBounds, 
																																							 ThereIsNoFeasibleSolution, HyperCubeErrorFound {
	    return super.copyEdges(solution, nedges, parameters, sol_, edges_);
	}
	
	protected final void initMainObjects() {		
    }
		
	protected ContourND getContour() {
		return this.contour;
	}
	
	protected Constraint[] initializeConstraints(int res[], double rect[]) throws PointsAreNotSameDimension{
    	return contour.initializeConstraints(res, rect);
    }
	
	protected GridGenerator initializeSolutionConstraints(int dimension, 
														  Constraint[] constraints, 
														  CubeFunction[] function,
														  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) 
																							throws DimensionOutOfBounds,
																							       CanNotInitializeGrid {
		return contour.initializeSolutionConstraints(dimension, constraints, function, hyperCubeErrorTreatment);
	}
	
	protected double[][] basicHypercubeGeneration() throws DimensionOutOfBounds {
		return contour.basicHypercubeGeneration();
	}
	
	protected MultipleLoop initializeLoop(int[] res) {
		return contour.initializeLoop(res);
	}
	
	protected double[][] evaluateFunctions(GridGenerator solution, FunctionParameters parameters) throws SolutionNotFound,
																										 DimensionOutOfBounds,
																										 CanNotPerformCalculations {
		return contour.evaluateFunctions(solution, parameters);
	}
		  
	protected double [][] solving(double[][] foncub_) {
		return contour.solving(foncub_);
	}
		
	protected int [][] makeEdges() {
		return contour.makeEdges();
	}
		
}
