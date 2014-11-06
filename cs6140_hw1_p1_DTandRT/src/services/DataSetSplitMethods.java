package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import bean.FeatureAndLabel;
import bean.InfoGainPoint;
import bean.MsePoint;
import bean.MyComparator;
import bean.SplitResult;

public class DataSetSplitMethods {
	/*
	 * split dataset into left subtree and right subtree according to best mse
	 */
	public SplitResult get_split_data_points(Vector<Vector<Double>> data_points, 
			Vector<Double> label_values, MsePoint best_mse_point) {
		
		// store the split result here
		SplitResult split_res = new SplitResult();
		int len = label_values.size();
		int feature_index = best_mse_point.getFeature_id();
		double feature_value = best_mse_point.getFeature_value();
		
		// all data points before mse point store into left,
		// rest points store into right
		for (int i = 0; i < len; i++) {
			
			Vector<Double> data_point = data_points.get(i);
			
			// if feature less than or equal to best mse point,
			// add to left tree, else add to right tree
			if (data_point.get(feature_index) <= feature_value) {
				
				split_res.data_opints_split_left.add(data_point);
				split_res.label_values_split_left.add(label_values.get(i));
			}
			else {
				split_res.data_opints_split_right.add(data_point);
				split_res.label_values_split_right.add(label_values.get(i));
			}
		}
		
		return split_res;
	}
	
	/*
	 * find best mse split point in all features
	 */
	public MsePoint get_best_split_point(Vector<Vector<Double>> data_points, 
			Vector<Double> label_values) {
		
		MsePoint best_mse_point = new MsePoint();
		int data_points_num = data_points.size();
		int feature_num = data_points.get(0).size() - 1;
		
		// traverse every value of each feature, computer the mse
		for (int i = 0; i < feature_num; i++) {
			
			// put the each data point's value of feature number i
			// into an array and sort it, find mse point in this feature
			ArrayList<FeatureAndLabel> pair_lst = new ArrayList<FeatureAndLabel>();
			for (int j = 0; j < data_points_num; j++) {
				
				// get the (feature value, label value) pair list for easy sort
				pair_lst.add(new FeatureAndLabel(data_points.get(j).get(i), label_values.get(j)));	
			}
			
			// sort the pair list
		    this.sort_pair_list(pair_lst);
			MsePoint mse_point = this.get_mse(pair_lst);
			
			// find best mse split point in all features
			if (i == 0 || mse_point.getTotal_mse() < best_mse_point.getTotal_mse()) {
				best_mse_point = mse_point;
				best_mse_point.setFeature_id(i);
			}
		}
		return best_mse_point;
	}

	/*
	 * compute MSE in one feature, return the mse point contains info
	 */
	public MsePoint get_mse(ArrayList<FeatureAndLabel> pair_lst) {
		
		int len = pair_lst.size();
		double left_error = 0.0;
		double right_error = 0.0;
		double total_error = 0.0;
		int feature_index = 0;
		double feature_value = 0.0;
		double left_mse = 0.0;
		double right_mse = 0.0;
		double total_mse = 0.0;
		double cur_feature_val;
		double post_feature_val;
		MsePoint mse_point = new MsePoint();
		
		for (int split_index = 0; split_index < len - 1; split_index ++) {

				cur_feature_val = pair_lst.get(split_index).getFeature_val();
				post_feature_val = pair_lst.get(split_index + 1).getFeature_val();
				
				if (cur_feature_val == post_feature_val) {
					continue;
				}
			
			// split the pair list according to each point
			ArrayList<FeatureAndLabel> left_sub_lst = new 
					ArrayList<FeatureAndLabel>(pair_lst.subList(0, split_index + 1));
			ArrayList<FeatureAndLabel> right_sub_lst = new 
					ArrayList<FeatureAndLabel>(pair_lst.subList(split_index + 1, len));
			
			left_error = this.get_se(left_sub_lst);
			right_error = this.get_se(right_sub_lst);
			total_error = left_error + right_error;
			
			// find the minimum total error as mse
			if (total_mse == 0 || total_error < total_mse) {
				
				left_mse = left_error;
				right_mse = right_error;
				total_mse = total_error;
				//System.out.println(left_mse);
				//System.out.println(right_mse);
				feature_value = pair_lst.get(split_index).getFeature_val();
				feature_index = split_index;
			}		
		}
		
		
		mse_point.setFeature_index(feature_index);
		mse_point.setFeature_value(feature_value);
		mse_point.setLeft_mse(left_mse);
		mse_point.setRight_mse(right_mse);
		mse_point.setTotal_mse(total_mse);
		
		return mse_point;
	}
	
	/*
	 * compute square error
	 */
	public double get_se(ArrayList<FeatureAndLabel> pair_lst) {
		
		int size = pair_lst.size();
		double avg = 0.0, sum = 0.0, se = 0.0;
		for (FeatureAndLabel pair : pair_lst) {
			sum += pair.getLabel_val();
		}
		avg = sum / size;
		for (FeatureAndLabel pair : pair_lst) {se += (pair.getLabel_val() - avg)*(pair.getLabel_val() - avg);}
		
		
		
		return se;
	}
	
