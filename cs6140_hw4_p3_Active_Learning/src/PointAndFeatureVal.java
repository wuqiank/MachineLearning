import java.util.Vector;


public class PointAndFeatureVal {

	Vector<Double> point;
	Double distance;
	
	public Vector<Double> getPoint() {
		return point;
	}
	public void setPoint(Vector<Double> point) {
		this.point = point;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public PointAndFeatureVal(Vector<Double> point, double distance) {
		
		this.point = point;
		this.distance = distance;
	}
	
	
}
