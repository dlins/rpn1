package rpnumerics.methods.contour.markedhypercubes;

public class HyperCubeErrorFound extends Exception {

	private Exception e;
	
	public HyperCubeErrorFound(Exception e) {
		super("");
		this.e = e;
	}
	
	public Exception getException() {
		return e;
	}

}
