/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.RealVector;
import wave.ode.ODESolution;
import wave.util.RealVector;


public class AdvanceCurve {
    public static ODESolution calc(RealVector point, int timeDirection) {
       return RPNUMERICS.odeSolver().solve(point, timeDirection);
    }
}
