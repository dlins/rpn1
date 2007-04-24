package rpnumerics.methods.contour.nraphson;

import java.util.Vector;

import rpnumerics.methods.contour.ContourNDDecorator;
import rpnumerics.methods.contour.ContourNDVectorialFunctions;
import rpnumerics.methods.contour.FunctionParameters;
import rpnumerics.methods.contour.GridGenerator;
import rpnumerics.methods.contour.exceptions.SolutionNotFound;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeMarker;
import wave.exceptions.DimensionOutOfBounds;

public class ContourNDNewtonRaphson extends ContourNDDecorator implements HyperCubeMarker {
// nao estah marcando
	private Exception hyperCubeMark = null;
	
	public ContourNDNewtonRaphson(ContourNDVectorialFunctions contour)
			throws IllegalArgumentException {
		super(contour);
		
		this.setCSolver(new CubeSolverNRaphson(this.getCFace(), (MDVectorFunction) function[0]));
	}
	
	protected double [][] solving(double[][] foncub_) {
    	double[][] vert = null;
    	
    	try { 
    		vert = basicHypercubeGeneration();
    	} catch (DimensionOutOfBounds e) {
    		e.printStackTrace();
    	}
    	
    	this.getCFace().setVertexCoordinatesArray(vert);	
    	
    	int[] exstfc = initializeExstfc();
		
    	CubeSolverNRaphson cubeSolver = (CubeSolverNRaphson) this.getCSolver();
    	
    	cubeSolver.cubsol(exstfc, foncub_);	
    	
    	if (cubeSolver.isThereProblem()) {
    		hyperCubeMark = new NRaphsonBlow();
    		cubeSolver.resetProblemFlag();
    	}
    	
		return this.getCSolver().getSolutionArray();
    }
	
	protected int [][] makeEdges() {
		this.getCSolver().mkedge();
		return  this.getCSolver().getEdgesArray(); 	
	}
		
	protected double[][] evaluateFunctions(GridGenerator solution, FunctionParameters parameters) throws SolutionNotFound,
																										 DimensionOutOfBounds,
																										 CanNotPerformCalculations {
		double[][] result = super.evaluateFunctions(solution, parameters);
		
		((CubeSolverNRaphson) this.getCSolver()).setParametersAndConstraints((FunctionParameters) parameters.clone(), solution);
				
		return result;
	}
	
	public Exception getMark() {
		return hyperCubeMark;
	}
	
	public void resetMark() {
		hyperCubeMark = null;
	}
	  
}
