package bean;

public class ProbAndLabel {

	Double prob ;
	Double label;
	
	public ProbAndLabel(double prob, double label) {
		this.prob = prob;
		this.label = label;
	}
	public Double getProb() {
		return prob;
	}
	public void setProb(double prob) {
		this.prob = prob;
	}
	public Double getLabel() {
		return label;
	}
	public void setLabel(double label) {
		this.label = label;
	}
	
}
