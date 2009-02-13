package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.FluxFunction;
import rpnumerics.FluxParams;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;
import rpnumerics.physics.*;

public class TestQuad extends FluxFunction {

	public TestQuad() {
		super();
	}

	public HessianMatrix D2F(RealVector U) {
		return null;
	}

	public RealMatrix2 DF(RealVector U) {
		return null;
	}

	public RealVector F(RealVector U) {
		
		double x = U.getElement(0);
		double y = U.getElement(1);
		
		RealVector res = new RealVector(2);
		
		res.setElement(0, x);
		res.setElement(1, y);
		
		return res;
	}

	public FluxParams fluxParams() {
		return new Quad2FluxParams();
	}

}
