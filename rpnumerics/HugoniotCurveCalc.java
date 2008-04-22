/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;


import rpnumerics.PhasePoint;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public interface HugoniotCurveCalc extends RpCalculation {
	void uMinusChangeNotify(PhasePoint uMinus);
        PhasePoint getUMinus();
        RealVector getFMinus();
        RealMatrix2 getDFMinus();
        double [] getPrimitiveUMinus();
}
