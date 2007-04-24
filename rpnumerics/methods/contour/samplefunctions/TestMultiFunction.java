package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.BifurcationFluxFunctionCluster;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.MultiBifurcationFunction;
import rpnumerics.physics.FluxFunction;
import wave.exceptions.DimensionOutOfBounds;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class TestMultiFunction extends MultiBifurcationFunction {
	
	public TestMultiFunction(HugoniotParams params,
							 BifurcationFluxFunctionCluster[] componentFunctions)
													throws DimensionOutOfBounds {
		super(2, params, componentFunctions);
	}

	protected RealVector calcMultiBifurcationValue(
			RealVector[] minusCoordinates, RealVector[] plusCoordinates,
			RealVector[] minusValue, RealVector[] plusValue,
			RealMatrix2[] minusDeriv, RealMatrix2[] plusDeriv)
			throws CanNotPerformCalculations {
		
		double[] vector = new double[1];
		
		double[] minusValueF = minusValue[0].toDouble();
		double[] minusValueG = minusValue[1].toDouble();
		
		double[] plusValueF = plusValue[0].toDouble();
		double[] plusValueG = plusValue[1].toDouble();
				
		vector[0] = plusValueG[0] - minusValueF[0];
		
		return new RealVector(vector);
	}

	protected RealMatrix2 calcMultiBifurcationJacobian(
			RealVector[] minusCoordinates, RealVector[] plusCoordinates,
			RealVector[] minusValue, RealVector[] plusValue,
			RealMatrix2[] minusDeriv, RealMatrix2[] plusDeriv,
			HessianMatrix[] minusHessian, HessianMatrix[] plusHessian)
			throws CanNotPerformCalculations {
		return null;
	}

	protected RealMatrix2 calcMultiBifurcationJacobian(RealVector minusCoordinates, RealVector plusCoordinates, RealVector[] minusValue, RealVector[] plusValue, RealMatrix2[] minusDeriv, RealMatrix2[] plusDeriv, HessianMatrix[] minusHessian, HessianMatrix[] plusHessian) throws CanNotPerformCalculations {
		// TODO Auto-generated method stub
		return null;
	}

	protected RealVector calcMultiBifurcationValue(RealVector minusCoordinates, RealVector plusCoordinates, RealVector[] minusValue, RealVector[] plusValue, RealMatrix2[] minusDeriv, RealMatrix2[] plusDeriv) throws CanNotPerformCalculations {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
