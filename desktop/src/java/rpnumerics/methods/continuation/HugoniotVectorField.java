/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.methods.continuation;

import wave.util.*;
import rpnumerics.WavePoint;

public class HugoniotVectorField implements VectorField {

    private VectorFunction f_;

    public HugoniotVectorField(VectorFunction f) {
        f_ = f;
    }

    public WavePoint f(RealVector y) {
        RealMatrix2 df = new RealMatrix2(f_.deriv(y));
        RealMatrix2 matrix = new RealMatrix2(df.getNumRow(), df.getNumRow());
        RealVector vector = new RealVector(df.getNumRow());
        RealVector result = new RealVector(y.getSize());
        int j = 1;
        for (int i = 0; i < y.getSize(); i++) {
            for (int i1 = 0; i1 < i; i1++) {
                df.getColumn(i1, vector);
                matrix.setColumn(i1, vector);
            }
            for (int i2 = i + 1; i2 < y.getSize(); i2++) {
                df.getColumn(i2, vector);
                matrix.setColumn(i2 - 1, vector);
            }
            result.setElement(i, j * matrix.determinant());
            j = -j;
        }

        WavePoint returned = new WavePoint(result, 0);
//    return result;

        return returned;
    }
}
