package wave.util;

public class TriangleLoop2DIntervalBeginInOne extends TriangleLoop2D {

	public TriangleLoop2DIntervalBeginInOne(int ceiling) {
		super(ceiling);
	}

	public int[] getIndex(int pos) {
		
		int[] index = super.getIndex(pos + 1);			
		
		return index;
	}
	
	public int getPos(int[] index) {
		
		int[] indexTmp = new int[2];
		
		indexTmp[0] = index[0];
		indexTmp[1] = index[1];
		
		return (super.getPos(indexTmp)) - 1;
	}
}
