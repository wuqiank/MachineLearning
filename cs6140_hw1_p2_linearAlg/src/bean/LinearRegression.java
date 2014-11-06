package bean;

import java.util.Vector;

import Jama.Matrix;

public class LinearRegression {

	Matrix mx_data_points;
	Matrix mx_labels;
	
	/*
	 * find the best objective J
	 */
	public double[] get_objective(Vector<Vector<Double>> dataSet, Vector<Double> label_values) {
		
		int x = dataSet.get(0).size();
		int y = label_values.size();
		double [][] data_points = new double[y][x + 1];
		double [] labels = new double[y];
		Matrix mx_objective;
		double[] objective = new double[x + 1];
		
		for (int i = 0; i < y; i++) {
			
			data_points[i][0] = 1;
			for (int j = 0; j < x; j++) {
				data_points[i][j+1] = dataSet.get(i).get(j);
			}
			labels[i] = label_values.get(i);
		}
		
		mx_data_points = new Matrix(data_points);
		mx_labels = new Matrix(labels, 1);
		mx_objective = mx_data_points.transpose().times(mx_data_points).inverse().times(mx_data_points.transpose()).times(mx_labels.transpose());
		
		for (int k = 0; k < x + 1; k++) {
			objective[k] = mx_objective.get(k, 0);
		}
		
				
		return objective;
	}
	
	/*
	 * predict label
	 */
	public double get_label(Vector<Double> data_point, double[] objective){
		
		double pre_label = 0;
		
		for (int i = 1; i < objective.length; i++) {

			pre_label += data_point.get(i - 1) * objective[i];
		}
		
		pre_label += objective[0];
		
		return pre_label;
	}
	
	/*
	 * compute error
	 */
	public double get_error(Vector<Vector<Double>> dataSet, Vector<Double> label_values, double[] objective) {
		
		int y = label_values.size();
		double pre_label;
		double se = 0;
		
		for (int i = 0; i < y; i++) {
			
			pre_label = this.get_label(dataSet.get(i), objective);
			se += (pre_label - label_values.get(i)) * (pre_label - label_values.get(i));
		}
		
		return se / y;	
	}
	
	/*
	 * compute acc
	 */
	public double get_acc(Vector<Vector<Double>> dataSet, Vector<Double> label_values, double[] objective) {
		
		double y = label_values.size();
		double pre_label;
		double count = 0;
		
		for (int i = 0; i < y; i++) {
			
			pre_label = this.get_label(dataSet.get(i), objective);
			
			if (pre_label <= 0.5) 
				pre_label = 0;
			else
				pre_label = 1;
			
			
			if (pre_label == label_values.get(i))
				count++;
		}
		//System.out.println(count/y);
		return count / y;	
	}
}
