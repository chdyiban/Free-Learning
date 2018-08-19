package ourck.priorq;

public class IndexMinPQImpl<V extends Comparable<V>>
implements IndexMinPQ<V> {
	
	private int rear;
	private int[] pq;
	private V[] vals;
	
	public IndexMinPQImpl(int size) {
		pq = new int[size];
		vals = (V[]) new Comparable[size];
	}
	
	public void insert(int i, V val) {
		pq[++rear] = i;
		vals[i] = val;
	}
	

// ----------------------------------------
	
	private void exch(int i, int j) {
		// Only takes operations on pq[]
		int t = i; i = j; j = t;
	}
	
	private boolean less(int i, int j) {
		return vals[pq[i]] < vals[pq[j]];
	}
	
	private void swim(int i) {
		
	}
}
