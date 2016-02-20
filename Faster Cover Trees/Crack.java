

public class Crack {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NearestCoverTree<Double> NCT = new NearestCoverTree<Double>("Euclidean", 0);
		double[] p1 = new double[]{0.1, 1};
		double[] p2 = new double[]{-0.1, 1.9};
		double[] p3 = new double[]{-1.5, 0};
		double[] p4 = new double[]{0, 1.5};
		double[] p5 = new double[]{1.5, 0};
		NCT.insertAtRoot(0.0, p1);
		NCT.insert(0.0, NCT.getRoot(), p2);
		NCT.insert(0.0, NCT.getRoot(), p3);
		NCT.insert(0.0, NCT.getRoot(), p4);
		NCT.insert(0.0, NCT.getRoot(), p5);
		
		NCT.tostring();
	}

}
