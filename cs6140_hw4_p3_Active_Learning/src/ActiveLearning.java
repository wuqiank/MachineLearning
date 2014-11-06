import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;


public class ActiveLearning {

	double test_percent = 10;
	double active_start_percent = 5;
	double increase_percent = 2;
	double rounds = 100;
	double size;
	double dim;
	double train_size;
	double fixed_test_size;
	double[] local_err = new double[(int)rounds];
	double[] train_err = new double[(int)rounds];
	double[] test_err = new double[(int)rounds];
	double[] test_auc = new double[(int)rounds];
	double[] TPR_nodes;
	double[] FPR_nodes;
	
	public void run(Vector<Vector<Double>> dataSet, String kind) {
		
		size = dataSet.size();
		dim = dataSet.get(0).size();
		fixed_test_size = Math.floor(size * test_percent / 100);
		TPR_nodes = new double[(int)fixed_test_size];
		FPR_nodes = new double[(int)fixed_test_size];
		
		double[][] test = new double[(int) fixed_test_size][(int) dim];
		double[][] train_all = new double[(int)(size - fixed_test_size)][(int) dim];
		
		
		// fixed size for testing
		for (int i = 0; i < fixed_test_size; i++) {
			for (int j = 0; j < dim; j++) {
				
				test[i][j] = dataSet.get(i).get(j);
			}
		}
		for (int i = (int) fixed_test_size; i < size; i++) {
			for (int j = 0; j < dim; j++) {
				
				train_all[(int) (i - fixed_test_size)][j] = dataSet.get(i).get(j);
			}
		}
		
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(5);
		values.add(10);
		values.add(15);
		values.add(20);
		values.add(30);
		values.add(50);
		
		if (kind == "active") {
			
			System.out.println("Active Learning");
			System.out.println("Testing Error:");
			for (int value : values) {
				
				//System.out.println("c = " + value);
				
				double train_all_size = train_all.length;
				double max_train_size = Math.floor(train_all_size * value / 100);

				
				Vector<Vector<Double>> train_start = new Vector<Vector<Double>>();
				Vector<Vector<Double>> train_remain = new Vector<Vector<Double>>();
				
				double train_start_size = Math.floor(train_all_size * active_start_percent / 100);
				double train_remain_size = train_all_size - train_start_size;
				
				
				for (int i = 0; i < train_start_size; i++) {
					
					Vector<Double> temp = new Vector<Double>();
					for (int j = 0; j < dim; j++) {
						
						temp.add(train_all[i][j]);
					}
					train_start.add(temp);
				}
				
				for (int i = 0; i < train_remain_size; i++) {
					
					Vector<Double> temp = new Vector<Double>();
					for (int j = 0; j < dim; j++) {
						temp.add(train_all[(int) (i + train_start_size)][j]);
					}
					train_remain.add(temp);
				}

				//System.out.println("train_start_size = " + train_start.size());
				//System.out.println("train_remain_size = " + train_remain.size());
				//System.out.println("max_train_size = " + max_train_size);
				this.active_learning(train_start, train_remain, test, max_train_size);
			    System.out.print(test_err[(int) (rounds - 1)] + " ");

			}
			
			System.out.println();
		}
		
		else if (kind == "adaboost"){
			
			System.out.println("Adaboosting");
			System.out.println("Testing Error:");
			for (int value : values) {
				
				//System.out.println("c = " + value);
				
				double train_all_size = train_all.length;
				train_size = Math.floor(train_all_size * value / 100);
				//System.out.println("train_size = " + train_size);
				
				double[][] train = new double[(int) train_size][(int) dim];
				
				for (int i = 0; i < train_size; i++) {
					for (int j = 0; j < dim; j++) {
						train[i][j] = train_all[i][j];
					}
				}

			
				ada_boosting(train, test);
			    System.out.print(test_err[(int) (rounds - 1)] + " ");

			}
			
			System.out.println();
		}
		
	}
	
