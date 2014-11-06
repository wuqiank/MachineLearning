import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class FileParser {

	Vector<Vector<Double>> dataSet;
	Vector<Double> label_values;
	double[][] data_points;
	double[] labels;
	
	/*
	 * Getters and Setters
	 */
	public Vector<Vector<Double>> getDataSet() {
		return dataSet;
	}
	public void setDataSet(Vector<Vector<Double>> dataSet) {
		this.dataSet = dataSet;
	}
	public Vector<Double> getLabel_values() {
		return label_values;
	}
	public void setLabel_values(Vector<Double> label_values) {
		this.label_values = label_values;
	}
	
	/*
	 * Read data set
	 */
	public void readData(FileReader fr) throws IOException{
	
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] split_relt = null;
		Vector<Double> data_point = null;
		this.dataSet = new Vector<Vector<Double>>();
		this.label_values = new Vector<Double>();
		int len = 0;
		
		// read one line once
		while((line=br.readLine())!=null) {
			
			data_point = new Vector<Double>();
			// split according to multiple spaces
		    split_relt = line.trim().split("\\s+");

			len = split_relt.length;
			
			for(int i = 0; i < len - 1; i++) {
				data_point.add(Double.parseDouble(split_relt[i]));
			}
			this.label_values.add(Double.parseDouble(split_relt[len - 1]));
			this.dataSet.add(data_point);	
		}
		
		int data_count = this.label_values.size();
		int dimension = this.dataSet.get(0).size();
		this.data_points = new double[data_count][dimension + 1];
		this.labels = new double[data_count];
		
		for (int i = 0; i < data_count; i++) {

			if (label_values.get(i) == -1) {
				
				data_points[i][0] = -1;
				for (int j = 0; j < dimension; j++) {
					data_points[i][j + 1] = -dataSet.get(i).get(j);
				}
				labels[i] = 1;
			}
			else {
				
				data_points[i][0] = 1;
				for (int j = 0; j < dimension; j++) {
					data_points[i][j + 1] = dataSet.get(i).get(j);
				}
				labels[i] = label_values.get(i);
			}	
		}
	}
	
	/*
	 * normalization 
	 */
	public void normalize() {
		
		int data_count = this.label_values.size();
		int dimension = this.dataSet.get(0).size();
		
		for (int i = 1; i < dimension + 1; i++) {

			double sum = 0.0;

			for (int j = 0; j < data_count; j++)
				sum += data_points[j][i];

			double mean = sum / data_count;
			double variance = 0.0;

			for (int j = 0; j < data_count; j++) {

				variance += (data_points[j][i] - mean)
						* (data_points[j][i] - mean);
			}
			variance = Math.sqrt(variance / data_count);

			for (int j = 0; j < data_count; j++) {

				data_points[j][i] = (data_points[j][i] - mean) / variance;
			}
		}
		
	}
	
	/*
	 * Print data set
	 */
	public void printData() {
		
		for (int i = 0; i < this.dataSet.size(); i++) {
			for (int j = 0; j < this.dataSet.get(0).size() + 1; j++) {
				System.out.print(data_points[i][j] + " ");
			}
			System.out.print(labels[i]);
			System.out.println();
		}
	}
}
