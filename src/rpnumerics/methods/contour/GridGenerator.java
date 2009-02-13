package rpnumerics.methods.contour;


import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.markedhypercubes.*;
import wave.util.*;
import wave.util.exceptions.*;

public class GridGenerator {
	// this is a class specific to Contour problem.
	// It have the contraint array for the problem given, it can work with n-dimension. 
	// It must be given.
	
	private int dimension;
	private DoubleInterval[] bounds;
	private int[] numberOfDivisions;
	private double[] variationOfAxes;
	
	private int numberOfVertices;
	private HyperCubeND basicHyperCube;	
	
	private HyperCubeErrorTreatmentBehavior errorTreatment;
		
	public GridGenerator(int dimension, 
						 Constraint[] constraint, 
						 HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment) throws DimensionOutOfBounds {
		
		if (constraint.length == dimension) {
			this.dimension = dimension;
			
			bounds = new DoubleInterval[dimension];
			numberOfDivisions = new int[dimension];
			variationOfAxes = new double[dimension];
						
			for (int constraint_pont = 0; constraint_pont < dimension; constraint_pont++) {
				this.bounds[constraint_pont] = constraint[constraint_pont].getInterval();
				int divisions = constraint[constraint_pont].getNumberOfDivisions();
				this.numberOfDivisions[constraint_pont] = divisions;
				
				double first  = this.bounds[constraint_pont].getFirstPoint(); 
				double second = this.bounds[constraint_pont].getSecondPoint();
				this.variationOfAxes[constraint_pont] = (second - first) / divisions;
			}			
			
			numberOfVertices = (int) Math.pow(2, dimension);
			
			basicHyperCube = HyperCubeND.generateBasicHyperCube(dimension);	
			
			this.errorTreatment = hyperCubeErrorTreatment;
					
		} else
			throw new DimensionOutOfBounds();
	}
	
	protected void markHyperCube(MarkedCubeItem markedCube) {
		this.errorTreatment.markHyperCube(markedCube);
	}
	
	public void setInterval(DoubleInterval intervalp, int numberOfDivisionsp, int dimension) throws DimensionOutOfBounds{
		if ((dimension >= 1) && (dimension <= this.dimension)) {
			bounds[dimension - 1] = intervalp;
			numberOfDivisions[dimension-1] = numberOfDivisionsp;
		} else {
			throw new DimensionOutOfBounds();
		}
	}
	
	public DoubleInterval getInterval(int dimension) throws DimensionOutOfBounds{
		if ((dimension >= 1) && (dimension <= this.dimension)) {
			return bounds[dimension - 1];
		} else {
			throw new DimensionOutOfBounds();
		}
	}
	
	public int getNumberOfDivisions(int dimension) throws DimensionOutOfBounds{
		if ((dimension >= 1) && (dimension <= this.dimension)) {			
			return numberOfDivisions[dimension-1];
		} else {
			throw new DimensionOutOfBounds();
		}
	}
	
	public double getVariation(int dimension) throws DimensionOutOfBounds{
		if ((dimension >= 1) && (dimension <= this.dimension)) {			
			return variationOfAxes[dimension-1];
		} else {
			throw new DimensionOutOfBounds();
		}
	}
	
	public PointNDimension getInitialPoint() {
		
		PointNDimension initial_point = new PointNDimension(dimension);
		
		try {
			for (int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
				
				double coordinateOfFirstPoint = bounds[(pont_dimension-1)].getFirstPoint();
				initial_point.setCoordinate(coordinateOfFirstPoint, pont_dimension);				
			}
		} catch (DimensionOutOfBounds e) {
			
		}
		
		return initial_point;
	}
	
	public double getFirstPointOfInterval(int dimension) throws DimensionOutOfBounds{
		if ((dimension >= 1) || (dimension <= this.dimension)) {			
			return bounds[dimension-1].getFirstPoint();
		} else {
			throw new DimensionOutOfBounds();
		}
	}
	
