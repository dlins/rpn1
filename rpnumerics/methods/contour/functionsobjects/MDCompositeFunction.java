package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

import rpnumerics.methods.contour.*;

import java.util.*;

public abstract class MDCompositeFunction extends MDVectorFunction {

	private Vector plusFunctionsList; 
	private Vector minusFunctionsList; 
		
	public MDCompositeFunction(int dimension, int numberOfComponents, FluxFunction fluxFunction,
			HugoniotParams params, GridFunction[] functions) {
		super(dimension, numberOfComponents, fluxFunction, params);		
		
		initFunctions(functions);
	}
	
	protected void initFunctions(GridFunction[] functions) {
		
		resetFunctionsList(); 
		
		for(int pont_function = 0; pont_function < functions.length; pont_function++) {
			this.addMinusFunction(functions[pont_function]);
			this.addPlusFunction(functions[pont_function]);
		}
				
	}
		
	protected void resetFunctionsList() {
		plusFunctionsList = new Vector(); 
		minusFunctionsList = new Vector(); 		
	}
	
    public final RealVector value(PointNDimension point) throws CanNotPerformCalculations {
		return calcValue(point);			
	}
		
	public RealVector value(FunctionParameters coordinate) throws CanNotPerformCalculations {
		return calcValue(coordinate);		
	}
	
	public abstract RealVector calcValue(PointNDimension point) throws CanNotPerformCalculations;	
	public abstract RealVector calcValue(FunctionParameters coordinate) throws CanNotPerformCalculations;
		
	protected void addPlusFunction(GridFunction function) {
		plusFunctionsList.add(function.clone());
	}
	
	protected void addMinusFunction(GridFunction function) {
		minusFunctionsList.add(function.clone());
	}
	
	protected FunctionParameters getPlusIndex(FunctionParameters index) {
		
		int dimension = index.myDimensionIs();
		
		int half = dimension / 2;
		
		int[] newIndex = new int[half];
		
		try {
			for(int pont_index = 0; pont_index < half; pont_index++ ) {
				newIndex[pont_index] = index.getIndex(pont_index + 1 + half);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new FunctionParameters(newIndex);
	}
	
	protected FunctionParameters getMinusIndex(FunctionParameters index) {
		int dimension = index.myDimensionIs();
		
		int half = dimension / 2;
		
		int[] newIndex = new int[half];
		
		try {
			for(int pont_index = 0; pont_index < half; pont_index++ ) {
				newIndex[pont_index] = index.getIndex(pont_index + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new FunctionParameters(newIndex);
	}
	
	protected PointNDimension getMinusIndex(PointNDimension point) {
		
		int dimension = point.myDimensionIs();
		int reducedDimension = dimension / 2;
		
		double[] coords = new double[dimension];
		
		for (int pont_coords = 0; pont_coords < dimension; pont_coords++) {
			try {
				coords[pont_coords] = point.getCoordinate(pont_coords + 1);
			} catch (Exception e) {
				
			}
		}
		
		double[] minusCoords = new double[reducedDimension];
		
		for(int pont_coords = 0; pont_coords < reducedDimension; pont_coords++) {
			minusCoords[pont_coords] = coords[pont_coords];
		}
		
		return new PointNDimension(minusCoords);
	}
	
	protected PointNDimension getPlusIndex(PointNDimension point) {
		int dimension = point.myDimensionIs();
		int reducedDimension = dimension / 2;
		
		double[] coords = new double[dimension];
		
		for (int pont_coords = 0; pont_coords < dimension; pont_coords++) {
			try {
				coords[pont_coords] = point.getCoordinate(pont_coords + 1);
			} catch (Exception e) {
				
			}
		}
		
		double[] plusCoords = new double[reducedDimension];
		
		for(int pont_coords = 0; pont_coords < reducedDimension; pont_coords++) {
			plusCoords[pont_coords] = coords[pont_coords + reducedDimension];
		}
		
		return new PointNDimension(plusCoords);
	}
	
	public GridFunction[] getPlusFunctions() {
		
		Object[] objectList = plusFunctionsList.toArray();
		GridFunction[] bufferedFunctions = new GridFunction[objectList.length];
		
		for (int pont_object = 0; pont_object < objectList.length; pont_object++) {
			bufferedFunctions[pont_object] = (GridFunction) objectList[pont_object];
		}
		
		return bufferedFunctions;
	}
	
    public GridFunction[] getMinusFunctions() {
		
		Object[] objectList = minusFunctionsList.toArray();
		GridFunction[] bufferedFunctions = new GridFunction[objectList.length];
		
		for (int pont_object = 0; pont_object < objectList.length; pont_object++) {
			bufferedFunctions[pont_object] = (GridFunction) objectList[pont_object];
		}
		
		return bufferedFunctions;
	}
        
}
