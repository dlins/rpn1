package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.FluxFunction;
import rpnumerics.FluxParams;
import rpnumerics.methods.contour.samplefunctions.Quad2FluxParams;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class BifurcationFluxFunctionTest extends FluxFunction {

	public BifurcationFluxFunctionTest() {
		super();
	}
	
	public HessianMatrix D2F(RealVector U) {
		return null;
	}

	public RealMatrix2 DF(RealVector U) {
		
		double x = U.getElement(0);
				
		double[][] result = new double[1][1];
		
		result[0][0] = 2*x;
		
		return RealMatrix2.convert(1,1,result);
	}

	public RealVector F(RealVector U) {
				
		double x = U.getElement(0);
		
		RealVector res = new RealVector(1);
		
		res.setElement(0, x*x);
		
		return res;
	}

	public FluxParams fluxParams() {
		return new Quad2FluxParams();
	}

}
