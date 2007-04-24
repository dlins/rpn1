package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class Function3D_1 extends CubeFunction {

	public Function3D_1(FluxFunction fluxFunction, HugoniotParams params) {
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
	
	public double function(PointNDimension point) {
		  double x = 0.0;
		  double y = 0.0;
		  double z = 0.0;
		  
		  try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
		  } catch (Exception e) {
			  
		  }
		  		  
		 return x*x + y*y + z*z - 36;
	}

}
