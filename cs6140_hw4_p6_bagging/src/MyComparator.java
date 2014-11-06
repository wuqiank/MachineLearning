

import java.util.Comparator;

public class MyComparator implements Comparator<Object> {
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object obj1, Object obj2) {

		FeatureAndLabel o1 = (FeatureAndLabel) obj1;
		FeatureAndLabel o2 = (FeatureAndLabel) obj2;
		
		return o1.getFeature_val().compareTo(o2.getFeature_val());
	}
}
