package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.methods.contour.FunctionParameters;
import rpnumerics.FluxFunction;
import wave.util.exceptions.DimensionOutOfBounds;
import wave.util.HessianMatrix;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public abstract class MultiBifurcationFunction extends  MDCompositeFunction {

	private BifurcationFluxFunctionCluster[] componentFunctions;
	private int numberOfFunctions;
	
	public MultiBifurcationFunction( int dimension, HugoniotParams params, BifurcationFluxFunctionCluster[] componentFunctions) throws DimensionOutOfBounds {
		super(dimension, (dimension - 1), getFluxFunction(componentFunctions), params, getDefaults(componentFunctions));
		
		this.componentFunctions = componentFunctions;
		this.numberOfFunctions = componentFunctions.length;		
	}
		
	private static GridFunction[] getDefaults(BifurcationFluxFunctionCluster[] componentFunctions) {
		
		GridFunction[] functionArray = new GridFunction[componentFunctions.length];
		
		for(int pont_function = 0; pont_function < componentFunctions.length; pont_function++) {
			functionArray[pont_function] = componentFunctions[pont_function].getFunctionAt(0);
		}
		
		return functionArray;
	}
	
	private static FluxFunction getFluxFunction(BifurcationFluxFunctionCluster[] componentFunctions) {		
		return (componentFunctions[0].getFunctionAt(0)).getFluxFunction();
	}
	
	protected void initFunctions(GridFunction[] functions) {
		// nothing to do		
	}
	
	public void setGridFunctions(int[][] pointers) throws CanNotPerformCalculations {
		
		int numberOfPointers = pointers.length;
		
		resetFunctionsList();
				
		if ( numberOfPointers != numberOfFunctions) {
			throw new CanNotPerformCalculations();
		}
		
		for(int pont_pointer = 0; pont_pointer < numberOfPointers; pont_pointer++) {
			if(pointers[pont_pointer].length != 2) {
				throw new CanNotPerformCalculations();
			}
			
			GridFunction componentMinusFunction = componentFunctions[pont_pointer].getFunctionAt(pointers[pont_pointer][0]);
			super.addMinusFunction(componentMinusFunction);
			
			GridFunction componentPlusFunction = componentFunctions[pont_pointer].getFunctionAt(pointers[pont_pointer][1]);
			super.addPlusFunction(componentPlusFunction);
			
		}
			
	}
	
	public RealVector calcValue(FunctionParameters point) throws CanNotPerformCalculations {
		
		GridFunction[] minusFunctions = this.getMinusFunctions();
		GridFunction[] plusFunctions = this.getPlusFunctions();			
				
		RealVector minusCoordinates = minusFunctions[0].getPointCoordinates(this.getMinusIndex(point));
		RealVector plusCoordinates = plusFunctions[0].getPointCoordinates(this.getPlusIndex(point));
		
		RealVector[] minusValue = new RealVector[numberOfFunctions];
		RealVector[] plusValue = new RealVector[numberOfFunctions];
		
		RealMatrix2[] minusDeriv = new RealMatrix2[numberOfFunctions];
		RealMatrix2[] plusDeriv = new RealMatrix2[numberOfFunctions];
				
		for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
			
			minusValue[pont_function] =  minusFunctions[pont_function].value(this.getMinusIndex(point));
			plusValue[pont_function] =  plusFunctions[pont_function].value(this.getPlusIndex(point));
			
			minusDeriv[pont_function] = ((BifurcationFluxFunction)  minusFunctions[pont_function]).deriv(this.getMinusIndex(point));
			plusDeriv[pont_function] =  ((BifurcationFluxFunction)  plusFunctions[pont_function]).deriv(this.getPlusIndex(point));
		}
				
		return calcMultiBifurcationValue(minusCoordinates, 
										 plusCoordinates, 
										 minusValue, 
										 plusValue,
										 minusDeriv, 
										 plusDeriv);
				
	}

	public RealVector calcValue(PointNDimension point) throws CanNotPerformCalculations {
		
		GridFunction[] minusFunctions = this.getMinusFunctions();
		GridFunction[] plusFunctions = this.getPlusFunctions();	
		
		RealVector minusCoordinates = (this.getMinusIndex(point)).toRealVector();
		RealVector plusCoordinates = (this.getPlusIndex(point)).toRealVector();
				
		RealVector[] minusValue = new RealVector[numberOfFunctions];
		RealVector[] plusValue = new RealVector[numberOfFunctions];
		
		RealMatrix2[] minusDeriv = new RealMatrix2[numberOfFunctions];
		RealMatrix2[] plusDeriv = new RealMatrix2[numberOfFunctions];
						
		for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
			
			minusValue[pont_function] =  minusFunctions[pont_function].value(this.getMinusIndex(point));
			plusValue[pont_function] =  plusFunctions[pont_function].value(this.getPlusIndex(point));
			
			minusDeriv[pont_function] = ((BifurcationFluxFunction)  minusFunctions[pont_function]).deriv(this.getMinusIndex(point));
			plusDeriv[pont_function] =  ((BifurcationFluxFunction)  plusFunctions[pont_function]).deriv(this.getPlusIndex(point));
		}
		
		return calcMultiBifurcationValue(minusCoordinates, 
										 plusCoordinates, 
										 minusValue, 
										 plusValue,
										 minusDeriv, 
										 plusDeriv);
	}
	
	public RealMatrix2 deriv(PointNDimension point) throws CanNotPerformCalculations {
		
		GridFunction[] minusFunctions = this.getMinusFunctions();
		GridFunction[] plusFunctions = this.getPlusFunctions();	
		
		RealVector minusCoordinates = (this.getMinusIndex(point)).toRealVector();
		RealVector plusCoordinates = (this.getPlusIndex(point)).toRealVector();
				
		RealVector[] minusValue = new RealVector[numberOfFunctions];
		RealVector[] plusValue = new RealVector[numberOfFunctions];
		
		RealMatrix2[] minusDeriv = new RealMatrix2[numberOfFunctions];
		RealMatrix2[] plusDeriv = new RealMatrix2[numberOfFunctions];
				
		HessianMatrix[] minusHessian = new HessianMatrix[numberOfFunctions];
		HessianMatrix[] plusHessian = new HessianMatrix[numberOfFunctions];
				
		for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {			
			minusValue[pont_function] =  minusFunctions[pont_function].value(this.getMinusIndex(point));
			plusValue[pont_function] =  plusFunctions[pont_function].value(this.getPlusIndex(point));
			
			minusDeriv[pont_function] = ((BifurcationFluxFunction)  minusFunctions[pont_function]).deriv(this.getMinusIndex(point));
			plusDeriv[pont_function] =  ((BifurcationFluxFunction)  plusFunctions[pont_function]).deriv(this.getPlusIndex(point));
			
			minusHessian[pont_function] = ((BifurcationFluxFunction)  minusFunctions[pont_function]).deriv2(this.getMinusIndex(point));
			plusHessian[pont_function] = ((BifurcationFluxFunction)  plusFunctions[pont_function]).deriv2(this.getPlusIndex(point));
		}
		
		return calcMultiBifurcationJacobian(minusCoordinates, 
											plusCoordinates, 
											minusValue, 
											plusValue,
											minusDeriv, 
											plusDeriv,
											minusHessian,
											plusHessian);
	}
	
	public RealMatrix2 deriv(FunctionParameters point) throws CanNotPerformCalculations {
		
		GridFunction[] minusFunctions = this.getMinusFunctions();
		GridFunction[] plusFunctions = this.getPlusFunctions();	
		
		RealVector minusCoordinates = minusFunctions[0].getPointCoordinates(this.getMinusIndex(point));
		RealVector plusCoordinates = plusFunctions[0].getPointCoordinates(this.getPlusIndex(point));
		
		RealVector[] minusValue = new RealVector[numberOfFunctions];
		RealVector[] plusValue = new RealVector[numberOfFunctions];
		
		RealMatrix2[] minusDeriv = new RealMatrix2[numberOfFunctions];
		RealMatrix2[] plusDeriv = new RealMatrix2[numberOfFunctions];
				
		HessianMatrix[] minusHessian = new HessianMatrix[numberOfFunctions];
		HessianMatrix[] plusHessian = new HessianMatrix[numberOfFunctions];
				
		for(int pont_function = 0; pont_function < numberOfFunctions; pont_function++) {
			
			minusValue[pont_function] =  minusFunctions[pont_function].value(this.getMinusIndex(point));
			plusValue[pont_function] =  plusFunctions[pont_function].value(this.getPlusIndex(point));
			
			minusDeriv[pont_function] = ((BifurcationFluxFunction)  minusFunctions[pont_function]).deriv(this.getMinusIndex(point));
			plusDeriv[pont_function] =  ((BifurcationFluxFunction)  plusFunctions[pont_function]).deriv(this.getPlusIndex(point));
			
			minusHessian[pont_function] = ((BifurcationFluxFunction)  minusFunctions[pont_function]).deriv2(this.getMinusIndex(point));
			plusHessian[pont_function] = ((BifurcationFluxFunction)  plusFunctions[pont_function]).deriv2(this.getPlusIndex(point));
		}
		
		return calcMultiBifurcationJacobian(minusCoordinates, 
											plusCoordinates, 
											minusValue, 
											plusValue,
											minusDeriv, 
											plusDeriv,
											minusHessian,
											plusHessian); 
	}

	protected abstract RealVector calcMultiBifurcationValue(RealVector minusCoordinates,
															RealVector plusCoordinates, 
													   		RealVector[] minusValue,
													   		RealVector[] plusValue,
													   		RealMatrix2[] minusDeriv,
													   		RealMatrix2[] plusDeriv) throws CanNotPerformCalculations;
	
	protected abstract RealMatrix2 calcMultiBifurcationJacobian(RealVector minusCoordinates,
															    RealVector plusCoordinates, 
													   		    RealVector[] minusValue,
													   		    RealVector[] plusValue,
													   		    RealMatrix2[] minusDeriv,
													   		    RealMatrix2[] plusDeriv,
													   		    HessianMatrix[] minusHessian,
													   		    HessianMatrix[] plusHessian) throws CanNotPerformCalculations;
		
}
