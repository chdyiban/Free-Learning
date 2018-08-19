package ourck.priorq;

public interface IndexMinPQ<T extends Comparable<T>> {
	void insert(int i, T val);
	void change(int i, T val);
	boolean contains(int i);
	void delete(int i);
	T min();
	int minIndex();
	int delMin();
	boolean isEmpty();
	int size();
}
