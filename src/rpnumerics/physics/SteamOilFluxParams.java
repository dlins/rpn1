/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class SteamOilFluxParams extends FluxParams {
    //
    // Constants
    //
    public static final double[] DEFAULT_A = new double[] { 0.0, 0.0, 0.0, 0.0 };

    public static final double[] [] DEFAULT_B = new double[] [] { { 0.01, 0.1, 0.02, 0.0 }, { -0.1, 0, 0, 0.01 }, {
            0.0, -0.02, -0.12, 0.01
        }, { 0.01, 0.0, 0.01, 0.11 }
    };

    public static final double[] [] [] DEFAULT_C = new double[] [] [] { { { -1.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, {
                0.0, 0.0, 0.0, 0.0
            }, { 0.0, 0.0, 0.0, 0.0 }
        }, { { 0.0, 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0 }
        }, { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.2 }, { 0.0, 0.0, 0.2, 0.0 }
        }, { { 0.0, 0.0, 0.0, 0.1 }, { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0 }, { 0.1, 0.0, 0.0, 0.0 }
        }
    };

    //
    // Constructors
    //
    public SteamOilFluxParams(FluxParams copy) {
        super(copy);
    }


    public SteamOilFluxParams() {
      super(SteamOil.FLUX_ID,new RealVector(3),0);

    }

    //
    // Accessors/Mutators
    //
    public FluxParams defaultParams() { return new Quad4Params(); }
}
