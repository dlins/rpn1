/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.RealVector;

public class FluxFunction extends NativeFluxFunction {
//	HessianMatrix D2F( RealVector U );
//	
//	RealMatrix2 DF( RealVector U );
//	
//	RealVector F( RealVector U );
    
     static private final double[] DEFAULT_A = new double[] { 0d, 0d };

    static private final double[] [] DEFAULT_B = new double[] [] { { 0d, .1 }, { -.1, 0d }
    };

    //    static public final double[][] DEFAULT_B = new double[][]{{1d,.1},{-.1,1d}};
    static private final double[] [] [] DEFAULT_C = new double[] [] [] { { { -1d, 0d }, { 0d, 1d }
        }, { { 0d, 1d }, { 1d, 0d }
        }
    };
    
    
    
    /**
     * 
     * @deprecated 
     */
    
    public FluxParams fluxParams() { //TODO HARDCODED TO QUAD2
        int m = 2;
        RealVector params = new RealVector(m + m * m + m * m * m);
        for (int i = 0; i < m; i++) {
            params.setElement(i, DEFAULT_A[i]);
            for (int j = 0; j < m; j++) {
                params.setElement(m + i * m + j,DEFAULT_B[i] [j]);
                for (int k = 0; k < m; k++) {
                    params.setElement(m + m * m + i * m * m + j * m + k, DEFAULT_C[i] [j] [k]);
                }
            }
        }
        return new FluxParams("Quad2", params, 1);
    }
}  
