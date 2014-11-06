
public class Perceptron {

	double learning_rate = 0.0001;
	
	/*
	 * check if classification is correct
	 */
	public boolean isCorrect (double[] data_point, double label, double[] objectives) {
		
		int len = data_point.length;
		double pred = 0.0;
		
		for (int i = 0; i < len; i++) {
			
			pred += data_point[i] * objectives[i];
		}
		
		if (pred * label < 0)
			return false;
		else
			return true;
    } 
	
	/*
	 * get weights through GD
	 */
	public void get_objectives(double[][] data_points, double[] labels) {
		
		int data_count = data_points.length;
		int dimension = data_points[0].length;
		double[] objectives = new double[dimension];
		int itr = 1;
		int mis_count = -1;
		
		/*
		 * initialize weights
		 */
		for (int i = 0; i < dimension; i++) {
			objectives[i] = 0;
		}
		
		while(mis_count != 0) {
			
			mis_count = 0;
			for (int i = 0; i < data_count; i++) {
				
				double[] data_point = data_points[i];
				double label = labels[i];
				
				int len = data_point.length;
				double pred = 0.0;
				//double error_sum = 0.0;
				
				for (int j = 0; j < len; j++) {
					
					pred += data_point[j] * objectives[j];
				}
				//error_sum = pred - label;
					
				// if classification is not correct
				if (pred <= 0) {
					
					for (int j = 0; j < dimension; j++) {

						objectives[j] += data_points[i][j];
					}
					
					mis_count++;
				}
			}
			
			System.out.println("Iteration " + itr + " , total_mistake " + mis_count);
			itr++;
		}
		
		/*
		 * print weights
		 */
		System.out.println();
		System.out.println("Classifier weights: ");
		
		for (int i = 0; i < dimension; i++)
			System.out.println("W[" + i + "] = " + objectives[i]);
		
		System.out.println();
		System.out.println("Normalized with threshold: ");
		for (int i = 1; i < dimension; i++)
			System.out.println("W[" + i + "] = " + objectives[i]/-objectives[0]);
	}
	
	/*
	 * 
	 */
	
	
	
}
