import java.util.Random;
import java.util.Vector;

public class Bagging {

	double test_percent = 10;
	double sample_percent = 80;
	double rounds = 50;
	double size;
	double dim;
	double sample_train_size;
	double fixed_test_size;
	double train_size;
	
	public void run(Vector<Vector<Double>> dataSet, Vector<Double> label_values) {
		
		size = dataSet.size();
		dim = dataSet.get(0).size();
		fixed_test_size = Math.floor(size * test_percent / 100);
		train_size = size -  fixed_test_size;
		sample_train_size = Math.floor(train_size * sample_percent / 100);
		
		double test_sum_se = 0.0;
		
		Vector<Vector<Double>> train_features = new Vector<Vector<Double>>();
		Vector<Double> train_labels = new Vector<Double>();
		Vector<Vector<Double>> test_features = new Vector<Vector<Double>>();
		Vector<Double> test_labels = new Vector<Double>();
		
		for (int i = 0; i < fixed_test_size; i++) {
			
				test_features.add(dataSet.get(i));
				test_labels.add(label_values.get(i));	
		}
		
		for (int i = 0; i < train_size; i++) {
			
			train_features.add(dataSet.get((int) (i + fixed_test_size)));
			train_labels.add(label_values.get((int) (i + fixed_test_size)));
		}
		
		//System.out.println("sample_train_size = " + sample_train_size);
		
		for (int t = 0; t < rounds; t++) {
			
			Vector<Vector<Double>> sample_train_features = new Vector<Vector<Double>>();
			Vector<Double> sample_train_labels = new Vector<Double>();
			
			for (int i = 0; i < sample_train_size; i++) {
				
				Random random = new Random();
				int index = random.nextInt(train_features.size());
				Vector<Double> random_point = train_features.get(index);
				
				sample_train_features.add(random_point);
				sample_train_labels.add(random_point.get((int) (dim - 1)));
			}
			
			
			DecisionTree dt = new DecisionTree();
			dt.DT_Creator(sample_train_features, sample_train_labels, 0);
			double test_se = dt.get_mse_travese_tree(test_features);		
			test_sum_se += test_se;

			
			//System.out.println("spam data testing mse = " + test_se);
		}
		
		System.out.println("spam data testing mse = " + test_sum_se / rounds);
		
		/*System.out.println("spam data training mse = " + train_sum_se / 10);
		System.out.println("spam data testing mse = " + test_sum_se / 10);
		
		System.out.println("spam data training acc = " + train_sum_acc / 10);
		System.out.println("spam data testing acc = " + test_sum_acc / 10);*/
	}
}
