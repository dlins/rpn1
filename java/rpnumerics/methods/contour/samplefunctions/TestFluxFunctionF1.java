package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.FluxFunction;
import rpnumerics.FluxParams;
import rpnumerics.methods.contour.samplefunctions.Quad2FluxParams;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class TestFluxFunctionF1 extends FluxFunction {

	public TestFluxFunctionF1() {
		super();
	}

	public HessianMatrix D2F(RealVector U) {		
		double u, v, out[];
		
		u=U.getElement(0);
		
		out = null;

		// calculo
			
		
		return new HessianMatrix (out);		
	}

	public RealMatrix2 DF(RealVector U) {
		double u, out = 0.0;
		
		u=U.getElement(0);
		
		 RealMatrix2 res = new RealMatrix2(1, 1);
	
		// calculo
		out = 2*u;
		
		res.setElement(0, 0, out);
		
		return res;
	}

	public RealVector F(RealVector U) {		
		double u, v, out = 0.0;
		
		u=U.getElement(0);
		
		RealVector res = new RealVector(1);
	
		// calculo
		out = u*u;
		
		res.setElement(0, out);
		
		return res;
	}

	public FluxParams fluxParams() {
		return new Quad2FluxParams();
	}

}
