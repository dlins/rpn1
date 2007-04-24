package wave.util;

import wave.exceptions.IndexOfArrayOutOfBounds;
public class MultipleMatrix {

  private int numberOfMatrixes;
  private int[] lengths;

  private int loopsize;

  public MultipleMatrix(int n) {

    this(MultipleMatrix.initSquareMatrix(n));

  }

  public MultipleMatrix(int[] matrixlengths) {
    numberOfMatrixes = matrixlengths.length;

    lengths = new int[numberOfMatrixes];

    loopsize = 1;

    for (int pont = 0; pont < numberOfMatrixes; pont++) {
      lengths[pont] = matrixlengths[pont];
      loopsize *= lengths[pont];
    }
  }

  private static int[] initSquareMatrix(int n) {

    int[] matrixlengths = new int[n];
    for (int i = 0; i < n; i++) {
      matrixlengths[i] = n;
    }
    return matrixlengths;
  }

  public int getBigMatrixSize() {
    return loopsize;
  }

  public int[] getIndex(int pos) {
    int temp = pos;
    int index[] = new int[numberOfMatrixes];

    for (int pont = 0; pont < numberOfMatrixes; pont++) {
      index[pont] = (temp % lengths[pont]);
      temp = temp / lengths[pont];
    }

    return index;
  }

  public int getPos(int[] A) throws IndexOfArrayOutOfBounds {

    int sum = 0;
    if (A.length <= lengths.length) {
      if (A.length != 1) {
        int pont = (A.length - 1);
        sum = A[pont] * lengths[pont - 1] + A[pont - 1];
        pont--;
        for (; pont >= 1; pont--) {
          sum = sum * lengths[pont - 1] + A[pont - 1];
        }
      }
      else {
        sum = A[0];
      }
    }
    else {
      throw new IndexOfArrayOutOfBounds();
    }

    return sum;
  }
}
