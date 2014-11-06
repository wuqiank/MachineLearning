import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.Map.Entry;

public class FileParser {

	Vector<HashMap<Integer, Double>> dataSet;
	HashMap<Integer, HashSet<Double>> id_values;
	double[][] dataSet_array;
	
	/*
	 * Read data set
	 */
	public void readData(FileReader fr, String kind) throws IOException{
	
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] split_relt = null;
		HashMap<Integer, Double> data_point = null;
		this.dataSet = new Vector<HashMap<Integer, Double>>();
		this.id_values = new HashMap<Integer, HashSet<Double>>();

		// read one line once
		while((line=br.readLine()) != null) {
			
			data_point = new HashMap<Integer, Double>();
		    split_relt = line.split("\\s+");
			
		    int len = split_relt.length;
		    for(int i = 1; i < len; i++) {
		    	
		    	String[] inter_split_relt = split_relt[i].split(":");
		    	int id = Integer.parseInt(inter_split_relt[0]);
		    	double val = Double.parseDouble(inter_split_relt[1]);
		    	
		    	data_point.put(id, val);
		    	
		    	if (kind == "train") {
		    		if (!id_values.containsKey(id)) {
			    		
			    		HashSet<Double> values = new HashSet<Double>();
			    		values.add(val);
			    		id_values.put(id, values);
			    	}
			    	else {
			    		id_values.get(id).add(val);
			    	}
		    	}
		    	
		    }

		    data_point.put(1754, Double.parseDouble(split_relt[0]));
		    this.dataSet.add(data_point);	
		}
		
		int size = dataSet.size();
		this.dataSet_array = new double[size][1755];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < 1755; j++) {

				this.dataSet_array[i][j] = 0;
			}	
		}
		
		for (int i = 0; i < size; i++) {
			
			data_point = dataSet.get(i);
			for (Entry<Integer, Double> entry : data_point.entrySet()) {
				
				this.dataSet_array[i][entry.getKey()] = entry.getValue();
			}	
		}
		
	}
	
	
	
	/*
	 * Print data set
	 */
	public void printData() {
		
		for (int i = 0; i < 11314; i++) {
			for (int j = 0; j < 1755; j++) {

				System.out.print(this.dataSet_array[i][j] + " ");
			}	
			System.out.println();
		}
	}
	
	/*
	 * Print feature values
	 */
	public void printFeatureValues() {
	
		for (Entry<Integer, HashSet<Double>> entry : this.id_values.entrySet()) {
		    System.out.println("Feature ID = " + entry.getKey() + ", Values = " + entry.getValue());
		}
		System.out.println();
	}
}
