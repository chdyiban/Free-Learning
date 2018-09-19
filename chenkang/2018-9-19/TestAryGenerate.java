public class TestAryGenerate {

	public static void main(String[] args) {
		Apple apple = new Apple();
		Apple[] apples = new Apple[4];
	}

}

class Apple {}

/** Javap Output:
 *
 Constant pool:
   #1 = Methodref          #5.#14         // java/lang/Object."<init>":()V
   #2 = Class              #15            // Apple
   #3 = Methodref          #2.#14         // Apple."<init>":()V
   #4 = Class              #16            // TestAryGenerate
   #5 = Class              #17            // java/lang/Object
   #6 = Utf8               <init>
   #7 = Utf8               ()V
   #8 = Utf8               Code
   #9 = Utf8               LineNumberTable
  #10 = Utf8               main
  #11 = Utf8               ([Ljava/lang/String;)V
  #12 = Utf8               SourceFile
  #13 = Utf8               TestAryGenerate.java
  #14 = NameAndType        #6:#7          // "<init>":()V
  #15 = Utf8               Apple
  #16 = Utf8               TestAryGenerate
  #17 = Utf8               java/lang/Object


 public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: new           #2                  // class Apple
         3: dup
         4: invokespecial #3                  // Method Apple."<init>":()V
         7: astore_1
         8: iconst_4
         9: anewarray     #2                  // class Apple
        12: astore_2
        13: return
      LineNumberTable:
        line 4: 0
        line 5: 8
        line 6: 13
 *
 * 第0行到第7行为语句Apple apple = new Apple();
 * 第8行到12行为语句Apple[] apples = new Apple[4];
 *
 * 对于单个Apple对象，JVM先通过new指令创建一个新对象，并将这个新对象的引用置于栈顶。
 * 之后invoke常量池当中#3的实例构造器<init>，
 * 之后将其存入本地符号表的第1 + 1 = 2个变量，
 * 其中第一个“1”指的是JVM自动加入的、原先就存在的this（P186）；
 *
 * 对于数组的生成，原理差不多。先将4入栈，指定要生成长度为4的数组，
 * 使用anewarray指令，将生成后的数组对象的引用置于栈顶；
 * 之后将其存入本地符号表的第1 + 2 = 3个变量.
 *
 * 这验证了一件事：Java数组也是对象。
 */
