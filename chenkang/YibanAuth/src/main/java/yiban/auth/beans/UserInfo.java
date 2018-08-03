package yiban.auth.beans;

//@Entity
public class UserInfo {
	
	// Naming behavior fits DB's.
	private String name;
	private String student_id;
	private String role; private String enter_year; // Students & teachers independent.
	private String build_time;
	
	public UserInfo setName(String name) { this.name = name; return this; }
	public UserInfo setID(String id) { this.student_id = id; return this; }
	public UserInfo setYear(String year) { this.enter_year = year; return this; }
	public UserInfo setRole(String role) { this.role = role; return this; }
	public UserInfo setTime() { 
		long t = System.currentTimeMillis() / 1000l;
		this.build_time = "" + t;
		return this;
	}
	
	public String getName() { return name; }
	public String getID() { return student_id; }
	public String getYear() { return enter_year; }
	public String getRole() { return role; }
	public String getTime() { return build_time; }
	
	
}
