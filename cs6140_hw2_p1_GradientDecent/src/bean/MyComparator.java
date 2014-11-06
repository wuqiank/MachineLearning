package bean;

import java.util.Comparator;

public class MyComparator implements Comparator<Object> {
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object obj1, Object obj2) {

		ProbAndLabel o1 = (ProbAndLabel) obj1;
		ProbAndLabel o2 = (ProbAndLabel) obj2;
		
		return o2.getProb().compareTo(o1.getProb());
	}
}
