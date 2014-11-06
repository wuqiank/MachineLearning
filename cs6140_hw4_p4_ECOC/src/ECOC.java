import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;


public class ECOC {

	int k = 8;
	int function_num_use = 20;
	int function_num = (int) (Math.pow(2, k - 1) - 1);
	double[][] code_matrix = new double[k][function_num];
	double[][] code_matrix_20 = new double[function_num_use][k];
	double[][] code_matrix_20_revs = new double[k][function_num_use];
	
	int rounds = 200;
	double[][] alpha = new double[function_num_use][rounds];
	int[][] hypo_feature_id = new int[function_num_use][rounds];
	double[][] hypo_threshold = new double[function_num_use][rounds];
	
	public void run(double[][] dataSet, double[][] testSet, HashMap<Integer, HashSet<Double>> id_values) {
		
		ArrayList<Double>[] ds_list = this.get_decision_stump(id_values);
		this.get_ext_codes();
		
		int train_size = dataSet.length;
		int test_size = testSet.length;
		
		for (int i = 0; i < function_num_use; i++) {
			
			double[] labels = code_matrix_20[i];
			double[] dist_train = new double[train_size];
			
			for (int j = 0; j < train_size; j++) {
				dist_train[j] = 1.0 / train_size;
			}
			
			System.out.println("function " + i);
			for (int t = 0; t < rounds; t++) {
				
				System.out.print("round " + t + ",  ");
				// local round error
				double local_err = this.get_optimal_ds(dataSet, ds_list, dist_train, labels, i, t);
				
				// alpha
				alpha[i][t] = 0.5 * Math.log((1 - local_err) / local_err);
				
				// update
				this.update(dataSet, dist_train, alpha[i][t], labels, i, t);
				
				// test error rate
				double test_acc = 0;
				for (int p = 0; p < test_size; p++) {
					
					double prob = 0.0;
					double label = labels[(int) testSet[p][1754]];
					for (int j = 0; j < t + 1; j++) {
						
						prob += alpha[i][j] * hypo_predict(testSet[p], hypo_feature_id[i][t], hypo_threshold[i][t]);
					}
					
					if(prob > 0.5) {
						prob = 1.0;
					}
					else {
						prob = 0.0;
					}
					
					if (prob == label) {
						test_acc ++;
					}
				}
				test_acc /= test_size;
				System.out.println("local test_acc = " + test_acc);
			}
			System.out.println();
		}
		
		
		double count_acc = 0;		
		for (int i = 0; i < test_size; i++) {
			
			double[] test_prob = new double[function_num_use];
			double label = testSet[i][1754];
			double pred = 0.0;
			
			for (int j = 0; j < function_num_use; j++) {
				
				test_prob[j] = 0.0;
				
				for (int t = 0; t < rounds; t++) {
					
					test_prob[j] += alpha[j][t] * hypo_predict(testSet[i], 
							hypo_feature_id[j][t], hypo_threshold[j][t]);
				}
				
				if(test_prob[j] > 0.5) {
					test_prob[j] = 1.0;
				}
				else {
					test_prob[j] = 0.0;
				}
			}
			
			int min_dis = 20;
			for (int j = 0; j < k; j++) {
				
				double[] code = code_matrix_20_revs[j];
				int dis_count = 0;
				
				for (int p = 0; p < function_num_use; p++) {
					
					if (code[p] != test_prob[p]) {
						dis_count ++;
					}
				}
				
				if (dis_count < min_dis) {
					
					min_dis = dis_count;
					pred = j;
				}
			}
			
			if (label == pred) {
				
				count_acc++;
			}
		}
		count_acc /= test_size;
		
		System.out.println("Test Acc = " + count_acc);
	}
	
	public double get_optimal_ds(double[][] dataSet, 
			ArrayList<Double>[] ds_list, double[] dist_train, double[] labels, int index1, int index2) {
		
		int train_size = dataSet.length;
		int ds_size = ds_list.length;
		double max_res = 0.0;
		double best_error = 0.0;
		
		for (int j = 0; j < ds_size; j++) {
			
			for (double ds : ds_list[j]) {
				
				double error_rate = 0;
				for(int i = 0; i < train_size; i++) {
					
					double[] data_point = dataSet[i];
					
					double label = labels[(int)(double)data_point[1754]];
					//System.out.println(label);
					double res = this.hypo_predict(data_point, j, ds);
					if (label != res) {
						error_rate += dist_train[i];
					}
					
				}
				double temp = Math.abs(0.5 - error_rate);
				
				if (temp > max_res) {
					
					max_res = temp;
					best_error = error_rate;
					
					hypo_feature_id[index1][index2] = j;
					hypo_threshold[index1][index2] = ds;
				}		
			}
		}
		
		/*System.out.println("ID: " + hypo_feature_id[index1][index2] + ", Threshold: " + hypo_threshold[index1][index2] +
		", Error_Rate: " + best_error + ", max of |1/2 - error(h)| = " + max_res);*/
				
		return best_error;
	}
	
