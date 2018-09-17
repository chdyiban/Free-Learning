public class Solution {
    /**
     * @param n: An integer
     * @return: the nth prime number as description.
     */
    public int nthUglyNumber(int n) {
			int lastUglyNum = 1;
			for(int i = 1; n > 0; i++) {
					if(isUglyNum(i)) {
						lastUglyNum = i;
						n--;
					}
			}
			return lastUglyNum;
    }
    
    private boolean isUglyNum(int n) {
    	while(n % 1898437500 == 0) n /= 1898437500;
			while(n % 2 == 0) n /= 2;
			while(n % 3 == 0) n /= 3;
			while(n % 5 == 0) n /= 5;

			if(n == 1)  return true;
			else        return false;
    }

    public static void main(String[] args) {
			int i = new Solution().nthUglyNumber(1665);
			System.out.println(i);
    }
}