	// active learning
	public void active_learning(Vector<Vector<Double>> train_start,
			Vector<Vector<Double>> train_remain, double[][] test, double max_train_size) {
		
		test_err = new double[(int)rounds];
		while (train_start.size() <= max_train_size) {
			
			
			double train_start_size = train_start.size();
			double train_remain_size = train_remain.size();
			double[] dist_train_start = new double[(int)train_start_size];
			double[] alpha = new double[(int)rounds];
			DecisionStump[] optimal_ds = new DecisionStump[(int)rounds];
			
			for (int j = 0; j < train_start_size; j++) {
				dist_train_start[j] = 1 / train_start_size;
			}
			
			double[][] train_start_array = this.vector_to_array(train_start);
			//System.out.println("train_start_size = " + train_start_array.length);
			
			ArrayList<DecisionStump> ds_lst = this.get_decision_stump(train_start_array);
			
			for (int t = 0; t < rounds; t++) {
				
				// local round error
				optimal_ds[t] = this.get_optimal_ds(train_start_array, ds_lst, dist_train_start);
				local_err[t] = optimal_ds[t].getError_rate();
				
				// alpha
				alpha[t] = 0.5 * Math.log((1 - local_err[t]) / local_err[t]);
				
				// update
				this.update(train_start_array, optimal_ds[t], dist_train_start, alpha[t]);
				
				// testing error rate
				test_err[t] = 0;
				double[] test_prob = new double[(int)fixed_test_size];
				
				for (int i = 0; i < fixed_test_size; i++) {
					
					test_prob[i] = 0.0;
					double label = test[i][(int) (dim - 1)];
					for (int j = 0; j < t + 1; j++) {
						
						test_prob[i] += alpha[j] * hypo_predict(test[i], optimal_ds[j]);
					}
					
					if(test_prob[i] > 0.5) {
						test_prob[i] = 1.0;
					}
					else {
						test_prob[i] = 0.0;
					}
					
					if (test_prob[i] != label) {
						test_err[t] ++;
					}
				}
				test_err[t] /= fixed_test_size;
			}
			
			DecisionStump best_ds = optimal_ds[(int) (rounds - 1)];
			//System.out.println(best_ds.getFeature_id() + " " + best_ds.getThreshold());
			int feature_id = best_ds.getFeature_id();
			double threshold_val = best_ds.getThreshold();
			double increase_num = Math.floor(train_remain_size * increase_percent / 100);
			PointAndFeatureVal[] p_fval_lst = new PointAndFeatureVal[(int) train_remain_size];
			//double[][] train_remain_array = this.vector_to_array(train_remain);
			
			for (int i = 0; i < train_remain_size; i++) {
				
				Vector<Double> point = train_remain.get(i);
				double distance = point.get(feature_id) - threshold_val;
				p_fval_lst[i] = new PointAndFeatureVal(point, Math.abs(distance));
			}
			
			MyComparator2 mc2 = new MyComparator2();
			Arrays.sort(p_fval_lst, mc2);
			
			/*for (PointAndFeatureVal temp : p_fval_lst) {
				//System.out.println(temp.getPoint());
				System.out.println(temp.getDistance());
			}*/
			
			if (train_start.size() == max_train_size) {
				break;
			}
			
			if ((increase_num + train_start_size) > max_train_size) {
				increase_num = max_train_size - train_start_size;
			}
			
			for (int i = 0; i < increase_num; i++) {
				
				train_start.add(p_fval_lst[i].getPoint());
				train_remain.remove(p_fval_lst[i].getPoint());
			}
			
		}
		
		//System.out.println("train_start_size = " + train_start.size());
		//System.out.println("train_remain_size = " + train_remain.size());
		//System.out.println("max_train_size = " + max_train_size);
	}

	
	public double[][] vector_to_array(Vector<Vector<Double>> mySet) {
		
		int size = mySet.size();
		int dim = mySet.get(0).size();
		
		double[][] res = new double[size][dim];
		
		for(int i = 0; i < size; i++) {
			
			for(int j = 0; j < dim; j++) {
				res[i][j] = mySet.get(i).get(j);
			}
		}
		
		return res;
	}
	
