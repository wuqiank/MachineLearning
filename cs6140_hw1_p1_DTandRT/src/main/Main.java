package main;

import java.io.FileReader;
import java.util.Vector;

import bean.DecisionTree;
import bean.RegressionTree;
import services.FileParser;
import services.Kfold;

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
		
		RegressionTree rt = new RegressionTree();
		rt.RT_Creator(fp_train.getDataSet(), fp_train.getLabel_values(), 0);
		rt.print_nodes();
		
		Vector<Vector<Double>> data_points = new Vector<Vector<Double>>();
		data_points.add(fp_train.getDataSet().get(0));
		data_points.add(fp_train.getDataSet().get(1));
		
		System.out.println("housing data training error = " + rt.get_mse_travese_tree(fp_train.getDataSet()));
		System.out.println("housing data testing error = " + rt.get_mse_travese_tree(fp_test.getDataSet()));
		System.out.println();
		
		DecisionTree dt = new DecisionTree();
		dt.DT_Creator(fp_spam.getDataSet(), fp_spam.getLabel_values(), 0);
		dt.print_nodes();
		Kfold kf = new Kfold();
		kf.get_kfold_split(fp_spam.getDataSet(), fp_spam.getLabel_values());
		
		/*
		 * unit test
		 */
		/*
		DataSetSplitMethods mse = new DataSetSplitMethods();
		ArrayList<FeatureAndLabel> pair_lst = new ArrayList<FeatureAndLabel>();
		FeatureAndLabel x1 = new FeatureAndLabel(1, 0);
		FeatureAndLabel x2 = new FeatureAndLabel(1, 0);
		FeatureAndLabel x3 = new FeatureAndLabel(1, 0);
		FeatureAndLabel x4 = new FeatureAndLabel(1, 0);
		FeatureAndLabel x5 = new FeatureAndLabel(1, 0);
		FeatureAndLabel x6 = new FeatureAndLabel(1, 1);
		FeatureAndLabel x7 = new FeatureAndLabel(2, 1);
		FeatureAndLabel x8 = new FeatureAndLabel(3, 1);
		FeatureAndLabel x9 = new FeatureAndLabel(1, 1);
		FeatureAndLabel x10 = new FeatureAndLabel(2, 1);
		FeatureAndLabel x11 = new FeatureAndLabel(3, 1);
		FeatureAndLabel x12 = new FeatureAndLabel(1, 1);
		FeatureAndLabel x13 = new FeatureAndLabel(2, 1);
		FeatureAndLabel x14 = new FeatureAndLabel(3, 1);
		pair_lst.add(x1);
		pair_lst.add(x2);
		pair_lst.add(x3);
		pair_lst.add(x4);
		pair_lst.add(x5);
		pair_lst.add(x6);
		pair_lst.add(x7);
		pair_lst.add(x8);
		pair_lst.add(x9);
		pair_lst.add(x10);
		pair_lst.add(x11);
		pair_lst.add(x12);
		pair_lst.add(x13);
		pair_lst.add(x14);

		
		System.out.println(mse.get_mse(pair_lst).getFeature_index());
		System.out.println(mse.get_mse(pair_lst).getFeature_value());
		System.out.println(mse.get_mse(pair_lst).getLeft_mse());
		System.out.println(mse.get_mse(pair_lst).getRight_mse());
		
		
		System.out.println(mse.get_info(pair_lst));
		System.out.println(mse.get_info_gain(pair_lst));
		*/	
	}
}
