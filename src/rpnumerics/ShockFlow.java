/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public  class ShockFlow implements WaveFlow{


    public native void setXZero(PhasePoint x0);
    public native WavePoint flux(RealVector x);
    public native RealMatrix2 fluxDeriv(RealVector x);
    public native HessianMatrix fluxDeriv2(RealVector x);
    public native PhasePoint getXZero();
    public native void setSigma(double sigma);
    public native void setSigma(RealVector sigma);
    public native double getSigma();





}
