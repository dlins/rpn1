/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.util.HessianMatrix;
import wave.util.JacobianMatrix;
import wave.util.RealVector;

public abstract class WaveFlow {
    //
    // Members
    //
    
    public native RealVector flux(RealVector x);

    public native JacobianMatrix fluxDeriv(RealVector x);

    public native HessianMatrix fluxDeriv2(RealVector x);

    public abstract PhasePoint getXZero();

    public abstract void setXZero(PhasePoint xzero);

}
