package services;

import java.util.Vector;
import bean.DecisionTree;

public class Kfold {

	// divide data into 10 folds
	int k = 10;
	int test_num = 460;
	int train_num = 5061;
	
	/*
	 * split data set
	 */
	public void get_kfold_split(Vector<Vector<Double>> dataSet, Vector<Double> label_values) {
		
		
		
		
		double train_sum_se = 0.0;
		double test_sum_se = 0.0;
		double train_sum_acc = 0.0;
		double test_sum_acc = 0.0;
		
		for (int k_index = 0; k_index < 10; k_index++) {
			
			Vector<Vector<Double>> train_features = new Vector<Vector<Double>>();
			Vector<Double> train_labels = new Vector<Double>();
			Vector<Vector<Double>> test_features = new Vector<Vector<Double>>();
			Vector<Double> test_labels = new Vector<Double>();
			
			for (int i = 0; i < label_values.size(); i++) {
				
				if (i >= k_index * test_num && i <= (k_index + 1) * test_num) {
					test_features.add(dataSet.get(i));
					test_labels.add(label_values.get(i));
				}
				else {
					train_features.add(dataSet.get(i));
					train_labels.add(label_values.get(i));
				}	
			}
			
			DecisionTree dt = new DecisionTree();
			dt.DT_Creator(train_features, train_labels, 0);
			double train_se = dt.get_mse_travese_tree(train_features);
			double test_se = dt.get_mse_travese_tree(test_features);		
			train_sum_se += train_se;
			test_sum_se += test_se;
			
			double train_acc = dt.get_acc_travese_tree(train_features);
			double test_acc = dt.get_acc_travese_tree(test_features);
			train_sum_acc += train_acc;
			test_sum_acc += test_acc;
			
			if (k_index == 0) {
				
				//System.out.println(test_features.size());
				dt.get_confusion_matrix(test_features);
			}
		}
		
		System.out.println("spam data training mse = " + train_sum_se / 10);
		System.out.println("spam data testing mse = " + test_sum_se / 10);
		
		System.out.println("spam data training acc = " + train_sum_acc / 10);
		System.out.println("spam data testing acc = " + test_sum_acc / 10);
	}
}
