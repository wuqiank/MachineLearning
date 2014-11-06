package bean;

import java.util.Vector;
import services.DataSetSplitMethods;

public class RegressionTree {

	int max_depth = 1;
	Node root = null;

	public Node RT_Creator(Vector<Vector<Double>> data_points, Vector<Double> label_values, int depth) {
		
		// termination point
		if (data_points.size() <= 1 || depth > max_depth) {
			return null;
		}
		
		DataSetSplitMethods dsm = new DataSetSplitMethods();
		MsePoint best_mse_point = dsm.get_best_split_point(data_points, label_values);
		SplitResult split_res = dsm.get_split_data_points(data_points, label_values, best_mse_point);
		
		if (split_res.getData_opints_split_left().size() == data_points.size()) {
			return null;
		}
		
		double left_label_value = 0;
		double right_label_value = 0;
		
		for (double label_value : split_res.getLabel_values_split_left()) {
			left_label_value += label_value;
		}
		left_label_value = left_label_value / split_res.getLabel_values_split_left().size();
		
		for (double label_value : split_res.getLabel_values_split_right()) {
			right_label_value += label_value;
		}
		right_label_value = right_label_value / split_res.getLabel_values_split_right().size();
		
		// create a new node	
		Node new_node = new Node(best_mse_point.getFeature_id(), best_mse_point.getFeature_value(),
				split_res.getData_opints_split_left().size(), split_res.getData_opints_split_right().size(),
				best_mse_point.getLeft_mse(), best_mse_point.getRight_mse(), left_label_value, right_label_value);
		depth++;
	
		
		
		if (root == null) {
			
			root = new_node;
			if (best_mse_point.getLeft_mse() != 0) {
				root.left_tree = RT_Creator(split_res.getData_opints_split_left(), 
						split_res.getLabel_values_split_left(), depth);
			}
			if (best_mse_point.getRight_mse() != 0) {
				root.right_tree = RT_Creator(split_res.getData_opints_split_right(), 
						split_res.getLabel_values_split_right(), depth);
			}
		}
		else {
			if (best_mse_point.getLeft_mse() != 0) {
				new_node.left_tree = RT_Creator(split_res.getData_opints_split_left(), 
						split_res.getLabel_values_split_left(), depth);
			}
			if (best_mse_point.getRight_mse() != 0) {
				new_node.right_tree = RT_Creator(split_res.getData_opints_split_right(), 
						split_res.getLabel_values_split_right(), depth);
			}		
		}
		
		return new_node;
	}
	
	/*
	 * compute mse according to the tree
	 */
	public double get_mse_travese_tree(Vector<Vector<Double>> data_points) {
		
		double sum_se = 0;
		double len = data_points.size();
		
		for (int i = 0; i < len; i++) {
		
			Vector<Double> data_point = data_points.get(i);
			sum_se += get_mse_single(root, data_point);
			//System.out.println(get_mse_single(root, data_point));
		}
		
		return sum_se / len;
	}
	
	/*
	 * get se for one data
	 */
	public double get_mse_single(Node cur_node, Vector<Double> data_point) {
		
		int label_index = data_point.size();
		int cur_feature_index = cur_node.getFeature_index();
		double cur_feature_value = cur_node.getFeature_value();
		double cur_feature_label;
		double se = 0;
		double data_feature_value = data_point.get(cur_feature_index);
		
		if (data_feature_value <= cur_feature_value) {
			
			if (cur_node.getLeft_tree() == null) {
				
				cur_feature_label = cur_node.getLeft_label_value();
				se = (cur_feature_label - data_point.get(label_index - 1)) *
					 (cur_feature_label - data_point.get(label_index - 1));
			}
			else {
				se = get_mse_single(cur_node.getLeft_tree(), data_point);
			}
		}
		else {
			
			if (cur_node.getRight_tree() == null) {
				cur_feature_label = cur_node.getRight_label_value();
				se = (cur_feature_label - data_point.get(label_index - 1)) *
					 (cur_feature_label - data_point.get(label_index - 1));
			}
			else {		
				se = get_mse_single(cur_node.getRight_tree(), data_point);
			}
		}
		
		return se;
	}
	
	/*
	 * print the tree
	 */
	public void print_RT(Node cur_node, String kind, String append, double mse, double count, double label_value) {
	
		
		if (cur_node != null) {
			// print current node information
			System.out.println(append + "(X[id=" + cur_node.getFeature_index() + "] " + kind + " "
					+ cur_node.getFeature_value() + "; MSE=" + mse + "; Count=" + count + 
					"; Label=" + label_value + ")");
			
			//System.out.println(cur_node.getFeature_index());
			//System.out.println(cur_node.getFeature_value());
			
			Node sub_tree = null;
			
			// if left tree
			if (kind == "<=") {
				sub_tree = cur_node.getLeft_tree();
			}
			else if (kind == ">") {
				sub_tree = cur_node.getRight_tree();
			}
			
			if (sub_tree != null) {
				print_RT(sub_tree, "<=", append + "|   ", sub_tree.getLeft_mse(), sub_tree.getLeft_data_num(), sub_tree.getLeft_label_value());
				print_RT(sub_tree, ">", append + "|   ", sub_tree.getRight_mse(), sub_tree.getRight_data_num(), sub_tree.getRight_label_value());
			}
		}
	}
	
	public void print_nodes() {
		System.out.println("Regression Tree, MAX Depth: " + (max_depth+2));
		System.out.println("Root");
		print_RT(root, "<=", "|   ", root.left_mse, root.left_data_num, root.left_label_value);
		print_RT(root, ">", "|   ", root.right_mse, root.right_data_num, root.right_label_value);
	}
}
