package rpnumerics.methods.contour.markedhypercubes;

import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.util.PointNDimension;
import wave.util.RealVector;

public class BoundedPointNDimension extends PointNDimension {

	private double tolerance = 0.0;
	
	public BoundedPointNDimension(double error) {
		super();
		this.tolerance = error;
	}

	public BoundedPointNDimension(double[] coords, double error) {
		super(coords);
		this.tolerance = error;
	}

	public BoundedPointNDimension(RealVector coords, double error) {
		super(coords);
		this.tolerance = error;
	}

	public BoundedPointNDimension(Space space, double error) {
		super(space);
		this.tolerance = error;
	}

	public BoundedPointNDimension(CoordsArray coords, double error) {
		super(coords);
		this.tolerance = error;
	}

	public BoundedPointNDimension(int dimension, double error) {
		super(dimension);
		this.tolerance = error;
	}
	
	public BoundedPointNDimension(PointNDimension point, double error) {
		super(point.toRealVector());
		this.tolerance = error;
	}
	
	public boolean equals(Object operand) {
		
		PointNDimension evaluated = (PointNDimension) operand;
		
		if (this.myDimensionIs() == evaluated.myDimensionIs()) {
			
			double sum = 0.0;
	      for (int pointer = 1; pointer <= this.myDimensionIs(); pointer++) {
	    	  try {
	    	  	sum += Math.abs(this.getCoordinate(pointer) - evaluated.getCoordinate(pointer));
	      	  } catch (Exception e) {
	      		  
	      	  }	      	   
	      }
	      
	      if (sum <= tolerance) {
	    	  return true;
	      } else {
	    	  return false;
	      }
		} else {
			return false;
	    }
		
	}
		
}
