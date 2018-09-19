public class ConstSpreading {
	
	public static void main(String[] args) {
		System.out.println(Parent.var);  //1.
		System.out.println(Child.var); //2.
	}
	
}

class Parent {
	public static final int var = 7777;
	
	static {
		System.out.println("Parent <clinit>!");
	}
}

class Child extends Parent {
	static {
		System.out.println("Child <clinit>!");
	}
}

/** Output:
 * 对于1，为什么没有Parent <clinit>？
 * 因为对于常量而言，编译器在编译的时候就已经作出优化，
 * 将要赋给常量的值写入ConstSpreading的Class文件的常量池当中了。
 * 这种优化机制称为“常量传播”。
 * 
 * 经过常量传播优化的程序，Java编译器会将值
 * 写入到ConstSpreading的Class文件之中。
 * 这相当于ConstSpreading也持有了值7777，而不必在持有Parent的引用。
 *
 * 对于2，照样是没有Child <init>的。
 * 这说明常量是允许通过继承来传播的。
 */
