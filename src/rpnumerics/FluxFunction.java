/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.HessianMatrix;
import wave.util.JetMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class FluxFunction implements RpFunction {

    private FluxParams fluxParams_;//	HessianMatrix D2F( RealVector U );
//	
//	RealMatrix2 DF( RealVector U );
//	
//	RealVector F( RealVector U );
    
    
    public FluxFunction (FluxParams fluxParams){
        fluxParams_=fluxParams;
    }
    
    
    static private final double[] DEFAULT_A = new double[]{0d, 0d};//TODO Passar para a classe QUAD2FLUXPARAMS
    static private final double[][] DEFAULT_B = new double[][]{{0d, .1}, {-.1, 0d}};    //    static public final double[][] DEFAULT_B = new double[][]{{1d,.1},{-.1,1d}};
    static private final double[][][] DEFAULT_C = new double[][][]{{{-1d, 0d}, {0d, 1d}}, {{0d, 1d}, {1d, 0d}}};

    
    
    public FluxFunction (){
        // HARDCODED TO QUAD2
        int m = 2;
        RealVector params = new RealVector(m + m * m + m * m * m);
        for (int i = 0; i < m; i++) {
            params.setElement(i, DEFAULT_A[i]);
            for (int j = 0; j < m; j++) {
                params.setElement(m + i * m + j, DEFAULT_B[i][j]);
                for (int k = 0; k < m; k++) {
                    params.setElement(m + m * m + i * m * m + j * m + k, DEFAULT_C[i][j][k]);
                }
            }
        }
        fluxParams_ = new FluxParams(params);
    }
    
    
    
    
    public int jet(WaveState input, JetMatrix output, int degree) {
        return nativeJet(input, output, degree);
    }

    public int jet(RealVector input, JetMatrix output, int degree) {
        return nativeVectorJet(input, output, degree);
    }

    private native int nativeJet(WaveState input, JetMatrix output, int degree);

    private native int nativeVectorJet(RealVector input, JetMatrix output, int degree);

    public HessianMatrix D2F(RealVector toRealVector) {
        JetMatrix output = new JetMatrix(toRealVector.getSize());
        jet(toRealVector, output, 2);
        return output.hessian();

    }

    public RealMatrix2 DF(RealVector toRealVector) {

        JetMatrix output = new JetMatrix(toRealVector.getSize());
        jet(toRealVector, output, 1);
        return output.jacobian();
    }

    public RealVector F(RealVector toRealVector) {
        JetMatrix output = new JetMatrix(toRealVector.getSize());
        jet(toRealVector, output, 1);
        return output.f();
    }

    
    
    
    
    /**
     * 
     * @deprecated 
     */
    public FluxParams fluxParams() { 
        
        return fluxParams_;

    }
}  
