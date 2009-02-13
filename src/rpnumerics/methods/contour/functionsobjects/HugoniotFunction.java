package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.*;
import wave.util.*;
import rpnumerics.methods.contour.*;

public abstract class HugoniotFunction extends MDVectorFunction {
	
	private PointNDimension initialPoint;
	private RealVector solutionAtInitialPoint;
	private RealMatrix2 derivAtInitialPoint;
	private HugoniotFluxGridFunction componentFunction;
		
	public HugoniotFunction(int dimension, FluxFunction fluxFunction,
			HugoniotParams params) throws InvalidHugoniotFunctionsRelation{
		super(dimension, (dimension - 1), fluxFunction, params);						
		componentFunction = new HugoniotFluxGridFunction(dimension, dimension, fluxFunction, params) ;	
	}
	
	public RealVector value(FunctionParameters point) throws CanNotPerformCalculations {		
		try {
			return calcHugoniotValue(this.getInitialPoint().toRealVector(), solutionAtInitialPoint, this.componentFunction.getPointCoordinates(point), this.componentFunction.value(point));
		} catch (CanNotPerformCalculations e) {
			return null;
		}
	} 
	
	public RealVector value(PointNDimension point) throws CanNotPerformCalculations {
		return calcHugoniotValue(this.getInitialPoint().toRealVector(), solutionAtInitialPoint, point.toRealVector(), componentFunction.value(point));
	}

	public RealMatrix2 deriv(PointNDimension point) throws CanNotPerformCalculations {		
		return calcHugoniotDeriv(this.getInitialPoint().toRealVector(), solutionAtInitialPoint,this.derivAtInitialPoint, point.toRealVector(), componentFunction.value(point), componentFunction.deriv(point));
	}
	
	public void setInitialPoint(PointNDimension initialPoint) throws CanNotPerformCalculations {
		this.initialPoint = initialPoint;
		this.solutionAtInitialPoint = componentFunction.value(initialPoint);
		this.derivAtInitialPoint = componentFunction.deriv(initialPoint);
	}
	
	public PointNDimension getInitialPoint() {
		return this.initialPoint;
	}
	
	public  HugoniotFluxGridFunction getComponentFunction() {
		return componentFunction;
	}

	protected abstract RealVector calcHugoniotValue(RealVector initialPoint, 
													RealVector fluxAtInitialPoint, 
													RealVector pointCoordinates, 
													RealVector fluxAtCoordinates) throws CanNotPerformCalculations;
	
	protected abstract RealMatrix2 calcHugoniotDeriv(RealVector initialPoint, 
													 RealVector fluxAtInitialPoint, 
													 RealMatrix2 jacobianOfFluxAtInitialPoint, 
													 RealVector pointCoordinates, 
													 RealVector fluxAtCoordinates, 
													 RealMatrix2 jacobianOfFlux) throws CanNotPerformCalculations;
}
