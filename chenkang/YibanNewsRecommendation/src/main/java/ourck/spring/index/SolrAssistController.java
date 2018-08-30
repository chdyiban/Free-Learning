package ourck.spring.index;

import java.io.*;
import java.util.*;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ourck.yiban.datahandler.CRANOperator;
import ourck.yiban.datahandler.DataTokenizer;
import ourck.yiban.dbgrab.DBGrabber;
import ourck.yiban.dbgrab.DBWriter;
import ourck.yiban.recommender.NewsIndexRecommender;
import ourck.yiban.solrassist.DIHTrigger;
import ourck.yiban.solrassist.SuffixSuggester;
import ourck.yiban.utils.DataCollection;

@RestController
public class SolrAssistController {
	@RequestMapping("/")
	public String index() {
		return "SolrAssist is on your serve!";
	}
	
	@RequestMapping("/chssuggest")
	public String chssuggest(String coreName, String key) {
		SuffixSuggester suggester = new SuffixSuggester(coreName);
		String suggest = null;
		try {
			suggest = suggester.getSuggest(key);
			return suggest;
		} catch (IOException e) {
			e.printStackTrace();
			JSONObject data = new JSONObject();
			data.put("success", false);
			data.put("info", "IOException: " + e.getMessage());
			return data.toString();
		}
	}
	
	@RequestMapping("/recommend")
	public String recommend(String keyword) {
		NewsIndexRecommender r = new NewsIndexRecommender();
		String[] kwds = keyword.split(" ");
		List<Object> result = r.recommend(kwds);
		
		JSONObject data = new JSONObject();
		data.put("docs", result);
		data.put("success", true);
		
		return data.toString();
	}
	
	@RequestMapping("/dbsync")
	public String dbsync(String coreName, String mask) {
		if(coreName == null || mask == null) {
			StringBuilder stb = new StringBuilder();
			stb.append(" <p>- USAGE: java -jar DIHTrigger [CoreName] [MaskCode] <p>");
			stb.append(" - The [MaskCode] is a 2-digits decimal number, <p>");
			stb.append("   converted by [binary number] represents DIH request params <p>");
			stb.append("   indicates how this import task should work. <p><p>");
			stb.append(" - The [binary number] is a 6 bit digit, <p>");
			stb.append("   it takes forms like this: <p>");
			stb.append("       [isFull][isVerbose][isClean][isCommit][isOptimized][isDebug] <p><p>");
			stb.append(" - Every digit's value(1 or 0) refers to TRUE or FALSE of an arg. <p>");
			stb.append(" - For example: <p>");
			stb.append("       java -jar DIHTrigger 64 <p>");
			stb.append("   operates a [Full][Verbose][Not Clean][Commit][Not Optimized][Not Debug] import. <p><p>");
			stb.append(" - P.S. 64(D) = 110100(B). <p>");
			stb.append(" - Send regards to CHMOD & CHGRP <p>");
			
			return stb.toString();
		}
		
		boolean[] params = DIHTrigger.decimalMaskCodeToBooleanAry(mask);
		
		DIHTrigger t = new DIHTrigger(coreName);
		t.invoke(params[0], params[1], params[2], params[3], params[4], params[5]);

		JSONObject data = new JSONObject();
		data.put("success", true);
		return data.toString();
	}
	
	@RequestMapping("/update")
	public String update() {
		JSONObject data = new JSONObject();
		
		final String RAW_DB_URL = "jdbc:mysql://47.52.34.62:3306/fastadmin";  
		final String RAW_DB_USERNAME = "fastadmin_read";
		final String RAW_DB_PWD = "Zpb5xp5cxAFIQEUq";
		
		final String CLUSTER_DB_URL = "jdbc:mysql://localhost:3306/NewsCluster";  
		final String CLUSTER_DB_USERNAME = "root";
		final String CLUSTER_DB_PWD = "voidPwd039";
		
		// Connect to DBs.
		DBGrabber dbGrabber = new DBGrabber(RAW_DB_URL, RAW_DB_USERNAME, RAW_DB_PWD);
		DBWriter dbWriter = new DBWriter(CLUSTER_DB_URL, CLUSTER_DB_USERNAME, CLUSTER_DB_PWD);

		// 1. Processing news index
		{
			final String RAW_DB_NEWS_INDEX_TABLENAME = "fa_cms_archives";
			final String CLUSTER_DB_NEWS_INDEX_TABLENAME = "news_index_cluster";
			
			final String TOKENIZED_FILE_PATH = "processedData/news_index.txt";
			final String CLUSTERED_FILE_PATH = "processedData/clustered_news_index.txt";
			
			final String CINDEX_CRAN_CMD = "Rscript src/main/resources/Rscripts/ProcessNewsIndex.r " 
					+ TOKENIZED_FILE_PATH + " " + CLUSTERED_FILE_PATH;
			
			try {
				DataCollection rawData = dbGrabber.grabNewsIndexData(RAW_DB_NEWS_INDEX_TABLENAME);
				
				// Tokenizing
				File tokenizedFile = new DataTokenizer().tokenizeAsFile(rawData, TOKENIZED_FILE_PATH, "title");
				System.out.println(" - Tokenizing success: " + tokenizedFile.getAbsolutePath());
				
				// Get terminal to execute R script& get output.
				System.out.println(" - R Clustering... \n");
				new CRANOperator().execScript(CINDEX_CRAN_CMD);
				
				// Write data into local ClusteredDB
				dbWriter.writeClusterInfo(CLUSTERED_FILE_PATH, CLUSTER_DB_NEWS_INDEX_TABLENAME);
				System.out.println(" - Cluster info written to DB ");

			} catch (IOException e) {
				data.put("success", false);
				data.put("info", "IOException: " + e.getMessage());
				return data.toString();
			} catch (Exception e) {
				data.put("success", false);
				data.put("info", "Exception: " + e.getMessage());
				return data.toString();
			}
			
		}
		
		// 2. Processing news content.
		{
			final String RAW_DB_NEWS_CTNT_TABLENAME = "fa_cms_addonnews";
			final String CLUSTER_DB_NEWS_CTNT_TABLENAME = "news_content_cluster";
			
			final String TOKENIZED_FILE_PATH = "processedData/news_content.txt";
			final String CLUSTERED_FILE_PATH = "processedData/clustered_news_content.txt";
			
			final String CCTNT_CRAN_CMD = "Rscript src/main/resources/Rscripts/ProcessNewsContent.r " 
					+ TOKENIZED_FILE_PATH + " " + CLUSTERED_FILE_PATH;
			
			DataCollection rawData = dbGrabber.grabNewsContentData(RAW_DB_NEWS_CTNT_TABLENAME);
			
			try {
				File tokenized = new DataTokenizer().tokenizeAsFile(rawData, TOKENIZED_FILE_PATH ,"content");
				System.out.println(" - Tokenizing success: " + tokenized.getAbsolutePath());
				
				new CRANOperator().execScript(CCTNT_CRAN_CMD);

				dbWriter.writeClusterInfo(CLUSTERED_FILE_PATH, CLUSTER_DB_NEWS_CTNT_TABLENAME);
				System.out.println(" - Cluster info written to DB ");
			} catch (IOException e) {
				data.put("success", false);
				data.put("info", "IOException: " + e.getMessage());
				return data.toString();
			}  catch (Exception e) {
				data.put("success", false);
				data.put("info", "Exception: " + e.getMessage());
				return data.toString();
			}
		}
		
		System.out.println(" [*] Processing all done! ");
		data.put("success", true);
		return data.toString();
	}
}
