import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis() ;
		FileReader fr_spam = new FileReader(args[0]);
		FileParser fp_spam = new FileParser();
		
		fp_spam.readData(fr_spam);
		//fp_spam.printData();
		
		Kfold kfold = new Kfold();
		kfold.get_kfold_split(fp_spam.dataSet);
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 
		
		
		// test for get Cov
		/*
		Vector<Vector<Double>> dataSet = new Vector<Vector<Double>>();
		Vector<Double> temp1 = new Vector<Double>();
		Vector<Double> temp2 = new Vector<Double>();
		Vector<Double> temp3 = new Vector<Double>();
		Vector<Double> temp4 = new Vector<Double>();
		
		temp1.add(1.0);
		temp1.add(2.0);
		temp1.add(1.0);
		
		temp2.add(3.0);
		temp2.add(6.0);
		temp2.add(0.0);
		
		temp3.add(4.0);
		temp3.add(2.0);
		temp3.add(1.0);
		
		temp4.add(5.0);
		temp4.add(2.0);
		temp4.add(0.0);
		
		dataSet.add(temp1);
		dataSet.add(temp2);
		dataSet.add(temp3);
		dataSet.add(temp4);
		
		double[][] Cov = kfold.get_cov(dataSet);
		double[] mean_0 = kfold.get_mean_vector(dataSet, 0.0);
		double[] mean_1 = kfold.get_mean_vector(dataSet, 1.0);
		
		System.out.println("Convariance:");
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.print(Cov[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println("mean vector 0:");
		for (int i = 0; i < mean_0.length; i++) {
			System.out.print(mean_0[i] + " ");
		}
		System.out.println();
		System.out.println();
		
		System.out.println("mean vector 1:");
		for (int i = 0; i < mean_1.length; i++) {
			System.out.print(mean_1[i] + " ");
		}
		System.out.println();
		System.out.println();
		
		System.out.println("prob for each x while y = 0:");
		for (Vector<Double> data : dataSet) {
			System.out.println(kfold.get_prob(mean_0, Cov, data));
		}
		System.out.println();
		System.out.println("prob for each x while y = 1:");
		for (Vector<Double> data : dataSet) {
			System.out.println(kfold.get_prob(mean_1, Cov, data));
		}
		*/
		
	}
}
