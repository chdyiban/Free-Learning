package ourck.yiban.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class CHSTokenizer {
	
	private Analyzer analyzer = new IKAnalyzer(true);

	/**
	 * 按行对文本进行中文分词。
	 * @param fileName 文件路径
	 * @return 分词后文本按行作为元素的List
	 * @throws IOException 当文件操作失败
	 */
	public List<String> tokenizeFile(String fileName) throws IOException {
		List<String> listByLines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String strline;
		while((strline = reader.readLine()) != null) {
			listByLines.add(tokenizeString(strline));
		}
		
		reader.close();
		return listByLines;
	}
	
	/**
	 * 按单个字符串对其进行中文分词。
	 * @param str 源字符串
	 * @return 分词后的字符串
	 * @throws IOException 
	 */
	public String tokenizeString(String str) throws IOException {
		TokenStream ts = analyzer.tokenStream("myName", new StringReader(str));
		ts.reset(); // TODO ???
		
		CharTermAttribute term  = ts.addAttribute(CharTermAttribute.class);
		StringBuilder stb = new StringBuilder();
		while(ts.incrementToken()) {
			stb.append(term + " ");
		}
		ts.close();
		ts.end();

		return stb.toString();
	}
}
