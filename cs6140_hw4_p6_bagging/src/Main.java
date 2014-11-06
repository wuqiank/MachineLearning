import java.io.FileReader;


public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis() ;
		FileReader fr_spam = new FileReader(args[0]);
		FileParser fp_spam = new FileParser();
		
		fp_spam.readData(fr_spam);
		//fp_spam.printData();
		
		//DecisionTree dt = new DecisionTree();
		//dt.DT_Creator(fp_spam.getDataSet(), fp_spam.getLabel_values(), 0);
	
		//dt.print_nodes();
		Bagging bag = new Bagging();
		bag.run(fp_spam.getDataSet(), fp_spam.getLabel_values());
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 
		
		
	}
}
