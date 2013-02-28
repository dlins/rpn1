package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDFunction7D_1 extends MDVectorFunction {

	public MDFunction7D_1(FluxFunction fluxFunction,
			HugoniotParams params) {
		super(7, 6, fluxFunction, params);
		// TODO Auto-generated constructor stub
	}

	public RealVector value(PointNDimension point) {
		
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		double w = 0.0;
		double v = 0.0;
		double u = 0.0;
		double t = 0.0;


		try {  
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
			w = point.getCoordinate(4);
			v = point.getCoordinate(5);
			u = point.getCoordinate(6);
			t = point.getCoordinate(7);
		} catch (Exception e) {
			  
		}

		double[] f = new double[6];
		
		f[0] = x*x + y*y + z*z + w*w + v*v + u*u + t*t - 64;		
		f[1] = x- y;
		f[2] = z - w;
		f[3] = x - v;
		f[4] = u - t;
		f[5] = t - v;
		
		return new RealVector(f);
		
	}
	
	public RealMatrix2 deriv(PointNDimension point) {		

		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		double w = 0.0;
		double v = 0.0;
		double u = 0.0;
		double t = 0.0;


		try {  
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
			z = point.getCoordinate(3);
			w = point.getCoordinate(4);
			v = point.getCoordinate(5);
			u = point.getCoordinate(6);
			t = point.getCoordinate(7);
		} catch (Exception e) {
			  
		}

		double[][] J = new double[6][7];
		
		J[0][0] = 2*x;
		J[0][1] = 2*y;
		J[0][2] = 2*z;
		J[0][3] = 2*w;
		J[0][4] = 2*v;
		J[0][5] = 2*u;
		J[0][6] = 2*t;
		
		J[1][0] = 1;
		J[1][1] = -1;
		J[1][2] = 0;
		J[1][3] = 0;
		J[1][4] = 0;
		J[1][5] = 0;
		J[1][6] = 0;
		
		J[2][0] = 0;
		J[2][1] = 0;
		J[2][2] = 1;
		J[2][3] = -1;
		J[2][4] = 0;
		J[2][5] = 0;
		J[2][6] = 0;
		
		J[3][0] = 1;
		J[3][1] = 0;
		J[3][2] = 0;
		J[3][3] = 0;
		J[3][4] = -1;
		J[3][5] = 0;
		J[3][6] = 0;
		
		J[4][0] = 0;
		J[4][1] = 0;
		J[4][2] = 0;
		J[4][3] = 0;
		J[4][4] = 0;
		J[4][5] = 1;
		J[4][6] = -1;
		
		J[5][0] = 0;
		J[5][1] = 0;
		J[5][2] = 0;
		J[5][3] = 0;
		J[5][4] = -1;
		J[5][5] = 0;
		J[5][6] = 1;
						
		return RealMatrix2.convert(6, 7, J);
	}
}
