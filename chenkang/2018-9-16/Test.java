import java.util.*;

public class Test{

	private int i;
	
	public Test() {
		List<Integer> list = null;
		i = list.get(0);
	}
	
	public static void main(String[] args) {
		new Test();
	}

}
/** 输出：
   Exception in thread "main" java.lang.NullPointerException
        at Test.<init>(Test.java:8)
        at Test.main(Test.java:12)
 * 注意是<init>方法。
 * 通过方法块的形式初始化也是一样的错误。
 *
 * <init>方法实际上正是构造器、非静态方法块的集合体，（还有别的吗？）
 * 实际上类的初始化工作（在JVM层面）是由他完成的——而非单纯由构造器。
 * 
 * 就Java语言的语法/语义定义而言，构造器就不是静态的。
 * 但由它生成的代码<init>()则是静态方法
 */
