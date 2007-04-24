/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */


package rpnumerics.methods;

import wave.util.RealVector;
import rpnumerics.HugoniotCurve;

public abstract class HugoniotMethod implements RPMethod {
    public abstract HugoniotCurve curve(RealVector initialPoint);

}
