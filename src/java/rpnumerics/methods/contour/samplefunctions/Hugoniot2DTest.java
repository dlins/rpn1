package rpnumerics.methods.contour.samplefunctions;

import rpnumerics.methods.contour.functionsobjects.*;
import rpnumerics.methods.contour.functionsobjects.HugoniotFunction;
import rpnumerics.methods.contour.functionsobjects.InvalidHugoniotFunctionsRelation;
import wave.util.RealMatrix2;
import wave.util.RealVector;
import rpnumerics.*;

public class Hugoniot2DTest extends HugoniotFunction {

	public Hugoniot2DTest(FluxFunction fluxFunction,
			HugoniotParams params) throws InvalidHugoniotFunctionsRelation {
		super(2, fluxFunction, params);
	}

	protected RealVector calcHugoniotValue(RealVector initialPoint, 
										   RealVector fluxAtInitialPoint, 
										   RealVector pointCoordinates, 
										   RealVector fluxAtCoordinates) throws CanNotPerformCalculations {
		
		double[] initialPointArray = initialPoint.toDouble();
		double[] fluxAtInitialPointArray = fluxAtInitialPoint.toDouble();
		double[] pointCoordinatesArray = pointCoordinates.toDouble();
		double[] fluxAtCoordinatesArray = fluxAtCoordinates.toDouble();
		
		double[] vector = new double[1];		
		
		vector[0] = (((fluxAtInitialPointArray[0] - fluxAtCoordinatesArray[0])*(initialPointArray[1] - pointCoordinatesArray[1])) - 
				     ((fluxAtInitialPointArray[1] - fluxAtCoordinatesArray[1])*(initialPointArray[0] - pointCoordinatesArray[0])));
		
		return new RealVector(vector);
	}
	
	protected RealMatrix2 calcHugoniotDeriv(RealVector initialPoint, 
											RealVector fluxAtInitialPoint, 
											RealMatrix2 jacobianOfFluxAtInitialPoint, 
											RealVector pointCoordinates, 
											RealVector fluxAtCoordinates, 
											RealMatrix2 jacobianOfFlux) throws CanNotPerformCalculations {	
		double[] initialPointArray = initialPoint.toDouble();
		double[] fluxAtInitialPointArray = fluxAtInitialPoint.toDouble();
		double[][] ArrayOfjacobianOfFluxAtInitialPoint = RealMatrix2.convert(jacobianOfFluxAtInitialPoint);
		double[] pointCoordinatesArray = pointCoordinates.toDouble();
		double[] fluxAtCoordinatesArray = fluxAtCoordinates.toDouble();
		double[][] ArrayOfJacobianOfFlux = RealMatrix2.convert(jacobianOfFlux);
		
		double[][] res = new double[1][2];
		
		res[0][0] = - (ArrayOfJacobianOfFlux[0][0] * (initialPointArray[1] - pointCoordinatesArray[1])) 
				    + (ArrayOfJacobianOfFlux[1][0] * (initialPointArray[0] - pointCoordinatesArray[0])) 
				    + (fluxAtInitialPointArray[1] - fluxAtCoordinatesArray[1]);
		
		res[0][1] = - (ArrayOfJacobianOfFlux[0][1] * (initialPointArray[1] - pointCoordinatesArray[1])) 
				    + (ArrayOfJacobianOfFlux[1][1] * (initialPointArray[0] - pointCoordinatesArray[0])) 
				    - (fluxAtInitialPointArray[0] - fluxAtCoordinatesArray[0]);
		
		return RealMatrix2.convert(1, 2, res);
	}

	
	
}
