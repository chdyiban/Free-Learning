public class QuickUnion extends UnionFind{

	protected int id[]; // For WeightedQuickUnion version
	
	public QuickUnion(int N) {
		super(N);
		id = new int[N];
		for(int i = 0; i < count(); i++)
			id[i] = i;
	}
	
	public QuickUnion union(int p, int q) {
		int pID = find(p);
		int qID = find(q);
		
		if(pID != qID) id[pID] = qID;
			
		return this;
		
	}
	
	public int find(int p) {
		int ptr = p;
		while(ptr != id[ptr]) ptr = id[ptr];
		return ptr;
	}
	
	public boolean connected(int p, int q) {
		return find(p) == find(q);
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
		
		System.out.println(q.connected(1, 2));
	}
}
