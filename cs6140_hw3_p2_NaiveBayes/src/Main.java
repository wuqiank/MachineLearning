import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis() ;
		FileReader fr_spam = new FileReader(args[0]);
		FileParser fp_spam = new FileParser();
		
		fp_spam.readData(fr_spam);
		//fp_spam.printData();
		
		Kfold kfold = new Kfold();
		kfold.get_kfold_split(fp_spam.dataSet, "Bernoulli Model");
		kfold.get_kfold_split(fp_spam.dataSet, "Gaussian Model");
		kfold.get_kfold_split(fp_spam.dataSet, "Histogram 4Bin");
		kfold.get_kfold_split(fp_spam.dataSet, "Histogram 9Bin");
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 
		
		
		
	}
}
