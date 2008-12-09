/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.ode;

import wave.util.RealVector;

public interface ODESolver {
    ODESolution solve(RealVector y, int timeDirection);

    // configuration parameters
    ODESolverProfile getProfile();
}
