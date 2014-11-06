package bean;

import java.util.Collections;
import java.util.Vector;

public class LogisticRegression {

	public double[] get_objective(Vector<Vector<Double>> dataSet,
			Vector<Double> label_values) {

		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x + 1];
		double[] labels = new double[y];
		double[] objective = new double[x + 1];
		double learning_rate = 0.0001;
		double error_sum = 0.0;

		for (int i = 0; i < y; i++) {

			data_points[i][0] = 1;
			for (int j = 0; j < x; j++) {
				data_points[i][j + 1] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		// normalize
		for (int i = 1; i < x + 1; i++) {

			double sum = 0.0;

			for (int j = 0; j < y; j++)
				sum += data_points[j][i];

			double mean = sum / y;
			double variance = 0.0;

			for (int j = 0; j < y; j++) {

				variance += (data_points[j][i] - mean)
						* (data_points[j][i] - mean);
			}
			variance = Math.sqrt(variance / y);

			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - mean) / variance;
			}
		}

		/*
		for (int col = 0; col < x + 1; col++) {
			double sum = 0.0;
			for (int i = 0; i < y; i++) {
				sum+= data_points[i][col];
			}
			double mean = sum / y;
			double error = 0.0;
			for (int i = 0; i < y; i++) {
				error+= Math.pow(data_points[i][col] - mean, 2);
			} 
			double mse = error / y;
			
			System.out.println(col+": " + mean + " "+mse);
		}*/

		/*
		 * DecimalFormat df = new DecimalFormat("0.0000"); for(double[] d :
		 * data_points){ for(double dd : d){
		 * System.out.print(df.format(dd)+" "); } System.out.println(); }
		 */

		for (int i = 0; i < x + 1; i++) {
			objective[i] = 0;
		}

		/*
		 * find best theata
		 */
		int itr = 0;

		while (itr < 300) {

			//System.out.println("iteration " + itr);
			for (int i = 0; i < y; i++) {

				double h = 0;
				for (int j = 0; j < x + 1; j++)
					h += objective[j] * data_points[i][j];
				
				h = 1 / (1 + Math.exp(-h));				
				error_sum = h - labels[i];

				// System.out.println("h = " + h);
				// System.out.println("label " + i + " = " + labels[i]);
				// System.out.println("error_sum = " + error_sum);
				// System.out.println("learning_rate: " + learning_rate);

				if (itr > 0) {
					for (int j = 0; j < x + 1; j++) {

						objective[j] -= learning_rate * error_sum * data_points[i][j];
					}
				}
			}
			
			double loss = 0.0;
			for (int i = 0; i < y; i++) {
				
				double sum = 0.0;
				for (int j = 0; j < x + 1; j++)
					sum += data_points[i][j] * objective[j];
				loss += (sum - labels[i]) * (sum - labels[i]);
			}

			//System.out.println("loss now = " + (loss / y));
			//System.out.println();
			itr++;
		}

		/*
		for (int k = 0; k < x + 1; k++) {
			System.out.println(objective[k]);
		}*/

