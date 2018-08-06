package ourck.sorting.fastsorting;

import ourck.sorting.*;

public class FastSorting extends BasicSorting {
	
	@Override
	public void sort(Comparable[] a) {
		sort(a, 0, a.length - 1);
	}
	
	private void sort(Comparable[] a, int lo, int hi) {
		if(hi <= lo) return;
		int pivot = partition(a, lo, hi);
		sort(a, lo, pivot - 1); // DON'T MODIFY PIVOT's VALUE!
		sort(a, pivot + 1, hi); // DON'T MODIFY PIVOT's VALUE!
	}
	
	private int partition(Comparable[] a, int lo, int hi) {
		int p = lo; 				// Pivot's index. p = lo by default.
		int i = lo, j = hi + 1;	// Ptrs
		
		while(true) {
			// Condition j < lo & i > hi stands for child ary's overflow.
			while(less(a[++i], a[p])) if(i >= hi) break;
			while(less(a[p], a[--j])) if(j <= lo) break;
			if(i >= j) break;		// [!] COMES FIRST! OR OUTOFBOUNDEX!
			exch(a, i, j);
		}
		exch(a, p, j);
		p = j;
		
		return p;
	}
	
	public static void main(String[] args) {
		BasicSorting s = new FastSorting();
		Integer[] ary = new Integer[]{1,3,6,8,9,7,4,6,2};
		
		s.sort(ary);
		
		BasicSorting.show(ary);
	}
}
