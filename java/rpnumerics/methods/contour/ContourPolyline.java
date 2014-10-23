package rpnumerics.methods.contour;

import java.util.*;

import wave.multid.model.SegmentCanNotJoin;
import wave.util.*;

public class ContourPolyline {

	private CubeFace faces;
	private LinkedList pointsList = new LinkedList();	
	
	private ContourPoint First, Last;
	private ContourPoint Previous, Next;
	
	private int dimension; 
	
	public ContourPolyline(ContourPoint First, ContourPoint Last, PointNDimension[] points ) {
		super();
		
		dimension = points[0].myDimensionIs();
		
		this.faces = new CubeFace(dimension, (dimension - 1));
			
		
		this.First = First;
		this.Last = Last;
		Previous = calculateNeighbour(First);		
		Next = calculateNeighbour(Last);
		
		for(int pont_point = 0; pont_point < points.length; pont_point++) {
			pointsList.add(points[pont_point]);
		}
	}

	public void add(ContourPolyline polyline) throws SegmentCanNotJoin {
		
		if (polyline.getDimension() != this.getDimension()) {
			throw new SegmentCanNotJoin();
		}
		
		if ((polyline.getSize() < 2) || (this.getSize() < 2)) {
			throw new SegmentCanNotJoin();
		}
		
		int polSize = polyline.getSize();
		
		ContourPoint previous = this.getPrevious();
		ContourPoint next = this.getNext();
		
		ContourPoint polFirst = polyline.getFirst();
		ContourPoint polLast = polyline.getLast();
		
		boolean isP1First = false;
		boolean isP2First = false;
		boolean isP1Last  = false;
		boolean isP2Last  = false;
		
		try {
			
			isP1First = previous.equals(polFirst);
			isP2First = previous.equals(polLast);
			isP1Last =  next.equals(polFirst);
			isP2Last =  next.equals(polLast);
							
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(isP1First) {
			PointNDimension[] polylinePoints = polyline.getPoints();
			
			for (int pont_point = 1; pont_point < polSize; pont_point++) {
				pointsList.addFirst(polylinePoints[pont_point]);
			}
			
			First = polyline.getLast();
			Previous = polyline.getNext();
			
		} else if(isP2First) {
			PointNDimension[] polylinePoints = polyline.getPoints();
			
			for (int pont_point = (polSize - 2); pont_point >= 0; pont_point--) {
				pointsList.addFirst(polylinePoints[pont_point]);
			}
			
			First = polyline.getFirst();
			Previous = polyline.getPrevious();
			
		} else if(isP1Last) {
			PointNDimension[] polylinePoints = polyline.getPoints();
			
			for (int pont_point = 1; pont_point < polSize; pont_point++) {
				pointsList.addLast(polylinePoints[pont_point]);
			}
			
			Last = polyline.getLast();
			Next = polyline.getNext();
			
		} else if(isP2Last) {
			PointNDimension[] polylinePoints = polyline.getPoints();
			
			for (int pont_point = (polSize - 2); pont_point >= 0; pont_point--) {
				pointsList.addLast(polylinePoints[pont_point]);
			}
			
			Last = polyline.getFirst();
			Next = polyline.getPrevious();
		} else {
			throw new SegmentCanNotJoin();
		}		
	
	}
	
	public int getDimension() {
		return faces.getFaceDimension();
	}
	
	public PointNDimension[] getPoints() {
		int size = pointsList.size();
		PointNDimension[] result = new PointNDimension[size];
		Iterator i = pointsList.iterator();
		for (int pont_point = 0; pont_point < size; pont_point++) {
			result[pont_point] = (PointNDimension) i.next();
		}
		
		return result;
	}
	
	public ContourPoint getFirst() {
		return this.First;
	}
	
	public ContourPoint getLast() {
		return this.Last;
	}
	
	private ContourPoint getPrevious() {
		return this.Previous;
	}
	
	private ContourPoint getNext() {
		return this.Next;
	}
	
	
	public int getSize() {
		return pointsList.size();
	}
	
	private ContourPoint calculateNeighbour(ContourPoint point) {
		
		FunctionParameters parameters = (FunctionParameters) (point.getHyperCubeCoordinate()).clone();
		
		int face = point.getFace(); 
		int simplex = point.getSimplex();
		
		int[][] facesPointers = faces.getFacePointersArray();
		face = facesPointers[simplex][face];
		
		int[][] attributes = faces.getAttributeArray(); // facatt_
		int[][] pointer = faces.getAttributePointersArray(); // ptfatt_
		
		int faceindex = (pointer[face][1] + pointer[face][0]) - 1;
		
		int dimension_index = invertIndex(Math.abs(attributes[faceindex][0]), dimension);
		int index, sign; 
		
		try {
			index = parameters.getIndex(dimension_index);
			if (attributes[faceindex][0] < 0){
				sign = -1;
			} else {
				sign = 1;
			}
			index += sign;
			parameters.setIndex(index, dimension_index);
		} catch (Exception e) {
			
		}	
		
		int numberOfSimplices = faces.getNumberOfSimplices();
		int numberOfSimplicesFaces = faces.getNumberOfSimplexFaces();
		boolean halt = false;
				
		for (int pont_simplex = 0; (pont_simplex < numberOfSimplices) && !halt; pont_simplex++) {
			for (int pont_face = 0; (pont_face < numberOfSimplicesFaces) && !halt; pont_face++) {
				if (facesPointers[pont_simplex][pont_face] == attributes[faceindex][1]) {
					simplex = pont_simplex;
					face = pont_face;
					halt = true;
				}
			}
		}
		
		return new ContourPoint(parameters, simplex, face);		
	}
	
	public Object clone() {
		return new ContourPolyline(First, Last, this.getPoints());
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
