package ourck.sorting.selection;
import ourck.sorting.*;
import java.util.*;

public class Selection extends BasicSorting {
	
	@Override
	public void sort(Comparable[] a) {
		for(int j = 0; j < a.length - 1; j++) {
			int min = j;
			for(int i = j + 1; i < a.length; i++) {
				if(less(a[i], a[min])) min = i;
			}
			exch(a, min, j);
		}
	}

	public static void main(String[] args) {
		BasicSorting s = new Selection();
		Integer[] ary = new Integer[]{1,3,6,8,9,7,4,6,2};
		
		s.sort(ary);
		
		BasicSorting.show(ary);
	}
}
