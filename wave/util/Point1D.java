package wave.util;

import wave.exceptions.DimensionOutOfBounds;

public class Point1D extends PointNDimension {

	public Point1D(double x) {

		super(1);

		try {
			setDimension(1);

			setCoordinate(x, 1);

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
}
