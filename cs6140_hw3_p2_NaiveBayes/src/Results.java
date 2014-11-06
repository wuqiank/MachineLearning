
public class Results {

	double TPR;
	double FPR;
	double FNR;
	double ERR;
	double[] TPR_nodes;
	double[] FPR_nodes;
	double AUC;
	
	public double getAUC() {
		return AUC;
	}
	public void setAUC(double aUC) {
		AUC = aUC;
	}
	public double[] getTPR_nodes() {
		return TPR_nodes;
	}
	public void setTPR_nodes(double[] tPR_nodes) {
		TPR_nodes = tPR_nodes;
	}
	public double[] getFPR_nodes() {
		return FPR_nodes;
	}
	public void setFPR_nodes(double[] fPR_nodes) {
		FPR_nodes = fPR_nodes;
	}
	public double getTPR() {
		return TPR;
	}
	public void setTPR(double tPR) {
		TPR = tPR;
	}
	public double getFPR() {
		return FPR;
	}
	public void setFPR(double fPR) {
		FPR = fPR;
	}
	public double getFNR() {
		return FNR;
	}
	public void setFNR(double fNR) {
		FNR = fNR;
	}
	public double getERR() {
		return ERR;
	}
	public void setERR(double eRR) {
		ERR = eRR;
	}
}
