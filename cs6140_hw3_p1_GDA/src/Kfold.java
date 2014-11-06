import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import Jama.Matrix;

public class Kfold {

	// divide data into 10 folds
	int k = 10;
	int test_num = 460;
	
	/*
	 * split data set
	 */
	public void get_kfold_split(Vector<Vector<Double>> dataSet) {
		
		double total_acc_train = 0;
		double total_acc_test = 0;
		double size = dataSet.size();
		
		// begin test
		HashMap<Double, Double> feature1_freq = new HashMap<Double, Double>();
		HashMap<Double, Double> feature2_freq = new HashMap<Double, Double>();
		
		for(int i = 0; i < size; i++) {
			
			double feature1 = dataSet.get(i).get(1);
			double feature2 = dataSet.get(i).get(2);
			
			if (feature1_freq.containsKey(feature1)) {
				feature1_freq.put(feature1, feature1_freq.get(feature1) + 1);
			}
			else {
				feature1_freq.put(feature1, 1.0);
			}
			
			if (feature2_freq.containsKey(feature2)) {
				feature2_freq.put(feature2, feature2_freq.get(feature2) + 1);
			}
			else {
				feature2_freq.put(feature2, 1.0);
			}

		}
		
		ArrayList<Entry<Double, Double>> feature1_freq_lst = new ArrayList(feature1_freq.entrySet());
		Collections.sort(feature1_freq_lst , new Comparator<Object>() {
            public int compare(Object o1 , Object o2 )
            {
				Map.Entry e1 = (Map.Entry)o1 ;
				Map.Entry e2 = (Map.Entry)o2 ;
                Double first = (Double)e1.getKey();
                Double second = (Double)e2.getKey();
                return first.compareTo(second);
            }
        });
		
		ArrayList<Entry<Double, Double>> feature2_freq_lst = new ArrayList(feature2_freq.entrySet());
		Collections.sort(feature2_freq_lst , new Comparator<Object>() {
            public int compare(Object o1 , Object o2 )
            {
				Map.Entry e1 = (Map.Entry)o1 ;
				Map.Entry e2 = (Map.Entry)o2 ;
                Double first = (Double)e1.getKey();
                Double second = (Double)e2.getKey();
                return first.compareTo(second);
            }
        });
		
		for (Entry<Double, Double> entry : feature1_freq_lst) {
		    System.out.print(entry.getKey() + " ");
		}
		System.out.println();
		for (Entry<Double, Double> entry : feature1_freq_lst) {
		    System.out.print(entry.getValue() + " ");
		}
		System.out.println();
		System.out.println();
		for (Entry<Double, Double> entry : feature2_freq_lst) {
		    System.out.print(entry.getKey() + " ");
		}
		System.out.println();
		for (Entry<Double, Double> entry : feature2_freq_lst) {
		    System.out.print(entry.getValue() + " ");
		}
		
		// end test
		
		for (int k_index = 0; k_index < 10; k_index++) {
			
			Vector<Vector<Double>> train_features = new Vector<Vector<Double>>();
			Vector<Vector<Double>> test_features = new Vector<Vector<Double>>();
			double acc_train = 0;
			double acc_test = 0;
			
			for (int i = 0; i < size; i++) {
				
				if (i >= k_index * test_num && i <= (k_index + 1) * test_num) {
					test_features.add(dataSet.get(i));
				}
				else {
					train_features.add(dataSet.get(i));
				}	
			}
			
			double[][] Cov = this.get_cov(train_features);
			double[] mean_0 = this.get_mean_vector(train_features, 0.0);
			double[] mean_1 = this.get_mean_vector(train_features, 1.0);
			
			acc_train = this.get_acc(train_features, mean_0, mean_1, Cov) ;
			total_acc_train += acc_train;
			
			acc_test = this.get_acc(test_features, mean_0, mean_1, Cov) ;
			total_acc_test += acc_test;
		}
		
		System.out.println("training spam data acc: " + total_acc_train / 10);
		System.out.println("testing spam data acc: " + total_acc_test / 10);
	}
	
