package yiban.auth.beans;

import java.util.HashMap;
import java.util.Map;

public class TeacherInfo extends UserInfo {
	
	private String teacher_id;
	
	public UserInfo setTeacher_id(String id) { this.teacher_id = id; return this; }
	public String getTeacher_id() { return teacher_id; }
	
	@Override
	public Map<String, String> toMap() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("name", getName());
		data.put("role", getRole());
		data.put("build_time", getBuild_time());
		data.put("teacher_id", teacher_id);
		return data;
	}
}