	/*
	 * sort the feature and label pair list
	 */
	public void sort_pair_list(ArrayList<FeatureAndLabel> pair_lst) {
		
		MyComparator mc = new MyComparator();
		Collections.sort(pair_lst, mc);
	}
	
	public SplitResult get_ig_split_data_points(Vector<Vector<Double>> data_points, 
			Vector<Double> label_values, InfoGainPoint best_mse_point) {
		
		// store the split result here
		SplitResult split_res = new SplitResult();
		int len = label_values.size();
		int feature_index = best_mse_point.getFeature_id();
		double feature_value = best_mse_point.getFeature_value();
		
		// all data points before mse point store into left,
		// rest points store into right
		for (int i = 0; i < len; i++) {
			
			Vector<Double> data_point = data_points.get(i);
			
			// if feature less than or equal to best mse point,
			// add to left tree, else add to right tree
			if (data_point.get(feature_index) <= feature_value) {
				
				split_res.data_opints_split_left.add(data_point);
				split_res.label_values_split_left.add(label_values.get(i));
			}
			else {
				split_res.data_opints_split_right.add(data_point);
				split_res.label_values_split_right.add(label_values.get(i));
			}
		}
		
		return split_res;
	}
	
	/*
	 * compute best info gain point
	 */
	public InfoGainPoint get_best_ig_split_point(Vector<Vector<Double>> data_points, 
			Vector<Double> label_values) {
		
		InfoGainPoint best_ig_point = new InfoGainPoint();
		int data_points_num = data_points.size();
		int feature_num = data_points.get(0).size() - 1;
		
		// traverse every value of each feature, computer the mse
		for (int i = 0; i < feature_num; i++) {
			
			// put the each data point's value of feature number i
			// into an array and sort it, find mse point in this feature
			ArrayList<FeatureAndLabel> pair_lst = new ArrayList<FeatureAndLabel>();
			for (int j = 0; j < data_points_num; j++) {
				
				// get the (feature value, label value) pair list for easy sort
				pair_lst.add(new FeatureAndLabel(data_points.get(j).get(i), label_values.get(j)));	
			}
			
			// sort the pair list
			InfoGainPoint ig_point = this.get_info_gain(pair_lst);
			
			// find best mse split point in all features
			if (i == 0 || ig_point.getTotal_subinfo() > best_ig_point.getTotal_subinfo()) {
				best_ig_point = ig_point;
				best_ig_point.setFeature_id(i);
			}
		}
		return best_ig_point;
	}
	
	/*
	 * compute info gain
	 */
	public InfoGainPoint get_info_gain(ArrayList<FeatureAndLabel> pair_lst) {
		
		InfoGainPoint ig_point = new InfoGainPoint();
		double cur_feature_val;
		double post_feature_val;
		double left_subinfo;
		double right_subinfo;
		double total_subinfo;
		double info_gain = 0;
		double max_ig = 0;
		double info;
		int max_ig_index = 0;
		int len = pair_lst.size();
		info = this.get_info(pair_lst);
		
		this.sort_pair_list(pair_lst);
		
		for (int split_index = 0; split_index < len - 1; split_index ++) {

			cur_feature_val = pair_lst.get(split_index).getFeature_val();
			post_feature_val = pair_lst.get(split_index + 1).getFeature_val();
			
			if (cur_feature_val == post_feature_val) {
				continue;
			}
		
			// split the pair list according to each point
			ArrayList<FeatureAndLabel> left_sub_lst = new 
				ArrayList<FeatureAndLabel>(pair_lst.subList(0, split_index + 1));
			ArrayList<FeatureAndLabel> right_sub_lst = new 
				ArrayList<FeatureAndLabel>(pair_lst.subList(split_index + 1, len));
		
			left_subinfo = this.get_info(left_sub_lst);
			right_subinfo = this.get_info(right_sub_lst);
			total_subinfo = left_subinfo * (double)(split_index + 1) / len + right_subinfo * (double)(len - split_index - 1) / len;
			info_gain = info - total_subinfo;
		
			// find the minimum total error as mse
			if (max_ig < info_gain) {
			
				max_ig = info_gain;
				max_ig_index = split_index;
			}		
		}
		
		ig_point.setTotal_subinfo(max_ig);
		ig_point.setFeature_index(max_ig_index);
		ig_point.setFeature_value(pair_lst.get(max_ig_index).getFeature_val());
		
		return ig_point;
	}
	
	/*
	 * compute info
	 */
	public double get_info(ArrayList<FeatureAndLabel> pair_lst) {
		
		double pos_num = 0;
		double neg_num = 0;
		double size = pair_lst.size();
		double info;
		
		for (FeatureAndLabel pair : pair_lst) {
			
			if (pair.getLabel_val() == 1) 
				pos_num++;
			else 
				neg_num++;
		}
		
		if (pos_num == 0 || neg_num == 0) {
			return 0;
		}
		
		info = (pos_num / size) * (Math.log(size / pos_num) / Math.log(2)) +
			   (neg_num / size) * (Math.log(size / neg_num) / Math.log(2));
		
		return info;
	}
}
