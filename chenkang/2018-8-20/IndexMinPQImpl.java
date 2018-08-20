package ourck.priorq;

public class IndexMinPQImpl<V extends Comparable<V>>
implements IndexMinPQ<V> {
	
	private int rear;
	private int[] pq;	// TODO Generic class as index?
	private V[] vals;
	
	public IndexMinPQImpl(int size) {
		pq = new int[size];
		for(int i : pq) i = -1;
		vals = (V[]) new Comparable[size];
	}
	
	public void insert(int i, V val) {
		vals[i] = val;
		pq[++rear] = i;
		swim(rear);
	}
	
	public void change(int i, V val) {
		// TODO Index exists?
		vals[i] = val;
		int index = findIndex(i);
		swim(index);
		sink(index);
	}
	
	public boolean contains(int i) { return vals[i] == null; }
	
	public void delete(int i) {
		int index = findIndex(i);
		exch(index, rear);
		
		pq[rear--] = -1;
		vals[i] = null; // GC
		
		swim(index); // TODO 多余？
		sink(index);
	}
	
	public V min() { return vals[minIndex()]; }
	
	public int minIndex() { return pq[1]; }
	
	public int delMin() {
		int minIndex = pq[1];
		
		exch(1, rear);
		pq[rear--] = -1;
		sink(1);
		
		vals[minIndex] = null;  // GC
		return minIndex;
	}
	
	public boolean isEmpty() { return rear == 0; }
	
	public int size() { return rear; }

	public void show() { 
		for(int i = 1; i <= rear; i++) {
			System.out.print(pq[i] + " ");
		}
	}

// ----------------------------------------
	
	private void exch(int i, int j) {
		// Only takes operations on pq[]
		int t = pq[i]; pq[i] = pq[j]; pq[j] = t;
	}
	
	private boolean greater(int i, int j) {
//		System.out.println(vals[pq[i]] + " - " + vals[pq[j]] + ": "
//											+ vals[pq[i]].compareTo(vals[pq[j]]));
		return vals[pq[i]].compareTo(vals[pq[j]]) > 0;
	}
	
	private void swim(int i) {
		while(i > 1 && greater(i / 2, i)) {
//			System.out.println(i / 2 + " - " + i);
//			System.out.println(vals[i / 2] + " - " + vals[i] + ": " + greater(i / 2, i));
			int p = i / 2;
			System.out.println("Exch: " + vals[pq[p]] + " - " + vals[pq[i]]);
			exch(p, i);
			i /= 2;
		}						
	}
	
	private void sink(int i) {
		while(2 * i <= rear) {
			int exchIndex = 2 * i;
			// TODO NULLPTREXP: exchIndex + 1!
			//			解决方法：利用(短路的)与表达式，优先确定是否仍在范围内
			if(exchIndex < rear && greater(exchIndex, exchIndex + 1)) exchIndex++;
			exch(exchIndex, i);
			i = exchIndex;
		}
	}
	
	private int findIndex(int i) {
		// TODO ~ N / 2
		for(int index : pq) {
			if(index == i) return i;
		}
		return -1;
	}
	
	public static void main(String[] args) {
		IndexMinPQImpl pq = new IndexMinPQImpl<Character>(200);
		
		for(Character c : "AINDEXMINPQIMPL".toCharArray()) {
			int index = c;
//			System.out.println(index);
			pq.insert(index, c);
		}
		pq.show();
		
		System.out.println("Empty? " + pq.isEmpty() + ", " + pq.size() + " elements");
		
		System.out.println(pq.minIndex() + " - " + pq.min());
		System.out.println(pq.delMin());
		System.out.println(pq.minIndex() + " - " + pq.min());

	}
	
}
