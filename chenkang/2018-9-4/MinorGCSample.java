/**
 * Args: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails
 *			-XX:SurvivorRatio=8
 * 			-XX:+UseSerialGC // 可测试不同的GC器，另外我使用的是OpenJDK 64-Bit Server VM，版本10.0.2，这个版本已经将默认的GC器改为G1。
 *
 * 当Eden区空间不足以分配下一个对象时，触发MinorGC
 */
public class MinorGCSample {
	
	private static final int _MB = 1024*1024;
	
	public static void main(String[] args) {
		byte[] obj1 = new byte[2 * _MB];
		byte[] obj2 = new byte[2 * _MB];
		byte[] obj3 = new byte[2 * _MB];
		byte[] obj4 = new byte[4 * _MB];
	}
	
}

/**
 * Output:
 
[0.001s][warning][gc] -XX:+PrintGCDetails is deprecated. Will use -Xlog:gc* instead.
[0.002s][info   ][gc] Using Serial
[0.002s][info   ][gc,heap,coops] Heap address: 0x00000000fec00000, size: 20 MB, Compressed Oops mode: 32-bit
[0.065s][info   ][gc,start     ] GC(0) Pause Young (Allocation Failure)
[0.069s][info   ][gc,heap      ] GC(0) DefNew: 7291K->536K(9216K) // 新生代被回收了
[0.069s][info   ][gc,heap      ] GC(0) Tenured: 0K->6144K(10240K) 
																								// ^ obj1,obj2,obj3通过担保分配转入老年代，因为obj4要使用4MB空间，而位于Eden的空间被这仨占完了，又不能把他们放到仅1MB大小的Survivor区。
[0.069s][info   ][gc,metaspace ] GC(0) Metaspace: 3741K->3741K(1056768K)
[0.069s][info   ][gc           ] GC(0) Pause Young (Allocation Failure) 7M->6M(19M) 3.576ms
[0.069s][info   ][gc,cpu       ] GC(0) User=0.00s Sys=0.00s Real=0.01s
[0.070s][info   ][gc,heap,exit ] Heap
[0.070s][info   ][gc,heap,exit ]  def new generation   total 9216K, used 4714K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
[0.070s][info   ][gc,heap,exit ]   eden space 8192K,  51% used [0x00000000fec00000, 0x00000000ff014930, 0x00000000ff400000) 									// 回收后，新生代仅容纳obj4，大小4MB
[0.070s][info   ][gc,heap,exit ]   from space 1024K,  52% used [0x00000000ff500000, 0x00000000ff586048, 0x00000000ff600000)										// ？？？是谁占用了这部分空间？
[0.070s][info   ][gc,heap,exit ]   to   space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
[0.070s][info   ][gc,heap,exit ]  tenured generation   total 10240K, used 6144K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)	// 老年代被obj1,obj2,obj3占满
[0.070s][info   ][gc,heap,exit ]    the space 10240K,  60% used [0x00000000ff600000, 0x00000000ffc00030, 0x00000000ffc00200, 0x0000000100000000)
[0.070s][info   ][gc,heap,exit ]  Metaspace       used 3748K, capacity 4486K, committed 4864K, reserved 1056768K
[0.070s][info   ][gc,heap,exit ]   class space    used 318K, capacity 386K, committed 512K, reserved 1048576K

 */
