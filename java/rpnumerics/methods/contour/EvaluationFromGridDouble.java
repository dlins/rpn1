package rpnumerics.methods.contour;

public class EvaluationFromGridDouble implements EvaluationInter {

	private double evaluation;
	
	public EvaluationFromGridDouble(double evaluation) {
		super();
		this.evaluation = evaluation;
	}
	
	public double getEvaluation() {
		return this.evaluation;
	}

}
