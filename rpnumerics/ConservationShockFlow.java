/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import wave.util.RealMatrix2;
import wave.util.HessianMatrix;
import wave.util.RealVector;
import wave.util.RealMatrix2;


public class ConservationShockFlow extends ShockFlow {
    //
    // Constants
    //
    //
    // Members
    //
    private RealVector fx0_;

    //
    // Constructors
    //

    public ConservationShockFlow(PhasePoint x0, double sigma) {
        this(new ShockFlowParams(x0,sigma));

    }

    public ConservationShockFlow(ShockFlowParams fParams) {

       super(fParams);
        updateXZeroTerms();
    }

    //
    // Accessors/Mutators
    //


    public void setXZero(PhasePoint x0) {

        super.setXZero(x0);
        updateXZeroTerms();
    }

    public RealVector fx0(){return fx0_;}

    //
    // Methods
    //
    public void updateXZeroTerms() {
        fx0_ = RPNUMERICS.fluxFunction().F(flowParams_.getPhasePoint().getCoords());

    }

    public RealVector fluxDerivSigma(RealVector x) {
        RealVector result = new RealVector(x.getSize());
        result.sub(new RealVector(flowParams_.getPhasePoint().getCoords()), x);
        return result;
    }

    public RealVector flux(RealVector x) {

        RealVector fx = RPNUMERICS.fluxFunction().F(x);
        // F(x) - F(x0)
        fx.sub(fx0_);
	// x - x0_
	RealVector xMinusX0 = new RealVector(x);
	xMinusX0.sub(flowParams_.getPhasePoint().getCoords());
	// (x - x0_)*sigma
	xMinusX0.scale(getSigma());

	// (Fx - Fx0) - sigma*(x - x0)
	fx.sub(xMinusX0);
	return fx;

    }

   public RealMatrix2 fluxDeriv(RealVector x) {
        RealMatrix2 fluxDF_x = RPNUMERICS.fluxFunction().DF(x);
        // flux.DFX(x) - sigma scaled matrix
        RealMatrix2 identity = new RealMatrix2(fluxDF_x.getNumRow(), fluxDF_x.getNumCol());
        identity.scale(getSigma());
        fluxDF_x.sub(identity);
        return fluxDF_x;
    }

    public HessianMatrix fluxDeriv2(RealVector x) {
        return RPNUMERICS.fluxFunction().D2F(x);
    }
}
