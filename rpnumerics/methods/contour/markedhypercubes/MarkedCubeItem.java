package rpnumerics.methods.contour.markedhypercubes;

import rpnumerics.methods.contour.FunctionParameters;
import rpnumerics.methods.contour.GridGenerator;

public class MarkedCubeItem {

	private FunctionParameters parameters;
	private Exception mark;
	private GridGenerator solution;
	
	public MarkedCubeItem(GridGenerator solution, FunctionParameters parameters, Exception mark) {
		super();
		
		this.solution = solution;
		this.parameters = parameters;
		this.mark = mark;
	}

	public Exception getMark() {
		return this.mark;
	}

	public FunctionParameters getParameters() {
		return this.parameters;
	}

	public GridGenerator getSolutionConstraints() {
		return this.solution;
	}

}
