/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.JetMatrix;
import wave.util.RealVector;

public class NativeFluxFunction implements RpFunction {

    public int jet(WaveState input, JetMatrix output, int degree) {
        return nativeJet(input, output, degree);
    }

    public int jet(RealVector input, JetMatrix output, int degree) {
        return nativeVectorJet(input, output, degree);
    }

    private native int nativeJet(WaveState input, JetMatrix output, int degree);

    private native int nativeVectorJet(RealVector input, JetMatrix output, int degree);
}
