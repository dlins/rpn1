package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.exceptions.*;

public class MatrixFunctionsOnGrid extends
		VectorialFunctionsOnMatrixGrid {

	private int lines;
	private int columns;
		
	public MatrixFunctionsOnGrid(int dimension,
			Constraint[] constraint, 
			MatrixFunction functionsp,
			HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
																throws DimensionOutOfBounds, 
																CanNotPerformCalculations {
		
		super(dimension, constraint, FromArrayToVector(functionsp), hyperCubeErrorTreatment);
		
		this.lines = functionsp.getNumberOfLines();
		this.columns = functionsp.getNumberOfColumns();
		
	}
	
	private static MDVectorFunction[] FromArrayToVector(MatrixFunction functions) {
		
		MDVectorFunction[] vectorFunctions = new MDVectorFunction[1];		
		vectorFunctions[0] = functions;		
		return vectorFunctions;
	}
	
	public EvaluationInter getSolutionAt(FunctionParameters parameters, int functionIndex) throws CanNotPerformCalculations {
		throw new CanNotPerformCalculations();
	}
	
	public double[][] getSolutionAt(FunctionParameters parameters) throws CanNotPerformCalculations {
		int[] coordinates = new int[parameters.myDimensionIs() + 1];
		int[] buffer = parameters.toIntArray();
		double[][] matrix = new double[lines][columns];
		
		for (int pont_coordinates = 0; pont_coordinates < parameters.myDimensionIs(); pont_coordinates++) {
			coordinates[pont_coordinates] = buffer[pont_coordinates];
		}
		
		for (int pont_line = 0; pont_line < lines; pont_line++) {
			for(int pont_column = 0; pont_column < columns; pont_column++) {
				coordinates[parameters.myDimensionIs()] = ((pont_column * lines) + pont_line);
				try {					
					matrix[pont_line][pont_column] = ((Double) this.getMatrix().getElement(coordinates)).doubleValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} 
		
		return matrix;
	}
}
