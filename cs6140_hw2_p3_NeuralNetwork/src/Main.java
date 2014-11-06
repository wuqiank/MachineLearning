import java.io.FileReader;

public class Main {

	public static void main(String[] args)throws Exception{

		FileReader fr_train = new FileReader(args[0]);
		FileParser fp_train = new FileParser();
		fp_train.readData(fr_train);
		//fp_train.printData();
		
		NeuralNetwork nn = new NeuralNetwork();
		nn.get_objectives(fp_train.data_points, fp_train.labels);
	}
}
