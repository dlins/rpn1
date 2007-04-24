package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDFunction4D_Parametric extends MDVectorFunction {

	public MDFunction4D_Parametric(FluxFunction fluxFunction, HugoniotParams params) {
		super(2, 1, fluxFunction, params);
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
		
		v[0] = x - y;
		
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
		
		v[0][0] = 1;
		v[0][1] = -1;
						
		return RealMatrix2.convert(1, 2, v);
	}

}
