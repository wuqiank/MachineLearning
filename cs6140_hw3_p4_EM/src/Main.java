import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis() ;
		FileReader fr_spam2 = new FileReader(args[0]);
		FileParser fp_spam2 = new FileParser();
		
		FileReader fr_spam3 = new FileReader(args[1]);
		FileParser fp_spam3 = new FileParser();
		
		fp_spam2.readData(fr_spam2, 2);
		//fp_spam2.printData();
		fp_spam3.readData(fr_spam3, 3);
		//fp_spam3.printData();
		
		EM em = new EM();
		em.run(fp_spam2.dataSet, 2);
		System.out.println();
		em.run(fp_spam3.dataSet, 3);
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 	
	}
}
