package rpnumerics.methods.contour.markedhypercubes;

public class TestHyperCubeErrorTreatment extends
		HyperCubeErrorTreatmentBehavior {

	public TestHyperCubeErrorTreatment() {
		super();
	}

	public void action() {
		int size = this.getCubeList().size();
		
		System.out.println("Numero de hypercubos = " + size);
		
	}

}
