import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class FileParser {

	Vector<Vector<Double>> dataSet;
	
	/*
	 * Read data set
	 */
	public void readData(FileReader fr, int model_num) throws IOException{
	
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] split_relt = null;
		Vector<Double> data_point = null;
		this.dataSet = new Vector<Vector<Double>>();
		
		// read one line once
		while((line=br.readLine())!=null) {
			
			data_point = new Vector<Double>();
			// split according to multiple spaces
		    split_relt = line.trim().split(" ");
			
			for(String feature : split_relt) {
				data_point.add(Double.parseDouble(feature));
			}
			
			Random random = new Random();
			data_point.add((double)(random.nextInt(model_num)));
			
			this.dataSet.add(data_point);	
		}
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
