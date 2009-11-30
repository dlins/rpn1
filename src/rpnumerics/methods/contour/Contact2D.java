package rpnumerics.methods.contour;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.functionsobjects.BifurcationFunction;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.FluxFunction;
import wave.util.exceptions.DimensionOutOfBounds;
import wave.util.HessianMatrix;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public class Contact2D extends BifurcationFunction {

	public Contact2D(FluxFunction fluxFunction,
			HugoniotParams params) throws DimensionOutOfBounds {
		super(4, fluxFunction, params);
		
		
	}

	protected RealVector calcBifurcationValue(
			RealVector pointCoordinatesOnMinusFunction,
			RealVector pointCoordinatesOnPlusFunction,
			RealVector fluxAtCoordinatesOnMinusFunction,
			RealVector fluxAtCoordinatesOnPlusFunction,
			RealMatrix2 jacobianOfFluxOnMinusFunction,
			RealMatrix2 jacobianOfFluxOnPlusFunction) throws CanNotPerformCalculations {
		
		double[] pointOnM = pointCoordinatesOnMinusFunction.toDouble();
		double[] fluxOnM = fluxAtCoordinatesOnMinusFunction.toDouble();
		double[] pointOnP = pointCoordinatesOnPlusFunction.toDouble();
		double[] fluxOnP = fluxAtCoordinatesOnPlusFunction.toDouble();
		double[][] jacOnM = RealMatrix2.convert(jacobianOfFluxOnMinusFunction); 
		double[][] jacOnP = RealMatrix2.convert(jacobianOfFluxOnPlusFunction);
		
		double[] vector = new double[3];		
		
		double numer = (((fluxOnM[0] - fluxOnP[0])*(pointOnM[0] - pointOnP[0])) +  
			 	    	((fluxOnM[1] - fluxOnP[1])*(pointOnM[1] - pointOnP[1])));
		
		double nomin = (((pointOnM[0] - pointOnP[0]) * (pointOnM[0] - pointOnP[0])) 
			 	  		+ ((pointOnM[1] - pointOnP[1]) * (pointOnM[1] - pointOnP[1])));
		
		if (Math.abs(nomin) < 0.00001) {
			throw new CanNotPerformCalculations();
		}
					
		double s =  numer / nomin;
					
		vector[0] = (((fluxOnM[0] - fluxOnP[0])*(pointOnM[1] - pointOnP[1])) - 
				 	 ((fluxOnM[1] - fluxOnP[1])*(pointOnM[0] - pointOnP[0])));
		
		vector[1] = ((jacOnM[0][0] - s) * (jacOnM[1][1] - s))    
				   - (jacOnM[1][0] * jacOnM[0][1]);			
		vector[2] = ((jacOnP[0][0] - s) * (jacOnP[1][1] - s)) 
				   - (jacOnP[1][0] * jacOnP[0][1]); 
				
		return new RealVector(vector);
	}

	protected RealMatrix2 calcBifurcationJacobian(
			RealVector pointCoordinatesOnMinusFunction,
			RealVector pointCoordinatesOnPlusFunction,
			RealVector fluxAtCoordinatesOnMinusFunction,
			RealVector fluxAtCoordinatesOnPlusFunction,
			RealMatrix2 jacobianOfFluxOnMinusFunction,
			RealMatrix2 jacobianOfFluxOnPlusFunction,
			HessianMatrix hessianOfFluxOnPlusFunction,
			HessianMatrix hessianOfFluxOnMinusFunction) throws CanNotPerformCalculations {
		
		double[] pointOnM = pointCoordinatesOnMinusFunction.toDouble();
		double[] fluxOnM = fluxAtCoordinatesOnMinusFunction.toDouble();
		double[] pointOnP = pointCoordinatesOnPlusFunction.toDouble();
		double[] fluxOnP = fluxAtCoordinatesOnPlusFunction.toDouble();
		double[][] jacOnM = RealMatrix2.convert(jacobianOfFluxOnMinusFunction);
		double[][] jacOnP = RealMatrix2.convert(jacobianOfFluxOnPlusFunction);
		
		// fazer for para o HessianMatrix
		
		double[][][] hessianOnM = new double[2][2][2];
		double[][][] hessianOnP = new double[2][2][2];
		
		try {
			for(int pont_x = 0; pont_x < 2; pont_x++) {
				for(int pont_y = 0; pont_y < 2; pont_y++) {
					for(int pont_z = 0; pont_z < 2; pont_z++) {
						hessianOnM[pont_x][pont_y][pont_z] = hessianOfFluxOnMinusFunction.getElement(pont_x, pont_y, pont_z);
						hessianOnP[pont_x][pont_y][pont_z] = hessianOfFluxOnPlusFunction.getElement(pont_x, pont_y, pont_z);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double[][] res = new double[3][4];		
		double[] dnumer = new double[4];
		double[] dnomin = new double[4];
		double[] ds = new double[4];
		
		double numer, nomin;
				
		numer = ((fluxOnM[0] - fluxOnP[0])*(pointOnM[0] - pointOnP[0])) + 
 	            ((fluxOnM[1] - fluxOnP[1])*(pointOnM[1] - pointOnP[1]));
		
		nomin = ((pointOnM[0] - pointOnP[0]) * (pointOnM[0] - pointOnP[0]))  
	 	  + ((pointOnM[1] - pointOnP[1]) * (pointOnM[1] - pointOnP[1]));
			
		if (Math.abs(nomin) < 0.00001) {
			throw new CanNotPerformCalculations();
		}
		
		double s = (numer / nomin);	
				
		dnumer[0] = ((jacOnM[0][0]*(pointOnM[0] - pointOnP[0])) 
				  + (fluxOnM[0] - fluxOnP[0]));
		dnumer[1] = ((jacOnM[0][1]*(pointOnM[0] - pointOnP[0])) 
				    - (jacOnM[1][0]*(pointOnM[1] 
		            - pointOnP[1])) - (fluxOnM[1] - fluxOnP[1]));
		dnumer[2] = -(-fluxOnP[0]*(pointOnM[0] - pointOnP[0]) 
				  + (fluxOnM[0] - fluxOnP[0]));
		dnumer[3] =  ((- jacOnP[1][1])*(pointOnM[1] - pointOnP[1])) - (fluxOnM[1] - fluxOnP[1]);
				
		dnomin[0] = 2.*(pointOnM[0] - pointOnP[0]);
		dnomin[1] = 2.*(pointOnM[1] - pointOnP[1]);
		dnomin[2] = dnomin[0];
		dnomin[3] = dnomin[1];
				
		ds[0] = (( (dnumer[0]*nomin) - (dnomin[0]*numer) ) ) / (nomin * nomin);
		ds[1] = (( (dnumer[1]*nomin) - (dnomin[1]*numer) ) ) / (nomin * nomin);
		ds[2] = (( (dnumer[2]*nomin) - (dnomin[2]*numer) ) ) / (nomin * nomin);
		ds[3] = (( (dnumer[3]*nomin) - (dnomin[3]*numer) ) ) / (nomin * nomin);
			
		res[0][0] = (jacOnM[0][0])*(pointOnM[1] - pointOnP[1]) - (fluxOnM[1] - fluxOnP[1]);
		res[0][1] = (fluxOnM[0] - fluxOnP[0]) - (jacOnM[0][1]*(pointOnM[0] - pointOnP[0]));
		res[0][2] = (-jacOnP[1][0]*(fluxOnM[0] - fluxOnP[0])) + (pointOnM[0] - pointOnP[0]);
		res[0][3] = -(fluxOnM[0] - fluxOnP[0]) + (jacOnP[1][1]*(pointOnM[0] - pointOnP[0]));
			
		res[1][0] = (hessianOnM[0][0][0] - ds[0]) * (jacOnM[1][1] - s) + 
					(jacOnM[0][0] - s) * (hessianOnM[1][1][0] - ds[0]) - 
					((hessianOnM[1][0][0] * jacOnM[0][1]) + (jacOnM[1][0] * hessianOnM[0][1][0]));
		res[1][1] = (hessianOnM[0][0][1] - ds[1]) * (jacOnM[1][1] - s) + 
					(jacOnM[0][0] - s) * (hessianOnM[1][1][1] - ds[1]) - 
				    ((hessianOnM[1][0][1] * jacOnM[0][1]) + (jacOnM[1][0] * hessianOnM[0][1][1]));
		res[1][2] = ((- ds[2]) * (jacOnM[1][1] - s)) + ((jacOnM[0][0] - s) * (- ds[2]));
		res[1][3] = ((- ds[3]) * (jacOnM[1][1] - s)) + ((jacOnM[0][0] - s) * (- ds[3]));
			
		res[2][0] = ((- ds[0]) * (jacOnP[1][1] - s)) + 
					((jacOnP[0][0] - s) * (- ds[0]));
		res[2][1] = ((- ds[1]) * (jacOnP[1][1] - s)) + ((jacOnP[0][0] - s) * (- ds[1]));
		res[2][2] = ((hessianOnP[0][0][0] - ds[2]) * (jacOnP[1][1] - s)) + 
					((jacOnP[0][0] - s) * (hessianOnP[1][1][0] - ds[2]));	
		res[2][3] = ((hessianOnP[0][0][1] - ds[3]) * (jacOnP[1][1] - s)) + 
					((jacOnP[0][0] - s) * (hessianOnP[1][1][1] - ds[3]));		
				
		return RealMatrix2.convert(3, 4, res);
	
	}
}
