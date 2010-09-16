package rpnumerics.methods.contour;

import rpnumerics.methods.contour.markedhypercubes.HyperCubeErrorFound;
import wave.multid.model.*;
import wave.util.*;
import wave.util.exceptions.*;
import wave.multid.*;
import wave.multid.view.*;

public class ContourEdgesMultiPolyline {

	private ChainedPolylineSet PolyLinesSetList = new ChainedPolylineSet();		// List of ChainedPolylineSet Objects
	private double[][] sol_;
	private FunctionParameters parameters;
	private int dimension; 
	private GridGenerator solution;
	
	private Exception hyperCubeErr = null;
	
	public ContourEdgesMultiPolyline(int nedges,
		int[][] edges_,
		double [][] sol_,
		FunctionParameters parameters,									 
		GridGenerator solution) throws ThereIsNoFeasibleSolution,
		DimensionOutOfBounds,
		HyperCubeErrorFound {
		super();
		this.sol_ = sol_;
		
		if (sol_ == null || edges_ == null) {
			throw new ThereIsNoFeasibleSolution();
		}
		
		this.parameters = parameters;
		this.dimension = solution.myDimensionIs();
		this.solution = solution;
    	
    	if(solution.myDimensionIs() == parameters.myDimensionIs()) {
    		generateMultiPolyLines(nedges, edges_);
    	} else 
    		throw new DimensionOutOfBounds();
    	
    	if (hyperCubeErr != null) {
    	//	throw new HyperCubeErrorFound(hyperCubeErr);
    	}
	}
	
	public int size() {
		return PolyLinesSetList.size();
	}
	
	private void generateMultiPolyLines(int nedges_, int[][] edges_) {
				
		for (int pont_edge = 0; pont_edge < nedges_; pont_edge++) {			
			VertexReference p1 = new VertexReference(edges_[0][pont_edge], edges_[2][pont_edge], edges_[3][pont_edge]);			
			VertexReference p2 = new VertexReference(edges_[1][pont_edge], edges_[2][pont_edge], edges_[4][pont_edge]);
						
			try {
				PolyLinesSetList.addSegment(p1, p2);
			} catch (SegmentAlreadyInList e) {	
				hyperCubeErr = e;
			} catch (SegmentCiclesPolyline e) {		
				hyperCubeErr = e;
			} catch (SegmentDegradesPolyline e) {				
				hyperCubeErr = e;
			}
		}
	}
	
	public ContourPoint getFirstPointFrom(int index) {
		
		VertexReference tmp = (VertexReference) PolyLinesSetList.getFirstPointAt(index);
		
		ContourPoint res = null;
		
		res = new ContourPoint((FunctionParameters) parameters.clone(), 
							   tmp.getSimplexIndex(),
               				   tmp.getFaceIndex());		
		return res;
	}
	
	public ContourPoint getLastPointFrom(int index) {
		
		VertexReference tmp = (VertexReference) PolyLinesSetList.getLastPointAt(index);
		
		ContourPoint res = null;
		
		res = new ContourPoint((FunctionParameters) parameters.clone(), 
							   tmp.getSimplexIndex(),
               				   tmp.getFaceIndex());		
		return res;
	}
		
	public ContourPolyline[] getContourPolylines() {
				
		int size = PolyLinesSetList.size();
		
		ContourPolyline[] result = new ContourPolyline[size];		
		for (int pont_polyline = 0; pont_polyline < size; pont_polyline++) {
			result[pont_polyline] = new ContourPolyline(this.getFirstPointFrom(pont_polyline), 
					 									this.getLastPointFrom(pont_polyline), 
					 									this.getPolyLineAt(pont_polyline));
		}
		
		return result;
	}
	
