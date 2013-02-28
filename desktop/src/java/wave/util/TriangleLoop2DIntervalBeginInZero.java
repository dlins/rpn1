package wave.util;

public class TriangleLoop2DIntervalBeginInZero extends TriangleLoop2D {

	public TriangleLoop2DIntervalBeginInZero(int ceiling) {
		super(ceiling);
	}

	public int[] getIndex(int pos) {
		
		int[] index = super.getIndex(pos + 1);			
		index[0]--;
		index[1]--;
		return index;
	}
	
	public int getPos(int[] index) {
		
		int[] indexTmp = new int[2];
		
		indexTmp[0] = index[0] + 1;
		indexTmp[1] = index[1] + 1;
		
		return (super.getPos(indexTmp)) - 1;
	}
}
