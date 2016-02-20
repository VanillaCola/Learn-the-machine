

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * Nearest Cover Tree Implementation
 * @author Jiyuan Li and Liye Guo
 *
 * @param <T>
 */

public class NearestCoverTree<T> {

	private Node<T> root;
	private int level;
	private String measure;
	
	public NearestCoverTree(String measure, int level) {
		root = null;
		this.level = level;
		this.measure = measure;
	}
	
	/**
	 * Get root of cover tree
	 * @return
	 */
	public Node<T> getRoot()
	{
		return root;
	}
	
	public boolean insertAtRoot(T element, double[] x)
	{
		if(root != null) return false;
		root = new Node<T>(null, x);
		root.setLevel(level);
		return true;
	}
	
	/**
	 * Insert a node with its points into the tree
	 * @param node
	 * @param points
	 * @return
	 */
	public Node<T> insert(T element, Node<T> p, double[] x)
	{
		//if d(p, x) > 2covdist(p)
		if(p.distanceToPoint(x, measure) > p.covdist())
		{
			while(p.distanceToPoint(x, measure) > 2 * p.covdist())
			{
				//remove any leaf(a node with no children) q from p	
				//Getting a node's descendants is very very expensive, we may change this later on
				for(Node<T> q : p.getDescendants())
				{
					if(q.getChildren().size() == 0)
					{
						Node<T> p_prime = q;
						q.getParent().removeChild(q);
						p_prime.addChildren(p);	//tree with root q and p as only child
						p = p_prime;
					}
				}
			}
			
			//tree with x as root and p as only child
			Node<T> newRoot = new Node<T>(null, x);
			newRoot.addChildren(p);
			newRoot.setLevel(p.getLevel()+1);
			this.root = newRoot;
			return newRoot;
		}
		return insert_(p , element, x);
	}
	
	/**
	 * 
	 * @param node
	 * @param points
	 * @return
	 */
	private Node<T> insert_(Node<T> p, T element, final double[] x)
	{	
		//Sort children in p by distance to x
		CopyOnWriteArrayList<Node<T>> children = p.getChildren();
		
		/*
		Comparator<Node<T>> comparator = new Comparator<Node<T>>()
		{
			@Override
			public int compare(Node<T> n1, Node<T> n2) {
				if(n1.distanceToPoint(x) < n2.distanceToPoint(x))
					return -1;
				else if(n1.distanceToPoint(x) > n2.distanceToPoint(x))
					return 1;
				return 0;
			}
		};
		ArrayList<Node<T>> temp = new ArrayList<Node<T>>(children);
		Collections.sort(temp, comparator);
		children.clear();
		children.addAll(temp);
		*/
		
		for(Node<T> q: children)
		{
			//if d(q, x) <= covdist(q)
			if(q.distanceToPoint(x, measure) <= q.covdist())
			{	
				int index = p.getChildren().indexOf(q);		//Get the index of child q in the p
				Node<T> q_prime = insert_(q, element, x);
			
				//p with child q replaced with q_prime
				p.getChildren().set(index, q_prime);
				
				return p;	//return p_prime
			}
		}
		return rebalance(p, element, x);
	}
	
	public Node<T> rebalance(Node<T> p, T element, double[] x)
	{
		
		//create tree x_prime with root node x at level level(p)-1 x_prime contains no other points
		Node<T> x_prime = new Node<T>(null, x);
		x_prime.setLevel(p.getLevel()-1);
		
		//p_prime = p
		Node<T> p_prime = p;
		for(Node<T> q : p.getChildren())
		{
			//Old
			//Pack<T> pack = rebalance_(p ,q, x, element);	//(q_prime, moveset, stayset) <- rebalanced_(p, q, x)
			
			Pack<T> pack = rebalance_(q ,q, element, x);	//(q_prime, moveset, stayset) <- rebalanced_(q, q, x)
			
			//p_prime with child q replaced with q_prime
			p_prime.getChildren().set(p_prime.getChildren().indexOf(q), pack.q_prime);
			
			for(double[] r: pack.moveset)
			{
				x_prime = insert(element, x_prime, r);
			}
		}
		p_prime.addChildren(x_prime);	//p_prime with x_prime added as a child
		x_prime.setParent(p_prime);
		return p_prime;
	}
	
