package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class MDNativeGeneralDimensionFunction extends MDVectorFunction {

	static {
		System.load("/impa/home/g/cbevilac/RP/Contour/Java/native/interfaceJNIContour/libContourFunctions.so");
	}
	
	private int dimension;
	
	public MDNativeGeneralDimensionFunction(int dimension, 
			FluxFunction fluxFunction, HugoniotParams params) {
		super(dimension, (dimension - 1), fluxFunction, params);
		this.dimension = dimension;
	}

	private native double[] interfaceNativeValue(double[] args, int dimension);
	private native double[][] interfaceNativeDeriv(double[] args, int dimension);
	
	public RealVector value(PointNDimension point) {
		
		double[] args = new double[dimension];

		try {
			for (int pont_arg = 0; pont_arg < dimension; pont_arg++) {
				args[pont_arg] = point.getCoordinate(pont_arg + 1);
			}					
		} catch (Exception e) {
			  
		}

		double[] v = interfaceNativeValue(args, dimension);
			
		return new RealVector(v);
		
	}
	
	public RealMatrix2 deriv(PointNDimension point) {		
		
		double[] args = new double[dimension];

		try {
			for (int pont_arg = 0; pont_arg < dimension; pont_arg++) {
				args[pont_arg] = point.getCoordinate(pont_arg + 1);
			}					
		} catch (Exception e) {
			  
		}

		double[][] v = interfaceNativeDeriv(args, dimension);
				
		return RealMatrix2.convert((dimension - 1), dimension, v);
	}
}
