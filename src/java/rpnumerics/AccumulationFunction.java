/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.HessianMatrix;
import wave.util.JetMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class AccumulationFunction implements RpFunction {

    public RealVector H(RealVector toRealVector) {
         JetMatrix output = new JetMatrix(toRealVector.getSize());
        jet(toRealVector, output, 1);
        return output.f();
      
    }

    public RealMatrix2 DH(RealVector toRealVector) {

        JetMatrix output = new JetMatrix(toRealVector.getSize());
        jet(toRealVector, output, 1);
        return output.jacobian();
    }

    public HessianMatrix D2H(RealVector toRealVector) {
        JetMatrix output = new JetMatrix(toRealVector.getSize());
        jet(toRealVector, output, 2);
        return output.hessian();

    }

    public int jet(WaveState input, JetMatrix output, int degree) {
        return 0;
    }

    public int jet(RealVector input, JetMatrix output, int degree) {
        return 0;
    }

  
}
