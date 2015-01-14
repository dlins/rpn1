package wave.util;

import wave.util.exceptions.IndexOfArrayOutOfBounds;

public interface MultipleMatrixInter {
	Object getElement(int[] index) throws IndexOfArrayOutOfBounds;
	void setElement(int[] index, Object element) throws IndexOfArrayOutOfBounds;
}
