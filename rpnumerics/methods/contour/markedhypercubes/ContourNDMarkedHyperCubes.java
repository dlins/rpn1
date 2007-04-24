package rpnumerics.methods.contour.markedhypercubes;

import wave.exceptions.DimensionOutOfBounds;
import java.util.*;

import rpnumerics.methods.contour.ContourND;
import rpnumerics.methods.contour.ContourNDDecorator;
import rpnumerics.methods.contour.ContourPolyline;
import rpnumerics.methods.contour.FunctionParameters;
import rpnumerics.methods.contour.GridGenerator;
import rpnumerics.methods.contour.ThereIsNoFeasibleSolution;

public abstract class ContourNDMarkedHyperCubes extends ContourNDDecorator {

	private Vector MarkedCubeList = new Vector();
	
	public ContourNDMarkedHyperCubes(HyperCubeMarker contour)
			throws IllegalArgumentException {
		super((ContourND) contour);		
	}
	
	public boolean isThereHyperCubes() {		
		if (MarkedCubeList.size() != 0) {
			return true;
		}		
		return false; 
	}
	protected ContourPolyline[] copyEdges(GridGenerator solution, int nedges, FunctionParameters parameters, double [][] sol_, int [][] edges_) throws DimensionOutOfBounds, 
	 																																						 ThereIsNoFeasibleSolution, 
	 																																						 HyperCubeErrorFound {
		
		ContourPolyline[] segend_ = null;
		
		try {
			segend_ = super.copyEdges(solution, nedges, parameters, sol_, edges_);
		} catch (HyperCubeErrorFound e) {
			MarkedCubeList.add(new MarkedCubeItem(solution, (FunctionParameters) parameters.clone(), ((HyperCubeErrorFound) e).getException()));
			throw e;
		}
		
		makeMarkingList(solution, parameters);
		
		return segend_;
	}
	
	protected void clearHyperCubeList() {
		MarkedCubeList.clear();
	}
	
	protected void setMarkedCubeList(Vector list) {
		MarkedCubeList = list;
	}
	
	protected Vector getMarkedCubeList() {
		return MarkedCubeList;
	}
		
	protected void makeMarkingList(GridGenerator solution, FunctionParameters parameters) throws HyperCubeErrorFound {
		HyperCubeMarker contour = (HyperCubeMarker) this.getContour();		
		Exception mark = contour.getMark();
		
		if(mark != null) {
			if (!MarkedCubeList.contains(parameters)) {
				MarkedCubeList.add(new MarkedCubeItem(solution, (FunctionParameters) parameters.clone(), mark));
				contour.resetMark();
				throw new HyperCubeErrorFound(mark);
			}
		}
	}
	
	public abstract void actionOnMarkedHyperCubes(); 
}
