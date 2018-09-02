package SearchEngine.SearchEngineImplementation.Solr;

import java.io.IOException;
import java.util.*;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;

import static ourck.utils.ScreenReader.jin;

public class SolrBasedUserBasedCFRecommender implements CommonFilterRecommender {
	private static final String SOLR_URL = "http://localhost:8983/solr";
	private static final String CORE_NAME_OF_USERS = "usersRecord";
	
	private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL)
														.withConnectionTimeout(10000)
														.withSocketTimeout(60000)
														.build();
	/**
	 * 根据特定用户ID协同过滤来的用户群ID获取的商品推荐结果。<p>
	 * 有两个思路：
	 * <ol>
	 * 	<li>针对该用户未购买的、但协同过滤来的用户群已购买的商品A，将A推荐给该用户；
	 * 	<li>针对每个协同过滤来的用户群中的用户，根据用户ID在 <b>商品列表 执行搜索，返回评分最高的商品。
	 * </ol>
	 * 这里采用第二种，因为方便。<p>
	 * TODO 但似乎第一种更科学？<p>
	 * TODO 考虑重构。有太多相同代码被复用。<p>
	 * @param id 要获取推荐的用户ID
	 * @return 相似用户ID列表
	 */
	@Override
	public List<String> recommend(String id) {
		List<String> similarUsers = getSimilarUsers(id, 5);
		
		// For each user, sum up their record.
		List<String> othersAlsoView = new ArrayList<String>();
		for(String user : similarUsers) {
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("q", user);
			rawParams.put("df", "user_id");
			rawParams.put("fl", "*,score");
			SolrParams params = new MapSolrParams(rawParams);
			
			QueryResponse qResponse = null;
			try {
				qResponse = client.query(CORE_NAME_OF_USERS, params);
			} catch (SolrServerException e) {
				System.err.println("( x_x )Oops! Something happened on server!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(" - Querying error!");
				return null;
			}
			final SolrDocument qResult = qResponse.getResults().get(0); // There should be only 1 result when querying by id.
			String purchasedItemsIDs = (String) qResult.get("purchased_items");
			Collections.addAll(othersAlsoView, purchasedItemsIDs.split(" "));
		}
		
		// Eliminate those viewed goods by this user.
		Map<String, String> rawParams = new HashMap<String, String>();
		rawParams.put("q", id);
		rawParams.put("df", "user_id");
		rawParams.put("fl", "*,score");
		SolrParams params = new MapSolrParams(rawParams);
		
		QueryResponse qResponse = null;
		try {
			qResponse = client.query(CORE_NAME_OF_USERS, params);
		} catch (SolrServerException e) {
			System.err.println("( x_x )Oops! Something happened on server!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(" - Querying error!");
			return null;
		}
		final SolrDocument qResult = qResponse.getResults().get(0); // There should be only 1 result when querying by id.
		List<String> hasViewed = Arrays.asList(
				((String) qResult.get("purchased_items")).split(" "));
		othersAlsoView.removeAll(hasViewed);
		
		return othersAlsoView;
	}

	/**
	 * 根据提供的id进行搜索，获得size个相似用户的用户ID
	 * @param id 提供的用户ID
	 * @param size 返回的相似用户个数
	 * @return 相似用户列表
	 */
	public List<String> getSimilarUsers(String id, int size) {
		// Build a Solr client for querying. https://lucene.apache.org/solr/guide/7_3/using-solrj.html
		String purchasedItemsIDs = null;
		{
			// Builds params.
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("q", id);
			rawParams.put("df", "user_id");
			rawParams.put("fl", "*,score");
			SolrParams params = new MapSolrParams(rawParams);
			
			// Starts querying & get JSON return.
			QueryResponse qResponse = null;
			try {
				qResponse = client.query(CORE_NAME_OF_USERS, params);
			} catch (SolrServerException e) {
				System.err.println("( x_x )Oops! Something happened on server!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(" - Querying error!");
				return null;
			}
			final SolrDocument qResult = qResponse.getResults().get(0); // There should be only 1 result when querying by id.
			purchasedItemsIDs = (String) qResult.get("purchased_items");
		}

		
		// Second querying: building params.
		List<String> result = new LinkedList<>();
		{
			Map<String, String> rawParamsForUsers = new HashMap<String, String>();
			rawParamsForUsers.put("q", purchasedItemsIDs);
			rawParamsForUsers.put("df", "purchased_items");
			rawParamsForUsers.put("fl", "*,score");
			rawParamsForUsers.put("mm", "3");
			rawParamsForUsers.put("rows", "" + ++size); // 排除掉搜索出自己的那条记录
			rawParamsForUsers.put("start", "" + 0);
			SolrParams paramsForUsers = new MapSolrParams(rawParamsForUsers);
			
			QueryResponse qResponse = null;
			try {
				qResponse = client.query(CORE_NAME_OF_USERS, paramsForUsers);
			} catch (SolrServerException e) {
				System.err.println("( x_x )Oops! Something happened on server!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(" - Querying error!");
				return null;
			}
			final SolrDocumentList qUsersResult = qResponse.getResults();
			for(SolrDocument doc : qUsersResult) {
				Integer userId = (Integer) doc.get("user_id"); // "user_id" is saved as BIGINT in db. 
				if(userId != Integer.parseInt(id)) result.add("" + userId);
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		SolrBasedUserBasedCFRecommender recommender = new SolrBasedUserBasedCFRecommender();
		System.out.print("输入您的用户ID："); String id = jin();
		System.out.print("与您最相似的用户ID有："); System.out.println(recommender.getSimilarUsers(id, 5));
		System.out.print("看了此商品ID的人还看了："); System.out.println(recommender.recommend(id));
	}
}
