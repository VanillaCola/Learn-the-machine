

import java.util.ArrayList;
import java.util.List;

/**
 * Linear Searching Implementation
 * @author Jiyuan Li and Liye Guo
 *
 */

public class LinearSearching<T> {
	
	List<T> values;
	List<double[]> list;
	String measure;

	public LinearSearching(String measure) {
		values = new ArrayList<T>();
		list = new ArrayList<double[]>();
		this.measure = measure;
	}

	/**
	 * Insert a point
	 * @param x
	 * @return
	 */
	public boolean insert(T element, double[] x)
	{
		values.add(element);
		return list.add(x);
	}
	
	/**
	 * Find the nearest point
	 * @param x
	 * @return
	 */
	public double[] findNearestNeighbor(double[] x)
	{
		double[] nearest = new double[2];
		double min = Double.MAX_VALUE;
		for(int i = 0; i < list.size(); i++)
		{
			double dist = distanceToPoint(list.get(i), x, measure);
			if(dist < min)
			{
				min = dist;
				nearest = list.get(i);
			}
		}
		return nearest;
	}
	
	/**
	 * Calculate distance to a point using Euclidean distance measurement or Hamming distance measurement
	 * @param point
	 * @return
	 */
	private double distanceToPoint(double[] p1, double[] p2, String measure)
	{
		if(measure.equals("Euclidean"))
		{
			double result = 0;
			for(int i = 0; i < p1.length; i++)
			{
				double distance = p1[i] - p2[i];
				result += distance * distance;
			}
			return Math.sqrt(result);
		}
		else if(measure.equals("Hamming"))
		{
			int diff = 0;
			for(int i = 0; i < p1.length; i++)
			{
				if(p1[i] != p2[i])	
					diff++;
			}
			return diff;
		}
		return 0;
	}


}
