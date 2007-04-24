/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import wave.util.RealVector;

public class Quad2Params extends QuadNDParams {
    //
    // Constants
    //
    static public final double[] DEFAULT_A = new double[] { 0d, 0d };

    static public final double[] [] DEFAULT_B = new double[] [] { { 0d, .1 }, { -.1, 0d }
    };

    //    static public final double[][] DEFAULT_B = new double[][]{{1d,.1},{-.1,1d}};
    static public final double[] [] [] DEFAULT_C = new double[] [] [] { { { -1d, 0d }, { 0d, 1d }
        }, { { 0d, 1d }, { 1d, 0d }
        }
    };

    //
    // Constructors
    //
    public Quad2Params(Quad2Params copy) {
        super(copy);
    }

    public Quad2Params(double[] A, double[] [] B, double[] [] [] C,int index) {
        super(Quad2.FLUX_ID, 2, A, B, C,index);
    }

    public Quad2Params() {
        this(DEFAULT_A, DEFAULT_B, DEFAULT_C,0);
    }

    //
    // Accessors/Mutators
    //
    public FluxParams defaultParams() { return new Quad2Params(); }
}
