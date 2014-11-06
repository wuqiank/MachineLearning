import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


public class Adaboost {

	double dim;
	double train_size;
	
	/*
	 * split data set
	 */
	public void run(Vector<Vector<Double>> train_dataSet) {

		train_size = train_dataSet.size();	
		dim = train_dataSet.get(0).size();
			
		double[][] train = new double[(int) train_size][(int) dim];
			
		for (int i = 0; i < train_size; i++) {
			for (int j = 0; j < dim; j++) {
					
				train[i][j] = train_dataSet.get(i).get(j);
			}
		}
			
		ada_boosting(train);
	}
	
	//ada boosting
	public void ada_boosting(double[][] train) {
		
		double rounds = 200;
		DecisionStump[] optimal_ds = new DecisionStump[(int)rounds];
		double[] dist_train = new double[(int)train_size];
		double[] alpha = new double[(int)rounds];
		double[] local_err = new double[(int)rounds];

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

			
			// update
			this.update(train, optimal_ds[t], dist_train, alpha[t]);	
		}	
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
	
	// Optimal ds
	public DecisionStump get_optimal_ds(double[][] train, 
			ArrayList<DecisionStump> ds_lst, double[] dist_train) {
		
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
		
		/*
		System.out.println("Best DecisionStump[" + best_ds.getFeature_id() +
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