/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.HugoniotCurveCalc;
import rpnumerics.PhasePoint;
import rpnumerics.RpSolution;
import rpnumerics.WaveFlow;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class CombHugoniotCurveCalc implements HugoniotCurveCalc
{
	public static final double DELTA_TAU = .1d;

	private double vPlus_;

	private PhasePoint origin_;

	private CombFluxParams params_;



	public CombHugoniotCurveCalc( CombFluxParams params, PhasePoint origin, double vPlus )
	{

		params_ = params;
		origin_ = origin;
		vPlus_ = vPlus;

	}

	public void uMinusChangeNotify(PhasePoint uMinus){
          origin_=uMinus;
	}


	public RpSolution calc( )
	{
		return null;
	}
	public RpSolution recalc( )
	{
		return null;
	}


        public PhasePoint getUMinus(){return null;}

        public double [] getPrimitiveUMinus(){ return null;}

        public RealVector getFMinus(){return null;}
        public RealMatrix2 getDFMinus() {return null;}

    public String getCalcMethodName() {
        return "comb hugoniot method";//TODO Put the correct method name
    }

    public WaveFlow getFlow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
