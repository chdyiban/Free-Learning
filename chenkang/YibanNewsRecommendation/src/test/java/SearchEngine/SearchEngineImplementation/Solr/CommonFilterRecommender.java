package SearchEngine.SearchEngineImplementation.Solr;

import java.util.List;

public interface CommonFilterRecommender {
	List<String> recommend(String id);
}
