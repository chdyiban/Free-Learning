package ourck.sorting.merge;

import ourck.sorting.*;

public class MergeSorting extends BasicSorting {
	
	private Comparable[] aux;
	
	public void merge(Comparable[] a, int lo, int mid, int hi) { // mid?
		int i = lo; int j = mid + 1; // 2x ptrs.
		
		if(less(a[mid], a[mid + 1])) return; // 降低复杂度至线性级别:P180 2.2.8
		
		for(int l = 0; l < a.length; l++) {
			aux[l] = a[l];
		}
		
		for(int k = lo; k <= hi; k++) { // For each e in a[]
			if(i > mid)							a[k] = aux[j++];
			else if(j > hi)					a[k] = aux[i++];
			else if(less(aux[i], aux[j]))	a[k] = aux[i++];
			else									a[k] = aux[j++];
		}
	}
	
	@Override
	public void sort(Comparable[] a) {
		aux = new Comparable[a.length];
		sort(a, 0, a.length - 1); // TODO -1!!!
	}
	
	private void sort(Comparable[] a, int lo, int hi) {
		if(lo >= hi) return;
		int mid = (lo + hi) / 2; // = lo + (hi - lo) / 2
		sort(a, lo, mid);
		sort(a, mid+1, hi);
		merge(a, lo, mid, hi);
	}
	
	public static void main(String[] args) {
		BasicSorting s = new MergeSorting();
		char[] ary = "MERGESORTEXAMPLE".toCharArray();
		
		Character[] inboxAry = new Character[ary.length];
		for(int i = 0; i < ary.length; i++) inboxAry[i] = ary[i];
		
		s.sort(inboxAry);
		
		BasicSorting.show(inboxAry);
	}
}
