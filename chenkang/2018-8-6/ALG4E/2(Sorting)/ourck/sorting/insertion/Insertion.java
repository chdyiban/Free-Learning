package ourck.sorting.insertion;
import ourck.sorting.*;
import java.util.*;

public class Insertion extends BasicSorting{
	
	@Override
	public void sort(Comparable[] a) {
		for(int i = 1; i < a.length; i++) {
			for(int j = i; j > 0; j--)
				if(less(a[j], a[j - 1])) exch(a, j, j-1); // 相邻的做比较
		}
	}


	public static void main(String[] args) {
		BasicSorting s = new Insertion();
		Integer[] ary = new Integer[]{1,3,6,8,9,7,4,6,2};
		
		s.sort(ary);
		
		BasicSorting.show(ary);
	}
}
