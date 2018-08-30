package SearchEngine.SearchEngineImplementation.Solr;

import static ourck.utils.ScreenReader.jin;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;

public class SolrBasedItemBasedCFRecommender implements CommonFilterRecommender {
	private static final String SOLR_URL = "http://localhost:8983/solr";
	private static final String CORE_NAME_OF_GOODS= "ourckCORE";
	
	private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL)
														.withConnectionTimeout(10000)
														.withSocketTimeout(60000)
														.build();
	/**
	 * 根据给定的商品ID进行基于搜索引擎的协同过滤和推荐
	 * @param id 要进行推荐的商品ID
	 * @return 相似商品ID列表
	 */
	@Override
	public List<String> recommend(String id) {
		// Build params.
		String purchasedUsers = null;
		{
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("q", id);
			rawParams.put("df", "id");
			rawParams.put("fl", "*,score");
			SolrParams params = new MapSolrParams(rawParams);
			
			// Execute query.
			QueryResponse qResponse = null;
			try {
				qResponse = client.query(CORE_NAME_OF_GOODS, params);
			} catch (SolrServerException e) {
				System.err.println("( x_x )Oops! Something happened on server!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(" - Querying error!");
				return null;
			}
			SolrDocument qResult = qResponse.getResults().get(0); // There should be only 1 result when querying by id.
			purchasedUsers = (String) qResult.get("purchased_users");
		}
		
		// Second query:
		List<String> result = new LinkedList<String>();
		{
			// Build params.
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("q", purchasedUsers);
			rawParams.put("df", "purchased_users");
			rawParams.put("fl", "*,score");
			rawParams.put("mm", "3");
			SolrParams params = new MapSolrParams(rawParams);
			
			// Execute query.
			QueryResponse qResponse = null;
			try {
				qResponse = client.query(CORE_NAME_OF_GOODS, params);
			} catch (SolrServerException e) {
				System.err.println("( x_x )Oops! Something happened on server!");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(" - Querying error!");
				return null;
			}
			final SolrDocumentList qResults = qResponse.getResults();
			for(SolrDocument doc : qResults) {
				String goodsId = (String) doc.get("id"); // "id" is saved as TEXT in db. 
				if(!goodsId.equals(id)) result.add("" + goodsId);
			}
		}
		
		return result;
	}

	public static void main(String[] args) {
		SolrBasedItemBasedCFRecommender recommender = new SolrBasedItemBasedCFRecommender();
		System.out.print("输入商品ID："); String id = jin();
		System.out.print("相似商品包括："); System.out.println(recommender.recommend(id));
	}
}
