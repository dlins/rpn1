package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class Function4D_3 extends CubeFunction {

	public Function4D_3(FluxFunction fluxFunction, HugoniotParams params) {
		super(fluxFunction, params);
		// TODO Auto-generated constructor stub
	}

	public RealVector value(RealVector u) {
		// TODO Auto-generated method stub
		return null;
	}

	public RealMatrix2 deriv(RealVector u) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getFunctionDimension() {
	    return 4;
	}
	
	public double function(PointNDimension point) {
		  double x = 0.0;
		  double y = 0.0;
		  double z = 0.0;
		  double w = 0.0;
		  
		  try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
			w = point.getCoordinate(4);
		  } catch (Exception e) {
			  
		  }
		  
		 return x - y;
	}

}
