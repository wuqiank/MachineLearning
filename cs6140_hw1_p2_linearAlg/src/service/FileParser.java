package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class FileParser {

	Vector<Vector<Double>> dataSet;
	Vector<Double> label_values;
	
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
	public void readData(FileReader fr, String kind) throws IOException{
	
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
			if (kind == " ") {
				split_relt = line.trim().split("\\s+");
			}
			else if (kind == ",") {
				split_relt = line.trim().split(",");
			}
			
			len = split_relt.length;
			
			for(int i = 0; i < len - 1; i++) {
				data_point.add(Double.parseDouble(split_relt[i]));
			}
			this.label_values.add(Double.parseDouble(split_relt[len - 1]));
			this.dataSet.add(data_point);
			
		}
	}
	
	/*
	 * Print data set
	 */
	public void printData() {
		System.out.println(this.dataSet);
		System.out.println(this.label_values);
	}
}
