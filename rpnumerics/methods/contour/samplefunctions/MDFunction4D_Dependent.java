package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDFunction4D_Dependent extends MDVectorFunction {

	public MDFunction4D_Dependent(FluxFunction fluxFunction, HugoniotParams params) {
		super(3, 2, fluxFunction, params);
	}
	
	public RealVector value(PointNDimension point) {
		
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

		double[] v = new double[2];
				
		v[0] = x*x + y*y + z*z + w*w - 16;		
		v[1] = x + y + z + w;
		
		return new RealVector(v);
		
	}
	
	public RealMatrix2 deriv(PointNDimension point) {		

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

		double[][] v = new double[3][4];
				
		v[0][0] = 2*x;
		v[0][1] = 2*y;
		v[0][2] = 2*z;
		v[0][3] = 2*w;
		
		v[1][0] = 1;
		v[1][1] = 1;
		v[1][2] = 1;
		v[1][3] = 1;
												
		return RealMatrix2.convert(3, 4, v);
	}

}
