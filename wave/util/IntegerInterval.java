package wave.util;

public class IntegerInterval {

	private int pt1, pt2;

	public IntegerInterval(int pt1, int pt2) {

		if (pt1 <= pt2) {
			this.pt1 = pt1;
			this.pt2 = pt2;
		} else {
			this.pt1 = pt2;
			this.pt2 = pt2;
		}

	}

	public int getPt1() {
		return pt1;
	}

	public int getPt2() {
		return pt2;
	}

}
