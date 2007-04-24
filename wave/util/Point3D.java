package wave.util;

import wave.exceptions.DimensionOutOfBounds;


public class Point3D extends PointNDimension {

	public Point3D(double x, double y, double z) {

		super();

		setDimension(3);

		try {
			setCoordinate(x, 1);
			setCoordinate(y, 2);
			setCoordinate(z, 3);
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

}
