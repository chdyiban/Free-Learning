package yiban.dorm.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import org.jsoup.Jsoup;

import yiban.dorm.beans.Recommendation;
import yiban.dorm.beans.UserInfo;

/**
 * TODO 向Solr发送查询请求的模块可以独立出来
 * TODO 注意随机性！
 * @author ourck
 *
 */
public class UserRecommend {
	
	private static final String SOLR_URL = "http://127.0.0.1:8983/solr/tagged_user"; // TODO 生产时URL
	
	public List<Recommendation> recommendByScore(UserInfo targetUser) {
		String field = getHighestScore(targetUser);
		String params = "/select?"
				+ "df=" + field
				+ "&q=*"
				+ "&sort=" + field + "%20desc"
				+ "&rows=10";
		String url = SOLR_URL + params;
		
		Connection ct = Jsoup.connect(url);
		Response repo = null;
		try {
			repo = ct.ignoreContentType(true)
			  .method(Method.GET)
			  .execute();
		} catch (IOException e) {
			System.err.println("[!] Failed to recommend: querying on Solr failed!"); // TODO DEBUG-ONLY
		}
		
		List<Recommendation> list = new ArrayList<Recommendation>();
		String repoString = repo.body();
		JSONArray userAry = new JSONObject(repoString).getJSONObject("response").getJSONArray("docs");
		for(Object raw : userAry) {
			UserInfo user = new UserInfo();
			JSONObject jUser = (JSONObject) raw;
			
			user.setEarphoneUser(jUser.getBoolean("earphone_user"));
			user.setEventLover(jUser.getBoolean("event_lover"));
			user.setTakeoutLover(jUser.getBoolean("takeout_lover"));
			user.setGamingLover(jUser.getBoolean("gaming_lover"));
			
			user.setOpenScore(jUser.getInt("open"));
			user.setExtrScore(jUser.getInt("extr"));
			user.setNeurScore(jUser.getInt("neur"));
			user.setRespScore(jUser.getInt("resp"));
			user.setAgreScore(jUser.getInt("agre"));
			user.setUserName(jUser.getString("user_name"));
			
			Recommendation recomm = new Recommendation(user, field);
			list.add(recomm);
		}
		
		return list;
	}
	
	public List<Recommendation> recommendByBehavior(UserInfo targetUser) {
		List<String> behaviors = new ArrayList<String>();
		if(targetUser.getEarphoneUser()) behaviors.add("earphone_user");
		if(targetUser.getEventLover()) behaviors.add("event_lover");
		if(targetUser.getTakeoutLover()) behaviors.add("takeout_lover");
		if(targetUser.getGamingLover()) behaviors.add("gaming_lover");
		
		List<Recommendation> resultList = new ArrayList<Recommendation>();
		
		// 接下来 为每一种行为习惯去Solr查询具有同样行为习惯的用户
		// TODO 要不要也查一遍没有相同行为习惯的用户？
		for(String behavior : behaviors) {
			String params = "/select?"
					+ "df=" + behavior
					+ "&q=1"
					+ "&rows=10";
			String url = SOLR_URL + params;
			Connection ct = Jsoup.connect(url);
			Response repo = null;
			try {
				repo = ct.ignoreContentType(true)
				  .method(Method.GET)
				  .execute();
			} catch (IOException e) {
				System.err.println("[!] Failed to recommend: querying on Solr failed!"); // TODO DEBUG-ONLY
			}
			
			String repoString = repo.body();
			JSONArray userAry = new JSONObject(repoString).getJSONObject("response").getJSONArray("docs");
			for(Object raw : userAry) {
				UserInfo user = new UserInfo();
				JSONObject jUser = (JSONObject) raw;
				
				user.setEarphoneUser(jUser.getBoolean("earphone_user"));
				user.setEventLover(jUser.getBoolean("event_lover"));
				user.setTakeoutLover(jUser.getBoolean("takeout_lover"));
				user.setGamingLover(jUser.getBoolean("gaming_lover"));
				
				user.setOpenScore(jUser.getInt("open"));
				user.setExtrScore(jUser.getInt("extr"));
				user.setNeurScore(jUser.getInt("neur"));
				user.setRespScore(jUser.getInt("resp"));
				user.setAgreScore(jUser.getInt("agre"));
				user.setUserName(jUser.getString("user_name"));
				
				Recommendation recomm = new Recommendation(user, behavior);
				resultList.add(recomm);
			}
		}
		
		return resultList;
	}
	
	private Map<Integer, String> getScores(UserInfo targetUser) {
		Map<Integer, String> scores = new HashMap<Integer, String>();
		scores.put(targetUser.getOpenScore(), "open");
		scores.put(targetUser.getRespScore(), "resp");
		scores.put(targetUser.getExtrScore(), "extr");
		scores.put(targetUser.getAgreScore(), "agre");
		scores.put(targetUser.getNeurScore(), "neur");
		
		return scores;
	}
	
	private String getHighestScore(UserInfo user) {
		Map<Integer, String> scores = getScores(user);
		Object[] ary = scores.keySet().toArray();
		Arrays.sort(ary);
		Integer maxScore = (Integer) ary[ary.length - 1];
		
		return scores.get(maxScore);
	}
	
}
