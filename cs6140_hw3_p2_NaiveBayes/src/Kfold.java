
import java.util.Vector;

public class Kfold {

	// divide data into 10 folds
	int k = 10;
	int test_num = 460;
	
	
	/*
	 * split data set
	 */
	public void get_kfold_split(Vector<Vector<Double>> dataSet, String type) {
		
		double err = 0;
		double size = dataSet.size();
		
		System.out.println(type + " Error table:");
		
		for (int k_index = 0; k_index < 10; k_index++) {
			
			Vector<Vector<Double>> train_set = new Vector<Vector<Double>>();
			Vector<Vector<Double>> test_set = new Vector<Vector<Double>>();
			
			for (int i = 0; i < size; i++) {
				
				if (i >= k_index * test_num && i <= (k_index + 1) * test_num) {
					test_set.add(dataSet.get(i));
				}
				else {
					train_set.add(dataSet.get(i));
				}	
			}
			
			if (type == "Bernoulli Model") {
				
				// Bernoulli Model
				BernoulliModel bm = new BernoulliModel();
				bm.run_bm(train_set, test_set);
				System.out.println("Fold " + (k_index + 1) + ": FPR = " + bm.test_res.getFPR() + 
						", FNR = " + bm.test_res.getFNR());
				err += bm.test_res.getERR();
				
				if (k_index == 0) {
					
					double[] TPR_nodes = bm.test_res.getTPR_nodes();
					double[] FPR_nodes = bm.test_res.getFPR_nodes();
					
					System.out.println("|");
					System.out.println("FPR for each date point");
					for (int i = 0; i < FPR_nodes.length; i++) {
						
						System.out.print(FPR_nodes[i] + " ");
						//System.out.println(TPR[i] + " " + FPR[i]);
					}
					System.out.println();
					System.out.println("TPR for each date point");
					for (int j = 0; j < TPR_nodes.length; j++) {
						
						System.out.print(TPR_nodes[j] + " ");
						//System.out.println(TPR[i] + " " + FPR[i]);
					}
					System.out.println();
					System.out.println("Bernoulli Model AUC = " + bm.test_res.getAUC());
					System.out.println("|");
					
				}
			}
			
			else if (type == "Gaussian Model") {
				
				// Gaussian Model
				GaussianModel gm = new GaussianModel();
				gm.run_gm(train_set, test_set);
				System.out.println("Fold " + (k_index + 1) + ": FPR = " + gm.test_res.getFPR() + 
						", FNR = " + gm.test_res.getFNR());
				err += gm.test_res.getERR();
				
				if (k_index == 0) {
					
					double[] TPR_nodes = gm.test_res.getTPR_nodes();
					double[] FPR_nodes = gm.test_res.getFPR_nodes();
					
					System.out.println("|");
					System.out.println("FPR for each date point");
					for (int i = 0; i < FPR_nodes.length; i++) {
						
						System.out.print(FPR_nodes[i] + " ");
						//System.out.println(TPR[i] + " " + FPR[i]);
					}
					System.out.println();
					System.out.println("TPR for each date point");
					for (int j = 0; j < TPR_nodes.length; j++) {
						
						System.out.print(TPR_nodes[j] + " ");
						//System.out.println(TPR[i] + " " + FPR[i]);
					}
					System.out.println();
					System.out.println("Gaussian Model AUC = " + gm.test_res.getAUC());
					System.out.println("|");
				}
			}
			
			else if (type == "Histogram 4Bin") {
				
				// Histogram4Bin
				Histogram4Bin h4b = new Histogram4Bin();
				h4b.run_h4b(train_set, test_set);
				System.out.println("Fold " + (k_index + 1) + ": FPR = " + h4b.test_res.getFPR() + 
						", FNR = " + h4b.test_res.getFNR());
				err += h4b.test_res.getERR();
				
				
				if (k_index == 0) {
					
					double[] TPR_nodes = h4b.test_res.getTPR_nodes();
					double[] FPR_nodes = h4b.test_res.getFPR_nodes();
					
					System.out.println("|");
					System.out.println("FPR for each date point");
					for (int i = 0; i < FPR_nodes.length; i++) {
						
						System.out.print(FPR_nodes[i] + " ");
						//System.out.println(TPR[i] + " " + FPR[i]);
					}
					System.out.println();
					System.out.println("TPR for each date point");
					for (int j = 0; j < TPR_nodes.length; j++) {
						
						System.out.print(TPR_nodes[j] + " ");
						//System.out.println(TPR[i] + " " + FPR[i]);
					}
					System.out.println();
					System.out.println("Histogram 4Bin AUC = " + h4b.test_res.getAUC());
					System.out.println("|");
				}
			}
			
			else if (type == "Histogram 9Bin") {
				
				// Histogram9Bin
				Histogram9Bin h9b = new Histogram9Bin();
				h9b.run_h9b(train_set, test_set);
				System.out.println("Fold " + (k_index + 1) + ": FPR = " + h9b.test_res.getFPR() + 
						", FNR = " + h9b.test_res.getFNR());
				err += h9b.test_res.getERR();
			}
		}
		
		System.out.println("Average Error Rate = " + err / 10);
		System.out.println();
	}
	
	
	
}
