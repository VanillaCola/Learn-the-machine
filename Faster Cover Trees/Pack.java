

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Pack
 * @author Jiyuan Li and Liye Guo
 *
 * @param <T>
 */
public class Pack<T> {
	Node<T> q_prime;
	CopyOnWriteArraySet<double[]> moveset;
	CopyOnWriteArraySet<double[]> stayset;
	
	public Pack(Node<T> q_prime, CopyOnWriteArraySet<double[]> moveset, CopyOnWriteArraySet<double[]> stayset)
	{
		this.q_prime = q_prime;
		this.moveset = moveset;
		this.stayset = stayset;
	}

}
