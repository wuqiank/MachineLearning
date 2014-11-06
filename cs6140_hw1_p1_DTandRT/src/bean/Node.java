package bean;

public class Node {

	int feature_index;
	int left_data_num;
	int right_data_num;
	double feature_value;
	double left_mse;
	double right_mse;
	double total_sub_ig;
	double left_label_value;
	double right_label_value;
	
	Node left_tree;
	Node right_tree;
	
	public Node(int feature_index, double feature_value, int left_data_num, 
			int right_data_num, double left_mse, double right_mse, 
			double left_label_value, double right_label_value) {
		
		this.feature_index = feature_index;
		this.left_data_num = left_data_num;
		this.right_data_num = right_data_num;
		this.feature_value = feature_value;
		this.left_mse = left_mse;
		this.right_mse = right_mse;
		this.left_label_value = left_label_value;
		this.right_label_value = right_label_value;
	}
	
	public Node(int feature_index, double feature_value, int left_data_num, 
			int right_data_num, double total_sub_ig, double left_label_value, double right_label_value) {
		
		this.feature_index = feature_index;
		this.left_data_num = left_data_num;
		this.right_data_num = right_data_num;
		this.feature_value = feature_value;
		this.total_sub_ig = total_sub_ig;
		this.left_label_value = left_label_value;
		this.right_label_value = right_label_value;
	}
	
	public double getTotal_sub_ig() {
		return total_sub_ig;
	}

	public void setTotal_sub_ig(double total_sub_ig) {
		this.total_sub_ig = total_sub_ig;
	}

	public int getFeature_index() {
		return feature_index;
	}
	public void setFeature_index(int feature_index) {
		this.feature_index = feature_index;
	}
	public int getLeft_data_num() {
		return left_data_num;
	}
	public void setLeft_data_num(int left_data_num) {
		this.left_data_num = left_data_num;
	}
	public int getRight_data_num() {
		return right_data_num;
	}
	public void setRight_data_num(int right_data_num) {
		this.right_data_num = right_data_num;
	}
	public double getFeature_value() {
		return feature_value;
	}
	public void setFeature_value(double feature_value) {
		this.feature_value = feature_value;
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
	public Node getLeft_tree() {
		return left_tree;
	}
	public void setLeft_tree(Node left_tree) {
		this.left_tree = left_tree;
	}
	public Node getRight_tree() {
		return right_tree;
	}
	public void setRight_tree(Node right_tree) {
		this.right_tree = right_tree;
	}

	public double getLeft_label_value() {
		return left_label_value;
	}

	public void setLeft_label_value(double left_label_value) {
		this.left_label_value = left_label_value;
	}

	public double getRight_label_value() {
		return right_label_value;
	}

	public void setRight_label_value(double right_label_value) {
		this.right_label_value = right_label_value;
	}
	
	/*
	 * Getters and Setters
	 */
	
}
