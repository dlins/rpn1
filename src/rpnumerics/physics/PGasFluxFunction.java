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

public class PGasFluxFunction implements FluxFunction
{
	private PGasFluxParams params_;
	

    //
    // Constructors/Inner Classes
    //
    public PGasFluxFunction(PGasFluxParams params) {
        params_ = params;
    }
    public RealVector F(RealVector x) {
        RealVector res = new RealVector(2);
        res.setElement(0, -x.getElement(1));
        double term1 = params_.beta() * Math.pow(x.getElement(0), params_.alpha());
        double term2 = params_.coefA3() * Math.pow(x.getElement(0), 3);
        double term3 = params_.coefA2() * Math.pow(x.getElement(0), 2);
        double term4 = params_.coefA1() * x.getElement(0);
        res.setElement(1, term1 + term2 + term3 + term4 + params_.coefA0());
        return res;
    }

    public RealMatrix2 DF(RealVector x) {
        RealMatrix2 res = new RealMatrix2(2, 2);
        res.setElement(0, 0, 0);
        res.setElement(0, 1, -1);
        res.setElement(1, 1, 0);
        double term1 = params_.alpha() * params_.beta() * Math.pow(x.getElement(0), params_.alpha() - 1);
        double term2 = 3d * params_.coefA3() * Math.pow(x.getElement(0), 2);
        double term3 = 2d * params_.coefA2() * x.getElement(0);
        res.setElement(1, 0, term1 + term2 + term3 + params_.coefA1());
        return res;
    }

    public HessianMatrix D2F(RealVector x) {
        HessianMatrix result = new HessianMatrix(3);
        result.setElement(0, 0, 0, 0);
        result.setElement(0, 0, 1, 0);
        result.setElement(0, 1, 0, 0);
        result.setElement(0, 1, 1, 0);
        double alpha = params_.alpha();
        double term1 = alpha * (alpha - 1) * params_.beta() * Math.pow(x.getElement(0), alpha - 2);
        double term2 = 6d * params_.coefA3() * x.getElement(0);
        result.setElement(1, 0, 0, term1 + term2 + 2d * params_.coefA2());
        result.setElement(1, 0, 1, 0);
        result.setElement(1, 1, 0, 0);
        result.setElement(1, 1, 1, 0);
        return result;
    }

	public FluxParams fluxParams( )	{return params_;}
	
}
