package yiban.auth.beans;

import java.util.Map;

//@Entity
public abstract class UserInfo {
	
	// Naming behavior fits DB's.
	private String name;
	private String role;
	private String build_time;
	
	// Naming behavior fits Yiban's.
	public UserInfo setName(String name) { this.name = name; return this; }
	public UserInfo setRole(String role) { this.role = role; return this; }
	public UserInfo setBuild_time() { 
		long t = System.currentTimeMillis() / 1000l;
		this.build_time = "" + t;
		return this;
	}
	
	public String getName() { return name; }
	public String getRole() { return role; }
	public String getBuild_time() { return build_time; }
	
	abstract public Map<String, String> toMap();
}
