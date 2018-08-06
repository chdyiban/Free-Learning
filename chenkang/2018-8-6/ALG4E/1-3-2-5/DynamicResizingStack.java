package ourck.mainconsole;

import java.util.*;

interface Stack<T> {
	Stack<T> push(T item);
	T pop();
}

@SuppressWarnings("unchecked")
public class DynamicResizingStack<T> implements Iterable<T>, Stack<T>{
	private T[] data = (T[])new Object[1];
	private int top = 0;
	private class LIFOIterator implements Iterator<T> { // NOT LIFOIterator<U>!
		private int ptr = top;
		@Override
		public boolean hasNext() {
			return ptr != 0;
		}
		@Override
		public T next() {
			return data[--ptr];
		}
	}
	
	private void resize(int newSize) {
		Object[] newData = new Object[newSize];
		for(int i = 0; i < top; i++) {
			newData[i] = data[i];
		}
		data = (T[])newData;
	}
	
	public DynamicResizingStack<T> push(T item) {
		data[top++] = item;
		if(top == data.length) resize(data.length * 2);
//		System.out.println(data.length);// TODO DEBUG-ONLY
		return this;
	}
	
	public T pop() {
		T t = data[--top];
		if(top < data.length / 4) resize(data.length / 2);
//		System.out.println(data.length);// TODO DEBUG-ONLY
		return t;
	}
	
	public LIFOIterator iterator() {
		return new LIFOIterator();
	}

	public static void main(String[] args) {
		DynamicResizingStack<String> stack = new DynamicResizingStack<>();
		stack.push("to be ")
			 .push("or not to be ")
			 .push("that is ")
			 .push("a question. ");
		for(String str : stack) {
			System.out.println(str);
		}
	}
}