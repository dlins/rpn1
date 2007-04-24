/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

import rpnumerics.PhasePoint;

public interface ApproxFunction {

double function(double x, double y);
double dx(double x, double y);
double dy(double x, double y);
PhasePoint xZero();
}

