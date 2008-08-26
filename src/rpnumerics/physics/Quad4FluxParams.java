/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class Quad4FluxParams extends QuadNDFluxParams {
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
    public Quad4FluxParams(Quad4FluxParams copy) {
        super(copy);
    }

    public Quad4FluxParams(double[] A, double[] [] B, double[] [] [] C,int index) {
        super(Quad4.FLUX_ID, 4, A, B, C,index);
    }

    public Quad4FluxParams() {
        this(DEFAULT_A, DEFAULT_B, DEFAULT_C,0);
    }

    //
    // Methods
    //
    public FluxParams defaultParams() { return new Quad4FluxParams(); }
}
