import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{

		FileReader fr_train = new FileReader(args[0]);
		FileParser fp_train = new FileParser();
		fp_train.readData(fr_train);
		//fp_train.normalize();
		//fp_train.printData();
		
		Perceptron per = new Perceptron();
		per.get_objectives(fp_train.data_points, fp_train.labels);
	}
}
