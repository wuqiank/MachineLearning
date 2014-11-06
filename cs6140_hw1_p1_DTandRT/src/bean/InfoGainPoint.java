package bean;

public class InfoGainPoint {

	public int feature_id; // nth feature
	public int feature_index;// nth point in sorted feature
	public double feature_value;
	public double left_subinfo;
	public double right_subinfo;
	public double total_subinfo;
	public int getFeature_id() {
		return feature_id;
	}
	public void setFeature_id(int feature_id) {
		this.feature_id = feature_id;
	}
	public int getFeature_index() {
		return feature_index;
	}
	public void setFeature_index(int feature_index) {
		this.feature_index = feature_index;
	}
	public double getFeature_value() {
		return feature_value;
	}
	public void setFeature_value(double feature_value) {
		this.feature_value = feature_value;
	}
	public double getLeft_subinfo() {
		return left_subinfo;
	}
	public void setLeft_subinfo(double left_subinfo) {
		this.left_subinfo = left_subinfo;
	}
	public double getRight_subinfo() {
		return right_subinfo;
	}
	public void setRight_subinfo(double right_subinfo) {
		this.right_subinfo = right_subinfo;
	}
	public double getTotal_subinfo() {
		return total_subinfo;
	}
	public void setTotal_subinfo(double total_subinfo) {
		this.total_subinfo = total_subinfo;
	}
}
