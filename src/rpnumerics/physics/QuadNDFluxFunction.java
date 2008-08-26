/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.FluxFunction;
import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class QuadNDFluxFunction implements FluxFunction
{

	//
	// Members
	//
	private QuadNDFluxParams params_;

	//
	// Constructors
	//
	public QuadNDFluxFunction( QuadNDFluxParams params )
	{
		params_ = params;
	}

	//
	// Accessors/Mutators
	//
	public FluxParams fluxParams( )	{return params_;}
       //
       // Methods
       //
    public RealVector F(RealVector x) {
        // F = A + B x + (1/2) x^T C x
        double[] A = params_.getA();
        double[] [] B = params_.getB();
        double[] [] [] C = params_.getC();
        int spaceDim = x.getSize();
        RealVector result = new RealVector(spaceDim);
        double[] q = new double[spaceDim];
        double[] res = new double[spaceDim];
        for (int i = 0; i < spaceDim; i++)
            q[i] = x.getElement(i);
        for (int i = 0; i < spaceDim; i++) {
            res[i] = A[i];
            for (int j = 0; j < spaceDim; j++) {
                res[i] = res[i] + B[i] [j] * q[j];
                for (int k = 0; k < spaceDim; k++)
                    res[i] = res[i] + 0.5 * C[i] [j] [k] * q[j] * q[k];
            }
        }
        for (int i = 0; i < spaceDim; i++)
            result.setElement(i, res[i]);

//        System.out.println("Valor de f (Java): "+ result.toString());

        return result;
    }

    public RealMatrix2 DF(RealVector x) {
        // DF = B + C x
        double[] [] B = params_.getB();
        double[] [] [] C = params_.getC();
        int spaceDim = x.getSize();
        RealMatrix2 result = new RealMatrix2(spaceDim, spaceDim);
        double[] q = new double[spaceDim];
        double[] [] res = new double[spaceDim] [spaceDim];
        for (int i = 0; i < spaceDim; i++)
            q[i] = x.getElement(i);
        for (int i = 0; i < spaceDim; i++)
            for (int j = 0; j < spaceDim; j++) {
                res[i] [j] = B[i] [j];
                for (int k = 0; k < spaceDim; k++)
                    res[i] [j] = res[i] [j] + C[i] [j] [k] * q[k];
            }
        for (int i = 0; i < spaceDim; i++)
            for (int j = 0; j < spaceDim; j++)
                result.setElement(i, j, res[i] [j]);


//        System.out.println("Valor de df (Java): "+ result.toString());

            return result;
    }

    public HessianMatrix D2F(RealVector x) {
        // DFF = C
        int spaceDim = x.getSize();
        HessianMatrix result = new HessianMatrix(spaceDim);
        for (int i = 0; i < spaceDim; i++)
            for (int j = 0; j < spaceDim; j++)
                for (int k = 0; k < spaceDim; k++)
                    result.setElement(i, j, k, params_.getC() [i] [j] [k]);

//                 System.out.println("Valor de d2f (Java): "+ result.toString());

        return result;
    }



}
