/**
 * Args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails
 * 	-XX:SurvivorRatio=8
 * 	-XX:PretenureSizeThreshold=1048576
 *	-XX:+UseSerial
 * 
 * OpenJDK 10.0.2 2018-07-17
 * 通过缩小老年代准入大小阈值来更容易地将对象装入老年代以观察GC日志。
 */

public class BigObjEnterTenuredDirectly {

	private static final int _MB = 1024 * 1024;

	public static void main(String[] args) {
		byte[] obj;
		obj = new byte[4 * _MB];
	}

}

/**
 * Output:

[0.001s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
[0.002s][info   ][gc] Using Serial
[0.002s][info   ][gc,heap,coops] Heap address: 0x00000000fec00000, size: 20 MB, Compressed Oops mode: 32-bit
[0.063s][info   ][gc,heap,exit ] Heap
[0.063s][info   ][gc,heap,exit ]  def new generation   total 9216K, used 5407K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
[0.063s][info   ][gc,heap,exit ]   eden space 8192K,  66% used [0x00000000fec00000, 0x00000000ff147e08, 0x00000000ff400000) // ？？？？？？？？？？并没有转移到老年代中？
[0.063s][info   ][gc,heap,exit ]   from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
[0.063s][info   ][gc,heap,exit ]   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
[0.063s][info   ][gc,heap,exit ]  tenured generation   total 10240K, used 0K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
[0.063s][info   ][gc,heap,exit ]    the space 10240K,   0% used [0x00000000ff600000, 0x00000000ff600000, 0x00000000ff600200, 0x0000000100000000)
[0.063s][info   ][gc,heap,exit ]  Metaspace       used 3745K, capacity 4486K, committed 4864K, reserved 1056768K
[0.063s][info   ][gc,heap,exit ]   class space    used 318K, capacity 386K, committed 512K, reserved 1048576K

 */
