package yiban.auth.beans;

import java.util.HashMap;
import java.util.Map;

public class StudentInfo extends UserInfo {
	
	private String student_id;
	private String enter_year;
	
	public UserInfo setStudent_id(String id) { this.student_id = id; return this; }
	public UserInfo setEnter_year(String year) { this.enter_year = year; return this; }
	
	public String getStudent_id() { return student_id; }
	public String getEnter_year() { return enter_year; }
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("name", getName());
		data.put("role", getRole());
		data.put("build_time", getBuild_time());
		data.put("student_id", student_id);
		data.put("enter_year", enter_year);
		return data;
	}
}
