

public class MsePoint {

	public int feature_id; // nth feature
	public int feature_index;// nth point in sorted feature
	public double feature_value;
	public double left_mse;
	public double right_mse;
	public double total_mse;
	
	/*
	 * Getters and Setters
	 */
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
	public int getFeature_id() {
		return feature_id;
	}
	public void setFeature_id(int feature_id) {
		this.feature_id = feature_id;
	}
	public double getLeft_mse() {
		return left_mse;
	}
	public void setLeft_mse(double left_mse) {
		this.left_mse = left_mse;
	}
	public double getRight_mse() {
		return right_mse;
	}
	public void setRight_mse(double right_mse) {
		this.right_mse = right_mse;
	}
	public double getTotal_mse() {
		return total_mse;
	}
	public void setTotal_mse(double total_mse) {
		this.total_mse = total_mse;
	}
}