		return objective;
	}
	
	public double get_logistic_error (Vector<Vector<Double>> dataSet,
			Vector<Double> label_values, double[] objective) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x];
		double[] labels = new double[y];
		
		for (int i = 0; i < y; i++) {

			for (int j = 0; j < x; j++) {
				data_points[i][j] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		for (int i = 0; i < x; i++) {

			double sum = 0.0;

			for (int j = 0; j < y; j++)
				sum += data_points[j][i];

			double mean = sum / y;
			double variance = 0.0;

			for (int j = 0; j < y; j++) {

				variance += (data_points[j][i] - mean)
						* (data_points[j][i] - mean);
			}
			variance = Math.sqrt(variance / y);

			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - mean) / variance;
				//System.out.println(data_points[j][i]);
			}
		}
		
		double loss = 0.0;
		double logistic_sum = 0.0;
		for (int i = 0; i < y; i++) {
			
			double sum = 0.0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
		
			sum += objective[0];
			
			logistic_sum = 1 / (1 + Math.exp(-sum));
			
			if (logistic_sum <= 0.5)
				logistic_sum = 0;
			else
				logistic_sum = 1;
			loss += (logistic_sum - labels[i]) * (logistic_sum - labels[i]);
		}
		
		return loss / y;
	}
	
	public double get_logistic_acc (Vector<Vector<Double>> dataSet,
			Vector<Double> label_values, double[] objective) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x];
		double[] labels = new double[y];
		
		for (int i = 0; i < y; i++) {

			for (int j = 0; j < x; j++) {
				data_points[i][j] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		for (int i = 0; i < x; i++) {

			double sum = 0.0;

			for (int j = 0; j < y; j++)
				sum += data_points[j][i];

			double mean = sum / y;
			double variance = 0.0;

			for (int j = 0; j < y; j++) {

				variance += (data_points[j][i] - mean)
						* (data_points[j][i] - mean);
			}
			variance = Math.sqrt(variance / y);

			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - mean) / variance;
				//System.out.println(data_points[j][i]);
			}
		}
		
		double count = 0.0;
		double logistic_sum = 0.0;
		for (int i = 0; i < y; i++) {
			
			double sum = 0.0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
		
			sum += objective[0];
			
			logistic_sum = 1 / (1 + Math.exp(-sum));
			
			if (logistic_sum <= 0.5)
				logistic_sum = 0;
			else
				logistic_sum = 1;
			if (logistic_sum == label_values.get(i))
				count++;
		}
		
		return count / y;
	}
	
	public double get_logistic_test_acc (Vector<Vector<Double>> dataSet,
			Vector<Double> label_values, double[] objective, double[] train_mean, double[] train_variance) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x];
		double[] labels = new double[y];
		
		for (int i = 0; i < y; i++) {

			for (int j = 0; j < x; j++) {
				data_points[i][j] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - train_mean[i]) / train_variance[i];
				//System.out.println(variance);
			}
		}
		
		double count = 0.0;
		double logistic_sum = 0.0;
		for (int i = 0; i < y; i++) {
			
			double sum = 0.0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
		
			sum += objective[0];
			
			logistic_sum = 1 / (1 + Math.exp(-sum));
			
			if (logistic_sum <= 0.5)
				logistic_sum = 0;
			else
				logistic_sum = 1;
			
			//System.out.println(logistic_sum + "  " + label_values.get(i));
			
			if (logistic_sum == label_values.get(i))
				count++;
		}
		
		return count / y;
	}
	
	/*
	 * TPR and FPR for curve
	 */
	public void get_ROC(Vector<Vector<Double>> dataSet,
			Vector<Double> label_values, double[] objective, double[] train_mean, double[] train_variance) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x];
		double[] labels = new double[y];
		
		for (int i = 0; i < y; i++) {

			//data_points_list.get(i).new double[x + 1];
			
			for (int j = 0; j < x; j++) {
				data_points[i][j] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - train_mean[i]) / train_variance[i];
				//System.out.println(variance);
			}
		}
		
		double[] TPR = new double[y];
		double[] FPR = new double[y];
		Vector<ProbAndLabel> p_l_list = new Vector<ProbAndLabel>();
		double sum;
		
		for (int i = 0; i < y; i++) {
			
			sum = 0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
			sum += objective[0];	
			sum = 1 / (1 + Math.exp(-sum));
			ProbAndLabel p_l = new ProbAndLabel(sum, labels[i]);
			p_l_list.add(p_l);
		}
		
		MyComparator mc = new MyComparator();
		Collections.sort(p_l_list, mc);
		
		for (int i = 0; i < y; i++) {
			
			ProbAndLabel p_l = p_l_list.get(i);
			p_l.setProb(0);
			//System.out.println(p_l.getProb() + " " + p_l.getLabel());
		}
		
		for (int i = 0; i < y; i++) {
			
			double TP = 0;
			double TN = 0;
			double FP = 0;
			double FN = 0;
			double P = 0;
			double N = 0;
			
			ProbAndLabel p_l = p_l_list.get(i);
			p_l.setProb(1);
			
			for (int j = 0; j < y; j++) {
				
				p_l = p_l_list.get(j);
				double prob = p_l.getProb();
				double label = p_l.getLabel();
				if (prob == label) {
					if (prob == 1) {
						
						P++;
						TP++;
					}
					else {
						
						N++;
						TN++;
					}
				}
				else {
					if (prob == 1) {
						
						N++;
						FP++;
					}
					else {
						
						P++;
						FN++;
					}
				}
			}
			TPR[i] = TP / P;
			FPR[i] = FP / N;
		}
		
		System.out.println("FPR for each date point");
		for (int i = 0; i < y; i++) {
			
			System.out.print(FPR[i] + " ");
			//System.out.println(TPR[i] + " " + FPR[i]);
		}
		System.out.println();
		System.out.println("TPR for each date point");
		for (int j = 0; j < y; j++) {
			
			System.out.print(TPR[j] + " ");
			//System.out.println(TPR[i] + " " + FPR[i]);
		}
		
		System.out.println();
		
		double sum_area = 0.0;
		for (int i = 0; i < y - 1; i++) {
			
			double x_pre = FPR[i];
			double x_post = FPR[i + 1];
			
			if (x_pre == x_post)
				continue;
			else {
				
				double y_pre = TPR[i];
				double y_post = TPR[i + 1];
				double area = (y_pre + y_post) * (x_post - x_pre) / 2;
				sum_area += area;
			}	
		}
		System.out.println("Logistic Regression AUC = " + sum_area);
	}
	
	/*
	 * confusion matrix
	 */
	public void get_logistic_confusion_matrix(Vector<Vector<Double>> dataSet,
			Vector<Double> label_values, double[] objective, double[] train_mean, double[] train_variance) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x];
		double[] labels = new double[y];
		
		for (int i = 0; i < y; i++) {

			for (int j = 0; j < x; j++) {
				data_points[i][j] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - train_mean[i]) / train_variance[i];
				//System.out.println(variance);
			}
		}
		
		double count = 0.0;
		double logistic_sum = 0.0;
		for (int i = 0; i < y; i++) {
			
			double sum = 0.0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
		
			sum += objective[0];
			
			logistic_sum = 1 / (1 + Math.exp(-sum));
			
			if (logistic_sum <= 0.5)
				logistic_sum = 0;
			else
				logistic_sum = 1;
		}
		
		double loss = 0.0;
		int TP = 0;
		int TN = 0;
		int FP = 0;
		int FN = 0;
		
		for (int i = 0; i < y; i++) {
			
			double sum = 0.0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
			sum += objective[0];	
			loss += (sum - labels[i]) * (sum - labels[i]);
			
			if (sum <= 0.5)
				sum = 0;
			else
				sum = 1;
			
			if (sum == labels[i]) {
				if (sum == 1)
					TP++;
				else
					TN++;
			}
			else {
				if (sum == 1)
					FP++;
				else
					FN++;
			}
		}
		
		System.out.println("Logistic Regression Confusion Matrix");
		System.out.println("TP = " + TP + "    FN = " + FN);
		System.out.println("FP = " + FP + "    TN = " + TN);
	}
	
	/*
	 * compute test mse
	 */
	public double get_test_logistic_error (Vector<Vector<Double>> dataSet,
			Vector<Double> label_values, double[] objective, double[] train_mean, double[] train_variance) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double[][] data_points = new double[y][x];
		double[] labels = new double[y];
		
		for (int i = 0; i < y; i++) {

			for (int j = 0; j < x; j++) {
				data_points[i][j] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {

				data_points[j][i] = (data_points[j][i] - train_mean[i]) / train_variance[i];
				//System.out.println(variance);
			}
		}
		
		double loss = 0.0;
		double logistic_sum = 0.0;
		for (int i = 0; i < y; i++) {
			
			double sum = 0.0;
			for (int j = 0; j < x; j++) {
				
				sum += data_points[i][j] * objective[j + 1];
			}
		
			sum += objective[0];
			
			logistic_sum = 1 / (1 + Math.exp(-sum));
			
			if (logistic_sum <= 0.5)
				logistic_sum = 0;
			else
				logistic_sum = 1;
			
			loss += (logistic_sum - labels[i]) * (logistic_sum - labels[i]);
		}
		
		return loss / y;
	}
}
