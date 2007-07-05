/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import rpnumerics.PhasePoint;
import rpnumerics.WavePoint;
import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public abstract interface WaveFlow {
    //
    // Members
    //
    WavePoint flux( RealVector x );

    RealMatrix2 fluxDeriv( RealVector x );

    HessianMatrix fluxDeriv2( RealVector x );

    PhasePoint getXZero();

    void setXZero (PhasePoint xzero);

}
