package wave.util;

import wave.exceptions.DimensionOutOfBounds;

public class Point2D extends PointNDimension{

	public Point2D(double x, double y) {

		super();

		try {
			setDimension(2);

			setCoordinate(x, 1);
			setCoordinate(y, 2);
		} catch (DimensionOutOfBounds e) {

		}
	}

	public double getX() {

		double buf = 0.0;

		try {
			buf = getCoordinate(1);

		} catch (DimensionOutOfBounds e) {

		}

		return buf;
	}

	public double getY() {
		double buf = 0.0;

		try {
			buf = getCoordinate(2);

		} catch (DimensionOutOfBounds e) {

		}

		return buf;
	}

}
