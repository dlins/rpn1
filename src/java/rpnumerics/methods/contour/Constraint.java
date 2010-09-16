package rpnumerics.methods.contour;


import wave.util.*;

public class Constraint {
	private DoubleInterval interval;
	private int numberOfDivisions;

	public Constraint(DoubleInterval interval, int numberOfDivisions) {
		this.interval = interval;
		this.numberOfDivisions = numberOfDivisions;
	}

	public DoubleInterval getInterval() {
		return interval;
	}

	public int getNumberOfDivisions() {
		return numberOfDivisions;
	}

}
