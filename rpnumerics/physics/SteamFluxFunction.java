/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class SteamFluxFunction implements FluxFunction {

  //
  // Members
  //
  private SteamFluxParams params_;

  //
  // Constructors
  //
  public SteamFluxFunction(SteamFluxParams params) {
    params_ = params;
  }

  //
  // Accessors/Mutators
  //
  public FluxParams fluxParams() {
    return params_;
  }

  //
  // Methods
  //
  public RealVector F(RealVector x) {

    return new RealVector(4);
  }

  public RealMatrix2 DF(RealVector x) {

    return new RealMatrix2(4, 4);
  }

  public HessianMatrix D2F(RealVector x) {

    return new HessianMatrix(4);
  }

}
