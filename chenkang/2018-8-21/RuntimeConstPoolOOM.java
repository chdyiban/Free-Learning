import java.util.*;

/**
 * Args: -XX:PermSize=10M -XX:MaxPermSize=10M <p>
 * 参数-XX:PermSize指定了方法区（所谓“永久代”，又所谓“非堆区）的初始大小 <p>
 * 参数-XX:MaxPermSize指定了方法区的最大大小 <p>
 
 * @deprecated 在OpenJDK 10中似乎真的实现了书上提到的“去永久代”<p>
 * 在实验时出现如下提示:<p>
 
		OpenJDK 64-Bit Server VM warning: Ignoring option PermSize; support was removed in 8.0 <p>
		OpenJDK 64-Bit Server VM warning: Ignoring option MaxPermSize; support was removed in 8.0 <p>
 
 * 容易受GC影响 而造成移动的永久代 是很难调优的，
 * （我的理解：明明是静态的数据却发生了空间上的变动？）<p>
 * 
 * 实际上，随着Java 8的发布，永久代已经被元空间（Meta Space)代替。<p>
 * @see https://blog.csdn.net/chenleixing/article/details/48286127 <p>
 */
public class RuntimeConstPoolOOM {

	public static void main(String[] args) {
		int i = 0;
		while(true) {
			List<String> list = new ArrayList<>();
			String str = "" + i;
			list.add(str.intern());
		}		
	}

}

/**
 * P.S. String.intern()
 * 
 * 若该String对象在字符串常量池中，则返回该对象（的引用）；
 * 若不在，则将此字符串加入常量池，并返回该对象的引用。
 */
