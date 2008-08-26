/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics.physics;

import rpnumerics.WaveFlow;
import rpnumerics.PhasePoint;
import rpnumerics.ShockFlowParams;
import wave.util.*;
import rpnumerics.WavePoint;

public class CombShockFlow implements WaveFlow
{

    //Members

    PhasePoint x0_;
    double sigma_;

    //Constructors

    public CombShockFlow( ShockFlowParams fParams){

	x0_=fParams.getPhasePoint();
	sigma_=fParams.getSigma();

    }


    //
    // Methods
    //
    public WavePoint flux( RealVector U )
    {
	return null;
    }

    public RealMatrix2 fluxDeriv( RealVector x )
    {
	return null;
    }

    public HessianMatrix fluxDeriv2( RealVector x )
    {
	return null;
    }

    public PhasePoint getXZero() { return x0_;}

    public void setXZero(PhasePoint xzero) {

        x0_=xzero;
    }

    public String getName() { return "Comb Shock Flow";}


}
