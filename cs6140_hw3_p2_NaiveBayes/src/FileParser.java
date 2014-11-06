import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class FileParser {

	Vector<Vector<Double>> dataSet;
	
	/*
	 * Read data set
	 */
	public void readData(FileReader fr) throws IOException{
	
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] split_relt = null;
		Vector<Double> data_point = null;
		this.dataSet = new Vector<Vector<Double>>();
		Vector<Vector<Double>> dataSet_temp = new Vector<Vector<Double>>();
		
		// read one line once
		while((line=br.readLine())!=null) {
			
			data_point = new Vector<Double>();
			// split according to multiple spaces
		    split_relt = line.trim().split(",");
			
			for(String feature : split_relt) {
				data_point.add(Double.parseDouble(feature));
			}
			dataSet_temp.add(data_point);	
		}
		
		Collections.shuffle(dataSet_temp);
		for (int i = 0; i < dataSet_temp.size(); i++) {
			 
			 data_point = new Vector<Double>();
			 
			 for (int j = 0; j < dataSet_temp.get(0).size(); j++) {
				 data_point.add(dataSet_temp.get(i).get(j));
			 }
			 this.dataSet.add(data_point);
		 }
		
		// normalize
		/*
		int dim = dataSet.get(0).size() - 1;
		double size = dataSet.size();
		
		for (int i = 0; i < dim; i++) {

			double sum = 0.0;

			for (int j = 0; j < size; j++)
				sum += dataSet.get(j).get(i);

			double mean = sum / size;
			double variance = 0.0;

			for (int j = 0; j < size; j++) {

				variance += (dataSet.get(j).get(i) - mean) * (dataSet.get(j).get(i) - mean);
			}
			variance = Math.sqrt(variance / size);

			for (int j = 0; j < size; j++) {

				dataSet.get(j).set(i,  (dataSet.get(j).get(i) - mean) / variance);
			}
		}
		*/
	}
	
	
	
	/*
	 * Print data set
	 */
	public void printData() {
		
		for (Vector<Double> data_point : this.dataSet) {
			System.out.println(data_point);
		}
	}
}