	// get acc
	public double get_acc(Vector<Vector<Double>> dataSet, double[] mean_0, double[] mean_1, double[][] Cov) {
		
		double acc = 0;
		double acc_count = 0;
		int len = dataSet.get(0).size();
		
		for (Vector<Double> data_point : dataSet) {
			
			double flag = 0.0;
			double prob_0 = this.get_prob(mean_0, Cov, data_point);
			double prob_1 = this.get_prob(mean_1, Cov, data_point);
			double label = data_point.get(len - 1);
			
			if (prob_0 >= prob_1) {
				flag = 0.0;
			}
			else {
				flag = 1.0;
			}
			
			if (flag == label) {
				acc_count++;
			}
		}
		
		acc = acc_count / dataSet.size();
		return acc;
	}
	
	// compute mean vector
	public double[] get_mean_vector(Vector<Vector<Double>> dataSet, double label) {
		
		double size = 0.0;
		int dim = dataSet.get(0).size() - 1;
		double[] mean_vector = new double[dim];
		
		// initialize
		for(int i = 0; i < dim; i++) {  
			mean_vector[i] = 0.0;
		}
		
		for (Vector<Double> data_point : dataSet) {
			if (data_point.get(dim) == label) {
				for(int i = 0; i < dim; i++) {  
					mean_vector[i] += data_point.get(i);
				}
				size++;
			}
		}
		
		for(int i = 0; i < dim; i++) {  
			mean_vector[i] = mean_vector[i] / size;
		}
		
		return mean_vector;
	}
	
	// compute Cov
	public double[][] get_cov(Vector<Vector<Double>> dataSet) {
		
		double size = dataSet.size();
		int dim = dataSet.get(0).size() - 1;
		double[] feature_mean = new double[dim];
		double[][] Cov = new double[dim][dim];
		
		// get mean for each feature
		for(int i = 0; i < dim; i++) {  
			feature_mean[i] = 0;
			for (Vector<Double> data_point : dataSet) {
				feature_mean[i] += data_point.get(i);
			}
			feature_mean[i] = feature_mean[i] / size;
        }
		
		// get Covarience
		for(int i = 0; i < dim; i++) {  
            
			double[] tmp = Cov[i];  
            for(int j = 0; j < dim; j++) {  
                
            	double cov = 0;  
                for(Vector<Double> data : dataSet) {  
                    cov += (data.get(i) - feature_mean[i]) * (data.get(j) - feature_mean[j]);  
                }  
                tmp[j] = cov / (size - 1);  
            }  
            Cov[i] = tmp;  
        }		
		return Cov;
	}
	
	// compute density estimation
	public double get_prob(double[] mean_vector, double[][] Cov, Vector<Double> data) {
		
		int dim = data.size() - 1;
		
		double[] x = new double[dim];
		
		for (int i = 0; i < dim; i++) {
			x[i] = data.get(i);
		}
		
		double prob = 0.0;
		double pi = Math.PI;
		Matrix Cov_mx = new Matrix(Cov);
		Matrix x_mx = new Matrix(x, 1).transpose();
		Matrix mean_vector_mx = new Matrix(mean_vector, 1).transpose();
		double det = Cov_mx.det();
		double part1 = 1 / (Math.pow(2 * pi, dim / 2) * Math.pow(det, 0.5));
		Matrix diff = x_mx.minus(mean_vector_mx);
		Matrix diff_T = diff.transpose();
		Matrix inver = Cov_mx.inverse();
		
		//System.out.println(diff.getRowDimension());
		
		double matrix_res = (diff_T.times(inver)).times(diff).get(0, 0) * -0.5;
		
		prob = part1 * Math.exp(matrix_res);
		
		return prob;
	}
}
