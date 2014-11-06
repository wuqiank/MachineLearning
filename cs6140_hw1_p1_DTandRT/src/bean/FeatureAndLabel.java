package bean;

public class FeatureAndLabel {

	Double feature_val;
	Double label_val;
	
	public FeatureAndLabel(double x, double y) {
		this.feature_val = x;
		this.label_val = y;
	}

	public Double getFeature_val() {
		return feature_val;
	}

	public void setFeature_val(Double feature_val) {
		this.feature_val = feature_val;
	}

	public Double getLabel_val() {
		return label_val;
	}

	public void setLabel_val(Double label_val) {
		this.label_val = label_val;
	}
	
	
}
