/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class Quad3FluxParams extends QuadNDFluxParams {
    //
    // Constants
    //
    static public final double[] DEFAULT_A = new double[] { 0.0, 0.0, 0.0 };

    static public final double[] [] DEFAULT_B = new double[] []
    { { 0.0, 0.1, 0.01 }, { -0.1, 0.01, 0.01 }, { 0.001, 0.01, -0.2 }
    };

    static public final double[] [] [] DEFAULT_C = new double[] [] [] { { { -1.0, 0.01, 0.01 }, { 0.0, 1.0, 0.01 }, {
                0.0, 0.0001, 0.0001
            }
        }, { { 0.01, 1.0, 0.01 }, { 1.0, 0.01, 0.01 }, { 0.0001, 0.0, 0.01 }
        }, { { 0.0, 0.01, 0.0 }, { 0.01, 0.0, 0.01 }, { 0.0001, 0.0, 0.01 }
        }
    };

    //
    // Constructors
    //
    public Quad3FluxParams(Quad3FluxParams copy) {
        super(copy);
    }

    public Quad3FluxParams(double[] A, double[] [] B, double[] [] [] C,int index) {
        super(Quad3.FLUX_ID, 3, A, B, C,index);
    }

    public Quad3FluxParams() {
        this(DEFAULT_A, DEFAULT_B, DEFAULT_C,0);
    }

    //
    // Methods
    //
    public FluxParams defaultParams() { return new Quad3FluxParams(); }
}
