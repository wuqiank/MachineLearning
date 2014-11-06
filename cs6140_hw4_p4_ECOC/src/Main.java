import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{
			
		long time_start = System.currentTimeMillis();
		
		FileReader fr_train = new FileReader(args[0]);
		FileReader fr_test = new FileReader(args[1]);
		
		FileParser fp_train = new FileParser();
		FileParser fp_test = new FileParser();
		
		fp_train.readData(fr_train, "train");
		//fp_train.printData();
		//fp_train.printFeatureValues();
		
		fp_test.readData(fr_test, "test");
		//fp_test.printData();
		
		ECOC ecoc = new ECOC();
		ecoc.run(fp_train.dataSet_array, fp_test.dataSet_array, fp_train.id_values);
		
		long time_end = System.currentTimeMillis(); 
		System.out.println();
	    System.out.println("run time: " + (time_end - time_start) / 1000 + " seconds"); 
		
		
	}
}