	public double getSecondPointOfInterval(int dimension) throws DimensionOutOfBounds{
		if ((dimension >= 1) || (dimension <= this.dimension)) {			
			return bounds[dimension-1].getSecondPoint();
		} else {
			throw new DimensionOutOfBounds();
		}
	}
	
	public int myDimensionIs() {
		return this.dimension;
	}
	
	public int getNumberOfVertices() {
		return this.numberOfVertices;
	}
		
	public HyperCubeND getHyperCube(FunctionParameters parameters) throws DimensionOutOfBounds {
		// When we slice the Solution Cosntraints hypercube, this function gives the respective
		// hypercube to the given indexes above. It is assumed that the first point of the hypercube is
		// the point of each interval correspondent to each dimension.
		
		HyperCubeND hypercube = null;
		PointNDimension[] hypercubeVertices = new PointNDimension[numberOfVertices];
		
		if (parameters.myDimensionIs() == dimension) {		
	
			PointNDimension initial_point = getInitialPoint();
						
			for(int i = 1; 1 <= numberOfVertices; i++) {
				PointNDimension temp = basicHyperCube.getVertice(i);
				PointNDimension hypercubePoint = new PointNDimension(dimension);
				
				for (int dimension_i = 1; dimension_i<= this.dimension;) {
					
					double variationOfDimension = 0;
					int indexOfHyperCube = parameters.getIndex(dimension_i);
				
					try {
						variationOfDimension = getVariation(dimension_i);
					} catch(Exception e) {
						
					}
										
					double value = initial_point.getCoordinate(dimension_i) + 
					            (variationOfDimension * indexOfHyperCube * temp.getCoordinate(dimension_i));
					
					hypercubePoint.setCoordinate(value, dimension_i);
				}				
				
				hypercubeVertices[i-1] = hypercubePoint;
			
			}
			
			try {			
				hypercube = new HyperCubeND(dimension, hypercubeVertices);	
			} catch (DimensionOutOfBounds e) {
				throw e;
			}
			
		} else {
			throw new DimensionOutOfBounds();
		}
		
		return hypercube;
	}
	
	protected HyperCubeND getBasicHyperCube() {
		return this.basicHyperCube;
	}
	
