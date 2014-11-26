package wave.multid.model;

import java.util.*;
import wave.util.*;

public class ChainedPolylineSet {

	private Vector PolyLinesList = new Vector();		// List of ChainedPolyline Objects
		
	public ChainedPolylineSet() {
		super();
	}
	
	public ChainedPolylineSet(PointNDimension[][] polyline) throws CanNotCreatePolylineSet {
		super();
		
		for (int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {
			PointNDimension previous = polyline[pont_polyline][0];
			for (int pont_point = 1; pont_point < polyline[pont_polyline].length; pont_point++) {
				PointNDimension actual = polyline[pont_polyline][pont_point];
				try {
					this.addSegment(previous, actual);
				} catch (SegmentAlreadyInList e) {
					
				} catch (SegmentDegradesPolyline e) {
					throw new CanNotCreatePolylineSet();
				} catch (SegmentCiclesPolyline e) {
					throw new CanNotCreatePolylineSet();
				}
				previous = actual;
			}
		}
		
	}
	
	public int size() {
		return PolyLinesList.size();
	}
	
	public void addSegment(PointNDimension p1, PointNDimension p2) throws SegmentAlreadyInList,
																		  SegmentDegradesPolyline,
																		  SegmentCiclesPolyline {
		Iterator i = PolyLinesList.iterator();
		
		boolean linked = false;
		ChainedPolyline tmp1 = null;
		
		Exception exceptionMark = null;
		
		while (i.hasNext() && !linked) {
			tmp1 = (ChainedPolyline) i.next();			
			linked = connectPointsToSegment(tmp1, p1, p2);			
		}
		
		if(!linked) {
			int index = PolyLinesList.size();
			ChainedPolyline tmp = new ChainedPolyline(index);
			/*try {
				tmp.addSegment(p1, p2);
			} catch (Exception e) {
				
			}*/
			
			connectPointsToSegment(tmp, p1, p2);			
			PolyLinesList.add(tmp);
		} else {
			
			linked = false;
			ChainedPolyline tmp2 = null;
			
			while (i.hasNext() && !linked) {
				tmp2 = (ChainedPolyline) i.next();
				
				/*linked = true;
				try{
					tmp2.addSegment(p1, p2);
				} catch (SegmentAlreadyInList e) {
					throw e;
				} catch (SegmentCanNotJoin e) {
					linked = false;
				} catch (SegmentCiclesPolyline e) {
					throw e;
				}*/
				
				linked = connectPointsToSegment(tmp2, p1, p2);	
			}
			
			if(linked) {
				try {
					tmp1.merge(tmp2);
				} catch (Exception e) {
					
				}
				PolyLinesList.remove(tmp2);
			}
		}
		
	}

	public void removeSegment(PointNDimension p1, PointNDimension p2) {
		// a fazer
	}
	
	protected boolean connectPointsToSegment(ChainedPolyline segment, PointNDimension p1, PointNDimension p2) throws SegmentAlreadyInList,
																												     SegmentDegradesPolyline,
																													 SegmentCiclesPolyline {
		boolean linked = true;
		
		try{
			segment.addSegment(p1, p2);
		} catch (SegmentAlreadyInList e) {
			linked = false;
			throw e;
		} catch (SegmentCanNotJoin e) {
			linked = false;
		} catch (SegmentCiclesPolyline e) {				
			throw e;
		} catch (SegmentDegradesPolyline e) {
			linked = false;
			throw e;
		}
		
		return linked;
	}
	
	public void addPolyline(PointNDimension[] polyline) throws SegmentAlreadyInList,
															   SegmentDegradesPolyline,
															   SegmentCiclesPolyline {
		PointNDimension previous = polyline[0];
		for (int pont_point = 1; pont_point < polyline.length; pont_point++) {
			PointNDimension actual = polyline[pont_point];
			try {
				this.addSegment(previous, actual);
			} catch (SegmentAlreadyInList e) {
				throw e;
			} catch (SegmentDegradesPolyline e) {
				throw e;
			} catch (SegmentCiclesPolyline e) {
				throw e;
			}
			previous = actual;
		}
	}
	
	public void removePolyline(int index) {
		PolyLinesList.remove(index);
	}
	
	public PointNDimension getFirstPointAt(int index) {
		ChainedPolyline tmp = (ChainedPolyline) PolyLinesList.get(index);
		
		return tmp.getFirst();
	}
	
	public PointNDimension getLastPointAt(int index) {
		ChainedPolyline tmp = (ChainedPolyline) PolyLinesList.get(index);
		
		return tmp.getLast();
	}
	
	public PointNDimension[][] getPolylines() {
		
		int size = PolyLinesList.size();
		
		PointNDimension[][] buffer = new PointNDimension[size][]; 
		Iterator i = PolyLinesList.iterator();
		
		for (int pont_polyline = 0; pont_polyline < size; pont_polyline++) {
			ChainedPolyline tmp = (ChainedPolyline) i.next();
			
			buffer[pont_polyline] = tmp.getPolyLine();
		}
		
		return buffer;
	}

}

class CanNotCreatePolylineSet extends Exception {
	public CanNotCreatePolylineSet() {
		
	}
}
