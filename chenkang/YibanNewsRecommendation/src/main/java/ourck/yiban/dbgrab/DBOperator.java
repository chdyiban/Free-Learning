package ourck.yiban.dbgrab;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBOperator {
	
	private final String dbUrl;
	private final String userName;
	private final String pwd;
					
	public DBOperator(String url, String userName, String pwd) {
		dbUrl = url;
		this.userName = userName;
		this.pwd = pwd;
	}
	
	protected Connection buildConnection() {
		String driver = "com.mysql.cj.jdbc.Driver";  
		Connection conn = null; 
		
		try{  
			Class.forName(driver); // Create by Reflect.
		} catch(ClassNotFoundException e) {  
			System.out.println("Cant't load Driver");  
		} 
		
		try {                                                                                 
			conn = DriverManager.getConnection(dbUrl, userName, pwd);
			System.out.println(" - Connect Successful: " + conn.getCatalog());  
		} catch(Exception e) {  
			System.out.println(" - Connect fail: " + e.getMessage());  
		}
		return conn;
	}
}