	// adaboosting
	public void ada_boosting(double[][] train, double[][] test) {
		
		test_err = new double[(int)rounds];
		double[] dist_train = new double[(int)train_size];
		double[] alpha = new double[(int)rounds];
		DecisionStump[] optimal_ds = new DecisionStump[(int)rounds];
		ArrayList<DecisionStump> ds_lst = this.get_decision_stump(train);
		
		for (int j = 0; j < train_size; j++) {
			dist_train[j] = 1 / train_size;
		}
		
		
		for (int t = 0; t < rounds; t++) {
			
			// local round error
			
			optimal_ds[t] = this.get_optimal_ds(train, ds_lst, dist_train);
			local_err[t] = optimal_ds[t].getError_rate();
			
			// alpha
			alpha[t] = 0.5 * Math.log((1 - local_err[t]) / local_err[t]);
			
			/*
			System.out.println("1: " + Math.exp(-1 * alpha[t] * 1 * -1));
			System.out.println("2: " + Math.exp(-1 * alpha[t] * 1 * 1));
			System.out.println("3: " + Math.exp(-1 * alpha[t] * -1 * 1));
			System.out.println("4: " + Math.exp(-1 * alpha[t] * -1 * -1));
			*/
			
			// update
			this.update(train, optimal_ds[t], dist_train, alpha[t]);

			// training error rate
			train_err[t] = 0;
			for (int i = 0; i < train_size; i++) {
				
				double prob = 0.0;
				double label = train[i][(int) (dim - 1)];
				for (int j = 0; j < t + 1; j++) {
					
					prob += alpha[j] * hypo_predict(train[i], optimal_ds[j]);
				}
				
				if(prob > 0.5) {
					prob = 1.0;
				}
				else {
					prob = 0.0;
				}
				
				if (prob != label) {
					train_err[t] ++;
				}
			}
			train_err[t] /= train_size;
			
			// testing error rate
			test_err[t] = 0;
			double[] test_prob = new double[(int)fixed_test_size];
			
			for (int i = 0; i < fixed_test_size; i++) {
				
				test_prob[i] = 0.0;
				double label = test[i][(int) (dim - 1)];
				for (int j = 0; j < t + 1; j++) {
					
					test_prob[i] += alpha[j] * hypo_predict(test[i], optimal_ds[j]);
				}
				
				if(test_prob[i] > 0.5) {
					test_prob[i] = 1.0;
				}
				else {
					test_prob[i] = 0.0;
				}
				
				if (test_prob[i] != label) {
					test_err[t] ++;
				}
			}
			test_err[t] /= fixed_test_size;
			
			// test auc
			ProbAndLabel[] p_l_lst = new ProbAndLabel[(int) fixed_test_size];
			
			for (int i = 0; i < fixed_test_size; i++) {
				
				double label = test[i][(int) (dim - 1)];
				
				//System.out.println("dist_test " + i + ", " + dist_test[i]);
				p_l_lst[i] = new ProbAndLabel(test_prob[i], label);
				
			}
			
			MyComparator mc = new MyComparator();
			Arrays.sort(p_l_lst, mc);
			
			/*
			System.out.println("start");
			for (int i = 0; i < test_size; i++) {
				System.out.println(p_l_lst[i].getProb());
			}
			System.out.println("end");
			*/
		
			for (int i = 0; i < fixed_test_size; i++) {
				
				ProbAndLabel p_l = p_l_lst[i];
				//System.out.println(p_l.getProb() + " " + p_l.getLabel());
				p_l.setProb(0);
				//System.out.println(p_l.getProb() + " " + p_l.getLabel());
			}

			for (int i = 0; i < fixed_test_size; i++) {
				
				double TP_node = 0;
				double TN_node = 0;
				double FP_node = 0;
				double FN_node = 0;
				
				ProbAndLabel p_l = p_l_lst[i];
				p_l.setProb(1);
				
				/*
				System.out.println(i + " start");
				for (int j = 0; j < test_size; j++) {
					p_l = p_l_lst[j];
					System.out.println(p_l.getProb() + " " + p_l.getLabel());
				}
				System.out.println(i + " end");
				*/
				
				for (int j = 0; j < fixed_test_size; j++) {
					
					p_l = p_l_lst[j];
					
					double prob = p_l.getProb();
					double label = p_l.getLabel();
					if (prob == label) {
						if (prob == 1) {
							
							TP_node++;
						}
						else {
							
							TN_node++;
						}
					}
					else {
						if (prob == 1) {
							
							FP_node++;
						}
						else {
							
							FN_node++;
						}
					}
				}
				
				TPR_nodes[i] = TP_node / (TP_node + FN_node);
				FPR_nodes[i] = FP_node / (FP_node + TN_node);
			}
			
			
			test_auc[t] = 0.0;
			for (int i = 0; i < fixed_test_size - 1; i++) {
				
				double x_pre = FPR_nodes[i];
				double x_post = FPR_nodes[i + 1];
				
				if (x_pre == x_post)
					continue;
				else {
					
					double y_pre = TPR_nodes[i];
					double y_post = TPR_nodes[i + 1];
					double area = (y_pre + y_post) * (x_post - x_pre) / 2;
					test_auc[t] += area;
				}	
			}
		}
		
		//System.out.println(optimal_ds[(int) (rounds - 1)].getFeature_id() + " " + optimal_ds[(int) (rounds - 1)].getThreshold());
		
		// print out results in each round
		/*System.out.println("Round Error:");
		for (double err : local_err[k_index]) {
			System.out.print(0.5 - Math.abs(0.5 - err) + " ");
		}
		System.out.println();
		
		System.out.println("Training Error:");
		for (double err : train_err[k_index]) {
			System.out.print(err + " ");
		}
		System.out.println();
		
		System.out.println("Testing Error:");
		for (double err : test_err[k_index]) {
			System.out.print(err + " ");
		}
		System.out.println();
		
		System.out.println("Testing AUC:");
		for (double auc : test_auc[k_index]) {
			System.out.print(auc + " ");
		}
		System.out.println();
		
		System.out.println("Final ROC:");
		for (double fpr : FPR_nodes[k_index]) {
			System.out.print(fpr + " ");
		}
		System.out.println();
		for (double tpr : TPR_nodes[k_index]) {
			System.out.print(tpr + " ");
		}
		System.out.println();*/
		
	}
	
