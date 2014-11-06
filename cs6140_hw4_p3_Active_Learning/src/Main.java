import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis() ;
		FileReader fr_spam = new FileReader(args[0]);
		FileParser fp_spam = new FileParser();
		
		fp_spam.readData(fr_spam);
		//fp_spam.printData();
		ActiveLearning al = new ActiveLearning();
		al.run(fp_spam.dataSet, "adaboost");
		al.run(fp_spam.dataSet, "active");
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 
		
		
	}
}
