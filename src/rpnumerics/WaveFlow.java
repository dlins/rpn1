/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public abstract class WaveFlow extends NativeRpFunction {
    //
    // Members
    //
//    WavePoint flux( RealVector x );
//
//    RealMatrix2 fluxDeriv( RealVector x );
//
//    HessianMatrix fluxDeriv2( RealVector x );
//
    public abstract PhasePoint getXZero();

    public abstract void setXZero(PhasePoint xzero);

//    public abstract String getName();
}
