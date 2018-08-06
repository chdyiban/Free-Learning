package ourck.sorting.shell;
import ourck.sorting.*;
import java.util.*;


public class Shell extends BasicSorting {

	private static final int[] DIV_ARY = new int[20];
	static {
		for(int i = 0; i < 20; i++) {
			DIV_ARY[i] = (int) ( (Math.pow(3, i) - 1) / 2 );
		}
	}

	@Override
	public void sort(Comparable[] a) {
		int h = division(a.length);
		while(h >= 1) {
			System.out.println(h);
			for(int i = h; i < a.length; i++) {
				for(int j = i; j >= h; j-=h) { 						// [!] j >= h!!!!
					if(less(a[j], a[j - h])) exch(a, j, j - h); 	// [!] j & j-h !!!!
				}
			}
			h /= 3; //?
		}
	}
	
	private int division(int i) { 
		int pre = DIV_ARY[0];
		for(int val : DIV_ARY) {
			if(val > i) return pre;
			pre = val;		
		}
		return 0; // Exception!
	}
		
	public static void main(String[] args) {
		BasicSorting s = new Shell();
		Integer[] ary = new Integer[]{7,3,6,9,8,5,2,4,1,13,89,74,23,16,45};
		
		s.sort(ary);
		
		BasicSorting.show(ary);
	}
}
