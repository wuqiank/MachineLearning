package main;

import java.io.FileReader;

import bean.LinearRegression;

import service.FileParser;
import service.Kfold;

public class Main {

	public static void main(String[] args)throws Exception{
		
		// reader of the training data set
		FileReader fr_train = new FileReader(args[0]);
		FileParser fp_train = new FileParser();
		
		// reader of the test data set
		FileReader fr_test = new FileReader(args[1]);
		FileParser fp_test = new FileParser();
		
		FileReader fr_spam = new FileReader(args[2]);
		FileParser fp_spam = new FileParser();
		
		fp_train.readData(fr_train, " ");
		fp_test.readData(fr_test, " ");
		fp_spam.readData(fr_spam, ",");
		
		LinearRegression lr = new LinearRegression();
		double[] objective = lr.get_objective(fp_train.getDataSet(), fp_train.getLabel_values());
		double testing_house_mse = lr.get_error(fp_test.getDataSet(), fp_test.getLabel_values(), objective);
		double training_house_mse = lr.get_error(fp_train.getDataSet(), fp_train.getLabel_values(), objective);
		System.out.println("testing housing data mse: " + testing_house_mse);
		System.out.println("training housing data mse: " + training_house_mse);
		
		Kfold kfold = new Kfold();
		kfold.get_kfold_split(fp_spam.getDataSet(), fp_spam.getLabel_values());
	}
}
