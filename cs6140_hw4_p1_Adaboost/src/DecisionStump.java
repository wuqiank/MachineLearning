
public class DecisionStump {

	int feature_id;
	double threshold;
	double error_rate;
	
	public DecisionStump () {
		;
	}
	
	public DecisionStump(int feature_id, double threshold) {
		this.feature_id = feature_id;
		this.threshold = threshold;
		this.error_rate = 0.0;
	}
	
	public double getError_rate() {
		return error_rate;
	}
	public void setError_rate(double error_rate) {
		this.error_rate = error_rate;
	}
	public int getFeature_id() {
		return feature_id;
	}
	public void setFeature_id(int feature_id) {
		this.feature_id = feature_id;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}	
}