	private Pack<T> rebalance_(Node<T> p, Node<T> q, T element, double[] x)
	{	
		//if d(p,q) > d(q, x)
		if(p.distanceToPoint(q.getPoints(), measure) > q.distanceToPoint(x, measure))
		{
			//moveset, stayset <- empty sets
			CopyOnWriteArraySet<double[]> moveset = new CopyOnWriteArraySet<double[]>();
			CopyOnWriteArraySet<double[]> stayset = new CopyOnWriteArraySet<double[]>();
			
			//for r in descendants(q)
			for(Node<T> r : q.getDescendants())
			{
				//if d(r,p) > d(q, x)
				if(r.distanceToPoint(p.getPoints(), measure) > r.distanceToPoint(x, measure))
						moveset.add(r.getPoints());	//moveset U {r}
				else
						stayset.add(r.getPoints());	//stayset U {r}
			}
			
			Pack<T> pack = new Pack<T>(null, moveset, stayset);
			
			return pack;
		}
		else
		{
			//moveset_prime, stayset_prime <- empty sets
			CopyOnWriteArraySet<double[]> moveset_prime = new CopyOnWriteArraySet<double[]>();
			CopyOnWriteArraySet<double[]> stayset_prime = new CopyOnWriteArraySet<double[]>();
			Node<T> q_prime = q;	//q_prime = q
			
			//for r in children(q) do
			for(Node<T> r: q.getChildren())
			{
				Pack<T> pack = rebalance_(p, r, element, x);	//(r_prime, moveset, stayset) <- rebalance_(p,r,x)
				for(double[] point: pack.moveset)
				{
						moveset_prime.add(point);
				}
				//moveset_prime.addAll(pack.moveset);	//moveset_prime <- moveset U moveset_prime
				
				for(double[] point: pack.stayset)
				{
						stayset_prime.add(point);
				}
				//stayset_prime.addAll(pack.stayset); //stayset_prime <- stayset U stayset_prime
				
				//if r^prime == null
				if(pack.q_prime == null)
				{
					//this is not working!  q_prime <- q with the subtree r removed
					q.removeChild(r);
				}
				else
				{
					//q_prime <- q with subtree r replaced by r_prime
					int index = q.getChildren().indexOf(r);
					q.getChildren().set(index, pack.q_prime);
				}
			}
			
			//for r in stayset_prime
			for(double[] r: stayset_prime)
			{
				//if d(r,q)_prime <= covdist(q)_prime
				if(q.distanceToPoint(r, measure) <= q_prime.covdist())
				{
					q_prime = insert(element, q_prime, r);		//q_prime = insert(q^prime, r)
					stayset_prime.remove(r);	//stayset_prime <- stayset_prime - {r}
				}
			}
			
			Pack<T> pack = new Pack<T>(q_prime, moveset_prime, stayset_prime);
			return pack;
		}

	}
	
	/**
	 * Find nearest neighbor for a specific point
	 * @param points
	 * @return
	 */
	public Node<T> findNearestNeighbor(Node<T> p, final double[] x, Node<T> y)
	{
		
		if(p.distanceToPoint(x, measure) < y.distanceToPoint(x, measure))
			y = p;
		
		//List<Node<T>> children = p.getChildren();
		//Sort children in p by distance to x
		
		CopyOnWriteArrayList<Node<T>> children = p.getChildren();

		/*
		Comparator<Node<T>> comparator = new Comparator<Node<T>>()
		{
			@Override
			public int compare(Node<T> n1, Node<T> n2) {
				if(n1.distanceToPoint(x) < n2.distanceToPoint(x))
					return -1;
				else if(n1.distanceToPoint(x) > n2.distanceToPoint(x))
					return 1;
				return 0;
			}
		};
		ArrayList<Node<T>> temp = new ArrayList<Node<T>>(children);
		Collections.sort(temp, comparator);
		children.clear();
		children.addAll(temp);
		*/
		for(Node<T> q : children)
		{
			//Old
			/*
			if(y.distanceToPoint(x) > (y.distanceToPoint(q.getPoints()) - q.maxdist()))
			{
				y = findNearestNeighbor(q, x, y);
			}
			*/
			
			
			//d(y, x) > d(x, q) - maxdist(q)
			if(y.distanceToPoint(x, measure) > (q.distanceToPoint(x, measure) - q.maxdist()))
			{
				y = findNearestNeighbor(q, x, y);
			}
		}
		return y;
	}
	
	public void updateMaxDist()
	{
		Queue<Node<T>> queue = new LinkedList<Node<T>>();
		queue.add(root);
		while(queue.size() != 0)
		{
			Node<T> cur = queue.poll();
			double max = -1;
			for(Node<T> descendant: cur.getDescendants())
			{
				double dist = cur.distanceToPoint(descendant.getPoints(), measure);
				if(dist > max)
					max = dist;
			}
			cur.setmaxdist(max);
			if(cur.getChildren().size() != 0)
			{
				for(Node<T> child: cur.getChildren())
				{
					queue.add(child);
				}
			}
		}
	}
	
	/**
	 * NCT to String level by level
	 */
	public void tostring()
	{
		//StringBuilder builder = new StringBuilder();
		Node<T> node = root;
		Queue<Node<T>> queue = new LinkedList<Node<T>>();
		queue.add(node);
		while(!queue.isEmpty())
		{
			int size = queue.size();
			while(size != 0)
			{
				Node<T> cur = queue.poll();
				System.out.println(Arrays.toString(cur.getPoints()) + " " + cur.getLevel());
				if(cur.getChildren().size() != 0)
				{
					for(Node<T> child: cur.getChildren())
					{
						queue.add(child);	
					}
				}
				
				size--;
			}
			System.out.println("\n");
		}
	}
	

}
