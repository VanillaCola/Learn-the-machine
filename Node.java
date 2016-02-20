

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Node implementation
 * @author Jiyuan Li and Liye Guo
 *
 * @param <T>
 */
public class Node<T>{
	
	private Node<T> parent;
	private CopyOnWriteArrayList<Node<T>> children;
	private double distance;
	private double[] points;
	private int level;	//The leveling invariant
	//private T val;
	
	private double maxdist;

	public Node(Node<T> parent, double[] points) {
		this.parent = parent;
		this.points = points;
		this.children = new CopyOnWriteArrayList<Node<T>>();
		this.maxdist = 0;
		//this.val = val;
	}
	
	/*
	public T getValue()
	{
		return val;
	}
	
	public void setValue(T val)
	{
		this.val = val;
	}
	*/
	
	/**
	 * The covering invariant
	 * @return
	 */
	public double covdist()
	{
		return Math.pow(2, getLevel());
	}
	
	/**
	 * The separating invariant
	 * @return
	 */
	public double sepdist()
	{
		return Math.pow(2, getLevel()-1);
	}
	
	/**
	 * Set level
	 * @param level
	 */
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	/**
	 * Get level
	 * @return
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Set the parent of current node
	 * @param n
	 */
	public void setParent(Node<T> n)
	{
		this.parent = n;
	}
	
	/**
	 * Get the parent of current node
	 * @return
	 */
	public Node<T> getParent()
	{
		return parent;
	}
	
	/**
	 * Add a child
	 * @param n
	 */
	public void addChildren(Node<T> n)
	{
		children.add(n);
	}
	
	/**
	 * Get the children of this current including itself?
	 * @return
	 */
	public CopyOnWriteArrayList<Node<T>> getChildren()
	{
		return children;
	}
	
	/**
	 * Remove the child from children
	 * @param node
	 */
	public void removeChild(Node<T> n)
	{
		children.remove(n);
	}
	
	/**
	 * Remove children
	 */
	public void removeChildren()
	{
		children.clear();
	}
	
	/**
	 * Get Descendants
	 * @return
	 */
	public List<Node<T>> getDescendants()
	{
		Queue<Node<T>> queue = new LinkedList<Node<T>>();
		queue.add(this);
		List<Node<T>> descendants = new LinkedList<Node<T>>();
		descendants.add(this);
		while(!queue.isEmpty())
		{
			Node<T> node = queue.poll();
			if(node.getChildren().size() != 0)
			{
				queue.addAll(node.children);
				descendants.addAll(node.children);
			}
		}
		return descendants;
	}
	
	/**
	 * set distance
	 * @param distance
	 */
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	/**
	 * get distance
	 * @return
	 */
	public double getDistance()
	{
		return distance;
	}
	
	/**
	 * maxdist invariant
	 * @return
	 */
	public double maxdist()
	{
		return maxdist;
	}
	
	public void setmaxdist(double dist)
	{
		this.maxdist = dist;
	}
	
	
	
	public double[] getPoints()
	{
		return points;
	}
	
	/**
	 * Calculate distance to a point using Euclidean or Hamming distance measurement
	 * @param point
	 * @return
	 */
	public double distanceToPoint(double[] points, String measurement)
	{
		if(measurement.equals("Euclidean"))
		{
			double result = 0;
			for(int i = 0; i < this.points.length; i++)
			{
				double distance = this.points[i] - points[i];
				result += distance * distance;
			}
			return Math.sqrt(result);
		}
		else if(measurement.equals("Hamming"))
		{
			int diff = 0;
			for(int i = 0; i < this.points.length; i++)
			{
				if(this.points[i] != points[i])	
					diff++;
			}
			return diff;
		}
		return 0;
	}
}
