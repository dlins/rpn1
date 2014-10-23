package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import wave.util.*;
import wave.util.exceptions.*;

public class GridForReducedDimension extends
		GridGeneratorUsingReducedMemory {

	private RealSegment segment;
		
	public GridForReducedDimension(int dimension,
								   Constraint[] constraint, 
								   CubeFunction[] functionsp, 
								   HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment, 
								   RealSegment segment)
									throws DimensionOutOfBounds {
		
		super(dimension, constraint, functionsp, hyperCubeErrorTreatment);
		
		this.segment = segment;
		
	}
	
	public PointNDimension getPointCoordinates(int[] coordinates) throws DimensionOutOfBounds{
				
		PointNDimension point = super.getPointCoordinates(coordinates);
		
		int originalPoint_Dimension = point.myDimensionIs();
		
		PointNDimension extendedPoint = new PointNDimension(2*(originalPoint_Dimension - 1));
		
		for(int pont_dimension = 1; pont_dimension < (originalPoint_Dimension); pont_dimension++) {
			extendedPoint.setCoordinate(point.getCoordinate(pont_dimension), pont_dimension);
		}
		
		int extendedDimension = segment.p1().getSize();
		
		if (point.getCoordinate(originalPoint_Dimension) == 0) {
			for(int pont_dimension = originalPoint_Dimension; pont_dimension < (originalPoint_Dimension + extendedDimension); pont_dimension++) {
				extendedPoint.setCoordinate(segment.p1().getElement(pont_dimension - originalPoint_Dimension), pont_dimension);
			}
		} else {
			if (point.getCoordinate(originalPoint_Dimension) == 1){
				for(int pont_dimension = originalPoint_Dimension; pont_dimension < (originalPoint_Dimension + extendedDimension); pont_dimension++) {
					extendedPoint.setCoordinate(segment.p2().getElement(pont_dimension - originalPoint_Dimension), pont_dimension);
				}
			} else {
				System.out.println("Deu tilt em SolutionConstraintsNDReducedDimension");
			}
		}
		
		return extendedPoint;
	}

}
