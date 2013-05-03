package wave.util;

import wave.util.exceptions.PointsAreNotSameDimension;

public class DoubleInterval {

	private double begining, end;

	public DoubleInterval(double begining, double end)
			throws PointsAreNotSameDimension {

		if (begining > end) {
			this.end = begining;
			this.begining = end;
		} else if (begining != end){
			this.begining = begining;
			this.end = end;
		} else {

		}

	}

	public double getFirstPoint() {
		return this.begining;
	}

	public double getSecondPoint() {
		return this.end;
	}

}
