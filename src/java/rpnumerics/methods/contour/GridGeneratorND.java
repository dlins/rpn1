package rpnumerics.methods.contour;

import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.util.*;
import wave.util.exceptions.*;

public class GridGeneratorND extends GridGenerator {
	
	public GridGeneratorND(int dimension, 
						   Constraint[] constraint, 
						   HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
			throws DimensionOutOfBounds {
		super(dimension, constraint, hyperCubeErrorTreatment);
		
	}
	
	public HyperCubeND getHyperCube(FunctionParameters parameters) throws DimensionOutOfBounds {
		
		int dimension = this.myDimensionIs();
		
		HyperCubeND hypercube = null;
		
		if (parameters.myDimensionIs() == dimension) {	
		
			HyperCubeND basicHyperCube = getBasicHyperCube() ;
			
			PointNDimension initial_point = getInitialPoint();
			
			int numberOfHyperCubeVertices = basicHyperCube.getNumberOfVertices();
			
			// initialize variations
			
			double[]  variationOfDimension = new double[dimension];
			
			for(int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
				try {
					variationOfDimension[pont_dimension -1] = getVariation(pont_dimension);
				} catch(Exception e) {
					
				}
			}
			
			PointNDimension[] hypercubeVertices = new PointNDimension[numberOfHyperCubeVertices];
			
			for (int pont_vertice = 1; pont_vertice <= numberOfHyperCubeVertices; pont_vertice++) {
				
				PointNDimension basicCubeVertice = basicHyperCube.getVertice(pont_vertice);
				
				PointNDimension hypercubePoint = new PointNDimension(dimension);
				
				for(int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
					
					double index = parameters.getIndex(invertIndex(pont_dimension, dimension)) + basicCubeVertice.getCoordinate(invertIndex(pont_dimension, dimension));
					
					double value = initial_point.getCoordinate(pont_dimension) + ((index - 1) * variationOfDimension[pont_dimension - 1]);
					
					hypercubePoint.setCoordinate(value, pont_dimension);
				}
				
				hypercubeVertices[pont_vertice - 1] = hypercubePoint;
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
}
