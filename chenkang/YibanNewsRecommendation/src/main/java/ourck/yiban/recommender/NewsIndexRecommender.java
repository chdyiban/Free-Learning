package ourck.yiban.recommender;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsIndexRecommender extends SolrWorker {
	
	private static final String RECOMMEND_BY = "title";
	private static final String CATEGORY_KEY = "cluster_id";

	private static final String coreName = "News_Index";
	private static final String clusteredCoreName = "News_Index_ID2Cluster";

	/**
	 * 根据输入的关键词进行基于搜索引擎的新闻推荐。<p>
	 * 推荐有两种方法：<p>
	 * 	1.根据搜索结果返回后面几条;<p>
	 *  2.根据搜索结果，提取其共有分类，然后返回该分类中的几条。本方法采用这一手段。<p>
	 * @param words 输入的关键词
	 * @return 推荐结果（有序）
	 * @throws IOException 当查询失败
	 */
	public List<Object> recommend(String... words) {
		List<Object> qList = null;
		qList = query(true, coreName, RECOMMEND_BY, words);

		Map<String, Integer> statics = new LinkedHashMap<String, Integer>();
		
		// 对于结果中的每一条记录，到另一个文档集合中去查询其分类。
		for(Object obj : qList) {
			@SuppressWarnings("unchecked") 
			JSONObject item = new JSONObject((HashMap<String, String>) obj);
			String newsId = item.getString("id");
			String clusterID = getNewsClusterByID(newsId);
			
			// TODO NullPointerEx! Integer can be "null' while int can't be "null". int's "null" = 0.
			Integer times;
			if((times = statics.get(clusterID)) == null)
				statics.put(clusterID, 1);
			else
				statics.put(clusterID, ++times);
		}
		
		// 获取搜索结果中出现频率最大的分类作为推荐依据
		String maxCategory = null; int maxVal = 0;
		for(String key : statics.keySet()) {
			int i;
			if((i = statics.get(key)) >= maxVal) {
				maxVal = i;
				maxCategory = key;
			}
		}
		
		List<Object> maxCategoryItems = query(false, clusteredCoreName, CATEGORY_KEY, maxCategory);
		return maxCategoryItems;
	}
	
	@SuppressWarnings("unchecked")
	private String getNewsClusterByID(String newsId) {
		Map<String, String> qMap = null;
		try {
			// TODO 这里query要求新闻数据库和聚类数据库必须严格同步
			qMap = (HashMap<String, String>) query(false, clusteredCoreName, "id", newsId).get(0);
		} catch (JSONException e) { 
			e.printStackTrace();
			return null;
		} catch(IndexOutOfBoundsException e) {
			System.err.println("[!]IndexOutOfBoundsException! newsId = " + newsId + ", clusteredCoreName = " + clusteredCoreName);
		}
		
		
		JSONObject jsonObj = new JSONObject(qMap);
		return "" + jsonObj.getInt("cluster_id");
	}
}
