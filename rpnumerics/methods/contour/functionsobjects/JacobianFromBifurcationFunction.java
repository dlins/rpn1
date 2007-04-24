package rpnumerics.methods.contour.functionsobjects;

import wave.util.*;

public class JacobianFromBifurcationFunction extends MatrixFunction {

	private MDVectorFunction function;
	
	public JacobianFromBifurcationFunction(int dimension, MDVectorFunction function) {
		super(dimension, dimension, dimension, null, null);
		this.function = function;
	}

	protected RealMatrix2 getValueArray(PointNDimension point) throws CanNotPerformCalculations {
		return this.function.deriv(point);
	}

	protected RealMatrix2 getDerivArray(PointNDimension point) throws CanNotPerformCalculations {
		return null;
	}

}
