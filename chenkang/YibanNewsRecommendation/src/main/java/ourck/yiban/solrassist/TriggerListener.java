package ourck.yiban.solrassist;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.Arrays;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * 用于端口监听，实现客户端请求的各种服务。
 * TODO 重构，将这个程序作为一个中间件 + 多个后台服务组件的模式
 * @author ourck
 *
 */
public class TriggerListener {
	private static final int PORT_ONLISTEN = 8866;
	
	public static void main(String[] args) throws IOException {
//        HttpServer httpserver = HttpServer.create();
		if(args.length != 3 && args.length != 1) {
			System.err.println(" - Usage: java -jar [OnListenFlag = 1]");
			System.err.println(" - Or: java -jar [OnListenFlag = 0] [CoreName] [MaskCode]");
			DIHTrigger.main(new String[0]);
			System.exit(0);
		}
		if(args[0].equals("1")) {
			HttpServerProvider provider = HttpServerProvider.provider();  
			HttpServer httpserver = provider.createHttpServer(new InetSocketAddress(PORT_ONLISTEN), 100);
			httpserver.createContext("/", new MyHandler());   
			httpserver.setExecutor(null);  
			httpserver.start();  
			System.out.println(" - DIHTrigger by OURCK: server started");  
		}
		if(args[0].equals("0")) {
			DIHTrigger.main(Arrays.copyOfRange(args, 1, 3));
		}
    }
   
}

class MyHandler implements HttpHandler {
//	@Override // TODO ERROR??????
    public void handle(HttpExchange httpExchangeInstance) throws IOException {
        System.out.println("[+] Triggered:" + httpExchangeInstance.getRemoteAddress());
        String requestMethod = httpExchangeInstance.getRequestMethod();
        try {
            if(requestMethod.equalsIgnoreCase("POST")){
                Headers headerForResponse = httpExchangeInstance.getResponseHeaders();
                headerForResponse.set("Content-Type", "text/html;charset=utf-8");

//    			1.Handle client's msg here:
                byte[] buff;
                BufferedInputStream is = new BufferedInputStream(
                		httpExchangeInstance.getRequestBody());
                is.read((buff = new byte[is.available()]), 0, is.available());
                String param = new String(buff);
                
//              2.Processing string...
                String[] processedParams = param.split("\\&");
                if(processedParams.length != 2)
                	throw new InvalidRequestException();
                String coreParam = null, maskCodeParam = null;
                for(String p : processedParams) {
                	if(p.contains("CoreName"))
                		coreParam = p.split("=")[1];
                	else if(p.contains("MaskCode"))
                		maskCodeParam = p.split("=")[1];
                	else
                		throw new InvalidRequestException();
                }
                
//              3.Calls the DIHTrigger
                boolean[] params = DIHTrigger.decimalMaskCodeToBooleanAry(maskCodeParam);
        		DIHTrigger t = new DIHTrigger(coreParam);
        		t.invoke(params[0], params[1], params[2], params[3], params[4], params[5]);
                
//        		4.Send response.
                String response = "request received"; // TODO Maybe JSON?
                httpExchangeInstance.sendResponseHeaders(HttpURLConnection.HTTP_OK,	// HTTP 200 OK
                										response.getBytes("UTF-8").length);
                OutputStream responseBody = httpExchangeInstance.getResponseBody();	// Get stream for writing.
                OutputStreamWriter writer = new OutputStreamWriter(responseBody, "UTF-8");
                writer.write(response);
                writer.close();
                responseBody.close();  
            }
            else
            	throw new InvalidRequestException();
        } catch(InvalidRequestException e) {
        	StringBuilder stb = new StringBuilder();
        	stb.append("[!] Invalid request!");
 			stb.append(" - USAGE: A POST includes 2 params: [CoreName] [MaskCode] \n\n");
 			stb.append(" - The [MaskCode] is a 2-digits decimal number, \n");
 			stb.append("   converted by [binary number] represents DIH request params \n");
 			stb.append("   indicates how this import task should work. \n\n");
 			stb.append(" - The [binary number] is a 6 bit digit, \n");
 			stb.append("   it takes forms like this: \n");
 			stb.append("       [isFull][isVerbose][isClean][isCommit][isOptimized][isDebug] \n\n");
 			stb.append(" - Every digit's value(1 or 0) refers to TRUE or FALSE of an arg. \n");
 			stb.append(" - For example: \n");
 			stb.append("       java -jar DIHTrigger 64 \n");
 			stb.append("   operates a [Full][Verbose][Not Clean][Commit][Not Optimized][Not Debug] import. \n\n");
 			stb.append(" - P.S. 64(D) = 110100(B). \n");
 			stb.append(" - Send regards to CHMOD & CHGRP \n");
 			String response = stb.toString();
        	httpExchangeInstance.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,
        			response.getBytes("UTF-8").length);
            OutputStream responseBody = httpExchangeInstance.getResponseBody();	// Get stream for writing.
            OutputStreamWriter writer = new OutputStreamWriter(responseBody, "UTF-8");
            writer.write(response);
            writer.close();
            responseBody.close();  
        }
        //[!]Exceptions thrown by other threads can't be handled by main thread!
//        catch(Exception e) {
//        	e.printStackTrace();
//        	System.err.println("[!] Oops! Something went wrong...But +1s!");
//        }
        
    }
}

@SuppressWarnings("serial")
class InvalidRequestException extends Exception {}
