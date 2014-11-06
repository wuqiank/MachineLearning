import java.util.Collections;
import java.util.Vector;


public class Histogram4Bin {

	double count_spam = 0.0;
	double count_non_spam = 0.0;
	double size;
	int dim;
	double[] feature_min;
	double[] feature_low_mean;
	double[] feature_mean;
	double[] feature_high_mean;
	double[] feature_max;
	
	double[] prob_h1_spam;
	double[] prob_h2_spam;
	double[] prob_h3_spam;
	double[] prob_h4_spam;
	
	double[] prob_h1_non_spam;
	double[] prob_h2_non_spam;
	double[] prob_h3_non_spam;
	double[] prob_h4_non_spam;
	
	Results train_res;
	Results test_res;
	
	public void run_h4b(Vector<Vector<Double>> train_set, Vector<Vector<Double>> test_set) {
		
		this.initialize(train_set);
		this.get_feature_prob(train_set);
		//train_res = this.naiveBayes(train_set);
		test_res = this.naiveBayes(test_set);
	}
	
	public void initialize(Vector<Vector<Double>> dataSet) {
		
		size = dataSet.size();
		dim = dataSet.get(0).size() - 1;
		feature_min = new double[dim];
		feature_low_mean = new double[dim];
		feature_mean = new double[dim];
		feature_high_mean = new double[dim];
		feature_max = new double[dim];
		
		for (Vector<Double> data_point : dataSet) {
			
			if (data_point.get(dim) == 1.0) 
				count_spam++;
			else
				count_non_spam++;
		}
	
		// mean
		for (int i = 0; i < dim; i++) {
			
			double sum = 0.0;
			double sum_spam = 0.0;
			double sum_non_spam = 0.0;
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;
			double mean_spam = 0.0;
			double mean_non_spam = 0.0;
			
			for (int j = 0; j < size; j++) {
				
				double feature = dataSet.get(j).get(i);
				double label = dataSet.get(j).get(dim);
				
				if (feature > max) 
					max = feature;
				
				if (feature < min)
					min = feature;
				
				if (label == 1.0)
					sum_spam += feature;
				
				if (label == 0.0)
					sum_non_spam += feature;
				
				sum += feature;
			}
			
			mean_spam = sum_spam / count_spam;
			mean_non_spam = sum_non_spam / count_non_spam;
			
			
			feature_min[i] = min;
			feature_low_mean[i] = Math.min(mean_spam, mean_non_spam);
			feature_mean[i] = sum / size;
			feature_high_mean[i] = Math.max(mean_spam, mean_non_spam);
			feature_max[i] = max;
			
			/*
			System.out.println("feature " + i);
			System.out.println("feature_min: " + feature_min[i]);
			System.out.println("feature_low_mean: " + feature_low_mean[i]);
			System.out.println("feature_mean: " + feature_mean[i]);
			System.out.println("feature_high_mean: " + feature_high_mean[i]);
			System.out.println("feature_max: " + feature_max[i]);
			System.out.println();
			*/
		}
		
		//System.out.println("spam cunt " + count_spam);
		//System.out.println("non spam cunt " + count_non_spam);
	}
	
	public void get_feature_prob(Vector<Vector<Double>> dataSet) {

		prob_h1_spam = new double[dim];
		prob_h2_spam = new double[dim];
		prob_h3_spam = new double[dim];
		prob_h4_spam = new double[dim];
		
		prob_h1_non_spam = new double[dim];
		prob_h2_non_spam = new double[dim];
		prob_h3_non_spam = new double[dim];
		prob_h4_non_spam = new double[dim];
		
		for (int i = 0; i < dim; i++) {
			
			double count_h1_spam = 1.0;
			double count_h2_spam = 1.0;
			double count_h3_spam = 1.0;
			double count_h4_spam = 1.0;
			
			double count_h1_non_spam = 1.0;
			double count_h2_non_spam = 1.0;
			double count_h3_non_spam = 1.0;
			double count_h4_non_spam = 1.0;
			
			for (int j = 0; j < size; j++) {
				
				double feature = dataSet.get(j).get(i);
				double label = dataSet.get(j).get(dim);
				
				if (label == 1.0) {
					if (feature_min[i] <= feature && feature <= feature_low_mean[i]) {
						count_h1_spam++;
					}
					else if (feature_low_mean[i] < feature && feature <= feature_mean[i]) {
						count_h2_spam++;
					}
					else if (feature_mean[i] < feature && feature <= feature_high_mean[i]) {
						count_h3_spam++;
					}
					else if (feature_high_mean[i] < feature && feature <= feature_max[i]) {
						count_h4_spam++;
					}
				}
				else if (label == 0.0) {
					if (feature_min[i] <= feature && feature <= feature_low_mean[i]) {
						count_h1_non_spam++;
					}
					else if (feature_low_mean[i] < feature && feature <= feature_mean[i]) {
						count_h2_non_spam++;
					}
					else if (feature_mean[i] < feature && feature <= feature_high_mean[i]) {
						count_h3_non_spam++;
					}
					else if (feature_high_mean[i] < feature && feature <= feature_max[i]) {
						count_h4_non_spam++;
					}
				}
			}
			
			prob_h1_spam[i] = count_h1_spam / count_spam;
			prob_h2_spam[i] = count_h2_spam / count_spam;
			prob_h3_spam[i] = count_h3_spam / count_spam;
			prob_h4_spam[i] = count_h4_spam / count_spam;
			
			prob_h1_non_spam[i] = count_h1_non_spam / count_non_spam;
			prob_h2_non_spam[i] = count_h2_non_spam / count_non_spam;
			prob_h3_non_spam[i] = count_h3_non_spam / count_non_spam;
			prob_h4_non_spam[i] = count_h4_non_spam / count_non_spam;
			
			/*
			System.out.println("feature " + i);
			System.out.println("prob_h1_spam " + prob_h1_spam[i]);
			System.out.println("prob_h2_spam " + prob_h2_spam[i]);
			System.out.println("prob_h3_spam " + prob_h3_spam[i]);
			System.out.println("prob_h4_spam " + prob_h4_spam[i]);
			System.out.println("prob_h1_non_spam " + prob_h1_non_spam[i]);
			System.out.println("prob_h2_non_spam " + prob_h2_non_spam[i]);
			System.out.println("prob_h3_non_spam " + prob_h3_non_spam[i]);
			System.out.println("prob_h4_non_spam " + prob_h4_non_spam[i]);
			System.out.println();
			*/
		}
	}
	
