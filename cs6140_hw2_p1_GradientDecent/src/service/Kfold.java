package service;

import java.util.Vector;

import bean.LinearRegression;
import bean.LogisticRegression;

public class Kfold {

	// divide data into 10 folds
	int k = 10;
	int test_num = 460;
	int train_num = 5061;
	
	/*
	 * split data set
	 */
	public void get_kfold_split(Vector<Vector<Double>> dataSet, Vector<Double> label_values, String kind) {
		
		LinearRegression lr = new LinearRegression();
		LogisticRegression lor = new LogisticRegression();
		double[] objective;
		double test_sum_se = 0.0;
		double train_sum_se = 0.0;
		double test_sum_acc = 0.0;
		double train_sum_acc = 0.0;
		double train_se = 0.0;
		double test_se = 0.0;
		double train_acc = 0.0;
		double test_acc = 0.0;
		double[] train_mean;
		double[] train_variance;
		
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
			
			//System.out.println(train_features.size());
			
			train_mean = lr.get_train_mean(train_features);
			train_variance = lr.get_train_variance(train_features, train_mean);
			if (kind == "lr") {
				
				objective = lr.get_objective(train_features, train_labels);
				train_se = lr.get_error(train_features, train_labels, objective);	
				test_se = lr.get_test_error(test_features, test_labels, objective, train_mean, train_variance);
				train_acc = lr.get_acc(train_features, train_labels, objective);
				test_acc = lr.get_test_acc(test_features, test_labels, objective, train_mean, train_variance);
				
				if (k_index == 0) {
					lr.get_confusion_matrix(test_features, test_labels, objective, train_mean, train_variance);
					lr.get_ROC(test_features, test_labels, objective, train_mean, train_variance);
				}	
			}
			
			else if (kind == "lor") {
				
				objective = lor.get_objective(train_features, train_labels);
				train_se = lor.get_logistic_error(train_features, train_labels, objective);
				test_se = lor.get_test_logistic_error(test_features, test_labels, objective, train_mean, train_variance);
				train_acc = lor.get_logistic_acc(train_features, train_labels, objective);
				test_acc = lor.get_logistic_test_acc(test_features, test_labels, objective, train_mean, train_variance);
				
				if (k_index == 0) {
					lor.get_logistic_confusion_matrix(test_features, test_labels, objective, train_mean, train_variance);
					lor.get_ROC(test_features, test_labels, objective, train_mean, train_variance);
				}
					
			}
			
			
			test_sum_acc += test_acc;
			train_sum_acc += train_acc;
			
			test_sum_se += test_se;
			train_sum_se += train_se;
		}
		
		//System.out.println("--training spam data mse: " + train_sum_se / 10);
		//System.out.println("--testing spam data mse: " + test_sum_se / 10);
		System.out.println("--training spam data acc: " + train_sum_acc / 10);
		System.out.println("--testing spam data acc: " + test_sum_acc / 10);
	}
}
