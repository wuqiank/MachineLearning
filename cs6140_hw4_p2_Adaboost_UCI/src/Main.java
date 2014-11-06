import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis() ;
		
		FileReader fr_crx = new FileReader(args[0]);
		FileReader fr_vote = new FileReader(args[1]);
		
		FileParser fp = new FileParser();
		fp.readData(fr_crx, "crx");
		//fp.printData();
		
		fp.readData(fr_vote, "vote");
		//fp.printData();
		
		Kfold kfold = new Kfold();
		//kfold.get_kfold_split(fp.dataSet);
		
		kfold.get_c_percent(fp.dataSet);
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 
		
		
	}
}
