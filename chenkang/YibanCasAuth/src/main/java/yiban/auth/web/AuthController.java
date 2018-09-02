<<<<<<< HEAD
package yiban.auth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yiban.auth.beans.AuthInfo;
import yiban.auth.beans.UserInfo;
import yiban.auth.service.CHDCwlAuthService;
import yiban.auth.service.YibanProcessingService;
import yiban.auth.service.UserQueryService;

@Controller
@RequestMapping("/auth")
/**
 * CAS用户认证控制器。提供用户名和密码，认证失败返回错误信息，成功返回加密的用户信息。<p>
 * 使用传统爬虫技术进行认证。<p>
 * TODO 这个模块太复杂，考虑拆分为认证 + 查库 + 加密3个，但拆分开之后也要考虑安全问题（如控制器的访问权）。
 * @deprecated 该类的验证原理是基于CAS服务器分发的cookies，而非持有票据并返回服务器验证。<p>
 * 				若要使用该模块，应将Spring的CASConfig文件禁用。
 * @author ourck
 */
public class AuthController {
	
	private static String SERVER_HOSTNAME = "http://ids.chddata.com/";
	private static String CHD_AUTH_URL = "http://ids.chd.edu.cn/authserver/login?service=";
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private YibanProcessingService yibanProcessingService;
	
//	@Autowired
	private CHDCwlAuthService cwlAuthService;

	@PostMapping("/login")
	@ResponseBody
	public String login(@ModelAttribute AuthInfo info) {
//		CHDCwlAuthService loginer = new CHDCwlAuthService(authUrl);
		// TODO 这么做对吗？
		AnnotationConfigApplicationContext ctxt = 
				new AnnotationConfigApplicationContext();
		cwlAuthService = ctxt.getBean(CHDCwlAuthService.class);
		boolean loginFlag = cwlAuthService.login(info.getUserName(), info.getPassWord());
		
		ctxt.close();
		
		if(loginFlag) return "Login Success!";
		else return "Login Failed!";
	}
	
	/**
	 * Servlet风格用户认证
	 * @deprecated DEBUG-ONLY 
	 * @param user 用户名
	 * @param pwd 密码
	 * @return 加密用户信息 或null（当认证失败）
	 */
	@GetMapping("/debuglogin")
	public void debuglogin(HttpServletRequest request, HttpServletResponse response) {
		String serviceUrl = SERVER_HOSTNAME;
		String authUrl = CHD_AUTH_URL + serviceUrl;
		String user = request.getHeader("user");
		String pwd = request.getHeader("pwd");
		
		CHDCwlAuthService loginer = new CHDCwlAuthService(authUrl);
		boolean loginFlag = loginer.login(user, pwd);
		
		if(!loginFlag) {
			response.setStatus(401);
			response.setHeader("SuccessFlag", "false");
		}
		else {
			UserInfo userInfo = userQueryService.findByID(user);
			response.setStatus(200);
			response.setHeader("SuccessFlag", "true");
			try {
				yibanProcessingService.run(response, userInfo);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}
	}
	
	@RequestMapping(value = "/cas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void query(String url, HttpServletRequest request, HttpServletResponse response) { 
		// 在到达这里之前 一定是通过了CAS认证的
		System.out.println(url);
		String user = request.getRemoteUser();

		UserInfo userInfo = userQueryService.findByID(user);
		response.setHeader("SuccessFlag", "true");
		try {
			yibanProcessingService.run(response, userInfo, url);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setHeader("SuccessFlag", "false");
		}
	}
	
}
=======
package yiban.auth.web;

import java.util.Map;

import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yiban.auth.beans.AuthInfo;
import yiban.auth.service.CHDLoginer;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private static String SERVER_HOSTNAME = "http://ids.chddata.com/";
	private static String CHD_AUTH_URL = "http://ids.chd.edu.cn/authserver/login?service=";
	
	@PostMapping("/login")
	@ResponseBody
	public String login(@ModelAttribute AuthInfo info, String serviceUrl) throws LoginException {
		String authUrl = CHD_AUTH_URL + serviceUrl;
		CHDLoginer loginer = new CHDLoginer(authUrl);
		Map<String, String> result = loginer.login(info.getUserName(), info.getPassWord());
		
		if(result == null) return "Login failed!";
		else {
			System.out.println(result.get("Location")); // TODO DEBUG-ONLY
			return null;
		}
	}
	
	@RequestMapping("/RESTlogin") // TODO DEBUG-ONLY
	@ResponseBody
	public String RESTlogin(String user, String pwd) throws LoginException {
		String serviceUrl = SERVER_HOSTNAME; // + "/verifyTicket";
		String authUrl = CHD_AUTH_URL + serviceUrl;
		CHDLoginer loginer = new CHDLoginer(authUrl);
		Map<String, String> result = loginer.login(user, pwd);
		
		if(result == null) return "Login failed!";
		else {
			System.out.println(result.get("Location"));
			return "Login Success!!";
		}
	}
	
	@RequestMapping("/verifyTicket")
	public String verifyTicket(String ticket) {
		if(ticket != null && ticket != "") {
			System.out.println(ticket);
			return "redirect:/";
		}
		else {
			return null; // TODO Return an error message.
		}
		
	}
	
	
}
>>>>>>> refs/remotes/choose_remote_name/master
