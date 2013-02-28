package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDFunction2D_1 extends MDVectorFunction {

	public MDFunction2D_1(FluxFunction fluxFunction,
			HugoniotParams params) {
		super(2, 1, fluxFunction, params);
		// TODO Auto-generated constructor stub
	}
	
	public RealVector value(PointNDimension point) {
		
		double x = 0.0;
		double y = 0.0;


		try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
					
		} catch (Exception e) {
			  
		}

		double[] v = new double[1];
		
		//v[0]=  Math.pow(x,11) + x*x + x - y*y;
		//v[0] = (x*x/9) - (y*y/25) - 1;
		//v[0]= x - y;
		
		
		//v[0]= x*x*x - 2*x*x + 3 + y;
//		v[0]= Math.log(x) - y;
		v[0]= x*x+y*y-16;
		//v[0] = x*x*x + y*y - 4;
		//v[0] = (x-0.012) * (y + 0.21);
//		v[0]= (1/x) - y;
		
		return new RealVector(v);
		
	}
	
	public RealMatrix2 deriv(PointNDimension point) {		
		double x = 0.0;
		double y = 0.0;
		
		try {
			x = point.getCoordinate(1);
			y = point.getCoordinate(2);
								
		} catch (Exception e) {
			  
		}

		double[][] v = new double[1][2];
		
		//v[0][0] = 3*x*x - 4*x;
		//v[0][1] =  1;
		
		//v[0][0] = 3*x*x;
		//v[0][1] = 2*y;
		
		v[0][0] = y + 0.21;
		v[0][1] = x - 0.012;
		
		//v[0][0] = 11 * Math.pow(x,10) + 2 * x + 1;
		//v[0][1] = -2*y;
		
		//v[0][0] = 2*x;
		//v[0][1] = 2*y;
		
		//v[0][0] = 1;		
		//v[0][0] = 2*x; 
		
		
		//v[0][1] = -1;		
						
		return RealMatrix2.convert(1, 2, v);
	}

}