	// update
	public void update(double[][] dataSet, DecisionStump ds, double[] dist, double alpha) {
		
		int size = dataSet.length;
		double sum = 0;
		for (int i = 0; i < size; i++) {
			
			double label = dataSet[i][(int) (dim - 1)];
			double predict_res = hypo_predict(dataSet[i], ds);
			
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
	
	
	// get the decision stump
	public ArrayList<DecisionStump> get_decision_stump(double[][]train) {
		
		int train_size = train.length;
		ArrayList<DecisionStump> ds_list = new ArrayList<DecisionStump>();
		
		for (int j = 0; j < dim - 1; j++) {
			
			Set<Double> feature_values = new HashSet<Double>();
			
			for (int i = 0; i < train_size; i++) {
				feature_values.add(train[i][j]);
			}
			
			Object[] feature_values_lst = feature_values.toArray();
			Arrays.sort(feature_values_lst);
			
			int len = feature_values_lst.length;
			double min_threshold = (Double)feature_values_lst[0] - 1;
			double max_threshold = (Double)feature_values_lst[len - 1] + 1;
			ds_list.add(new DecisionStump(j, min_threshold));
			
			for (int i = 0; i < len - 1; i++) {
				
				double cur = (Double)feature_values_lst[i];
				double post = (Double)feature_values_lst[i + 1];
				ds_list.add(new DecisionStump(j, (cur + post) / 2.0));
			}
			ds_list.add(new DecisionStump(j, max_threshold));
		}
		
		/*
		for(DecisionStump ds : ds_list) {
			System.out.println("Feature ID: " + ds.getFeature_id() + 
					", Threshold: " + ds.getThreshold());
		}*/
		//System.out.println(ds_list.size());

		
		return ds_list;
	}
	
	// random ds
	public DecisionStump get_random_ds(double[][] train,
			ArrayList<DecisionStump> ds_lst) {
		
		Random random = new Random();
		int index = random.nextInt(ds_lst.size());
		DecisionStump random_ds = new DecisionStump();
		random_ds = ds_lst.get(index);
		
		double error_rate = 0;
		for(int i = 0; i < train_size; i++) {
				
			double label = train[i][(int) (dim - 1)];
			double res = this.hypo_predict(train[i], random_ds);
			if (label != res) {
				error_rate ++;
			}
		}
		error_rate /= train_size;
		random_ds.setError_rate(error_rate);
		
		
		/*System.out.println("DecisionStump[" + ds_lst.get(index).getFeature_id() +
				", " + ds_lst.get(index).getThreshold() + "]");
		*/
		return random_ds;
	}
	
	// Optimal ds
	public DecisionStump get_optimal_ds(double[][] train, 
			ArrayList<DecisionStump> ds_lst, double[] dist_train) {
		
		double train_size = train.length;
		double max_res = 0.0;
		DecisionStump best_ds = new DecisionStump();
		
		for (DecisionStump ds : ds_lst) {
			
			double error_rate = 0;
			for(int i = 0; i < train_size; i++) {
				
				double label = train[i][(int) (dim - 1)];
				double res = this.hypo_predict(train[i], ds);
				if (label != res) {
					error_rate += dist_train[i];
				}
			}
			ds.setError_rate(error_rate);
			double temp = Math.abs(0.5 - error_rate);
			
			if (temp > max_res) {
				
				max_res = temp;
				best_ds = ds;
				//best_ds.setError_rate(max_res);
			}
			
			/*
			System.out.println("DecisionStump[" + ds.getFeature_id() +
					", " + ds.getThreshold() + "], Error_Rate: " +
					ds.getError_rate());
					*/
		}
		
		
		/*System.out.println("Best DecisionStump[" + best_ds.getFeature_id() +
				", " + best_ds.getThreshold() + "], Error_Rate: " +
				best_ds.getError_rate() + ", max of |1/2 - error(h)| = " +
				max_res);*/
				
		return best_ds;
	}
	
	// predict according to threshold
	public double hypo_predict(double[] data_point, DecisionStump ds) {
		
		double feature_id = ds.getFeature_id();
		double threshold = ds.getThreshold();
		double feature_value = data_point[(int) feature_id];
		
		if (feature_value > threshold) {
			return 1.0;
		}
		else {
			return 0.0;
		}	
	}
}
