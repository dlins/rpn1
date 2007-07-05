/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.ode;

import wave.util.RealVector;
import rpnumerics.ode.ODESolverProfile;

public interface ODESolver {

    // configuration parameters
    ODESolverProfile getProfile();
}
