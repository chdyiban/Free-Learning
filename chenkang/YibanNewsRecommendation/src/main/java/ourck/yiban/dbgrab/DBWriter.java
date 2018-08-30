package ourck.yiban.dbgrab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 向本地数据库写入/更新聚类信息的实现类。
 * TODO 与<b>新闻头<b>的表结构紧耦合
 * @author ourck
 *
 */
public class DBWriter extends DBOperator {
	
	public DBWriter(String url, String userName, String pwd) {
		super(url, userName, pwd);
	}
	
	/**
	 * 将聚类文件的内容写入数据库
	 * @param clusterFilePath 聚类文件
	 * @param tableName 操作表名
	 * @throws IOException 当文件操作失败
	 */
	public void writeClusterInfo(File clusteredFile, String tableName) throws IOException {
		if(!clusteredFile.exists())
			try { clusteredFile.getParentFile().mkdirs(); clusteredFile.createNewFile(); }
			catch (IOException e) { e.printStackTrace(); return;}
		
		Connection conn = buildConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("[!] Creating statement failed!");
			return;
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(clusteredFile));
		reader.readLine(); // Pass header.
		
		String line;
		try {
			while((line = reader.readLine()) != null) {
				String[] tokens = line.split("\t");
				String id = tokens[0].substring(1, tokens[0].length() - 1); // 去掉双引号
				
				// First, query the db to check if the item has already exists.
				StringBuilder queryCmd = new StringBuilder();
				queryCmd.append("SELECT * FROM ").append(tableName)
					.append(" WHERE news_id = ").append(id);
				ResultSet r = stmt.executeQuery(queryCmd.toString()); 
				boolean notNullFlag = r.last();
				
				StringBuilder sqlCmd = new StringBuilder();

				// If not exists...
				if(notNullFlag) {
					sqlCmd.append("UPDATE ").append(tableName)
						.append(" SET cluster_id = ").append(tokens[1])
						.append(" WHERE news_id = ").append(id);
				}
				else {
					sqlCmd.append("INSERT INTO ").append(tableName)
						.append(" (news_id, cluster_id) VALUES")
						.append(" (" + id + "," + tokens[1] + ")");
				}
				
				stmt.executeUpdate(sqlCmd.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} finally {
			reader.close();
		}
		
	}
	
	/**
	 * 将聚类文件的内容写入数据库
	 * @param clusterFilePath 聚类文件路径
	 * @param tableName 操作表名
	 * @throws IOException 当文件操作失败
	 */
	public void writeClusterInfo(String clusterFilePath, String tableName) throws IOException {
		writeClusterInfo(new File(clusterFilePath), tableName);
	}
	
}