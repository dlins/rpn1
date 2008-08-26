/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.physics;

import wave.util.RealVector;
import wave.util.RealMatrix2;
import wave.util.HessianMatrix;

public class DefaultAccFunction implements AccumulationFunction {

  DefaultAccParams accumulationParams_;

  public DefaultAccFunction() {

      accumulationParams_ = new DefaultAccParams();

  }

public  HessianMatrix D2H(RealVector U) {

    return new HessianMatrix(U.getSize());

  }

public  RealMatrix2 DH(RealVector U) {

    return new RealMatrix2(U.getSize(),U.getSize());
  }

public  RealVector H(RealVector U) {

    return U;
  }

public  AccumulationParams accumulationParams() {
    return accumulationParams_;
  }

}
