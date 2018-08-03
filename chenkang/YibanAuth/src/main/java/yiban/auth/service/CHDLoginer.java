package yiban.auth.service;

import java.io.*;
import java.util.*;

import javax.security.auth.login.LoginException;

import org.jsoup.*;
import org.jsoup.Connection.*;
import org.jsoup.nodes.*;

public class CHDLoginer {
	
	private final String URL;
	// Handle SINGLE connection to avoid logging out.  
	private Connection ct; 
	private Map<String, String> cookies;
	
	public CHDLoginer(String targetUrl) {
		URL = targetUrl;
		ct = Jsoup.connect(URL);
	}
	
	public Map<String, String> login(String userName, String passwd) throws LoginException {
			ct.header("User-Agent",
	                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
			try {
				
				Response response = ct.execute();
				
				// Grep page elements for POST:
				Document doc = Jsoup.parse(response.body());
				Element e = doc.getElementById("casLoginForm");
				Map<String, String> params = new HashMap<String, String>();
				{
					params.put("username", userName);
					params.put("password", passwd);
					params.put("btn", "");
					// ... There's a hidden login key called "lt":
					params.put("lt", 
							e.select("input[name=\"lt\"]").get(0).attr("value").toString());
					params.put("dllt",
							e.select("input[name=\"dllt\"]").get(0).attr("value").toString());
					params.put("execution", 
							e.select("input[name=\"execution\"]").get(0).attr("value").toString());
					params.put("_eventId", 
							e.select("input[name=\"_eventId\"]").get(0).attr("value").toString());
					params.put("rmShown", 
							e.select("input[name=\"rmShown\"]").get(0).attr("value").toString());
				}
				
				Response loginResponse = ct.ignoreContentType(true)
						.method(Method.POST)
						.data(params)
						.cookies(response.cookies())
						.execute();
				
				cookies = loginResponse.cookies();
				Map<String, String> resultMap = loginResponse.headers();				
				
				// TODO How to identify a successful login?
				return resultMap;
				
			} catch(IOException e) { 
				System.err.println(" [!] Login Failed!");
				return null;
			}
		}
		
	public Map<String, String> getCookies() { return cookies; }
	
	public Connection getConnection() { return ct; }
}
