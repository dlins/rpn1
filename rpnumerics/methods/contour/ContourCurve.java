package rpnumerics.methods.contour;

import java.util.*;

import wave.multid.model.*;
import wave.util.*;

public class ContourCurve {

//	 checar se aceita ciclagem
		
	private Vector segmentList = new Vector();
	
	public ContourCurve() {
		
	}
	
	public ContourCurve(ContourPolyline[] polyline) {
		super();
	
		for (int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {
			addPolyline(polyline[pont_polyline]);
		}
	}
	
	public void addPolyline(ContourPolyline polyline) {
		Iterator i = segmentList.iterator();
		ContourPolyline tmp1 = null;
		ContourPolyline tmp2 = null;
				
		boolean notJoined = true;		
		while(i.hasNext() && notJoined) {			
			tmp1 = (ContourPolyline) i.next();
			notJoined = false;
			try { 
				tmp1.add(polyline);
			} catch (SegmentCanNotJoin e) {
				notJoined = true;
			}
		}
		
		if (notJoined) {
			segmentList.add(polyline);
		} else {			
			boolean notMerged = true;			
			while(i.hasNext() && notMerged) {
				tmp2 = (ContourPolyline) i.next();
				notMerged = false;				
				try { 
					tmp2.add(tmp1);
				} catch (SegmentCanNotJoin e) {
					notMerged = true;
				}
			}
			
			if (!notMerged) {
				segmentList.remove(tmp1);
			}
		}
	}
	
	public int numberOfPolylines() {
		return segmentList.size();
	}
	
	public int numberOfSegments() {
		int total = 0;
		
		int numberOfPolylines = this.numberOfPolylines();
		
		Iterator i = segmentList.iterator();
		
		for (int pont_polyline = 0; pont_polyline < numberOfPolylines; pont_polyline++) {
			total += ((ContourPolyline) i.next()).getSize();
		}
		
		return total;
	}

	public ContourPolyline[] getContourPolylines() {
		Object[] tmp = segmentList.toArray();
		
		ContourPolyline[] result = new ContourPolyline[tmp.length];		
		for(int pont_polyline = 0; pont_polyline < tmp.length; pont_polyline++) {
			result[pont_polyline] = (ContourPolyline) tmp[pont_polyline];
		}
		
		return result;
	}
		
	public PointNDimension[][] getCurve() {
		
		ContourPolyline[] curve = this.getContourPolylines();
		PointNDimension[][] multipolyline = new PointNDimension[curve.length][];
		
		for(int pont_polyline = 0; pont_polyline < curve.length; pont_polyline++) {
			multipolyline[pont_polyline] = curve[pont_polyline].getPoints();
			
		}
		
		return multipolyline;
	}
		
	public Object clone() {
		ContourCurve clone = new ContourCurve();
		clone.segmentList = (Vector) this.segmentList.clone();
		
		return clone;
	}
	
}
