package ourck.solr.trigger;

import java.io.IOException;
import java.util.*;

import org.jsoup.*;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class DIHTrigger  {
	private static final String REQUEST_SUFFIX = "/dataimport?&command="; // What's this number?
	private final String solrUrl;
	
	private final String requested_core;
	private final String requestURL;
	
	public DIHTrigger(String url, String coreName) {
		solrUrl = url;
		requested_core = coreName;
		
		StringBuilder stb = new StringBuilder();
		stb.append(solrUrl);
		stb.append("/").append(requested_core).append(REQUEST_SUFFIX);
		requestURL = stb.toString();
	}
	
	public void invoke(Boolean isFull)
	{
		String param = null;
		if(isFull) param = "full-import";
		else param = "delta-import";
		
		Connection ct = Jsoup.connect(requestURL + param);
		Map<String, String> body = new HashMap<String, String>();

		ct.header("Referer", solrUrl)
		  .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) "
		  		+ "AppleWebKit/537.36 (KHTML, like Gecko) "
		  		+ "Chrome/63.0.3239.84 Safari/537.36");
		
		try {
			Response response = ct.ignoreContentType(true)
					.method(Method.GET)
					.data(body)
					.execute();
			System.out.println(response.body());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
