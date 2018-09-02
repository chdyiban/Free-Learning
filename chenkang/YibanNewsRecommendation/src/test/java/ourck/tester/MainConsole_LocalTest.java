package ourck.tester;

import static ourck.utils.ScreenReader.jin;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import ourck.yiban.datahandler.CRANOperator;
import ourck.yiban.datahandler.DataTokenizer;
import ourck.yiban.dbgrab.DBGrabber;
import ourck.yiban.dbgrab.DBWriter;
import ourck.yiban.recommender.NewsIndexRecommender;
import ourck.yiban.utils.DataCollection;

public class MainConsole_LocalTest {
	
	private static void preProcessor() {
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
			
			final String TOKENIZED_FILE_PATH = "processedData/news_index.txt"; // Add ".txt" to avoid creating a directory.
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

			} catch (Exception e) {
				e.printStackTrace();
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
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(" [*] Processing all done! ");
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static void onlineService() {
		// 1. 推荐
		NewsIndexRecommender r = new NewsIndexRecommender();
		System.out.print("请输入您的新闻关键词，用空格分割："); String keyword = jin();
		System.out.println("---------------------------------------");
		System.out.println("推荐新闻如下：");

		List<Object> result = r.recommend(keyword.split(" "));
		for(Object obj : result) {
			System.out.println(new JSONObject((HashMap<String, String>) obj));
		}
		
		// 2.动态同步数据库
		// 3.拼写建议
	}
	
	public static void main(String[] args) throws IOException {
		preProcessor();
//		onlineService();
	}
}
