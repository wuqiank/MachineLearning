

import java.util.Vector;

public class SplitResult {

	public Vector<Vector<Double>> data_opints_split_left;
	public Vector<Vector<Double>> data_opints_split_right;
	public double left_feature_value;
	public double right_feature_value;
	public Vector<Double> label_values_split_left;
	public Vector<Double> label_values_split_right;
	
	/*
	 * Getters and Setters
	 */
	public Vector<Vector<Double>> getData_opints_split_left() {
		return data_opints_split_left;
	}
	public void setData_opints_split_left(
			Vector<Vector<Double>> data_opints_split_left) {
		this.data_opints_split_left = data_opints_split_left;
	}
	public Vector<Vector<Double>> getData_opints_split_right() {
		return data_opints_split_right;
	}
	public void setData_opints_split_right(
			Vector<Vector<Double>> data_opints_split_right) {
		this.data_opints_split_right = data_opints_split_right;
	}
	public double getLeft_feature_value() {
		return left_feature_value;
	}
	public void setLeft_feature_value(double left_feature_value) {
		this.left_feature_value = left_feature_value;
	}
	public double getRight_feature_value() {
		return right_feature_value;
	}
	public void setRight_feature_value(double right_feature_value) {
		this.right_feature_value = right_feature_value;
	}
	public Vector<Double> getLabel_values_split_left() {
		return label_values_split_left;
	}
	public void setLabel_values_split_left(Vector<Double> label_values_split_left) {
		this.label_values_split_left = label_values_split_left;
	}
	public Vector<Double> getLabel_values_split_right() {
		return label_values_split_right;
	}
	public void setLabel_values_split_right(Vector<Double> label_values_split_right) {
		this.label_values_split_right = label_values_split_right;
	}
	
	/*
	 * Constructor
	 */
	public SplitResult() {
		data_opints_split_left = new Vector<Vector<Double>>();
		data_opints_split_right = new Vector<Vector<Double>>();
		label_values_split_left     = new Vector<Double>();
		label_values_split_right    = new Vector<Double>();
		left_feature_value = 0.0;
		right_feature_value = 0.0;
	}
}
