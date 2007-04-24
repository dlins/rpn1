/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

public class Quad2FluxParams extends QuadNDFluxParams {
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
    public Quad2FluxParams(Quad2FluxParams copy) {
        super(copy);
    }

    public Quad2FluxParams(double[] A, double[] [] B, double[] [] [] C,int index) {
        super(Quad2.FLUX_ID, 2, A, B, C,index);
    }

    public Quad2FluxParams() {
        this(DEFAULT_A, DEFAULT_B, DEFAULT_C,0);
    }

    //
    // Methods
    //
    public FluxParams defaultParams() { return new Quad2FluxParams(); }
}
