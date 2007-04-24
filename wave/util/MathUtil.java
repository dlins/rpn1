/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

import org.netlib.util.intW;
import org.netlib.lapack.DGESV;

public class MathUtil {
    public static final double sign(double value, double check) {
        if (check >= 0)
            return Math.abs(value);
        return -Math.abs(value);
    }

    public static final int signedOne(double check) {
        if (check >= 0)
            return 1;
        return -1;
    }

    // solves the equation Ax = b
    // can be put into XtRealVector
    static public RealVector linearSolver(RealMatrix2 A, RealVector b) {
        double[] [] Ad = new double[A.getNumRow()] [A.getNumCol()];
        double[] [] bd = new double[b.getSize()] [1];
        for (int i = 0; i < A.getNumRow(); i++) {
            bd[i] [0] = b.getElement(i);
            for (int j = 0; j < A.getNumRow(); j++)
                Ad[i] [j] = A.getElement(i, j);
        }
        int[] intArray = new int[A.getNumRow()];
        intW info = new intW(1);
        DGESV.DGESV(A.getNumRow(), 1, Ad, intArray, bd, info);
        RealVector result = new RealVector(A.getNumCol());
        for (int i = 0; i < A.getNumCol(); i++)
            result.setElement(i, bd[i] [0]);
        return result;
    }
}
