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
		
		//Collections.shuffle(dataSet_temp);
		for (int i = 0; i < dataSet_temp.size(); i++) {
			 
			 data_point = new Vector<Double>();
			 
			 for (int j = 0; j < dataSet_temp.get(0).size(); j++) {
				 data_point.add(dataSet_temp.get(i).get(j));
			 }
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
