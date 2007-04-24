package rpnumerics.methods.contour.functionsobjects;

import rpnumerics.HugoniotParams;
import rpnumerics.physics.FluxFunction;
import wave.util.PointNDimension;
import wave.util.RealMatrix2;
import wave.util.RealVector;

public abstract class MatrixFunction extends MDVectorFunction {

	private int lines;
	private int columns;
	
	public MatrixFunction(int dimension, int lines, int columns, FluxFunction fluxFunction,
			HugoniotParams params) {
		super(dimension, lines, fluxFunction, params);
		
		this.lines = lines;
		this.columns = columns;
	}
	
	public RealVector value(PointNDimension point) throws CanNotPerformCalculations {
		
		double[][] matrix = RealMatrix2.convert(getValueArray(point));
		double[] result = new double[lines * columns];
		
		for (int pont_lines = 0; pont_lines < lines; pont_lines++) {
			for(int pont_columns = 0; pont_columns < columns; pont_columns++) {
				result[(pont_columns * lines) + pont_lines] = matrix[pont_lines][pont_columns];
			}
		}
		
		return new RealVector(result);
	}

	public RealMatrix2 deriv(PointNDimension u) throws CanNotPerformCalculations {	
		// to complete...later
		return null;
	}
	
	public int getNumberOfLines() {
		return this.lines;
	}
	
	public int getNumberOfColumns() {
		return this.columns;
	}
	
	public int getResultComponentNumber() {
	    return this.lines * this.columns;
	}	
	
	protected abstract RealMatrix2 getValueArray(PointNDimension point) throws CanNotPerformCalculations;
	protected abstract RealMatrix2 getDerivArray(PointNDimension point) throws CanNotPerformCalculations;
}
