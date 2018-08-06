public abstract class UnionFind{
	
	private int count;
	
	public UnionFind(int N) {
		count = N;
	}

	public abstract UnionFind union(int p, int q);
	
	public abstract int find(int p);

	public abstract boolean connected(int p, int q);
	
	public int count() { return count; }

}