	// update
	public void update(double[][] dataSet, double[] dist, double alpha, double[] labels, int index1, int index2) {
		
		int size = dataSet.length;
		double sum = 0;
		for (int i = 0; i < size; i++) {
			
			double[] data_point = dataSet[i];
			double label = labels[(int)(double)data_point[1754]];
			double predict_res = this.hypo_predict(data_point, hypo_feature_id[index1][index2], hypo_threshold[index1][index2]);
			
			if (label == predict_res)  {
				dist[i] = dist[i] * Math.exp(-1 * alpha);
			}
			else {
				dist[i] = dist[i] * Math.exp(alpha);
			}
			
			sum += dist[i];
		}
		// nomalize
		for (int i = 0; i < size; i++) {
			
			dist[i] = dist[i] / sum;
		}
	}
	
	// predict according to threshold
	public double hypo_predict(double[] data_point, int feature_id, double threshold) {

		double feature_value = data_point[feature_id];
		
		if (feature_value > threshold) {
			return 1.0;
		}
		else {
			return 0.0;
		}	
	}
	
	
	public ArrayList<Double>[] get_decision_stump(HashMap<Integer, HashSet<Double>> id_values) {
		
		ArrayList<Double>[] ds_array = new ArrayList[1754];
		
		for (Entry<Integer, HashSet<Double>> entry : id_values.entrySet()) {
			
			int id = entry.getKey();
			HashSet<Double> feature_values = entry.getValue();
			Object[] feature_values_lst = feature_values.toArray();
			Arrays.sort(feature_values_lst);
			
			ArrayList<Double> temp = new ArrayList<Double>();
			
			int len = feature_values_lst.length;
			double min_threshold = (Double)feature_values_lst[0] - 1;
			double max_threshold = (Double)feature_values_lst[len - 1] + 1;
			temp.add(min_threshold);
			
			for (int i = 0; i < len - 1; i++) {
				
				double cur = (Double)feature_values_lst[i];
				double post = (Double)feature_values_lst[i + 1];
				temp.add((cur + post) / 2.0);
			}
			temp.add(max_threshold);
			ds_array[id] = temp;
		}
		
		/*for(int i = 0; i < 1754; i++) {
			
			System.out.print("Feature ID: " + i + ", Threshold: ");
			for(double ds : ds_array[i]) {
				System.out.print(ds + " ");
			}
			System.out.println();
		}*/
		
		
		return ds_array;
	}
	
	public void get_ext_codes() {
		
		for (int j = 0; j < function_num; j++) {
			code_matrix[0][j] = 1;
		}
		
		for (int i = 1; i < k; i++) {
			
			code_matrix[i] = this.get_new_line(code_matrix[i - 1]);
		}
		
		/*for (int i = 0; i < k; i++) {
			for (int j = 0; j < function_num; j++) {
				
				System.out.print(code_matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();*/
		
		this.get_20_functions();	
	}
	
	public void get_20_functions() {
		
		int interval = function_num / function_num_use;
		
		for (int i = 0; i < k; i++){
			for (int j = 0; j < function_num_use; j++) {
				code_matrix_20[j][i] = code_matrix[i][j * (interval-1)];
			}
		}
		
		for (int i = 0; i < k; i++){
			for (int j = 0; j < function_num_use; j++) {
				code_matrix_20_revs[i][j] = code_matrix[i][j * (interval-1)];
			}
		}
		
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < k; j++) {
			
				System.out.print(code_matrix_20[i][j] + " ");
			}
			System.out.println();
		}
		
		/*for (int i = 0; i < k; i++) {
			for (int j = 0; j < 20; j++) {
			
				System.out.print(code_matrix_20_revs[i][j] + " ");
			}
			System.out.println();
		}*/
		
	}
	
	public double[] get_new_line(double[] code_line) {
		
		double[] res = new double[function_num];
		int count_start = 0;
		int count_same = 0;
		
		for (int i = 1; i < function_num; i++) {
			
			if (code_line[i] == code_line[i - 1]) {
				count_same++;
			}
			else {
					
				int half_point = (count_same + 2) / 2;		
				for (int j = 0; j < half_point; j++) {
					res[j + count_start] = 0;
				}
				
				for (int j = half_point; j < count_same + 1; j++) {
					res[j + count_start] = 1;
				}
				
				count_start += count_same + 1;
				count_same = 0;
			}
			
			if (i == function_num - 1) {
				
				//System.out.println(count_same);
				int half_point = (count_same + 2) / 2;
				
				for (int j = 0; j < half_point; j++) {
					res[j + count_start] = 0;
				}
				for (int j = half_point; j < count_same + 1; j++) {
					res[j + count_start] = 1;
				}
				
				count_start = count_same;
			}
		}
		
		return res;
	}
}
