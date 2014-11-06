import java.util.Vector;


public class Histogram9Bin {

	double count_spam = 0.0;
	double count_non_spam = 0.0;
	double size;
	int dim;
	double[][] feature_rang;
	double[][] prob_spam;
	double[][] prob_non_spam;
	
	Results train_res;
	Results test_res;
	
	public void run_h9b(Vector<Vector<Double>> train_set, Vector<Vector<Double>> test_set) {
		
		this.initialize(train_set);
		this.get_feature_prob(train_set);
		//train_res = this.naiveBayes(train_set);
		test_res = this.naiveBayes(test_set);
	}
	
	public void initialize(Vector<Vector<Double>> dataSet) {
		
		size = dataSet.size();
		dim = dataSet.get(0).size() - 1;
		feature_rang = new double[10][dim];
		
		for (Vector<Double> data_point : dataSet) {
			
			if (data_point.get(dim) == 1.0) 
				count_spam++;
			else
				count_non_spam++;
		}
	
		// mean
		for (int i = 0; i < dim; i++) {
			
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;

			for (int j = 0; j < size; j++) {
				
				double feature = dataSet.get(j).get(i);
				
				if (feature > max) 
					max = feature;
				
				if (feature < min)
					min = feature;
			}
			
			double interval = (max - min) / 9;
			for (int k = 0; k < 10; k++) {
				feature_rang[k][i] = min + interval * k;
			}
			
		}
		
		//System.out.println("spam cunt " + count_spam);
		//System.out.println("non spam cunt " + count_non_spam);
	}
	
	public void get_feature_prob(Vector<Vector<Double>> dataSet) {

		prob_spam = new double[9][dim];
		prob_non_spam = new double[9][dim];
		
		for (int i = 0; i < dim; i++) {
			
			double[] count_spam = new double[9];
			double[] count_non_spam = new double[9];
			
			for (int k = 0; k < 9; k++) {
				count_spam[k] = 1.0;
				count_non_spam[k] = 1.0;
			}
			
			for (int j = 0; j < size; j++) {
				
				double feature = dataSet.get(j).get(i);
				double label = dataSet.get(j).get(dim);
				
				if (label == 1.0) {
					if (feature_rang[0][i] <= feature && feature <= feature_rang[1][i]) {
						count_spam[0]++;
					}
					else if (feature_rang[1][i] < feature && feature <= feature_rang[2][i]) {
						count_spam[1]++;
					}
					else if (feature_rang[2][i] < feature && feature <= feature_rang[3][i]) {
						count_spam[2]++;
					}
					else if (feature_rang[3][i] < feature && feature <= feature_rang[4][i]) {
						count_spam[3]++;
					}
					else if (feature_rang[4][i] < feature && feature <= feature_rang[5][i]) {
						count_spam[4]++;
					}
					else if (feature_rang[5][i] < feature && feature <= feature_rang[6][i]) {
						count_spam[5]++;
					}
					else if (feature_rang[6][i] < feature && feature <= feature_rang[7][i]) {
						count_spam[6]++;
					}
					else if (feature_rang[7][i] < feature && feature <= feature_rang[8][i]) {
						count_spam[7]++;
					}
					else if (feature_rang[8][i] < feature && feature <= feature_rang[9][i]) {
						count_spam[8]++;
					}
				}
				else if (label == 0.0) {
					if (feature_rang[0][i] <= feature && feature <= feature_rang[1][i]) {
						count_non_spam[0]++;
					}
					else if (feature_rang[1][i] < feature && feature <= feature_rang[2][i]) {
						count_non_spam[1]++;
					}
					else if (feature_rang[2][i] < feature && feature <= feature_rang[3][i]) {
						count_non_spam[2]++;
					}
					else if (feature_rang[3][i] < feature && feature <= feature_rang[4][i]) {
						count_non_spam[3]++;
					}
					else if (feature_rang[4][i] < feature && feature <= feature_rang[5][i]) {
						count_non_spam[4]++;
					}
					else if (feature_rang[5][i] < feature && feature <= feature_rang[6][i]) {
						count_non_spam[5]++;
					}
					else if (feature_rang[6][i] < feature && feature <= feature_rang[7][i]) {
						count_non_spam[6]++;
					}
					else if (feature_rang[7][i] < feature && feature <= feature_rang[8][i]) {
						count_non_spam[7]++;
					}
					else if (feature_rang[8][i] < feature && feature <= feature_rang[9][i]) {
						count_non_spam[8]++;
					}
				}
			}
			
			for (int k = 0; k < 9; k++) {
				prob_spam[k][i] = count_spam[k] / this.count_spam;
			}
			for (int k = 0; k < 9; k++) {
				prob_non_spam[k][i] = count_non_spam[k] / this.count_non_spam;
			}
		}
	}
	
