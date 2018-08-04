package yiban.auth.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import yiban.auth.beans.StudentInfo;
import yiban.auth.beans.TeacherInfo;
import yiban.auth.beans.UserInfo;
import yiban.auth.spring.config.DefaultWiringConfig;

@Repository
public class UserRepository {
	
	private static final String STU_TABLE_NAME = "chd_stu_detail";
	private static final String TEACHER_TABLE_NAME = "chd_teacher_detail";
	
	private JdbcTemplate jdbcTemplate;
	private AnnotationConfigApplicationContext ctxt; // TODO 可优化：多余的内存占用
	
	private UserInfo findByStuID(String ID) {
		String sql = "SELECT * FROM " + STU_TABLE_NAME + " WHERE XH = ?";
		UserInfo user = jdbcTemplate.queryForObject(sql, new String[] {ID}, new UserRowMapper());
		return user;
	}
	
	private UserInfo findByTeacherID(String ID) {
		String sql = "SELECT * FROM " + TEACHER_TABLE_NAME + " WHERE ID = ?";
		UserInfo user = jdbcTemplate.queryForObject(sql, new String[] {ID}, new UserRowMapper());
		return user;
	}

	public UserRepository() {
		ctxt = new AnnotationConfigApplicationContext(DefaultWiringConfig.class);
		jdbcTemplate = ctxt.getBean(JdbcTemplate.class);
	}
	
	public UserInfo findByID(String ID) {
		UserInfo user; // Teacher's ID = 6.
		if(ID.length() == 6)	{ user = findByTeacherID(ID); }
		else					{ user = findByStuID(ID); }
		return user;
	}
	
}

class UserRowMapper implements RowMapper<UserInfo>{

	@Override
	public UserInfo mapRow(ResultSet result, int rowNum) throws SQLException {
		UserInfo user = null;
		
		boolean stuFlag = false;
		try 					{ result.findColumn("ROLE"); } // Teacher table contains column "ROLE" = 1
		catch(SQLException e) 	{ stuFlag = true; }
		
		if(!stuFlag) { // If returns a teacher
			TeacherInfo teacher = new TeacherInfo();
			teacher.setRole(result.getString("ROLE"));
			user = teacher;
		}
		else { // If returns a student
			StudentInfo student = new StudentInfo();
			student.setRole("0");
			student.setStudent_id(result.getString("XH"));
			student.setEnter_year(result.getString("XH").substring(0, 4));
			user = student;
		}
		
		user.setName(result.getString("XM"));
		user.setBuild_time();
		
		return user;
	}
	
}
