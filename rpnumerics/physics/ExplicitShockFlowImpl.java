/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.ShockFlow;
import rpnumerics.PhasePoint;


public interface ExplicitShockFlowImpl
{
	ShockFlow createShockFlowInstance(PhasePoint xZero,double sigma);
}
