package wave.util;

import wave.exceptions.PointsAreNotSameDimension;
public class Edge {

	private PointNDimension begining, end;

	public Edge(PointNDimension begining, PointNDimension end) throws PointsAreNotSameDimension {
		super();
		// TODO Auto-generated constructor stub

		if (begining.myDimensionIs() == end.myDimensionIs()) {
			this.begining = begining;
			this.end = end;
		} else {
			throw new PointsAreNotSameDimension();
		}
	}

	// you can put functions to check if the point is here or it is linear dependent.

	public void getPoints(PointNDimension first, PointNDimension second) {
		first = this.begining;
		second = this.end;
	}

}
