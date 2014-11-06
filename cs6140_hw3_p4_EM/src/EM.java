import java.util.Vector;
import Jama.Matrix;

public class EM {

	// total gmm number
	int model_num;
	
	// parameters
	double[][] mean;
	double[][][] cov;
	double[] count;
	
	//invisible labels
	double[] invis_label;
	
	public void run(Vector<Vector<Double>> dataSet, int model_num) {
		
		this.model_num = model_num;
		this.initialize(dataSet);
		this.em(dataSet);
	}
	
	// Initialization
	public void initialize(Vector<Vector<Double>> dataSet) {
		
		double size = dataSet.size();
		mean = new double[model_num][2];
		count = new double[model_num];
		cov = new double[model_num][2][2];
		invis_label = new double[model_num];
		
		
		for (int k = 0; k < model_num; k++) {
			invis_label[k] = k;
			mean[k][0] = 0.0;
			mean[k][1] = 0.0;
			count[k] = 0.0;
		}
		
		// get mean
		for (int i = 0; i < size; i++) {
			
			Vector<Double> data_point = dataSet.get(i);	
			double label = data_point.get(2);
			
			for (int k = 0; k < model_num; k++) {
				if (label == invis_label[k]) {
					for(int j = 0; j < 2; j++) { 
						
						mean[k][j] += data_point.get(j);
					}
					count[k] ++;
				}
			}
		}
		for (int k = 0; k < model_num; k++) {
			for(int j = 0; j < 2; j++) {  
				mean[k][j] = mean[k][j] / count[k];
			}
			cov[k] = this.get_cov(dataSet, mean[k]);
			//System.out.println("mean_" + (k+1) + " number = " + count[k]);
			//System.out.println("mean_" + (k+1) + " = [" + mean[k][0] + ", " + mean[k][1] + "]");
		}
		
		// get cov
		/*
		for (int i = 0; i < size; i++) {
			
			Vector<Double> data_point = dataSet.get(i);	
			double label = data_point.get(2);
			
			for (int k = 0; k < model_num; k++) {
				if (label == invis_label[k]) {
					for(int h = 0; h < 2; h++) { 
						
						double[] tmp = cov[k][h];  
			            for(int j = 0; j < 2; j++) {  
			                
			            	double cov = 0;  
			            	cov += (data_point.get(h) - mean[k][h]) * (data_point.get(j) - mean[k][j]);   
			                tmp[h] = cov / count[k];  
			            }  
			            cov[k][h] = tmp; 
					}
				}
			}
		}
		*/
		
		
		/*
		for (int k = 0; k < model_num; k++) {
			
			System.out.println("mean_" + (k+1) + " = [" + mean[k][0] + ", " + mean[k][1] + "] ; " +
					"cov_" + (k+1) + " = [[" + cov[k][0][0] + ", " + cov[k][0][1] + "], [" +
					cov[k][1][0] + ", " + cov[k][1][1] + "]] ; n_" + (k+1) + " = " + count[k]);
			
		}
		*/
	}
	
	public void em(Vector<Vector<Double>> dataSet) {
		
		double size = dataSet.size();
		int itr = 0;
		
		// j = 0, 1, i = 0, 1, ... 5999
		double[][] w = new double[model_num][(int)size];
		
		for (int i = 0; i < size; i++) {
			for (int k = 0; k < model_num; k++) {
				w[k][i] = 1;
			}
		}
		
		while (itr < 100) {
			
			itr++;
			
			// E step
			for (int i = 0; i < size; i++) {
				
				Vector<Double> data_point = dataSet.get(i);
				double prob[][] = new double[model_num][(int)size];
				double prob_sum = 0.0;
				/*
				double[] phi = new double[model_num];
				for (int k = 0; k < model_num; k++) {
					
					phi[k] = 0.0;
					for (int j = 0; j < size; j++) {
						phi[k] += w[k][j];
					}
					phi[k] = phi[k] / size;
				}
				*/
				
				for (int k = 0; k < model_num; k++) {
					
					prob[k][i] = this.get_prob(mean[k], cov[k], data_point) * count[k] / size;
					prob_sum += prob[k][i];
				}
				
				for (int k = 0; k < model_num; k++) {
					
					w[k][i] = prob[k][i] / prob_sum;
					//System.out.println("w" + k + " " + i + " = " + w[k][i]);
				}
			}
			
			// M step
			// model_num
			for (int k = 0; k < model_num; k++) {
				
				double p_sum = 0.0;
				double mean_1st_sum = 0.0;
				double mean_2nd_sum = 0.0;
				double cov_00_sum = 0.0;
				double cov_01_sum = 0.0;
				double cov_10_sum = 0.0;
				double cov_11_sum = 0.0;
				
				for (int i = 0; i < size; i++) {
					
					Vector<Double> data_point = dataSet.get(i);
					double x1 = data_point.get(0);
					double x2 = data_point.get(1);
					
					// p
					p_sum += w[k][i];
					
					// mean
					mean_1st_sum += w[k][i] * x1;
					mean_2nd_sum += w[k][i] * x2;
					
					// cov
					cov_00_sum += w[k][i] * Math.pow((x1 - mean[k][0]), 2);
					cov_01_sum += w[k][i] * (x1 - mean[k][0]) * (x2 - mean[k][1]);
					cov_10_sum += w[k][i] * (x1 - mean[k][0]) * (x2 - mean[k][1]);
					cov_11_sum += w[k][i] * Math.pow((x2 - mean[k][1]), 2);
				}
				
				//System.out.println(p_sum);
				
				count[k] = p_sum;
				mean[k][0] = mean_1st_sum / p_sum;
				mean[k][1] = mean_2nd_sum / p_sum;
				cov[k][0][0] = cov_00_sum / p_sum;
				cov[k][0][1] = cov_01_sum / p_sum;
				cov[k][1][0] = cov_10_sum / p_sum;
				cov[k][1][1] = cov_11_sum / p_sum;
			}
		}
		
		// print
		for (int k = 0; k < model_num; k++) {
			
			System.out.println("mean_" + (k+1) + " = [" + mean[k][0] + ", " + mean[k][1] + "] ; " +
					"cov_" + (k+1) + " = [[" + cov[k][0][0] + ", " + cov[k][0][1] + "], [" +
					cov[k][1][0] + ", " + cov[k][1][1] + "]] ; n_" + (k+1) + " = " + count[k]);
		}
	}
	
	// compute Cov
	public double[][] get_cov(Vector<Vector<Double>> dataSet, double[] feature_mean) {
		
		double size = dataSet.size();
		int dim = dataSet.get(0).size() - 1;
		double[][] Cov = new double[dim][dim];
		
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
		
		//Cov_mx.print(2, 8);
		//System.out.println("det " + det);
		
		return prob;
	}
	
}
