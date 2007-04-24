package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDFunction6D_1 extends MDVectorFunction {

	public MDFunction6D_1(FluxFunction fluxFunction,
			HugoniotParams params) {
		super(6, 5, fluxFunction, params);
		// TODO Auto-generated constructor stub
	}

	public RealVector value(PointNDimension point) {
		
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		double w = 0.0;
		double v = 0.0;
		double u = 0.0;


		try {  
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
			w = point.getCoordinate(4);
			v = point.getCoordinate(5);
			u = point.getCoordinate(6);
		} catch (Exception e) {
			  
		}

		double[] f = new double[5];
		
		//v[0]= x-y+z;
		//v[0] = x*x*x + y*y + Math.pow(z, 4) - 5;
		//v[0] = x*x + y*y + Math.pow(z, 2) - 16;
		//v[1]= x-(2*z);
		f[0] = x- y;
		
		f[1] = x*x + y*y + z*z + w*w- 16;
		f[2] = z - w;
		
//		v[0]= Math.log(x) - y;
//		v[0]= x*x+y*y-16;
//		v[0]= (1/x) - y;
		
		return new RealVector(f);
		
	}
	
	public RealMatrix2 deriv(PointNDimension point) {		

		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		double w = 0.0;
		double v = 0.0;
		double u = 0.0;


		try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
			w = point.getCoordinate(4);
			v = point.getCoordinate(5);
			u = point.getCoordinate(6);
		} catch (Exception e) {
			  
		}

		double[][] J = new double[5][6];
		
		J[0][0] = 1;
		J[0][1] = -1;
		J[0][2] = 0;
		J[0][3] = 0;
		
		J[1][0] = 2*x;
		J[1][1] = 2*y;
		J[1][2] = 2*z;
		J[1][3] = 2*w;
		
		J[2][0] = 0;
		J[2][1] = 0;
		J[2][2] = 1;
		J[2][3] = -1;
						
		return RealMatrix2.convert(5, 6, J);
	}
}
