package ourck.priorq;

public class PriorityQueue<T extends Comparable<T>> {
	
	/**
	 * TODO Static array seems too easy...?
	 */
	private T[] data;
	private int ptr = 0;
	
	public PriorityQueue(int size) { data = (T[]) new Comparable[size + 1]; } // ???
	
	public boolean isEmpty() { return ptr == 0; }
	public void insert(T[] ary) { for(T t : ary) insert(t); }
	public void insert(T val) { data[++ptr] = val; swim(ptr); }
	public T delMax() {
		if(isEmpty()) return null;
		
		T max = data[1];
		exch(1, ptr);
		data[ptr--] = null;
		sink(1);
		return max;
	}
	
	private boolean less(int i, int j) {
		System.out.println(i + " vs " + j);
		return data[i].compareTo(data[j]) < 0;
	}
	private void exch(int i, int j) { 
		T t = data[j];
		data[j] = data[i];
		data[i] = t;
	}
	private void swim(int i) {
		while(i > 1 && less(i / 2, i)) { // [!]Short circuit! Make sure i > 1 first!
			exch(i, i / 2);
			i /= 2;
		}
	}
	private void sink(int i) {
		while(2 * i <= ptr) {
			int lChild = 2 * i;
			int rChild = 2 * i + 1;
			int exchIndex;
			
			// [!]NO "lChild < ptr" : OUTOFBOUNDEXCEPTION!
			if(lChild < ptr && !less(lChild, rChild)) exchIndex = lChild;
			else exchIndex = rChild;
			
			exch(i, exchIndex);
			i = exchIndex;
			
		}
	}
	
	public static void main(String[] args) {
		PriorityQueue<Character> pq = new PriorityQueue<>(20);
		char[] data = "DSFHGREFASDZ".toCharArray();
		
		Character[] inboxed = new Character[data.length];
		for(int i = 0; i < data.length; i++) {
			inboxed[i] = data[i];
		}	
		
		pq.insert(inboxed);
		System.out.println(pq.delMax());
	}
}
