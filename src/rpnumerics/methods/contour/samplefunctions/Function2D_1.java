package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class Function2D_1 extends CubeFunction {

	public Function2D_1(FluxFunction fluxFunction, HugoniotParams params) {
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
	
	public double function(PointNDimension point) throws CanNotPerformCalculations {
		  double x = 0.0;
		  double y = 0.0;
		 
		  
		  try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
					
		  } catch (Exception e) {
			  
		  }
		  if (x==0) {
			  throw new CanNotPerformCalculations();
		  }
		// return x*x*x + y*y - 4;
		  //return Math.log(x) - y;
		 return (1/x) - y;
		//return x*x+y*y-16;
	}
}
