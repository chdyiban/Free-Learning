public class WeightedQuickUnion extends QuickUnion{
	
	private int[] depth;
	
	public WeightedQuickUnion(int N) {
		super(N);
		depth = new int[N];
		for(int i : depth) i++; // Depth starts from 1.
	}
	
	@Override
	public WeightedQuickUnion union(int p, int q) {
		int pID = find(p);
		int qID = find(q);
		
		if(pID != qID) {
			int s, b; // Smaller & bigger.
			
			if(depth[pID] < depth[qID]) { s = pID; b = qID; }
			else { s = qID; b = pID; }
			
			id[s] = b;
			depth[b] += depth[s];
		}
			
		return this;
	}
	
	
	public static void main(String[] args) {
		UnionFind q = new QuickUnion(10);
		q.union(1,2).union(2,3).union(9,8);
		
		System.out.println(
		q.find(1) + " " + 
		q.find(2) + " " + 
		q.find(3) + " | " + 
		q.find(8)
		);
		
		System.out.println(q.connected(1, 2) + " | " + q.connected(6,8));
	}
}
