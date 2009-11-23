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
			     BifurcationFluxFunctionCluster[] componentFunctions) throws DimensionOutOfBounds {
		super(4, params, componentFunctions);
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
				
		// vector[0] = plusValueG[0] - minusValueF[0];

                double[] minus = minusCoordinates[0].toDouble();
                double x = minus[0];
		double y = minus[1];

                double[] plus = plusCoordinates[0].toDouble();
                double z = plus[0];
                double w = plus[1];
                
/*                vector[0] = x*x + y*y + z*z + w*w;
                vector[1] = x + y + z + w;
                vector[2] = x - y;*/
		
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
		double[] vector = new double[3];

		/*double[] minusValueF = minusValue[0].toDouble();
		double[] minusValueG = minusValue[1].toDouble();

		double[] plusValueF = plusValue[0].toDouble();
		double[] plusValueG = plusValue[1].toDouble();*/

		// vector[0] = plusValueG[0] - minusValueF[0];

                /*double[] minus = minusCoordinates.toDouble();
                double x = minus[0];
		double y = minus[1];

                double[] plus = plusCoordinates.toDouble();
                double z = plus[0];
                double w = plus[1];
                
                vector[0] = x*x + y*y + z*z + w*w - 0.04;
                vector[1] = x - 2*w;
                vector[2] = y - 2*z;*/

               /* v[0]= x*x + y*y + z*z + w*w - 16;
		v[1]= x - w;
		v[2]= y - z;*/

		//return new RealVector(vector);

                double[] pointOnM = minusCoordinates.toDouble();
                double[] pointOnP = plusCoordinates.toDouble();

		double[] fluxOnM = minusValue[0].toDouble();
		double[] fluxOnP = plusValue[0].toDouble();        

		double[][] jacOnM = RealMatrix2.convert(minusDeriv[0]);
		double[][] jacOnP = RealMatrix2.convert(plusDeriv[0]);

                /*double x = fluxOnM[0];
                double y = fluxOnM[1];                
                double z = fluxOnP[0];
                double w = fluxOnP[1];*/

                double x = pointOnM[0];
                double y = pointOnM[1];
                double z = pointOnP[0];
                double w = pointOnP[1];

                /*double x = jacOnM[0][0];
                double y = jacOnM[0][1];
                double z = jacOnP[1][0];
                double w = jacOnP[1][1];*/

                vector[0] = x*x + y*y + z*z + w*w - 0.0004;
                vector[1] = x - 2*w;
                vector[2] = y - 2*z;

                /*vector[0] = x*x + y*y + z*z + w*w - 16;
                vector[1] = x - 2*y;
                vector[2] = w - 6*z;*/

		/*double numer = (((fluxOnM[0] - fluxOnP[0])*(pointOnM[0] - pointOnP[0])) +
			 	    	((fluxOnM[1] - fluxOnP[1])*(pointOnM[1] - pointOnP[1])));

		double nomin = (((pointOnM[0] - pointOnP[0]) * (pointOnM[0] - pointOnP[0]))
			 	  		+ ((pointOnM[1] - pointOnP[1]) * (pointOnM[1] - pointOnP[1])));

		if (Math.abs(nomin) < 0.001) {

                    /*vector[0] = 3;
                    vector[1] = 1;
                    vector[2] = 2; */
                    
/*			 throw new CanNotPerformCalculations() ;
		}

		double s =  numer / nomin;

		vector[0] = (((fluxOnM[0] - fluxOnP[0])*(pointOnM[1] - pointOnP[1])) -
				 	 ((fluxOnM[1] - fluxOnP[1])*(pointOnM[0] - pointOnP[0])));

		vector[1] = ((jacOnM[0][0] - s) * (jacOnM[1][1] - s))
				   - (jacOnM[1][0] * jacOnM[0][1]);
		vector[2] = ((jacOnP[0][0] - s) * (jacOnP[1][1] - s))
				   - (jacOnP[1][0] * jacOnP[0][1]); 

*/


		return new RealVector(vector);
		
	}
	
	

}