	public PointNDimension[][] getPolylines() {
		
		PointNDimension[][] polylines = PolyLinesSetList.getPolylines();
		int size = polylines.length;
		PointNDimension[][] buffer = new PointNDimension[polylines.length][];
		
		for (int pont_point_i = 0; pont_point_i < size; pont_point_i++) {
			buffer[pont_point_i] = new PointNDimension[polylines[pont_point_i].length];
			for(int pont_point_j = 0; pont_point_j < buffer[pont_point_i].length; pont_point_j++) {
				int index = 0;
				try {
					index = (int) polylines[pont_point_i][pont_point_j].getCoordinate(1);
				} catch (Exception e) {
					
				}
				
				buffer[pont_point_i][pont_point_j] = new PointNDimension(dimension);
				PointNDimension initial_point = solution.getInitialPoint();
								
				for (int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
					try {
						double tmp = sol_[pont_dimension - 1][index - 1];
						tmp = initial_point.getCoordinate(pont_dimension) + 
	                    solution.getVariation(pont_dimension) * ((parameters.getIndex(invertIndex(pont_dimension, dimension)) -1) + tmp);
						buffer[pont_point_i][pont_point_j].setCoordinate(tmp, pont_dimension);						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		return buffer;
	}
	
	public PointNDimension[] getPolyLineAt(int indexp) {

		PointNDimension[][] polylines = PolyLinesSetList.getPolylines();
		
		PointNDimension[] buffer = null;
		
		int size = polylines[indexp].length;
		buffer= new PointNDimension[polylines[indexp].length];
		for(int pont_point_j = 0; pont_point_j < size; pont_point_j++) {
			int index = 0;
			try {
				index = (int) polylines[indexp][pont_point_j].getCoordinate(1);
			} catch (Exception e) {
				
			}
			
			buffer[pont_point_j] = new PointNDimension(dimension);
			PointNDimension initial_point = solution.getInitialPoint();
			
			for (int pont_dimension = 1; pont_dimension <= dimension; pont_dimension++) {
				try {
					double tmp = sol_[pont_dimension - 1][index - 1];
					tmp = initial_point.getCoordinate(pont_dimension) + 
                    solution.getVariation(pont_dimension) * ((parameters.getIndex(invertIndex(pont_dimension, dimension)) -1) + tmp);
					buffer[pont_point_j].setCoordinate(tmp, pont_dimension);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return buffer;
	}
	
	public MultiPolyLine[] getMultidPolylines(ViewingAttr attributes) {
		
		PointNDimension[][] tmp = PolyLinesSetList.getPolylines();
		MultiPolyLine[] polylines = new MultiPolyLine[tmp.length];
		
		for (int pont_polyline = 0; pont_polyline < tmp.length; pont_polyline++) {
			CoordsArray[] polyline = new CoordsArray[tmp[pont_polyline].length];
			for(int pont_point = 0; pont_point < tmp[pont_polyline].length; pont_point++) {
				polyline[pont_point] = tmp[pont_polyline][pont_point].toCoordsArray();
			}
			polylines[pont_polyline] = new MultiPolyLine(polyline, attributes);	
		}
		
		return polylines;
	}
	
	protected int invertIndex(int index, int dimension) {
    	/*
    	 * this function move the higher indexes of the array to low indexes. Inverts the array
    	 * to make it compatible.
    	 * 
    	 * Example: 1 2 3 4 makes them 4 3 2 1
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

class VertexReference extends PointNDimension{
	
	private int SimplexIndex;
	private int FaceIndex;
	
	public VertexReference(int SolutionIndex, int SimplexIndex, int FaceIndex) {
		super(1);
		try {
			this.setCoordinate((double) SolutionIndex, 1);
		} catch (Exception e) {
			
		}
		
		this.SimplexIndex = SimplexIndex;
		this.FaceIndex = FaceIndex;
	}
	
	public int getSimplexIndex() {
		return SimplexIndex;
	}
	
	public int getFaceIndex() {
		return FaceIndex;
	}
	
	public Object clone() {
		
		int SolutionIndex = 0;
		
		try {
			SolutionIndex = (int) this.getCoordinate(1);
		} catch (DimensionOutOfBounds e) {
			
		}
		
		return new VertexReference(SolutionIndex, SimplexIndex, FaceIndex);
	}
}
