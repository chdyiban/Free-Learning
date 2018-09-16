package ourck.mainconsole;

public class Solution {
    /**
     * @param n: An integer
     * @param nums: An array
     * @return: the Kth largest element
     */
    public Integer kthLargestElement(Integer n, Integer[] nums) {
    	if(n <= 0) return 0;
    	n--;
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>(nums.length + 1);
		pq.insert(nums);
        for(Integer i = 0; i < n; i++) {
            System.out.println(pq.delMax());
        }
        return pq.delMax();
    }

    public static void main(String[] args) {
		Integer[] ary = {1,2,3,4,5,6,7,8,9};
		int i = new Solution().kthLargestElement(1, ary);
		System.out.println(i);
	}
    
}

class PriorityQueue<T extends Comparable<T>> {
	
	/**
	 * TODO Static array seems too easy...?
	 */
	private T[] data;
	private int ptr = 0;
	
	@SuppressWarnings("unchecked")
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
//		System.out.println(i + " vs " + j + ", " + data[i].compareTo(data[j]));
		return data[i].compareTo(data[j]) < 0;
	}
	private void exch(int i, int j) { 
		T t = data[j];
		data[j] = data[i];
		data[i] = t;
	}
	private void swim(int i) {
		while(i > 1 && less(i / 2, i)) { // [!]Short circuit + Make sure i > 1 first!
			exch(i, i / 2);
			i /= 2;
		}
	}
	private void sink(int i) {
		while(2 * i <= ptr) {
			int lChild = 2 * i;
			int exchIndex = lChild;
			// [!]NO "lChild < ptr" : OUTOFBOUNDEXCEPTION!
			// TODO 注意&&运算符：这里的布尔表达式false的条件是什么？
			// 			lchild > ptr 或 lChild大于rChild
			// 			注意第一个式子，要是前面没有while作保证，这里将成为一个严重的BUG。
			if(lChild < ptr && less(lChild, lChild + 1)) exchIndex = lChild + 1;
			
			exch(i, exchIndex);
			i = exchIndex;
			
		}
	}
	
	public void show() {
		for(T val : data) {
			System.out.print(val + " ");
		}
	}
}