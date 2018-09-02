package yiban.auth.service;

import java.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import yiban.auth.beans.UserInfo;

/**
 * 进行向易班提交前的处理。<p>
 * 包括对用户数据加密到请求体、指定跳转目标url两项操作。
 * @author ourck 易班团队
 */
public class YibanProcessingService {

	private static final String PRIV_FILE = "src/main/resources/pkcs8_priv.pem";
	
	public void run(HttpServletResponse response, UserInfo user, String targetUrl) throws Exception {
		CHDYibanUIS uis = CHDYibanUIS.getInstance();
		uis.setup(response, PRIV_FILE);
		uis.run(user.toMap(), targetUrl, false);
	}
	
	public void run(HttpServletResponse response, UserInfo user) throws Exception {
		CHDYibanUIS uis = CHDYibanUIS.getInstance();
		uis.setup(response, PRIV_FILE);
		uis.run(user.toMap(), null, false);
	}
	
}

/**
 * 由于业务需要（重定向），自己另外实现UIS实现类（作为工具类）。<p>
 * 由于原来的版本写的实在太烂（一个方法承载几个业务功能并且各方法紧耦合 + 面向过程），因此不选择（也不可能）继承而是直接重写
 * @author ourck
 */
class CHDYibanUIS {
	private static CHDYibanUIS mpInstance = null;

	private String priv_file;

	private RSAPrivateKey privkey;
	private HttpServletResponse response;

	private static final String CHECKURL = "https://o.yiban.cn/uiss/check?scid=";
	private static final int SPLIT_BLOCK = 245;
	private static final String CHD_ID = "10002_0";


	public static CHDYibanUIS getInstance()
	{
		if (mpInstance == null)
		{
			synchronized(CHDYibanUIS.class)
			{
				mpInstance = new CHDYibanUIS();
			}
		}
		return mpInstance;
	}

	private CHDYibanUIS() {}
	
	/**
	 * 设置RSA私钥文件及跳转目标Url
	 *
	 * @param  response  Servlet中的HttpServletResponse对象，用于URL重定向
	 * @param  priv_file RSA私钥文件（需要使用PKCS8格式编码）
	 * @throws Exception 
	 */
	public void setup(HttpServletResponse response, String priv_file) throws Exception
	{
		this.response  = response;
		this.priv_file = priv_file;
				
		InputStream in = new FileInputStream(this.priv_file);
		BufferedReader reader  = new BufferedReader(new InputStreamReader(in));
		StringBuilder  builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			if(line.charAt(0)=='-')
			{
				continue;
			}
			else
			{
				builder.append(line);
			}
		}
		// ~ for java8
		byte[] buffer = Base64.getDecoder().decode(builder.toString());
		
		PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory= KeyFactory.getInstance("RSA");
		this.privkey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		reader.close();
	}

	/**
	 * 重定向URL执行UIS验证工作
	 * 
	 * @param  data			HTTP请求的用户信息参数数组
	 * @param	targetUrl	如需跳转，提供目标url
	 * @param  isMobile		是否移动端标志
	 * @throws Exception 
	 */
	public void run(Map<String, String> data, String targetUrl, boolean isMobile) throws Exception
	{
		long now = System.currentTimeMillis();
		data.put("build_time", String.valueOf(Long.valueOf(now/1000).intValue()));
		JSONObject json = JSONObject.fromObject(data);
		String result = this.doencode(json.toString());
		String url = CHECKURL + CHD_ID;
		if (isMobile)
		{
			url += "&type=mobile";
		}
		// url += "&say=" + result;
		// response.sendRedirect(url);
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html />");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>UIS binding</title>");
		out.println("<script type=\"text/javascript\">");
		out.println("window.onload=function() {");
		out.println("document.forms[\"uis\"].submit();");
		out.println("};");
		out.println("</script>");
		out.println("</head>");
		
		out.println("<body>");
		out.printf("<form name=\"uis\" action=\"%s\" method=\"POST\">\n", url);
		out.printf("<input type=\"hidden\" name=\"say\" value=\"%s\" />\n", result);
		
		if(targetUrl != null) {
			targetUrl = URLEncoder.encode(targetUrl, "UTF-8"); // ByRef
			out.printf("<input type=\"hidden\" name=\"goto\" value=\"%s\" />\n", targetUrl);
		}
		
		if(isMobile) {
			out.printf("<input type=\"hidden\" name=\"type\" value=\"%s\" />\n", "mobile");
		}
		
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * 参数加密
	 * 
	 * @param  data      需要加密的字符串
	 * @return           经过Base64及Urlencode双重编码后的加密串
	 * @throws Exception 
	 */
	private String doencode(String data) throws Exception
	{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privkey);
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		int offSet = 0;
		byte[] cache;
		int i = 0;
		byte[] seq = data.getBytes("utf-8");
		int inputLen = seq.length;
		while (inputLen - offSet > 0)
		{
			if (inputLen - offSet > SPLIT_BLOCK)
			{
				cache = cipher.doFinal(seq, offSet, SPLIT_BLOCK);  
			}
			else
			{
				cache = cipher.doFinal(seq, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * SPLIT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		// ~ for java8
		byte[] b64enc = Base64.getEncoder().encode(encryptedData);
		String result = new String(b64enc); 
		result = result.replaceAll("=*$", "");
		//result = result.replaceAll("\\+/", "\\-_");
		result = result.replace('+', '-');
		result = result.replace('/', '_');
		return result;	// use POST -- if GET: return URLEncoder.encode(result, "utf-8");
	}



}
