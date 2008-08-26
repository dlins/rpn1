/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.JetMatrix;

public class NativeRpFunction implements RpFunction {


    public int jet(WaveState input, JetMatrix output, int degree) {
        return nativeJet(input, output, degree);
    }
    private native int nativeJet(WaveState input, JetMatrix output, int degree);
}
