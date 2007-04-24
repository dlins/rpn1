/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.physics;

import rpnumerics.ConservationShockFlow;
import rpnumerics.PhasePoint;
import wave.util.RealVector;
import wave.util.RealMatrix2;

public class TriPhaseShockFlow extends ConservationShockFlow
{
    //
    // Constants
    //
    //
    // Members
    //
    private RealVector hx0_;

	private TriPhaseFluxFunction fluxFunction_;
	
   //
    // Constructors
    //	
	public TriPhaseShockFlow(TriPhaseFluxFunction fluxFunction,
				 PhasePoint x0,
				 double sigma)
	{
		super(x0,sigma);
		fluxFunction_ = fluxFunction;		
	}

    //
    // Accessors/Mutators
    //

    //
    // Methods
    //
    public RealVector flux(RealVector x) {
        RealVector fx = fluxFunction_.F(x);
        // F(x) - F(x0)
        fx.sub(fx0());
        // H(x)
        RealVector hx = new RealVector(x);
        // (H(x) - H(x0))*sigma
        RealVector firstTerm = new RealVector(x.getSize());
        firstTerm.sub(hx, hx0_);
        firstTerm.scale(getSigma());
        fx.sub(firstTerm);
        fx.mul(fluxFunction_.viscosity(x), fx);
        return fx;
    }
}
