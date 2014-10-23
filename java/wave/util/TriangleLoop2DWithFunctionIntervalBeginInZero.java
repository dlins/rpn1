package wave.util;

public class TriangleLoop2DWithFunctionIntervalBeginInZero extends
		TriangleLoop2DIntervalBeginInZero {

	private int numberOfFunctions;
	private int loopSizeXY;
	
	public TriangleLoop2DWithFunctionIntervalBeginInZero(int ceiling, int numberOfFunctions) {
		super(ceiling);
		
		this.numberOfFunctions = numberOfFunctions;
		loopSizeXY = super.getLoopSize();
	}
	
	public int getLoopSize() {
		return loopSizeXY * ((numberOfFunctions - 1) + 1);
	}
	
	public int[] getIndex(int pos) {
		
		int[] resp = new int[3];		
		int index[] = super.getIndex(pos % loopSizeXY);
				
		resp[0] = index[0];
		resp[1] = index[1];
		
		resp[2] = pos / loopSizeXY;
		
		return resp;
	}
	
	public int getPos(int[] index) {
		
		int[] variablesIndex = new int[2];
		variablesIndex[0] = index[0];
		variablesIndex[1] = index[1];
		
		int pos = super.getPos(variablesIndex) + (loopSizeXY * index[2]);
				
		return pos;
	}
}
