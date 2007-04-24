package wave.util;

import wave.exceptions.IndexOfArrayOutOfBounds;
public class MultipleIntMatrix extends MultipleMatrix implements MultipleMatrixInter {

	private int[] matrix;

	public MultipleIntMatrix(int[] matrixlengths) {
		super(matrixlengths);

		matrix = new int[this.getBigMatrixSize()];
	}

	public Object getElement(int[] index) throws IndexOfArrayOutOfBounds{
		try {
			return new Integer(matrix[this.getPos(index)]);
		} catch (IndexOfArrayOutOfBounds e) {
			throw e;
		}
	}

	public void setElement(int[] index, Object element) throws IndexOfArrayOutOfBounds{

		try {
			matrix[this.getPos(index)] = ((Integer) element).intValue();
		} catch (IndexOfArrayOutOfBounds e) {
			throw e;
		}
	}

}
