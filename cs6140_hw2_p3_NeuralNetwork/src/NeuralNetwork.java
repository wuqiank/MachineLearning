import java.text.DecimalFormat;


public class NeuralNetwork {

	public void get_objectives(double[][] data_points, double[][] labels) {
		
		/*
    	1.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 label: 1.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 
		0.0 1.0 0.0 0.0 0.0 0.0 0.0 0.0 label: 0.0 1.0 0.0 0.0 0.0 0.0 0.0 0.0 
		0.0 0.0 1.0 0.0 0.0 0.0 0.0 0.0 label: 0.0 0.0 1.0 0.0 0.0 0.0 0.0 0.0 
		0.0 0.0 0.0 1.0 0.0 0.0 0.0 0.0 label: 0.0 0.0 0.0 1.0 0.0 0.0 0.0 0.0 
		0.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 label: 0.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 
		0.0 0.0 0.0 0.0 0.0 1.0 0.0 0.0 label: 0.0 0.0 0.0 0.0 0.0 1.0 0.0 0.0 
		0.0 0.0 0.0 0.0 0.0 0.0 1.0 0.0 label: 0.0 0.0 0.0 0.0 0.0 0.0 1.0 0.0 
		0.0 0.0 0.0 0.0 0.0 0.0 0.0 1.0 label: 0.0 0.0 0.0 0.0 0.0 0.0 0.0 1.0  
		*/
		
		double learning_rate = 1;
		int itr = 1;
		
		// weight of hidden layer and output layer
		double[][] hidden_objectives = new double[3][8];
		double[] hidden_theata = new double[3];
		double[][] output_objectives = new double[8][3];
		double[] output_theata = new double[8];
		double[][] output_values = new double[8][8];
		double[][] hidden_values = new double[8][3];
		
		// Initialize
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				hidden_objectives[i][j] = Math.random();
			}
		}
		for (int i = 0; i < 3; i++) {
			hidden_theata[i] = Math.random();
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 3; j++) {
				output_objectives[i][j] = Math.random();
			}
		}
		for (int i = 0; i < 8; i++) {
			output_theata[i] = Math.random();
		}
		
		
		while (itr < 10000) {

			System.out.println("itr " + itr);
			
			//totally 8 data points
			for (int k = 0; k < 8; k++) {
				
				// get sum
				double[] data_point = data_points[k];
				double[] label = labels[k];
				hidden_values[k] = new double[3];
				output_values[k] = new double[8];
				for (int i = 0; i < 3; i++) {
					
					double sum = 0;
					for (int j = 0; j < 8; j++)
						sum += hidden_objectives[i][j] * data_point[j];
					sum += hidden_theata[i];
					sum = 1 / (1 + Math.exp(-sum));	
					hidden_values[k][i] = sum;
				} 
				/*
				for (int i = 0; i < 3; i++) {
					
					System.out.println("S" + (i+1) + " = " + hidden_values[i]);
				}*/ 
				
				for (int i = 0; i < 8; i++) {
					
					double sum = 0;
					for (int j = 0; j < 3; j++) {
						
						sum += hidden_values[k][j] * output_objectives[i][j];
					}
					sum += output_theata[i];
					sum = 1 / (1 + Math.exp(-sum));
					output_values[k][i] = sum;
				}
				
				
				
				// adjust weight and theata from output to hidden
				for (int i = 0; i < 8; i++) {
					
					double factor = (label[i] - output_values[k][i]) * output_values[k][i] * (1 - output_values[k][i]);
					for (int j = 0; j < 3; j++) {
						
						double delta = learning_rate * factor * hidden_values[k][j];
						output_objectives[i][j] += delta;
					}
					output_theata[i] += learning_rate * factor;
				}
				
				
				// adjust weight and theata from hidden to input
				for (int i = 0; i < 3; i++) {
					
					double sum = 0.0;
					for (int j = 0; j < 8; j++) {
						double factor = (label[j] - output_values[k][j]) * output_values[k][j] * (1 - output_values[k][j]);
						sum += output_objectives[j][i] * factor;
					}
					
					double factor = sum * hidden_values[k][i] * (1 - hidden_values[k][i]);
					
					for (int j = 0; j < 8; j++) {
						
						double delta = learning_rate * factor * data_point[j];
						hidden_objectives[i][j] += delta;
					}
					hidden_theata[i] += learning_rate * factor;
				}
				
				
			}
			// print output
			DecimalFormat df = new DecimalFormat("#.##");
			for (int i = 0; i < 8; i++) {
				
				// print input
				for (int j = 0; j < 8; j++) {
					
					System.out.print(df.format(data_points[i][j]) + " ");
				}
				
				System.out.print(" -> ");
				
				// print weights
				for (int j = 0; j < 3; j++) {
					
					System.out.print(df.format(hidden_values[i][j]) + " ");
				}
				
				System.out.print(" -> ");
				
				// print output
				for (int j = 0; j < 8; j++) {
					
					System.out.print(df.format(output_values[i][j]) + " ");
				}
				
				System.out.println();
			}
			
			itr++;
		}
		
		
		
		
		
	}
	
	
}
