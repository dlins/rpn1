package rpnumerics.methods.contour;
import java.util.HashMap;

import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.MarkedCubeItem;


import wave.util.*;
import wave.exceptions.*;

public class GridGeneratorUsingReducedMemory extends GridOnMatrix {

	private int numberOfFunctions;
	private HashMap functions = new HashMap();
	
	private int dimensionN = 0;
		
	public GridGeneratorUsingReducedMemory(int dimension, 
										   Constraint[] constraint, 
										   CubeFunction[] functionsp,
										   HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws DimensionOutOfBounds {
		
		super(dimension, constraint, hyperCubeErrorTreatment);
			
		HashMap functionsMap = new HashMap();
		
		
		// Save functions and associate with indexes.
		
		this.numberOfFunctions = functionsp.length;
		for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
			functions.put(String.valueOf(pont_function), functionsp[pont_function]);
			functionsMap.put(functionsp[pont_function], String.valueOf(pont_function));			
		}
		
		this.setFunctionsMap(functionsMap);
		
		// prepare matrix Matrix.
		// index 0 -> 2 positions, for balance
		// index 1 -> my positions
		// ... and so
		// index dimension -> number of functions.
		
		int[] indexes = new int[dimension + 1];
		
		indexes[0] = 2;
		
		int pont_dimension= 2;
		for (; pont_dimension <= dimension; pont_dimension++) {
			indexes[pont_dimension - 1] = (this.getNumberOfDivisions(pont_dimension) + 1);			
		}
		
		indexes[pont_dimension - 1] = this.numberOfFunctions;
		
		this.setMatrix(setDomainMultipleMatrix(indexes));
	}
	
	public HyperCubeND getHyperCube(FunctionParameters parameters) throws DimensionOutOfBounds {
		
		HyperCubeND hypercube = super.getHyperCube(parameters);
		
		int numberOfVertices = hypercube.getNumberOfVertices();
		
		for (int pont_vertices = 1; pont_vertices <= numberOfVertices; pont_vertices++) {
			
			PointNDimension vertice = hypercube.getVertice(pont_vertices);
			
			double coordinateValue = vertice.getCoordinate(1);
			vertice.setCoordinate((coordinateValue % 2), 1);
		}
		
		return hypercube;
	}
		
	
	public double[] solveHyperCube(FunctionParameters parameters, 
				                   CubeFunction evaluationFunction)  throws DimensionOutOfBounds, 
				                   											CanNotPerformCalculations {
		
		int dimension = this.myDimensionIs();
		int parameterN = parameters.getIndex(dimension);
		
		if (parameterN != dimensionN) {			
			dimensionN = parameterN;
			
			buildArray(dimensionN);
		}
		
		return super.solveHyperCube(parameters, evaluationFunction);
	}
	
	protected void buildArray(int parameter) throws DimensionOutOfBounds {
	
		int dimension = this.myDimensionIs();
			
		IntegerInterval[] loopIntervals = new IntegerInterval[dimension + 1];

		loopIntervals[0] = new IntegerInterval(0, 0);
		
		for (int dimension_pont = 2; dimension_pont <= dimension; dimension_pont++) {
			loopIntervals[dimension_pont - 1] = new IntegerInterval(0, this.getNumberOfDivisions(dimension_pont));
		}	
		
		loopIntervals[dimension] = new IntegerInterval(0, (numberOfFunctions - 1));
		
		MultipleLoop loop = setDomainMultipleLoop(loopIntervals);
		
		int loopSize = loop.getLoopSize();
		
		if (parameter == 1) {

			for(int loopHash = 0; loopHash < loopSize; loopHash++) {
				
				int[] position = loop.getIndex(loopHash);
				
				int[] coordinates = new int[dimension];
				
				coordinates[0] = 0;
				position[0] = 0;
				
				int pont_coordinates = 1;
				
				for (; pont_coordinates < dimension; pont_coordinates++) {
					coordinates[pont_coordinates] = position[pont_coordinates];				
				}
				
				try {
					
					PointNDimension point = this.getPointCoordinates(coordinates);
					
					getMatrix().setElement(position, 
						              new Double(((CubeFunction) functions.get(String.valueOf(position[dimension]))).function(point)));
				} catch (CanNotPerformCalculations e) {
					MarkedCubeItem markedCube = new MarkedCubeItem(this, new FunctionParameters(coordinates), e); 
					try {
						this.getMatrix().setElement(position, markedCube);
					} catch (IndexOfArrayOutOfBounds e1) {
						throw new DimensionOutOfBounds();
					}
					markHyperCube(markedCube);
				} catch (IndexOfArrayOutOfBounds e) {
					throw new DimensionOutOfBounds();
				}
			}

		}
		
		for(int loopHash = 0; loopHash < loopSize; loopHash++) {
						
			int[] position = loop.getIndex(loopHash);
			
			int[] coordinates = new int[dimension];
			
			coordinates[0] = parameter;
			position[0] = parameter % 2;
			
			int pont_coordinates = 1;
			
			for (; pont_coordinates < dimension; pont_coordinates++) {
				  coordinates[pont_coordinates] = position[pont_coordinates];				
			}
			
			try {
				
				PointNDimension point = this.getPointCoordinates(coordinates);
				
				getMatrix().setElement(position, 
			              new Double(((CubeFunction) functions.get(String.valueOf(position[dimension]))).function(point)));
			} catch (CanNotPerformCalculations e) {
				MarkedCubeItem markedCube = new MarkedCubeItem(this, new FunctionParameters(coordinates), e); 
				try {
					this.getMatrix().setElement(position, markedCube);
				} catch (IndexOfArrayOutOfBounds e1) {
					throw new DimensionOutOfBounds();
				}
				markHyperCube(markedCube);
			} catch (IndexOfArrayOutOfBounds e) {
				throw new DimensionOutOfBounds();
			}
		}

	}
	
	protected HashMap getFunctionsMap() {
		return functions;
	}
	
}
