package service;

import java.util.Vector;

import bean.LinearRegression;

public class Kfold {

	// divide data into 10 folds
	int k = 10;
	int test_num = 460;
	int train_num = 5061;
	
	/*
	 * split data set
	 */
	public void get_kfold_split(Vector<Vector<Double>> dataSet, Vector<Double> label_values) {
		
		Vector<Vector<Double>> train_features = new Vector<Vector<Double>>();
		Vector<Double> train_labels = new Vector<Double>();
		
		Vector<Vector<Double>> test_features = new Vector<Vector<Double>>();
		Vector<Double> test_labels = new Vector<Double>();
		LinearRegression lr = new LinearRegression();
		double test_sum_se = 0.0;
		double train_sum_se = 0.0;
		double test_sum_acc = 0.0;
		double train_sum_acc = 0.0;
		
		for (int k_index = 0; k_index < 10; k_index++) {
			
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
			
			double[] objective = lr.get_objective(train_features, train_labels);
			double test_se = lr.get_error(test_features, test_labels, objective);
			double train_se = lr.get_error(train_features, train_labels, objective);
			test_sum_se += test_se;
			train_sum_se += train_se;
			
			double train_acc = lr.get_acc(train_features, train_labels, objective);
			double test_acc = lr.get_acc(test_features, test_labels, objective);
			test_sum_acc += test_acc;
			train_sum_acc += train_acc;
		}
		
		System.out.println("testing spam data mse: " + test_sum_se / 10);
		System.out.println("training spam data mse: " + train_sum_se / 10);
		
		System.out.println("testing spam data acc: " + test_sum_acc / 10);
		System.out.println("training spam data acc: " + train_sum_acc / 10);
	}
}
