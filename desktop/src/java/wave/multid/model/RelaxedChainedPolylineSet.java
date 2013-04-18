package wave.multid.model;

import wave.util.PointNDimension;

public class RelaxedChainedPolylineSet extends ChainedPolylineSet {

	public RelaxedChainedPolylineSet() {
		super();
	}

	public RelaxedChainedPolylineSet(PointNDimension[][] polyline)
			throws CanNotCreatePolylineSet {
		super(polyline);
	}

	protected boolean connectPointsToSegment(ChainedPolyline segment, PointNDimension p1, PointNDimension p2) throws SegmentAlreadyInList,
																												     SegmentDegradesPolyline,
																													 SegmentCiclesPolyline {
		boolean linked = true;
		
		try{
			segment.addSegment(p1, p2);
		} catch (SegmentAlreadyInList e) {
			linked = false;
		} catch (SegmentCanNotJoin e) {
			linked = false;
		} catch (SegmentCiclesPolyline e) {	
		} catch (SegmentDegradesPolyline e) {
			linked = false;
		}
		
		return linked;
	}
}
