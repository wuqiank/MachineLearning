import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class FileParser {

	Vector<Vector<Double>> dataSet;
	double[][] data_points;
	double[][] labels;
	
	/*
	 * Getters and Setters
	 */
	public Vector<Vector<Double>> getDataSet() {
		return dataSet;
	}
	public void setDataSet(Vector<Vector<Double>> dataSet) {
		this.dataSet = dataSet;
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
		int len = 0;
		
		// read one line once
		while((line=br.readLine())!=null) {
			
			data_point = new Vector<Double>();
			// split according to multiple spaces
		    split_relt = line.trim().split("\\s+");

			len = split_relt.length;
			
			for(int i = 0; i < len; i++) {
				data_point.add(Double.parseDouble(split_relt[i]));
			}
			this.dataSet.add(data_point);	
		}
		
		int data_count = this.dataSet.size();
		int dimension = this.dataSet.get(0).size();
		this.data_points = new double[data_count][dimension];
		this.labels = new double[data_count][data_count];
		
		//System.out.println(dimension);
		
		for (int i = 0; i < data_count; i++) {
			for (int j = 0; j < dimension; j++) {
				
				data_points[i][j] = dataSet.get(i).get(j);
				if (j == i)
					labels[i][j] = 1;
				else
					labels[i][j] = 0;
			}
		}
	}
	
	/*
	 * Print data set
	 */
	public void printData() {
		
		for (int i = 0; i < this.dataSet.size(); i++) {
			for (int j = 0; j < this.dataSet.get(0).size(); j++) {
				System.out.print(data_points[i][j] + " ");
			}
			
			System.out.print("label: ");
			
			for (int j = 0; j < this.dataSet.get(0).size(); j++) {
				System.out.print(labels[i][j] + " ");
			}
			//System.out.print(labels[i]);
			System.out.println();
		}
	}
}
