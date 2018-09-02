package yiban.dorm.dao;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import yiban.dorm.beans.UserInfo;

@Repository
/**
 * @see https://bbs.csdn.net/topics/310103844
 * @author ourck
 */
public class UserRepository {
	
	private static String TABLE_NAME = "user_repository";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public UserRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void addUser(UserInfo user) {
		
		String sql = "INSERT INTO " + TABLE_NAME
				+ "(user_name, open, resp, extr, agre, neur,"
				+ " gaming_lover, earphone_user, event_lover, takeout_lover) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Boolean[] flags = {
				user.getGamingLover(),
				user.getEarphoneUser(),
				user.getEventLover(),
				user.getTakeoutLover()
		};
		
		Object[] scores = {
				user.getUserName(),
				user.getOpenScore(),
				user.getRespScore(),
				user.getExtrScore(),
				user.getAgreScore(),
				user.getNeurScore(),
		};
		
		Object[] data = Arrays.copyOf(scores, scores.length + flags.length);
		
		for(int i = scores.length; i < data.length; i++) {
			data[i] = flags[i - scores.length];
		}
		
		// TODO DEBUG-ONLY
//		System.out.println(sql);
//		for(Object obj : data) System.out.print(obj + " ");
		jdbcTemplate.update(sql, data);
	}
	
}
