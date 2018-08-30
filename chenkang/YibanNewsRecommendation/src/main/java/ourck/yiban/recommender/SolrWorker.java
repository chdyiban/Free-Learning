package ourck.yiban.recommender;

import java.io.*;
import java.util.*;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

/**
 * 通用的Solr搜索引擎推荐器模板类。<p>
 * 提供了统一的查询实现，推荐实现由子类给出。
 * TODO 建议多写一个通用查询
 * @author ourck
 */
public abstract class SolrWorker {
	// SOLR_URL + coreName + queryParam + queryWords;
	private static final String SOLR_URL = "http://localhost:8983/solr/";
	
	private String queryWords = null;
	private static final String QUERY_PARAM = 
			"/select"
			+ "?q=";
	private static final String EXACT_QUERY_PARAM = 
			"/select"
			+ "?defType=edismax"
			+ "&fl=*,score"
			+ "&mm=30"
			+ "&rows=100&start=0"
			+ "&q=";
	
	public List<Object> queryByParams(String params, String coreName, String key, String... words) throws IOException {
		StringBuilder stb = new StringBuilder();
		for(int i = 0; i < words.length; i++) {
			stb.append(key).append(":").append(words[i]);
			if(i < words.length - 1) stb.append(" OR ");
		}
		queryWords = stb.toString();
		
		String requestURl = SOLR_URL + coreName + params + queryWords;
		Connection ct = Jsoup.connect(requestURl);
		Response repo = ct.ignoreContentType(true)
				.method(Method.GET)
				.execute();

		// 结果是有序的。在以此构造JSON对象时仍会保持其有序性。
		JSONObject qResult = new JSONObject(repo.body());
		List<Object> qList = qResult.getJSONObject("response").getJSONArray("docs").toList();
		return qList;
	}

	public List<Object> query(boolean exactFlag, String coreName, String key, String... words) {
		List<Object> result = null;
			try {
				if(exactFlag) result = queryByParams(EXACT_QUERY_PARAM, coreName, key, words);
				else result = queryByParams(QUERY_PARAM, coreName, key, words);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	/**
	 * 根据输入的关键词进行基于搜索引擎的推荐。
	 * @param 输入的关键词
	 * @return 推荐结果（有序）
	 */
	abstract public List<Object> recommend(String... words); 
	
}
