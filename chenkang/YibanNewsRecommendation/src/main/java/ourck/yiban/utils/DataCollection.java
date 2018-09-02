package ourck.yiban.utils;

import java.util.*;

/**
 * 自定义的数据库查询结果集合类，专为关系型数据库的表结构设计匹配。<p>
 * 继承自java容器，便于操作。<p>
 * 底层使用链表结构，链表成员为String到String的Map映射。
 * @author ourck
 */
public final class DataCollection extends ArrayList<Map<String, String>> { // TODO 采用不同的表结构对性能有影响吗？
	private static final long serialVersionUID = 417232558616269486L;
	public DataCollection() { super(); }
	public DataCollection(List<Map<String, String>> data) { super(data); }
}
