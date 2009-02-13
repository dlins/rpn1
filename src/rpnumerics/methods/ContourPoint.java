package rpnumerics.methods.contour;

public class ContourPoint {

	private FunctionParameters hyperCubeCoordinate;
	private int simplex;
	private int face;
	
	public ContourPoint(FunctionParameters hyperCubeCoordinate, int simplex, int face) {
		super();
		this.hyperCubeCoordinate = (FunctionParameters) hyperCubeCoordinate.clone();
		this.simplex = simplex;
		this.face = face;
	}
	
	public boolean equals(ContourPoint point) {
		if (this.hyperCubeCoordinate.equals(point.getHyperCubeCoordinate())) {
			if (simplex == point.getSimplex()) {
				if(face == point.getFace()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public FunctionParameters getHyperCubeCoordinate() {
		return this.hyperCubeCoordinate;
	}
	
	public int getFace() {
		return this.face;
	}

	public int getSimplex() {
		return this.simplex;
	}
}
