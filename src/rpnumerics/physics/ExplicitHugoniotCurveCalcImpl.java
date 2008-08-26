/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.HugoniotCurveCalc;
import rpnumerics.PhasePoint;


public interface ExplicitHugoniotCurveCalcImpl
{
	HugoniotCurveCalc createHugoniotCurveCalcInstance(PhasePoint point);
}
