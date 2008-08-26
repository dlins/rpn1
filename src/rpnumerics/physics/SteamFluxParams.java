/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class SteamFluxParams extends FluxParams {
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
    public SteamFluxParams(FluxParams copy) {
        super(copy);
    }


    public SteamFluxParams() {
      super(Steam.FLUX_ID,new RealVector(4),0);

    }

    //
    // Accessors/Mutators
    //
    public FluxParams defaultParams() { return new Quad4Params(); }
}
