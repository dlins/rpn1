package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDFunction3D_1 extends MDVectorFunction {

	public MDFunction3D_1(FluxFunction fluxFunction,
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
		
		//v[0]= x-y+z;
		//v[0] = x*x*x + y*y + Math.pow(z, 4) - 5;
		//v[0] = x*x + y*y + Math.pow(z, 2) - 16;
		//v[1]= x-(2*z);
		v[0] = x- z;
		
		v[1] = x*x + y*y + z*z - 16;
//		v[0]= Math.log(x) - y;
//		v[0]= x*x+y*y-16;
//		v[0]= (1/x) - y;
		
		return new RealVector(v);
		
	}
	
	public RealMatrix2 deriv(PointNDimension point) {		
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		
		try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
								
		} catch (Exception e) {
			  
		}

		double[][] v = new double[2][3];
		
		v[0][0] = 1;
		v[0][1] = 0;
		v[0][2] = -1;
		
		//v[0][0] = 3*x*x;
		//v[0][1] = 2*y;
		//v[0][2] = 4*z*z*z;
		
		v[1][0] = 2*x;
		v[1][1] = 2*y;
		v[1][2] = 2*z;
						
		return RealMatrix2.convert(2, 3, v);
	}
}
