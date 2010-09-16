package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealVector;

public class MDFunction3D extends MDVectorFunction {

	public MDFunction3D(FluxFunction fluxFunction,
			HugoniotParams params) {
		super(3, 2, fluxFunction, params);
		// TODO Auto-generated constructor stub
	}
	
	public RealVector value(PointNDimension point) {
		
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;


		try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
					
		} catch (Exception e) {
			  
		}

		double[] v = new double[2];
		
		v[0]= x*x + y*y + z*z - 16;
		v[1]= x-z;
		
		//v[0] = x*x*x - 5*x*x - 6 + y + z;
//		v[0]= Math.log(x) - y;
//		v[0]= x*x+y*y-16;
//		v[0]= (1/x) - y;
		
		return new RealVector(v);
		
	}

}
