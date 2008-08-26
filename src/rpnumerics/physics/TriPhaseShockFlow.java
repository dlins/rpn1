/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.physics;

import rpnumerics.ConservationShockFlow;
import rpnumerics.FluxFunction;
import rpnumerics.PhasePoint;
import rpnumerics.ShockFlowParams;
import wave.util.RealVector;
import wave.util.RealMatrix2;
import rpnumerics.WavePoint;

public class TriPhaseShockFlow extends ConservationShockFlow
{
    //
    // Constants
    //
    //
    // Members
    //
    private RealVector hx0_;

//	private TriPhaseFluxFunction fluxFunction_;

   //
    // Constructors
    //
	public TriPhaseShockFlow(TriPhaseFluxFunction fluxFunction,
				 PhasePoint x0,
				 double sigma){
	
		super(new ShockFlowParams(x0, sigma),(FluxFunction)fluxFunction);
//		fluxFunction_ = fluxFunction;
	}

    //
    // Accessors/Mutators
    //

    //
    // Methods
    //
    public WavePoint flux(RealVector x) {
        RealVector fx = getFlux().F(x);
        // F(x) - F(x0)
        fx.sub(fx0());
        // H(x)
        RealVector hx = new RealVector(x);
        // (H(x) - H(x0))*sigma
        RealVector firstTerm = new RealVector(x.getSize());
        firstTerm.sub(hx, hx0_);
        firstTerm.scale(getSigma());
        fx.sub(firstTerm);
        fx.mul(((TriPhaseFluxFunction)getFlux()).viscosity(x), fx);

        WavePoint returned = new WavePoint(fx,getSigma());

        return returned;


//        return fx;
    }
    
        public String getName(){
        return "TriPhase Shock Flow";
    }

    
    
}
