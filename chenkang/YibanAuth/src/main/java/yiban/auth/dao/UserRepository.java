package yiban.auth.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import yiban.auth.beans.UserInfo;
import yiban.auth.spring.DefaultDataSourceConfig;

@Repository
public class UserRepository {
	
	private static final String STU_TABLE_NAME = "chd_stu_detail";
	private static final String TEACHER_TABLE_NAME = "chd_teacher_detail";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UserRepository() {
		AnnotationConfigApplicationContext ctxt = new AnnotationConfigApplicationContext(DefaultDataSourceConfig.class);
		jdbcTemplate = ctxt.getBean(JdbcTemplate.class);
		ctxt.close();
	}
	
	public UserInfo findByStuID(String ID) {
		String sql = "SELECT * FROM " + STU_TABLE_NAME + " WHERE XH = ?";
		UserInfo user = jdbcTemplate.queryForObject(sql, new String[] {ID}, new UserRowMapper());
		return user;
	}
	
	public UserInfo findByTeacherID(String ID) {
		String sql = "SELECT * FROM " + TEACHER_TABLE_NAME + " WHERE ID = ?";
		UserInfo user = jdbcTemplate.queryForObject(sql, new String[] {ID}, new UserRowMapper());
		return user;
	} 
}

class UserRowMapper implements RowMapper<UserInfo>{

	@Override
	public UserInfo mapRow(ResultSet result, int rowNum) throws SQLException {
		UserInfo user = new UserInfo();
		String pk = "XH"; // Prime key = "XH" or "ID"
		
		if(result.getString("ROLE") == "1")  pk = "ID";// If returns a teacher
		
		user.setID(result.getString("XH"));
		user.setName(result.getString("XM"));
		user.setRole(result.getString("ROLE"));
		user.setTime();
		user.setYear(result.getString(pk).substring(0, 4));
		
		return null;
	}
	
}