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
	public void readData(FileReader fr, String kind) throws IOException{
	
		System.out.println(kind + ":");
		BufferedReader br = new BufferedReader(fr);
		String line;
		String[] split_relt = null;
		Vector<Double> data_point = null;
		this.dataSet = new Vector<Vector<Double>>();
		Vector<Vector<Double>> dataSet_temp = new Vector<Vector<Double>>();
		
		// read one line once
		while((line=br.readLine())!=null) {
			
			data_point = new Vector<Double>();
		    split_relt = line.split("\\s+");
		    
		    if (kind == "crx") {
		    	
		    	Vector<Double> temp = this.parse_crx(split_relt, data_point);
		    	if (temp != null) {
		    		dataSet_temp.add(temp);
		    	}
		    	
		    }
		    else if (kind == "vote") {
		    	dataSet_temp.add(this.parse_vote(split_relt, data_point));
		    }
		}
		
		Collections.shuffle(dataSet_temp);
		for (int i = 0; i < dataSet_temp.size(); i++) {
			 
			 data_point = new Vector<Double>();
			 
			 for (int j = 0; j < dataSet_temp.get(0).size(); j++) {
				 data_point.add(dataSet_temp.get(i).get(j));
			 }
			 this.dataSet.add(data_point);
		 }
	}
	
	public Vector<Double> parse_crx(String[] split_result, Vector<Double> data_point) {
		
		int len = split_result.length;
		
		for (int i = 0; i < len; i++) {
			
			String feature_val = split_result[i];
			//System.out.println(feature_val);
			if (feature_val.equals("?")) {
				//System.out.print("aa");
				split_result[i] = "-1.0";
				//return null;
			} 
			else {
				if (i == 0) {
					if (feature_val.equals("b")) {
						split_result[i] = "0.0";
					}
					else {
						split_result[i] = "1.0";
					}
				}
			
				if (i == 3) {
					if (feature_val.equals("u")) {
						split_result[i] = "0.0";
					}
					else if (feature_val.equals("y")) {
						split_result[i] = "1.0";
					}
					else if (feature_val.equals("l")) {
						split_result[i] = "2.0";
					}
					else {
						split_result[i] = "3.0";
					}
				}
			
				if (i == 4 || i == 12) {
					if (feature_val.equals("g")) {
						split_result[i] = "0.0";
					}
					else if (feature_val.equals("p")) {
						split_result[i] = "1.0";
					}
					else {
						split_result[i] = "2.0";
					}
				}
			
				if (i == 5) {
					if (feature_val.equals("c")) {
						split_result[i] = "0.0";
					}
					else if (feature_val.equals("d")) {
						split_result[i] = "1.0";
					}
					else if (feature_val.equals("g")) {
						split_result[i] = "2.0";
					}
					else if (feature_val.equals("i")) {
						split_result[i] = "3.0";
					}
					else if (feature_val.equals("j")) {
						split_result[i] = "4.0";
					}
					else if (feature_val.equals("k")) {
						split_result[i] = "5.0";
					}
					else if (feature_val.equals("m")) {
						split_result[i] = "6.0";
					}
					else if (feature_val.equals("r")) {
						split_result[i] = "7.0";
					}
					else if (feature_val.equals("q")) {
						split_result[i] = "8.0";
					}
					else if (feature_val.equals("w")) {
						split_result[i] = "9.0";
					}
					else if (feature_val.equals("x")) {
						split_result[i] = "10.0";
					}
					else if (feature_val.equals("e")) {
						split_result[i] = "11.0";
					}
					else if (feature_val.equals("a")) {
						split_result[i] = "12.0";
					}
					else {
						split_result[i] = "13.0";
					}
				}
			
				if (i == 6) {
					if (feature_val.equals("v")) {
						split_result[i] = "0.0";
					}
					else if (feature_val.equals("h")) {
						split_result[i] = "1.0";
					}
					else if (feature_val.equals("b")) {
						split_result[i] = "2.0";
					}
					else if (feature_val.equals("j")) {
						split_result[i] = "3.0";
					}
					else if (feature_val.equals("n")) {
						split_result[i] = "4.0";
					}
					else if (feature_val.equals("z")) {
						split_result[i] = "5.0";
					}
					else if (feature_val.equals("d")) {
						split_result[i] = "6.0";
					}
					else if (feature_val.equals("f")) {
						split_result[i] = "7.0";
					}
					else {
						split_result[i] = "8.0";
					}
				}
			
				if (i == 8 || i == 9 || i == 11) {
					if (feature_val.equals("t")) {
						split_result[i] = "0.0";
					}
					else {
						split_result[i] = "1.0";
					}
				}
			
				if (i == 15) {
					if (feature_val.equals("-")) {
						split_result[i] = "0.0";
					}
					else {
						split_result[i] = "1.0";
					}
				}
			}
			
			data_point.add(Double.parseDouble(split_result[i]));
		}
		
		return data_point;
	}
	
	public Vector<Double> parse_vote(String[] split_result, Vector<Double> data_point) {
		
		int len = split_result.length;
		
		for (int i = 0; i < len; i++) {
			
			String feature_val = split_result[i];
			//System.out.println(feature_val);
			if (feature_val.equals("?")) {
				//System.out.print("aa");
				split_result[i] = "-1.0";
			}
			else {
				if (i == 16) {
					if (feature_val.equals("d")) {
						split_result[i] = "0.0";
					}
					else {
						split_result[i] = "1.0";
					}
				}
				else {
					if (feature_val.equals("n")) {
						split_result[i] = "0.0";
					}
					else {
						split_result[i] = "1.0";
					}
				}
			}
			data_point.add(Double.parseDouble(split_result[i]));
		}
		return data_point;
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
