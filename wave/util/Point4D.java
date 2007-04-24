package wave.util;

import wave.exceptions.DimensionOutOfBounds;


public class Point4D extends PointNDimension {

	public Point4D(double x, double y, double z, double w) {

		super();

		setDimension(4);

		try {
			setCoordinate(x, 1);
			setCoordinate(y, 2);
			setCoordinate(z, 3);
			setCoordinate(w, 4);
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

	public double getZ() {

		double buf = 0.0;

		try {
			buf = getCoordinate(3);

		} catch (DimensionOutOfBounds e) {

		}

		return buf;
	}

	public double getW() {

		double buf = 0.0;

		try {
			buf = getCoordinate(4);

		} catch (DimensionOutOfBounds e) {

		}

		return buf;
	}

}
