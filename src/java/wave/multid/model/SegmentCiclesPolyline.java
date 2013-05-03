package wave.multid.model;

public class SegmentCiclesPolyline extends Exception {

	private int polylineIndex;
	
	public SegmentCiclesPolyline(int polylineIndex) {
		super();
		this.polylineIndex = polylineIndex;
	}
	
	public int getPolyLineIndex() {
		return this.polylineIndex;
	}
}
