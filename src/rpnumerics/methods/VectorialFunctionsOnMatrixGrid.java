package rpnumerics.methods.contour;

import java.util.HashMap;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorTreatmentBehavior;
import rpnumerics.methods.contour.markedhypercubes.MarkedCubeItem;
import wave.util.exceptions.DimensionOutOfBounds;
import wave.util.exceptions.IndexOfArrayOutOfBounds;
import wave.util.IntegerInterval;
import wave.util.MultipleObjectMatrix;
import wave.util.MultipleLoop;
import wave.util.PointNDimension;
import wave.util.RealVector;

public class VectorialFunctionsOnMatrixGrid extends
		GridOnMatrix {

	private int numberOfFunctions;
	private MDVectorFunction vectorialFunction;
	private int dimension;
		
	public VectorialFunctionsOnMatrixGrid(int dimension,
										  Constraint[] constraint, 
										  MDVectorFunction[] functionsp,
										  HyperCubeErrorTreatmentBehavior hyperCubeErrorTreatment)
																			throws DimensionOutOfBounds,
																				   CanNotPerformCalculations {
		super(dimension, constraint, functionsp, hyperCubeErrorTreatment); // retirar isto daqui
		
		if(functionsp.length != 1) {
			throw new DimensionOutOfBounds();
		}
		
		numberOfFunctions = ((MDVectorFunction) functionsp[0]).getResultComponentNumber();		
		vectorialFunction = functionsp[0];
		this.dimension = dimension;
				
		numberOfFunctions = ((MDVectorFunction) functionsp[0]).getResultComponentNumber();
		
		vectorialFunction = functionsp[0];
		
		HashMap functionsMap = new HashMap();
				
		functionsMap.put(vectorialFunction, String.valueOf(0));
		
		this.setFunctionsMap(functionsMap);
				
		int[] indexes = new int[dimension + 1];
				
		for (int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
			indexes[pont_dimension - 1] = (this.getNumberOfDivisions(pont_dimension) + 1);			
		}
		
		indexes[dimension] = this.numberOfFunctions;
		
		this.setMatrix(setDomainMultipleMatrix(indexes));
		
		buildArray();
	}
		
    protected void buildArray() throws DimensionOutOfBounds {
		
		int dimension = this.myDimensionIs();
			
		IntegerInterval[] loopIntervals = new IntegerInterval[dimension];
		
		for (int dimension_pont = 1; dimension_pont <= dimension; dimension_pont++) {
			loopIntervals[dimension_pont - 1] = new IntegerInterval(0, this.getNumberOfDivisions(dimension_pont));
		}	
		
		MultipleLoop loop = setDomainMultipleLoop(loopIntervals);
		
		int loopSize = loop.getLoopSize();
		
		for(int loopHash = 0; loopHash < loopSize; loopHash++) {
							
			int[] coordinates =  loop.getIndex(loopHash);
			
			int[] position = new int[dimension + 1]; 
						
			for (int pont_coordinates = 0; pont_coordinates < dimension; pont_coordinates++) {
				   position[pont_coordinates] = coordinates[pont_coordinates];				
			}
			
			try {
				
				PointNDimension point = this.getPointCoordinates(coordinates);
				
				RealVector result = vectorialFunction.value(point);
									
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
}
