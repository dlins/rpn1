package wave.util;

public class TriangleLoop2D extends MultipleLoop {
	
	private int ceiling;
	private int loopsize;
	
	public TriangleLoop2D(int ceiling) {
		super(FromIntToIntArray());
		this.ceiling = ceiling;
		
		loopsize = getLoopSize(ceiling);
	}
	
	private static IntegerInterval[] FromIntToIntArray() {
		
		IntegerInterval[] tmp = new IntegerInterval[1];
		tmp[0] = new IntegerInterval(0,1);
		
		return tmp;
	}
	
	public int getLoopSize(){
		return loopsize;
	}

	public int[] getIndex(int pos) {
		int index[] = new int[2];
		
		index[0] = (int) Math.ceil((((2*ceiling + 1) - Math.sqrt(((2*ceiling + 1)*(2*ceiling + 1)) - (8*pos))) / 2));
		index[1] = (int) (pos - ((2*ceiling - index[0] + 2) * (index[0] - 1)) / 2);

		return index;
	}
	
	public int getPos(int[] index) {
		
		int m = index[0] - 1;
		int j = index[1];
				
		return (somaPAInvertida(m) + j);
	}
	
	private int getLoopSize(int ceiling) {
		
		int result = 0;
		
		for(int pont_int = 0; pont_int <= ceiling; pont_int++) {
			result += pont_int;
		}
		
		return result;
	}
	
	private int somaPAInvertida(int m) {
		return ((((ceiling*2) - (m-1)) * m)/2);
	}
}