	public Results naiveBayes(Vector<Vector<Double>> dataSet) {
		
		/*
		 * P(Yi|x) = P(x|Yi) * P(Yi) /  P(x)
		 * 
		 * P(x|Yi) = P(f1|Yi) * P(f2|Yi) * ... * P(fn|Yi)
		 */
		Vector<ProbAndLabel> p_l_list = new Vector<ProbAndLabel>();
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
				if (feature_min[i] <= feature && feature <= feature_low_mean[i]) {
					
					prob_spam_x *= prob_h1_spam[i];
					prob_non_spam_x *= prob_h1_non_spam[i];
				}
				else if (feature_low_mean[i] < feature && feature <= feature_mean[i]) {
					
					prob_spam_x *= prob_h2_spam[i];
					prob_non_spam_x *= prob_h2_non_spam[i];
				}
				else if (feature_mean[i] < feature && feature <= feature_high_mean[i]) {
					
					prob_spam_x *= prob_h3_spam[i];
					prob_non_spam_x *= prob_h3_non_spam[i];
				}
				else if (feature_high_mean[i] < feature && feature <= feature_max[i]) {
					
					prob_spam_x *= prob_h4_spam[i];
					prob_non_spam_x *= prob_h4_non_spam[i];
				}
			}
			
			prob_spam_x *= prob_spam * one_over_prob_x;
			prob_non_spam_x *= prob_non_spam * one_over_prob_x;
			
			/*
			System.out.println("prob_spam_x " + prob_spam_x);
			System.out.println("prob_non_spam_x " + prob_non_spam_x);
			System.out.println();
			*/
			
			double prob = Math.log(prob_spam_x) - Math.log(prob_non_spam_x);
			ProbAndLabel temp = new ProbAndLabel(prob, label);
			p_l_list.add(temp);	
			
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
		
		double[] TPR_nodes = new double[(int)size];
		double[] FPR_nodes = new double[(int)size];
		MyComparator mc = new MyComparator();
		Collections.sort(p_l_list, mc);
		for (int i = 0; i < size; i++) {
			
			ProbAndLabel p_l = p_l_list.get(i);
			p_l.setProb(0);
			//System.out.println(p_l.getProb() + " " + p_l.getLabel());
		}
		
		for (int i = 0; i < size; i++) {
			
			double TP_node = 0;
			double TN_node = 0;
			double FP_node = 0;
			double FN_node = 0;
			double P = 0;
			double N = 0;
			
			ProbAndLabel p_l = p_l_list.get(i);
			p_l.setProb(1);
			
			for (int j = 0; j < size; j++) {
				
				p_l = p_l_list.get(j);
				double prob = p_l.getProb();
				double label = p_l.getLabel();
				if (prob == label) {
					if (prob == 1) {
						
						P++;
						TP_node++;
					}
					else {
						
						N++;
						TN_node++;
					}
				}
				else {
					if (prob == 1) {
						
						N++;
						FP_node++;
					}
					else {
						
						P++;
						FN_node++;
					}
				}
			}
			TPR_nodes[i] = TP_node / P;
			FPR_nodes[i] = FP_node / N;
		}
		
		double sum_area = 0.0;
		for (int i = 0; i < size - 1; i++) {
			
			double x_pre = FPR_nodes[i];
			double x_post = FPR_nodes[i + 1];
			
			if (x_pre == x_post)
				continue;
			else {
				
				double y_pre = TPR_nodes[i];
				double y_post = TPR_nodes[i + 1];
				double area = (y_pre + y_post) * (x_post - x_pre) / 2;
				sum_area += area;
			}	
		}
		
		res.setFPR(FP / (FP + TN));
		res.setFNR(FN / (FN + TP));
		res.setERR(err_count / size);
		res.setFPR_nodes(FPR_nodes);
		res.setTPR_nodes(TPR_nodes);
		res.setAUC(sum_area);
		return res;
	}
}
