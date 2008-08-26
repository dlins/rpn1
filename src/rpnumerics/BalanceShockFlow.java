/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class BalanceShockFlow extends ShockFlow {

  public BalanceShockFlow(ShockFlowParams flowParams,FluxFunction flux) {
    super(flowParams,flux);
  }


 public WavePoint flux( RealVector x ){
    return null;
  }

public    RealMatrix2 fluxDeriv( RealVector x ){
      return null;
    }

public    HessianMatrix fluxDeriv2( RealVector x ){
      return null;

    }

    public String getName() { return "Balance Shock Flow";}

    


}
