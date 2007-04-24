package rpnumerics.methods.contour;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.MarkedCubeItem;
import wave.exceptions.DimensionOutOfBounds;
import wave.exceptions.IndexOfArrayOutOfBounds;
import wave.util.IntegerInterval;
import wave.util.MultipleLoop;
import wave.util.PointNDimension;
import wave.util.RealSegment;
import wave.util.RealVector;

public class VectorialFunctionsOnGridForReducedDimension extends
		GridForReducedDimension {

	private int numberOfFunctions;
		
	public VectorialFunctionsOnGridForReducedDimension(int dimension,
			Constraint[] constraint, 
			CubeFunction[] functionsp,
			HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment,
			RealSegment segment) throws DimensionOutOfBounds {
		super(dimension, constraint, functionsp, hyperCubeErrorTreatment, segment);		
		
		numberOfFunctions = ((MDVectorFunction) functionsp[0]).getResultComponentNumber();
		
	}

	protected void buildArray(int parameter) throws DimensionOutOfBounds {
		
		int dimension = this.myDimensionIs();
			
		IntegerInterval[] loopIntervals = new IntegerInterval[dimension];

		loopIntervals[0] = new IntegerInterval(0, 0);
		
		for (int dimension_pont = 2; dimension_pont <= dimension; dimension_pont++) {
			loopIntervals[dimension_pont - 1] = new IntegerInterval(0, this.getNumberOfDivisions(dimension_pont));
		}	
		
		MultipleLoop loop = setDomainMultipleLoop(loopIntervals);
		
		int loopSize = loop.getLoopSize();
		
		if (parameter == 1) {

			for(int loopHash = 0; loopHash < loopSize; loopHash++) {
								
				int[] coordinates =  loop.getIndex(loopHash);
				
				int[] position = new int[dimension + 1]; 
				
				coordinates[0] = 0;
				position[0] = 0;
				
				int pont_coordinates = 1;
				
				for (; pont_coordinates < dimension; pont_coordinates++) {
					   position[pont_coordinates] = coordinates[pont_coordinates];				
				}
				
				try {
					
					PointNDimension point = this.getPointCoordinates(coordinates);
					
					RealVector result = ((MDVectorFunction) this.getFunctionsMap().get(String.valueOf(0))).value(point);
										
					for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
						position[dimension] = pont_function;
						getMatrix().setElement(position, new Double(result.getElement(pont_function)));
					}
					
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
						
			int[] coordinates = loop.getIndex(loopHash); 
			
			int[] position = new int[dimension + 1]; 
			
			coordinates[0] = parameter;
			position[0] = parameter % 2;
			
			int pont_coordinates = 1;
			
			for (; pont_coordinates < dimension; pont_coordinates++) {
				   position[pont_coordinates] = coordinates[pont_coordinates];				
			}
						
			try {
				
				PointNDimension point = this.getPointCoordinates(coordinates);
				
				RealVector result = ((MDVectorFunction) this.getFunctionsMap().get(String.valueOf(0))).value(point);
								
				for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
					position[dimension] = pont_function;
					getMatrix().setElement(position, new Double(result.getElement(pont_function)));
				}
				
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
	
	protected int getFunctionIndex(CubeFunction function) {
		return ((MDVectorFunctionDecorator) function).getIndex();
	}

}
