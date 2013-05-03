package wave.util;

import wave.util.exceptions.IndexOfArrayOutOfBounds;

public class MultipleObjectMatrix extends MultipleMatrix implements MultipleMatrixInter {

	private Object[] matrix;

	public MultipleObjectMatrix(int[] matrixlengths) {
		super(matrixlengths);

		matrix = new Object[this.getBigMatrixSize()];
	}

	public Object getElement(int[] index) throws IndexOfArrayOutOfBounds{
		try {
			return matrix[this.getPos(index)];
		} catch (IndexOfArrayOutOfBounds e) {
			throw e;
		}
	}

	public void setElement(int[] index, Object element) throws IndexOfArrayOutOfBounds{

		try {
			matrix[this.getPos(index)] = element;
		} catch (IndexOfArrayOutOfBounds e) {
			throw e;
		}
	}
}
