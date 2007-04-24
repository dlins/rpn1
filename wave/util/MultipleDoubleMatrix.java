package wave.util;

import wave.exceptions.IndexOfArrayOutOfBounds;
public class MultipleDoubleMatrix extends MultipleMatrix implements MultipleMatrixInter {

	private double[] matrix;

	public MultipleDoubleMatrix(int[] matrixlengths) {
		super(matrixlengths);

		matrix = new double[this.getBigMatrixSize()];
	}

	public Object getElement(int[] index) throws IndexOfArrayOutOfBounds{
		try {
			return new Double(matrix[this.getPos(index)]);
		} catch (IndexOfArrayOutOfBounds e) {
			throw e;
		}
	}

	public void setElement(int[] index, Object element) throws IndexOfArrayOutOfBounds{

		try {
			matrix[this.getPos(index)] = ((Double) element).doubleValue();
		} catch (IndexOfArrayOutOfBounds e) {
			throw e;
		}
	}
}
