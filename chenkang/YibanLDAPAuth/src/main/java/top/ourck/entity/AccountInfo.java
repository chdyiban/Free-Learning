package top.ourck.entity;

/**
 * 用户信息实体类，包含用户名、密码，以及其getter & setter
 * @author ourck
 */
public class AccountInfo {
	
	private String userName;
	private String passwd;
	
	public void setUserName(String userName) { this.userName = userName; }
	public void setPwd(String passwd) { this.passwd = passwd; }
	
	public String getUserName() { return userName; }
	public String getPwd() { return passwd; }
}
