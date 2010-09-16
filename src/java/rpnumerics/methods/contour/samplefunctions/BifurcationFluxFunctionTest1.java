package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.FluxFunction;
import rpnumerics.FluxParams;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class BifurcationFluxFunctionTest1 extends FluxFunction {

	public BifurcationFluxFunctionTest1() {
		super();
	}

	public HessianMatrix D2F(RealVector U) {
		return null;
	}

	public RealMatrix2 DF(RealVector U) {
		return null;
	}

	public RealVector F(RealVector U) {
		return null;
	}

	public FluxParams fluxParams() {
		return null;
	}

}
