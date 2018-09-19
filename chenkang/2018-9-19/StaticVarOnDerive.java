public class StaticVarOnDerive {
	
	public static void main(String[] args) {
//		System.out.println(Parent.var);  //1.
		System.out.println(Child.var); //2.
	}
	
}

class Parent {
	public static int var = 7777;
	
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
 * 无论是1还是2，输出结果都总是Parent <clinit>!
 * 而没有Child <clinit>!
 *
 * 对于1，解释起来简单。因为有getstatic指令（访问了该类的静态域）；
 * 对于2，这是因为JVM只会对静态变量所在的、直接定义了该变量的类执行初始化。
 */
