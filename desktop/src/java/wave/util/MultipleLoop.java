package wave.util;

public class MultipleLoop {

	private int numberOfLoops;
	private int limits[];
	private int pt1[];

	private int loopsize;

	public MultipleLoop(IntegerInterval[] looplimits) {
		numberOfLoops = looplimits.length;

		this.limits = new int[numberOfLoops];
		pt1 = new int[numberOfLoops];

		loopsize = 1;

		for (int pont = 0; pont < numberOfLoops; pont++) {
			pt1[pont] = looplimits[pont].getPt1();
			int pt2 = looplimits[pont].getPt2();


			limits[pont] = pt2 - pt1[pont] + 1;
			loopsize *= limits[pont];
		}
	}

	public int getLoopSize(){
		return loopsize;
	}

	public int[] getIndex(int pos) {
		int temp = pos;
		int index[] = new int[numberOfLoops];

		for(int pont = 0; pont < numberOfLoops; pont++){
			index[pont] = (temp % limits[pont]) + pt1[pont];
			temp = temp / limits[pont];
		}

		return index;
	}
}
