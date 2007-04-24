package rpnumerics.methods.contour.nraphson;

import org.netlib.lapack.DGESV;
import org.netlib.util.intW;

import rpnumerics.methods.contour.CubeFace;
import rpnumerics.methods.contour.CubeSolver;
import rpnumerics.methods.contour.FunctionParameters;
import rpnumerics.methods.contour.GridGenerator;
import rpnumerics.methods.contour.functionsobjects.CanNotPerformCalculations;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import wave.util.*;
import wave.exceptions.*;

public class CubeSolverNRaphson extends CubeSolver {

	private MDVectorFunction function;
	
	private final double ERROR = 0.000000001;
	private final int INTERACTIONS = 100;
	
	private FunctionParameters parameters;
	private GridGenerator constraints;
	
	private boolean problem = false;
	
	public CubeSolverNRaphson(CubeFace cface, MDVectorFunction function) {
		super(cface);
		
		this.function = function;
	}
	
	public void setParametersAndConstraints(FunctionParameters parameters, GridGenerator constraints) {
		this.parameters = parameters;
		this.constraints = constraints;
	}
	
	public boolean isThereProblem() {
		return problem;
	}
	
	public void resetProblemFlag() {
		problem = false;
	}
	
	protected double[] afftrn(int ind, double[] [] u, double[] x) {	
		
		int flag = newtonRaphsonMethod(x, u);
		double[] solution = null;
		
		if (flag == 0) {		
			solution = super.afftrn(ind, u, x);				
		} else {
//			 sinalizar que a solucao estah fora e que deve ser marcada para analise futura
			// mesmo para quando ciclar		
			problem = true;
		}
		
		return solution;
	}
	
	private int newtonRaphsonMethod(double[] Xm, double[][] u) {
		
		int n_ = this.getN_();
		int m_ = this.getM_();
		
		double[] Fm 	= new double[m_];
		double[] Xn		= new double[n_];
		double[] deltaX = new double[m_];
		double[][] Jmm 	= new double[m_][m_];
		double[][] Jmn	= new double[m_][n_];
		
		int[] ipvt = new int[m_];
		int n1 = Xm.length;
		
		int blow 	= 0;
		int numIter = 0;
		boolean lessThanError = false;
						
		while ((blow == 0) && (numIter < INTERACTIONS) && (!lessThanError)) {
			try {    			
				Xn = XmToXn(Xm, u);
							
				PointNDimension XnPoint = new PointNDimension(new RealVector(Xn));					
				Jmn = RealMatrix2.convert(function.deriv(XnPoint));
							
				Jmm = JmnToJmm(Jmn, u);
									
				Fm = Fm(Xn);
							
				intW info = new intW(0);
				double xz[][];
				
				int sizeX = m_;
				xz = new double[sizeX][1];			
				
				for(int pont_x = 0; pont_x < sizeX; pont_x++) {
					xz[pont_x][0] = -Fm[pont_x];
				}
				
				DGESV.DGESV(n1, 1, Jmm, ipvt, xz, info);
				
				for (int pont_x = 0; pont_x < sizeX; pont_x++) {
					deltaX[pont_x] = xz[pont_x][0];
				}
				
				for (int pont_x = 0; pont_x < sizeX; pont_x++) {
					Xm[pont_x] += deltaX[pont_x];
				}
				
				lessThanError = true;
				for(int pont_x = 0; pont_x < sizeX && lessThanError; pont_x++) {
					if(Math.abs(deltaX[pont_x]) >= ERROR) {
						lessThanError = false;
					}				
				}
							
				blow = analyzeResult(0, Xm);
				numIter++;
				
			} catch (CanNotPerformCalculations e) {
				blow = 1;
			}	
		}
		
		if (blow != 0 ) {
			this.problem = true;
		}
		
		return blow;
	}
	
	private double[] XmToXn(double[] Xm, double[][] u) {
		int m_ = this.getM_();
		int n_ = this.getN_();
		double[] temp = new double[n_];
		double[] Xn = new double[n_];
		
		PointNDimension initial_point = constraints.getInitialPoint();
		
		for (int pont_k = 0; pont_k < n_; pont_k++) {
			temp[pont_k] = u[pont_k][0];
			for(int pont_j = 0; pont_j < m_; pont_j++) {
				temp[pont_k] = temp[pont_k] + ((u[pont_k][pont_j + 1] - u[pont_k][0]) * Xm[pont_j]);
				
			}
		}
		
		for (int pont_k = 0; pont_k < n_; pont_k++) {			
			try {
				Xn[pont_k] = initial_point.getCoordinate(pont_k + 1) + 
	            constraints.getVariation(pont_k + 1) * ((parameters.getIndex(invertIndex(pont_k + 1, n_)) - 1) +	    	
	            		temp[pont_k]);
			} catch (DimensionOutOfBounds e) {
				e.printStackTrace();
			}
		}
		
		return Xn;		
		  
	}
	
	private double[][] JmnToJmm(double[][] Jmn, double[][] u) {
		int n_ = this.getN_();  
		int m_ = this.getM_();
		
		double[][] Jmm = new double[m_][m_];
		double[][] tempJmn = new double[m_][n_];
		
		for(int pont_j = 0; pont_j < n_; pont_j++) {
			for(int pont_i = 0; pont_i < m_; pont_i++) {
				tempJmn[pont_i][pont_j] = 0;
				try {
					tempJmn[pont_i][pont_j] = Jmn[pont_i][pont_j] * constraints.getVariation(pont_j + 1);
				} catch (DimensionOutOfBounds e) {
					e.printStackTrace();
				}			
			}
		}
		
		for(int pont_i = 0; pont_i < m_; pont_i++) {
			for(int pont_j = 0; pont_j < m_; pont_j++) {
				Jmm[pont_i][pont_j] = 0.0;
				for(int pont_k = 0; pont_k < n_; pont_k++) {
					Jmm[pont_i][pont_j] = Jmm[pont_i][pont_j] 
					+ tempJmn[pont_i][pont_k] * (u[pont_k][pont_j + 1] - u[pont_k][0]);					
				}
			}
		}
		
		return Jmm;
	}
	
	private double[] Fm(double[] Xn) throws CanNotPerformCalculations {
		
		int m_ = this.getM_();
		double[] Fm = new double[m_];
				
		PointNDimension XnPoint = new PointNDimension(new RealVector(Xn));				
		RealVector result = function.value(XnPoint);
		
		for(int pont_i = 0; pont_i < m_; pont_i++) {			
			Fm[pont_i] = result.getElement(pont_i);
		}
		
		return Fm;
	}
	
	private int invertIndex(int index, int dimension) {
    	/*
    	 * this function move the higher indexes of the array to low indexes. Inverts the array
    	 * to make it compatible with the previous implementation.
    	 * 
    	 * Example: 1 2 3 4 is transformed in 4 3 2 1
    	 */
    	
    	int half = dimension / 2;
    	int rest = dimension % 2;
    	
    	int pont_half = half + 1;
    	
    	int result = 0;
    	
    	if (rest != 0) {
    		result = pont_half + (pont_half - index);
    	} else {
    		if (index <= half) {
    			result = pont_half + (half - index);
    		} else {
    			result = half - (index - pont_half);
    		}    		
    	}
    	
    	return result;
    }
}
