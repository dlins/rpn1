/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util;

import rpnumerics.WavePoint;

public interface VectorField {
    WavePoint f(RealVector y);
}
