import java.util.Comparator;

public class MyComparator2 implements Comparator<Object> {
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object obj1, Object obj2) {

		PointAndFeatureVal o1 = (PointAndFeatureVal) obj1;
		PointAndFeatureVal o2 = (PointAndFeatureVal) obj2;
		
		return o2.getDistance().compareTo(o1.getDistance());
	}
}