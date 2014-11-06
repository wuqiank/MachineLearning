package bean;

import java.util.Vector;

import services.DataSetSplitMethods;

public class DecisionTree {
	int max_depth = 4;
	Node root = null;

	public Node DT_Creator(Vector<Vector<Double>> data_points, Vector<Double> label_values, int depth) {
		
		// termination point
		if (data_points.size() <= 1 || depth > max_depth) {
			return null;
		}
		
		DataSetSplitMethods dsm = new DataSetSplitMethods();
		InfoGainPoint best_ig_point = dsm.get_best_ig_split_point(data_points, label_values);
		SplitResult split_res = dsm.get_ig_split_data_points(data_points, label_values, best_ig_point);
		
		double left_label_value;
		double right_label_value;
		double left_label_0 = 0;
		double left_label_1 = 0;
		double right_label_0 = 0;
		double right_label_1 = 0;
		
		for (double label_value : split_res.getLabel_values_split_left()) {
			if (label_value == 0)
				left_label_0 ++;
			else if (label_value == 1)
				left_label_1 ++;
		}
		if (left_label_0 >= left_label_1) {
			left_label_value = 0;
		}
		else {
			left_label_value = 1;
		}
		
		for (double label_value : split_res.getLabel_values_split_right()) {
			if (label_value == 0)
				right_label_0 ++;
			else if (label_value == 1)
				right_label_1 ++;
		}
		if (right_label_0 >= right_label_1) {
			right_label_value = 0;
		}
		else {
			right_label_value = 1;
		}
		
		if (split_res.getData_opints_split_left().size() == data_points.size()) {
			return null;
		}
		
		// create a new node	
		Node new_node = new Node(best_ig_point.getFeature_id(), best_ig_point.getFeature_value(),
				split_res.getData_opints_split_left().size(), split_res.getData_opints_split_right().size(),
				best_ig_point.getTotal_subinfo(), left_label_value, right_label_value);
		depth++;
	
		if (root == null) {
			
			root = new_node;
			if (best_ig_point.getTotal_subinfo() != 0) {
				root.left_tree = DT_Creator(split_res.getData_opints_split_left(), 
						split_res.getLabel_values_split_left(), depth);
				root.right_tree = DT_Creator(split_res.getData_opints_split_right(), 
						split_res.getLabel_values_split_right(), depth);
			}
		}
		else {
			if (best_ig_point.getTotal_subinfo() != 0) {
				new_node.left_tree = DT_Creator(split_res.getData_opints_split_left(), 
						split_res.getLabel_values_split_left(), depth);
				new_node.right_tree = DT_Creator(split_res.getData_opints_split_right(), 
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
	 * confusion matrix
	 */
	public void get_confusion_matrix(Vector<Vector<Double>> data_points) {
		
		int TP = 0;
		int TN = 0;
		int FP = 0;
		int FN = 0;
		String flag = "";
		
		double len = data_points.size();
		
		for (int i = 0; i < len; i++) {
		
			Vector<Double> data_point = data_points.get(i);
			flag = get_single_confusion_matrix(root, data_point);
			
			//System.out.println("flag = " + flag);
			
			if (flag == "TP") {
				TP++;
			}
			else if (flag == "TN") {
				TN++;
			}
			else if (flag == "FP") {
				FP++;
			}
			else if (flag == "FN") {
				FN++;
			}
		}
		System.out.println("Decision Tree Confusion Matrix");
		System.out.println("TP = " + TP + "    FN = " + FN);
		System.out.println("FP = " + FP + "    TN = " + TN);
	}
	
	public String get_single_confusion_matrix(Node cur_node, Vector<Double> data_point) {
		
		int label_index = data_point.size();
		int cur_feature_index = cur_node.getFeature_index();
		double cur_feature_value = cur_node.getFeature_value();
		double cur_feature_label;
		double data_feature_value = data_point.get(cur_feature_index);
		String flag = "";

		if (data_feature_value <= cur_feature_value) {
			
			if (cur_node.getLeft_tree() == null) {
				
				cur_feature_label = cur_node.getLeft_label_value();
				
				if (cur_feature_label == data_point.get(label_index - 1)) {	
					
					if (cur_feature_label == 1) {
						flag =  "TP";
					}
					else {
						flag = "TN";
					}
				}
				else {
					
					if (cur_feature_label == 0) {
						flag =  "FN";
					}
					else {
						flag = "FP";
					}
				}
			}
			else {
				flag = get_single_confusion_matrix(cur_node.getLeft_tree(), data_point);
			}
		}
		else {
			
			if (cur_node.getRight_tree() == null) {
				cur_feature_label = cur_node.getRight_label_value();
				if (cur_feature_label == data_point.get(label_index - 1)) {	
					
					if (cur_feature_label == 1) {
						flag =  "TP";
					}
					else {
						flag = "TN";
					}
				}
				else {
					
					if (cur_feature_label == 1) {
						flag =  "FP";
					}
					else {
						flag = "FN";
					}
				}
			}
			else {		
				flag = get_single_confusion_matrix(cur_node.getRight_tree(), data_point);
			}
		}
		
		return flag;
	}
	
	
	/*
	 * compute acc 
	 */
	public double get_acc_travese_tree(Vector<Vector<Double>> data_points) {
		
		double count_true = 0;
		double len = data_points.size();
		
		for (int i = 0; i < len; i++) {
		
			Vector<Double> data_point = data_points.get(i);
			if (get_label_single(root, data_point))
				count_true++;
			//System.out.println(get_mse_single(root, data_point));
		}
		
		return count_true / len;
	}
	
	public boolean get_label_single(Node cur_node, Vector<Double> data_point) {
		
		int label_index = data_point.size();
		int cur_feature_index = cur_node.getFeature_index();
		double cur_feature_value = cur_node.getFeature_value();
		double cur_feature_label;
		double data_feature_value = data_point.get(cur_feature_index);
		boolean flag;

		if (data_feature_value <= cur_feature_value) {
			
			if (cur_node.getLeft_tree() == null) {
				
				cur_feature_label = cur_node.getLeft_label_value();
				if (cur_feature_label == data_point.get(label_index - 1))
					flag =  true;
				else 
					flag =  false;
				/*
				se = (cur_feature_label - data_point.get(label_index - 1)) *
					 (cur_feature_label - data_point.get(label_index - 1));
					 */
			}
			else {
				flag = get_label_single(cur_node.getLeft_tree(), data_point);
			}
		}
		else {
			
			if (cur_node.getRight_tree() == null) {
				cur_feature_label = cur_node.getRight_label_value();
				if (cur_feature_label == data_point.get(label_index - 1))
					flag =  true;
				else 
					flag =  false;
				/*
				se = (cur_feature_label - data_point.get(label_index - 1)) *
					 (cur_feature_label - data_point.get(label_index - 1));
					 */
			}
			else {		
				flag = get_label_single(cur_node.getRight_tree(), data_point);
			}
		}
		
		return flag;
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
	public void print_DT(Node cur_node, String kind, String append, double ig, double count, double label) {
	
		
		if (cur_node != null) {
			// print current node information
			System.out.println(append + "(X[id=" + cur_node.getFeature_index() + "] " + kind + " "
					+ cur_node.getFeature_value() + "; Info Gain=" + ig + "; Count=" + count + 
					"; Label=" + label + ")");
			
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
				print_DT(sub_tree, "<=", append + "|   ", sub_tree.getTotal_sub_ig(), sub_tree.getLeft_data_num(), sub_tree.getLeft_label_value());
				print_DT(sub_tree, ">", append + "|   ", sub_tree.getTotal_sub_ig(), sub_tree.getRight_data_num(), sub_tree.getRight_label_value());
			}
		}
	}
	
	public void print_nodes() {
		System.out.println("Decision Tree, MAX Depth: " + (max_depth+2) );
		System.out.println("Root");
		print_DT(root, "<=", "|   ", root.total_sub_ig, root.left_data_num, root.left_label_value);
		print_DT(root, ">", "|   ", root.total_sub_ig, root.right_data_num, root.right_label_value);
	}
}
