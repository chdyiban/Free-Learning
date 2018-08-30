package ourck.yiban.dbgrab;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import ourck.yiban.utils.DataCollection;

/**
 * 数据库数据拉取功能的实现类。<p>
 * TODO 该类与数据库表结构紧耦合。
 * @author ourck
 */
public class DBGrabber extends DBOperator{

	public DBGrabber(String url, String userName, String pwd) {
		super(url, userName, pwd);
	}
	
	public DataCollection grabNewsContentData(String tableName) {
		Connection conn = buildConnection(); // 自父类
		DataCollection qResult = new DataCollection();
        try {
			Statement stmt = conn.createStatement();
			
            final int batch = 1000; // 1000 items per query. 
            int start = 0;
            StringBuilder sqlCmd = new StringBuilder();
            
			while (true) {
				// Reset StringBuilder when loop begins
				sqlCmd.delete(0, sqlCmd.length());
				// 1000 items per query.
				sqlCmd.append("SELECT * FROM ").append(tableName).append(" limit ")
						.append(start + ", " + batch);
				ResultSet results = stmt.executeQuery(sqlCmd.toString());
				int returnCnt = 0; // Item counter.
				
	            while (results.next()) {
	            	returnCnt++;
	            	int id = results.getInt("id");						// 表中该记录的id
	            	String content = results.getString("content");		// 新闻内容
	            	String author = results.getString("author");		// 作者
	            	
	            	Map<String, String> data = new HashMap<String, String>();
	            	data.put("id", "" + id);
	            	data.put("content", "" + content);
	            	data.put("author", author);
	            	
	            	// Collect every item.
	            	qResult.add(data);
	            }
	            if (returnCnt < batch) break; // 没有更多的查询结果了，退出
	            start += batch; // 查询下一1000条记录
            }
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
        
        return qResult;
	}
	
	public DataCollection grabNewsIndexData(String tableName) { 
		Connection conn = buildConnection(); // 自父类
		DataCollection qResult = new DataCollection();
        try {
			Statement stmt = conn.createStatement();
			
            final int batch = 1000; // 1000 items per query. 
            int start = 0;
            StringBuilder sqlCmd = new StringBuilder();
            
			while (true) {
				// Reset StringBuilder when loop begins
				sqlCmd.delete(0, sqlCmd.length());
				// 1000 items per query.
				sqlCmd.append("SELECT * FROM ").append(tableName).append(" limit ")
						.append(start + ", " + batch);
				ResultSet results = stmt.executeQuery(sqlCmd.toString());
				int returnCnt = 0; // Item counter.
				
	            while (results.next()) {
	            	returnCnt++;
	            	int id = results.getInt("id");						// 表中该记录的id
	            	int channel_id = results.getInt("channel_id");		// 网站栏目标签id
	            	String title = results.getString("title");			// 新闻标题
	            	String tags = results.getString("tags");			// 网站栏目标签
	            	
	            	Map<String, String> data = new HashMap<String, String>();
	            	data.put("id", "" + id);
	            	data.put("channel_id", "" + channel_id);
	            	data.put("title", title);
	            	data.put("tags", tags);
	            	
	            	// Collect every item.
	            	qResult.add(data);
	            }
	            if (returnCnt < batch) break; // 没有更多的查询结果了，退出
	            start += batch; // 查询下一1000条记录
            }
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
        
        return qResult;
	}
}
