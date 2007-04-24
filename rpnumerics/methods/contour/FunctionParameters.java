package rpnumerics.methods.contour;

import wave.exceptions.DimensionOutOfBounds;
import wave.util.*;

public class FunctionParameters {

	private int[] indexes;
	private int dimensionOfTheList;

	public FunctionParameters(int dimensionOfTheList) {

		indexes = new int[dimensionOfTheList];
		this.dimensionOfTheList = dimensionOfTheList;
	}
	
	public FunctionParameters(PointNDimension coordinates) {
		indexes = new int[coordinates.myDimensionIs()];
		
		try {
			for (int pont_index = 0; pont_index < 0; pont_index++) {
				indexes[pont_index] = (int) coordinates.getCoordinate(pont_index + 1);
			}
		} catch(DimensionOutOfBounds e) {
			
		}
	}
	
	public FunctionParameters(int[] indexes) {
		this.indexes = indexes;
		this.dimensionOfTheList = indexes.length;
	}

	public void setIndex(int index, int dimension) throws DimensionOutOfBounds {
		if ((dimension >= 1) || (dimension <= this.dimensionOfTheList)) {
			indexes[dimension - 1] = index;
		} else {
			throw new DimensionOutOfBounds();
		}
	}

	public int getIndex(int dimension) throws DimensionOutOfBounds {
		if ((dimension >= 1) || (dimension <= this.dimensionOfTheList)) {
			return indexes[dimension - 1];
		} else {
			throw new DimensionOutOfBounds();
		}
	}

	public int myDimensionIs() {
		return this.dimensionOfTheList;
	}
	
	public boolean equals(Object parameter) {
				
		int length = indexes.length;
		boolean equal = true; 
		
		if (parameter instanceof FunctionParameters) {
			for(int pont_index = 0; pont_index < length && equal; pont_index++) {
				
				int tmp = 0;
				
				try {
					tmp = ((FunctionParameters) parameter).getIndex(pont_index + 1);
				} catch (DimensionOutOfBounds e) {
					
				}
				if(indexes[pont_index] != tmp) {
					equal = false;
				}
			}
		} else {
			equal = false;
		}
		
		return equal;
	}
	
	public Object clone() {
		
		FunctionParameters clone = new FunctionParameters(dimensionOfTheList);
		
		for (int pont_dimension = 0; pont_dimension < dimensionOfTheList; pont_dimension++) {
			try {
				clone.setIndex(indexes[pont_dimension], (pont_dimension + 1));
			} catch (DimensionOutOfBounds e) {
				
			}
		}
		
		return clone;
	}
		
	public String toString() {
		
		String msg = "(";
		
		for (int pont_dimension = 0; pont_dimension < dimensionOfTheList; pont_dimension++) {
			msg+= " " + indexes[pont_dimension];
			if(pont_dimension != (dimensionOfTheList - 1)) {
				msg += ",";
			}
		}
		
		msg += ")";
		
		return msg;
	}
	
	public PointNDimension toPoint() {
		
		double[] indexesConverted = new double[indexes.length];
		
		for(int pont_indexes = 0; pont_indexes < indexes.length; pont_indexes++) {
			indexesConverted[pont_indexes] = (double) indexes[pont_indexes];
		}
		
		return new PointNDimension( indexesConverted);
	}
	
	public int[] toIntArray() {
		return indexes;
	}
}
