package yiban.dorm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import yiban.dorm.beans.TagInfo;

@Repository
public class TagsRepository {
	
	private static String TABLE_NAME = "tags2score";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public TagsRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public TagInfo getTagByName(String tagName) {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE tag_name = ?";
		TagInfo result = jdbcTemplate.queryForObject(sql, new Object[] { tagName }, new TagRowMapper());
//		System.out.println(result); // TODO DEBUG-ONLY
		return result;
	}
}

class TagRowMapper implements RowMapper<TagInfo> {

	@Override
	public TagInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		String name = rs.getString("tag_name");
		int openScore = rs.getInt("open");
		int respScore = rs.getInt("resp");
		int extrScore = rs.getInt("extr");
		int agreScore = rs.getInt("agre");
		int neurScore = rs.getInt("neur");

		TagInfo tag = new TagInfo();
		tag.setTagName(name);
		tag.setOpenScore(openScore);
		tag.setRespScore(respScore);
		tag.setExtrScore(extrScore);
		tag.setAgreScore(agreScore);
		tag.setNeurScore(neurScore);
		
		return tag;
	}
	
}
