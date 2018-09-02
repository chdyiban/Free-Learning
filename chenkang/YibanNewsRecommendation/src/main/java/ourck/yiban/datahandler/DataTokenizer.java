package ourck.yiban.datahandler;

import java.io.*;
import java.util.*;

import ourck.yiban.utils.CHSTokenizer;
import ourck.yiban.utils.DataCollection;

public class DataTokenizer {
	
	/**
	 * 将指定的数据集合中文分词化。<p>
	 * 注意：分词操作直接作用于原数据集合对象之上。
	 * @param rawData 数据集合
	 * @param toBoTokenized 需要对数据集合中进行分词的字段，若为空则默认所有字段。
	 * @return 分词后的数据集合
	 */
	public DataCollection tokenize(DataCollection rawData, String... toBoTokenized) {
		if(toBoTokenized.length == 0) {
			Set<String> headers = rawData.get(0).keySet();
			toBoTokenized = headers.toArray(new String[headers.size()]);
		}
		CHSTokenizer tokenizer = new CHSTokenizer();
		
		for(Map<String, String> item : rawData) {
			try {
				for(String col : toBoTokenized) {
					String tokenized = tokenizer.tokenizeString(item.get(col));
					item.put(col, tokenized);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return rawData;
	}
	
	/**
	 * 将指定的数据集合中文分词化，并输出到指定文件。<p>
	 * 注意：分词操作直接作用于原数据集合对象之上。
	 * @param rawData 数据集合
	 * @param toBoTokenized 需要对数据集合中进行分词的字段，若为空则默认所有字段。
	 * @return 分词后的数据集合文件
	 * @throws FileNotFoundException 当打开文件失败
	 */
	public File tokenizeAsFile(DataCollection data, File file, String... toBoTokenized) throws FileNotFoundException {
		if(!file.exists()) {
			try { file.getParentFile().mkdirs(); file.createNewFile(); }
			catch (IOException e) { e.printStackTrace(); return null;}
		}
		
		data = tokenize(data, toBoTokenized);
		PrintWriter writer = new PrintWriter(file);
		
		// Headers.
		int i = 0;
		for(String key : data.get(0).keySet()) {
			if(i == data.get(0).size() - 1) writer.println(key);
			else writer.print(key + "\t"); i++;
		}
		
		// Contents.
		for(Map<String, String> item : data) {
			int j = 0;
			for(String val : item.values()) {
				if(j == item.size() - 1) writer.println(val);
				else writer.print(val + "\t"); j++;
			}
		}
		
		writer.close();
		return file;
	}
	
	public File tokenizeAsFile(DataCollection data, String path, String... toBoTokenized) throws FileNotFoundException {
		return tokenizeAsFile(data, new File(path), toBoTokenized);
	}
}
