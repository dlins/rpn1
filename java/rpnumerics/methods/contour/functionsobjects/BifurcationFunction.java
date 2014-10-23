package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.FunctionParameters;
import rpnumerics.FluxFunction;
import wave.util.exceptions.*;
import wave.util.*;

public abstract class BifurcationFunction extends MDCompositeFunction {
	
	private BifurcationFluxFunction minusFunction = null;
	private BifurcationFluxFunction plusFunction = null;
	
	public BifurcationFunction(int dimension, FluxFunction fluxFunction,
			HugoniotParams params) throws DimensionOutOfBounds {
		super(dimension, ((dimension) - 1), fluxFunction, params, FromGridToBifurcationFunction(dimension, fluxFunction, params));
		
		GridFunction[] tmp = null;
		
		tmp = this.getMinusFunctions();		
		minusFunction = (BifurcationFluxFunction) tmp[0];
		
		tmp = this.getPlusFunctions();
		plusFunction = (BifurcationFluxFunction) tmp[0];
	}
	
	private static BifurcationFluxFunction[] FromGridToBifurcationFunction(int dimensionOfFunction,
																		   FluxFunction fluxFunction,
																		   HugoniotParams params) throws DimensionOutOfBounds {
		
		if ((dimensionOfFunction % 2) != 0) {
			throw new DimensionOutOfBounds();
		}
		
		BifurcationFluxFunction[] functions = new BifurcationFluxFunction[1];
		
		int bifurcationFunctionDimension = (dimensionOfFunction / 2);
		functions[0] = new BifurcationFluxFunction(bifurcationFunctionDimension, 
												   bifurcationFunctionDimension, 
												   fluxFunction, 
												   params);
		return functions;
	}
	
	public RealVector calcValue(FunctionParameters point) throws CanNotPerformCalculations {
		return calcBifurcationValue(minusFunction.getPointCoordinates(this.getMinusIndex(point)), 
										plusFunction.getPointCoordinates(this.getPlusIndex(point)), 
										minusFunction.value(this.getMinusIndex(point)), 
										plusFunction.value(this.getPlusIndex(point)),
										minusFunction.deriv(this.getMinusIndex(point)), 
										plusFunction.deriv(this.getPlusIndex(point)));
				
	}

	public RealVector calcValue(PointNDimension point) throws CanNotPerformCalculations {
		return calcBifurcationValue(this.getMinusIndex(point).toRealVector(), 
									this.getPlusIndex(point).toRealVector(), 
									minusFunction.value(this.getMinusIndex(point)), 
									plusFunction.value(this.getPlusIndex(point)),
								    minusFunction.deriv(this.getMinusIndex(point)), 
								    plusFunction.deriv(this.getPlusIndex(point)));
	}
	
	public RealMatrix2 deriv(PointNDimension point) throws CanNotPerformCalculations {		
		return calcBifurcationJacobian(this.getMinusIndex(point).toRealVector(), 
									   this.getPlusIndex(point).toRealVector(),  
									   minusFunction.value(this.getMinusIndex(point)), 
									   plusFunction.value(this.getPlusIndex(point)),
									   minusFunction.deriv(this.getMinusIndex(point)), 
									   plusFunction.deriv(this.getPlusIndex(point)),
									   minusFunction.deriv2(this.getMinusIndex(point)),
									   plusFunction.deriv2(this.getPlusIndex(point)));
	}
	
	public RealMatrix2 deriv(FunctionParameters point) throws CanNotPerformCalculations {
		
		return calcBifurcationJacobian(minusFunction.getPointCoordinates(this.getMinusIndex(point)), 
										   plusFunction.getPointCoordinates(this.getPlusIndex(point)),   
										   minusFunction.value(this.getMinusIndex(point)), 
										   plusFunction.value(this.getPlusIndex(point)),
										   minusFunction.deriv(this.getMinusIndex(point)), 
										   plusFunction.deriv(this.getPlusIndex(point)),
										   null, // provisorio
										   null); // provisorio
				
	}

	protected abstract RealVector calcBifurcationValue(RealVector pointCoordinatesOnMinusFunction,
													   RealVector pointCoordinatesOnPlusFunction,
													   RealVector fluxAtCoordinatesOnMinusFunction, 
													   RealVector fluxAtCoordinatesOnPlusFunction,
													   RealMatrix2 jacobianOfFluxOnMinusFunction,
													   RealMatrix2 jacobianOfFluxOnPlusFunction) throws CanNotPerformCalculations;
	
	protected abstract RealMatrix2 calcBifurcationJacobian(RealVector pointCoordinatesOnMinusFunction,
			   											   RealVector pointCoordinatesOnPlusFunction, 
														   RealVector fluxAtCoordinatesOnMinusFunction, 
														   RealVector fluxAtCoordinatesOnPlusFunction, 
														   RealMatrix2 jacobianOfFluxOnMinusFunction, 
														   RealMatrix2 jacobianOfFluxOnPlusFunction,
														   HessianMatrix hessianOfFluxOnPlusFunction,
														   HessianMatrix hessianOfFluxOnMinusFunction) throws CanNotPerformCalculations;
		
}