	public double[] solveHyperCube(FunctionParameters parameters, 
								   CubeFunction evaluationFunction)  throws DimensionOutOfBounds,
								   											CanNotPerformCalculations {

	/* test all vertices from the given cube, the position of the cube is given by the first
	* four parameters. The last parameter is the function that wants to be tested. */
	
		if (parameters.myDimensionIs() == this.myDimensionIs()) {
			HyperCubeND cube = null;
			
			double[] solution = null;
						
			try {
				cube = this.getHyperCube(parameters);
			} catch (DimensionOutOfBounds e) {
				throw e;
			}
			
			boolean foundZero = false;
			
			int dimensionOfHyperCube = cube.getDimension();
			
			if (dimensionOfHyperCube == evaluationFunction.getFunctionDimension() ) {
			
				int numberOfSolutionVertices = getNumberOfVertices();
				
				solution = new double[numberOfSolutionVertices];
				
				for (int vertices_pont = 1; vertices_pont <= numberOfSolutionVertices; vertices_pont++) {
					
					PointNDimension vertice = null;
					
					try {
						vertice = cube.getVertice(vertices_pont);
						
					} catch (DimensionOutOfBounds e) {				
						throw e;
					}	
					
					try {
						solution[vertices_pont - 1] = evaluationFunction.function(vertice);
					} catch (CanNotPerformCalculations e) {						
						this.markHyperCube(new MarkedCubeItem(this, (FunctionParameters) parameters.clone(), e));
						throw e;
					}
				}
				
				for(int i = 1; (i <= numberOfSolutionVertices); i++) {
					
					double refval = solution[i - 1];
							
					for (int j = 1; (j < i); j++) {
						
						double val = solution[j - 1];
						
						if ((refval * val) <= 0.0) {
							foundZero = true;
							
						}
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
	}
	
	public PointNDimension getPointCoordinates(int[] indexes) throws DimensionOutOfBounds{
	
		PointNDimension initial_point = this.getInitialPoint();
		PointNDimension point = new PointNDimension(dimension);
		
		if (indexes.length != initial_point.myDimensionIs()) {
			throw new DimensionOutOfBounds();
		}
		
		for (int pont_coordinate = 1; pont_coordinate <= dimension; pont_coordinate++) {
			try {
				double value = initial_point.getCoordinate(pont_coordinate) + 
				               (indexes[pont_coordinate - 1] * this.getVariation(pont_coordinate)); 
				point.setCoordinate(value, pont_coordinate);
			} catch(DimensionOutOfBounds e) {
				throw e;
			}
		}
		
		return point;
	}
	
	protected int invertIndex(int index, int dimension) {
    	/*
    	 * this function move the higher indexes of the array to low indexes. Inverts the array
    	 * to make it compatible.
    	 * 
    	 * Example: 1 2 3 4 makes them 4 3 2 1
    	 */
    	
    	int half = dimension / 2;
    	int rest = dimension % 2;
    	
    	int pont_half = half + 1;
    	
    	int result = 0;
    	
    	if (rest != 0) {
    		result = pont_half + (pont_half - index);
    	} else {
    		if (index <= half) {
    			result = pont_half + (half - index);
    		} else {
    			result = half - (index - pont_half);
    		}    		
    	}
    	
    	return result;
    }
		
	public double[] getHyperCubeInterval(FunctionParameters parameters) {
		
		double []paux= new double[2*dimension];
    	
		try {
			
			double[] initialPoint = this.getHyperCubeInitialPoint(parameters);
			
			for (int dimension_point_2 = 1; dimension_point_2 <= dimension; dimension_point_2++) {
				paux[(2*dimension_point_2) - 2] = initialPoint[dimension_point_2 - 1];
			}
			
			for (int dimension_point_2 = 1; dimension_point_2 <= dimension; dimension_point_2++) {
				paux[(2*dimension_point_2) - 1] = paux[(2*dimension_point_2) - 2] + this.getVariation(dimension_point_2);				
			}
			
		} catch (DimensionOutOfBounds e) {
			e.printStackTrace();
		}
		
		return paux;
	}
	
	public double[] getHyperCubeInitialPoint(FunctionParameters parameters) {
		double []paux= new double[dimension];
		
		PointNDimension initial_point = this.getInitialPoint();
		
		try {
			for (int dimension_point_2 = 1; dimension_point_2 <= dimension; dimension_point_2++) {
		    	paux[dimension_point_2 - 1] = initial_point.getCoordinate(dimension_point_2) + 
		    	                         this.getVariation(dimension_point_2) * ((parameters.getIndex(invertIndex(dimension_point_2, dimension)) -1) );
		    }
		} catch (DimensionOutOfBounds e) {
			e.printStackTrace();
		}
		
		return paux;
	}
	
	public double[] getCoordinateFromRelativePositionInHyperCube(FunctionParameters parameters, double[] relativePosition) {
		double paux[] = new double[dimension];
		
		PointNDimension initial_point = this.getInitialPoint();
		
		try {
			for (int dimension_point_2 = 1; dimension_point_2 <= dimension; dimension_point_2++) {
		    	paux[dimension_point_2 - 1] = initial_point.getCoordinate(dimension_point_2) + 
		    	                         this.getVariation(dimension_point_2) * ((parameters.getIndex(invertIndex(dimension_point_2, dimension)) -1) + relativePosition[dimension_point_2 - 1]);
		    }
		} catch (DimensionOutOfBounds e) {
			e.printStackTrace();
		}
		
		return paux;
	}
	
	public FunctionParameters getHyperCubeIndexFromGridIndex(int[] gridIndex) {
		
		int[] indexes = new int[dimension];
		
		for(int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
			indexes[pont_dimension] = (gridIndex[pont_dimension] / 2);
		}
		
		return new FunctionParameters(indexes);
	}
	
	public EvaluationInter getSolutionAt(FunctionParameters parameters, int functionIndex) throws CanNotPerformCalculations {
		// to implement
		return null;
	}
		
}