	public Results naiveBayes(Vector<Vector<Double>> dataSet) {
		
		/*
		 * P(Yi|x) = P(x|Yi) * P(Yi) /  P(x)
		 * 
		 * P(x|Yi) = P(f1|Yi) * P(f2|Yi) * ... * P(fn|Yi)
		 */
		Results res = new Results();
		double size = dataSet.size();
		double count_spam = 0.0;
		double count_non_spam = 0.0;
		double err_count = 0;
		double TP = 0;
		double TN = 0;
		double FP = 0;
		double FN = 0;
		
		for (Vector<Double> data_point : dataSet) {
			
			double label = data_point.get(dim);
			if (label == 1.0) 
				count_spam++;
			else
				count_non_spam++;
		}
		
		double prob_spam = count_spam / size;
		double prob_non_spam = count_non_spam / size;
		double one_over_prob_x = size;

		for (Vector<Double> data_point : dataSet) {
			
			double prob_spam_x = 1.0;
			double prob_non_spam_x = 1.0;
			double label = data_point.get(dim);
			double flag = 0.0;
			
			for (int i = 0; i < dim; i++) {
				
				double feature = data_point.get(i);
				if (feature_rang[0][i] <= feature && feature <= feature_rang[1][i]) {
					prob_spam_x *= this.prob_spam[0][i];
					prob_non_spam_x *= this.prob_non_spam[0][i];
				}
				else if (feature_rang[1][i] < feature && feature <= feature_rang[2][i]) {
					prob_spam_x *= this.prob_spam[1][i];
					prob_non_spam_x *= this.prob_non_spam[1][i];
				}
				else if (feature_rang[2][i] < feature && feature <= feature_rang[3][i]) {
					prob_spam_x *= this.prob_spam[2][i];
					prob_non_spam_x *= this.prob_non_spam[2][i];
				}
				else if (feature_rang[3][i] < feature && feature <= feature_rang[4][i]) {
					prob_spam_x *= this.prob_spam[3][i];
					prob_non_spam_x *= this.prob_non_spam[3][i];
				}
				else if (feature_rang[4][i] < feature && feature <= feature_rang[5][i]) {
					prob_spam_x *= this.prob_spam[4][i];
					prob_non_spam_x *= this.prob_non_spam[4][i];
				}
				else if (feature_rang[5][i] < feature && feature <= feature_rang[6][i]) {
					prob_spam_x *= this.prob_spam[5][i];
					prob_non_spam_x *= this.prob_non_spam[5][i];
				}
				else if (feature_rang[6][i] < feature && feature <= feature_rang[7][i]) {
					prob_spam_x *= this.prob_spam[6][i];
					prob_non_spam_x *= this.prob_non_spam[6][i];
				}
				else if (feature_rang[7][i] < feature && feature <= feature_rang[8][i]) {
					prob_spam_x *= this.prob_spam[7][i];
					prob_non_spam_x *= this.prob_non_spam[7][i];
				}
				else if (feature_rang[8][i] < feature && feature <= feature_rang[9][i]) {
					prob_spam_x *= this.prob_spam[8][i];
					prob_non_spam_x *= this.prob_non_spam[8][i];
				}
			}
			
			prob_spam_x *= prob_spam * one_over_prob_x;
			prob_non_spam_x *= prob_non_spam * one_over_prob_x;
			
			/*
			System.out.println("prob_spam_x " + prob_spam_x);
			System.out.println("prob_non_spam_x " + prob_non_spam_x);
			System.out.println();
			*/
			
			if (prob_spam_x >= prob_non_spam_x) {
				flag = 1.0;
			}
			else
				flag = 0.0;
			
			if (flag == label) {
				if (flag == 1) {
					
					TP++;
				}
				else {
					
					TN++;
				}
			}
			else {
				if (flag == 1) {
					
					FP++;
				}
				else {

					FN++;
				}
			}
			
			if (flag != label) {
				err_count ++;
			}
		}
		
		res.setFPR(FP / (FP + TN));
		res.setFNR(FN / (FN + TP));
		res.setERR(err_count / size);
		return res;
	}
}
