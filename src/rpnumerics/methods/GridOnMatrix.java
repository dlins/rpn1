 package rpnumerics.methods.contour;

import wave.util.*;
import wave.util.exceptions.*;

import java.util.*;

import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.MarkedCubeItem;


public class GridOnMatrix extends GridGenerator {

	private int numberOfFunctions;
	private HashMap functions = new HashMap();
	
	private MultipleObjectMatrix matrix;
		
	public GridOnMatrix(int dimension, 
						Constraint[] constraint, 
						HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws DimensionOutOfBounds {
		super(dimension, constraint, hyperCubeErrorTreatment);
	}

	public GridOnMatrix(int dimension, 
						Constraint[] constraint, 
						CubeFunction[] functionsp,
						HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws DimensionOutOfBounds,
																							 CanNotPerformCalculations {
		
		super(dimension, constraint, hyperCubeErrorTreatment);
		
		this.numberOfFunctions = functionsp.length;
		
		for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
			if (dimension == functionsp[pont_function].getFunctionDimension()) {
				functions.put(functionsp[pont_function], String.valueOf(pont_function));
			}
			else {
				throw new DimensionOutOfBounds();
			}
		}
		
		try {
			createMatrix(functionsp);
		}  catch (DimensionOutOfBounds e) {
			throw e;
		}
	}
	
	public HyperCubeND getHyperCube(FunctionParameters parameters) throws DimensionOutOfBounds {
		
		int dimension = this.myDimensionIs();
		
		HyperCubeND hypercube = null;
		
		if (parameters.myDimensionIs() == dimension) {
			HyperCubeND basicHyperCube = getBasicHyperCube() ;
			int numberOfVertices = basicHyperCube.getNumberOfVertices();
			
			PointNDimension[] hypercubeVertices = new PointNDimension[numberOfVertices];
			
			for (int pont_vertice = 1; pont_vertice <= numberOfVertices; pont_vertice++) { 
				
				PointNDimension basicCubeVertice = basicHyperCube.getVertice(pont_vertice);				
				PointNDimension hypercubePoint = new PointNDimension(dimension);
				
				for(int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
					double index = parameters.getIndex(invertIndex(pont_dimension, dimension)) + basicCubeVertice.getCoordinate(invertIndex(pont_dimension, dimension));					
					index -= 1;
					hypercubePoint.setCoordinate(index, pont_dimension);
				}
				
				hypercubeVertices[pont_vertice - 1] = hypercubePoint;
			}
			
			try {			
				hypercube = new HyperCubeND(dimension, hypercubeVertices);	
			} catch (DimensionOutOfBounds e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		return hypercube;
	}
	
	public double[] solveHyperCube(FunctionParameters parameters, 
				  				   CubeFunction evaluationFunction)  throws DimensionOutOfBounds, 
				  				   											CanNotPerformCalculations {
		
		int dimension = this.myDimensionIs();
		
		if (dimension == evaluationFunction.getFunctionDimension() ) {
			if (parameters.myDimensionIs() == dimension) {
								
				HyperCubeND cube = null;
				try {
					cube = this.getHyperCube(parameters);
				} catch (DimensionOutOfBounds e) {
					throw e;
				}
				
				int numberOfVertices = cube.getNumberOfVertices();
				double[] solution = new double[numberOfVertices];
				
				for (int pont_vertices = 1; pont_vertices <= numberOfVertices; pont_vertices++) {
					PointNDimension vertice = cube.getVertice(pont_vertices);
					
					int[] index = new int[dimension + 1];
					for (int pont_coordinates = 1; pont_coordinates <= dimension; pont_coordinates++) {
						index[pont_coordinates - 1] = (int) vertice.getCoordinate(pont_coordinates);						
					}
					
					index[dimension] = getFunctionIndex(evaluationFunction);
										
					try {
						Object object = matrix.getElement(index);
						
						if(object instanceof Double) {
							solution[pont_vertices - 1] = ((Double) object).doubleValue();
						} else if (object instanceof MarkedCubeItem) {
							throw new CanNotPerformCalculations();
						} else {
							throw new CanNotPerformCalculations();
						}
						
					} catch (IndexOfArrayOutOfBounds e) {	
						throw new DimensionOutOfBounds();
					}
				}
				
				boolean foundZero = false;
				
				for(int i = 1; (i <= numberOfVertices); i++) {
					
					double refval = solution[i - 1];
							
					for (int j = 1; (j < i); j++) {
						
						double val = solution[j - 1];
						
						if ((refval * val) <= 0.0) {
							foundZero = true;
							
						}
					}
					
				}
				
				if (foundZero) {
					return solution;
				} else {
					return null;
				}
				
			} else {				
				throw new DimensionOutOfBounds();
			}
		} else {			
			throw new DimensionOutOfBounds();
		}
			
	}
	
	protected void setMatrix(MultipleObjectMatrix matrix) {
		this.matrix = matrix;
	}
	
	protected MultipleObjectMatrix getMatrix() {
		return this.matrix;
	}
	
	protected HashMap getFunctionsMap() {
		return functions;
	}
	
	protected void setFunctionsMap(HashMap functions) {
		this.functions = functions;
	}
	
	protected void createMatrix(CubeFunction[] functionsp) throws DimensionOutOfBounds {
		
		int dimension = this.myDimensionIs();
		
		int[] indexes = new int[dimension + 1];
		
		int pont_dimension= 1;
		for (; pont_dimension <= dimension; pont_dimension++) {
			indexes[pont_dimension - 1] = (this.getNumberOfDivisions(pont_dimension) + 1);			
		}
		
		indexes[pont_dimension - 1] = this.numberOfFunctions;
		
		matrix = setDomainMultipleMatrix(indexes);
		
		IntegerInterval[] loopIntervals = new IntegerInterval[dimension + 1];

		for (int dimension_pont = 1; dimension_pont <= dimension; dimension_pont++) {
			loopIntervals[dimension_pont - 1] = new IntegerInterval(0, this.getNumberOfDivisions(dimension_pont));
		}	
				
		loopIntervals[dimension] = new IntegerInterval(0, (numberOfFunctions - 1));
		
		MultipleLoop loop = setDomainMultipleLoop(loopIntervals);
		
		int loopSize = loop.getLoopSize();
		
		for(int loopHash = 0; loopHash < loopSize; loopHash++) {
						
			int[] position = loop.getIndex(loopHash);
			
			int[] coordinates = new int[dimension];
			for (int pont_coordinates = 0; pont_coordinates < dimension; pont_coordinates++) {
				coordinates[pont_coordinates] = position[pont_coordinates];				
			}
			
			try {
				
				PointNDimension point = this.getPointCoordinates(coordinates);
				
				matrix.setElement(position, 
					              new Double(functionsp[position[dimension]].function(point)));
				
			} catch (CanNotPerformCalculations e) {				
				MarkedCubeItem markedCube = new MarkedCubeItem(this, getHyperCubeIndexFromGridIndex(coordinates), e); 
				try {
					matrix.setElement(position, markedCube);
				} catch (IndexOfArrayOutOfBounds e1) {
					throw new DimensionOutOfBounds();
				}
				markHyperCube(markedCube);
			} catch (IndexOfArrayOutOfBounds e) {
				throw new DimensionOutOfBounds();
			} 
		}

	}
	
	public EvaluationInter getSolutionAt(FunctionParameters parameters, int functionIndex) throws CanNotPerformCalculations {
		int[] coordinates = new int[parameters.myDimensionIs() + 1];
		int[] buffer = parameters.toIntArray();
		
		for (int pont_coordinates = 0; pont_coordinates < parameters.myDimensionIs(); pont_coordinates++) {
			coordinates[pont_coordinates] = buffer[pont_coordinates];
		}
		
		coordinates[parameters.myDimensionIs()] = functionIndex;
		
		try {
			return new EvaluationFromGridDouble(((Double) matrix.getElement(coordinates)).doubleValue());
		} catch (IndexOfArrayOutOfBounds e) {
			e.printStackTrace();
			throw new CanNotPerformCalculations();
		} 
	}
		
	protected int getFunctionIndex(CubeFunction function) {
		return Integer.parseInt((String) functions.get(function));
	}
	
	protected MultipleLoop setDomainMultipleLoop(IntegerInterval[] loopIntervals) {
		return new MultipleLoop(loopIntervals);
	}
	
	protected MultipleObjectMatrix setDomainMultipleMatrix(int[] indexes) {
		return new MultipleObjectMatrix(indexes);
	}
}
