package rpnumerics.methods.contour.markedhypercubes;

import java.util.Vector;

public abstract class HyperCubeErrorTreatmentBehavior {
	
	private Vector MarkedCubeList = new Vector();
	
	public HyperCubeErrorTreatmentBehavior() {
		
	}
	
	public boolean isThereMarkedHyperCubes() {		
		if (MarkedCubeList.size() != 0) {
			return true;
		}		
		return false; 
	}
	
	public void markHyperCube(MarkedCubeItem markedHyperCube) {
		MarkedCubeList.add(markedHyperCube);
	}
	
	protected Vector getCubeList() {
		return this.MarkedCubeList;
	}
	
	public abstract void action(); 
}
