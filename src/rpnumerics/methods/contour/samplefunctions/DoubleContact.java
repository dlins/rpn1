package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.BifurcationFluxFunctionCluster;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.MultiBifurcationFunction;
import wave.util.exceptions.DimensionOutOfBounds;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class DoubleContact extends MultiBifurcationFunction {

	public DoubleContact(HugoniotParams params,
			BifurcationFluxFunctionCluster[] componentFunctions)
			throws DimensionOutOfBounds {
		super(4, params, componentFunctions);
	}

	protected RealVector calcMultiBifurcationValue(
			RealVector minusCoordinates, RealVector plusCoordinates,
			RealVector[] minusValue, RealVector[] plusValue,
			RealMatrix2[] minusDeriv, RealMatrix2[] plusDeriv)
			throws CanNotPerformCalculations {
		// F -> 0
		// G -> 1
		
		double[] mCoord = minusCoordinates.toDouble();
		double[] pCoord = plusCoordinates.toDouble();
		
		double[] mValF = minusValue[0].toDouble();
		double[] pValF = plusValue[0].toDouble();
		
		double[][] mDerF = RealMatrix2.convert(minusDeriv[0]);
		double[][] pDerF = RealMatrix2.convert(plusDeriv[0]);
		
		double[] mValG = minusValue[1].toDouble();
		double[] pValG = plusValue[1].toDouble();
		
		double[][] mDerG = RealMatrix2.convert(minusDeriv[1]);
		double[][] pDerG = RealMatrix2.convert(plusDeriv[1]);
		
		double deltaG1 = mValG[0] - pValG[0];   
		double deltaG2 = mValG[1] - pValG[1];
		double deltaG3 = mValG[2] - pValG[2];
		
		double numSA = ((pValF[1]*mValF[0]) - (pValF[0]*mValF[1]));
		double demSA = ((pValF[0]*deltaG2)-(pValF[1]*deltaG1));
		
		double numSB = ((pValF[2]*mValF[1]) - (pValF[1]*mValF[2]));
		double demSB = ((pValF[1]*deltaG3)-(pValF[2]*deltaG2));
		
		double numSC = ((pValF[0]*mValF[2]) - (pValF[2]*mValF[0]));
		double demSC = ((pValF[2]*deltaG1)-(pValF[0]*deltaG3));
		
		double s = (((numSA * demSA) + (numSB * demSB) + (numSC * demSC)) / ((demSA * demSA) + (demSB * demSB) + (demSC * demSC)));
			
		double numUPlusA = ((mValF[0]*deltaG2) - (mValF[1]*deltaG1));
		double demUPlusA = ((pValF[0]*deltaG2)-(pValF[1]*deltaG1));
		
		double numUPlusB = ((mValF[1]*deltaG3) - (mValF[2]*deltaG2));
		double demUPlusB = ((pValF[1]*deltaG3)-(pValF[2]*deltaG2));
		
		double numUPlusC = ((mValF[2]*deltaG1) - (mValF[0]*deltaG3));
		double demUPlusC = ((pValF[2]*deltaG1)-(pValF[0]*deltaG3));
		
		
		double uPlus = (((numUPlusA * demUPlusA) + (numUPlusB * demUPlusB) + (numUPlusC * demUPlusC)) / ((demUPlusA * demUPlusA) + (demUPlusB * demUPlusB) + (demUPlusC * demUPlusC)));
		
			
		return null;
	}

	protected RealMatrix2 calcMultiBifurcationJacobian(
			RealVector minusCoordinates, RealVector plusCoordinates,
			RealVector[] minusValue, RealVector[] plusValue,
			RealMatrix2[] minusDeriv, RealMatrix2[] plusDeriv,
			HessianMatrix[] minusHessian, HessianMatrix[] plusHessian)
			throws CanNotPerformCalculations {
		return null;
	}

}
