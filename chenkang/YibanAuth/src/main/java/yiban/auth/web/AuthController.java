package yiban.auth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.yiban.open.Uis;
import yiban.auth.beans.AuthInfo;
import yiban.auth.beans.UserInfo;
import yiban.auth.service.CHDAuthServiceCrawlerImpl;
import yiban.auth.service.UserQuery;

@Controller
@RequestMapping("/auth")
/**
 * CAS用户认证控制器。提供用户名和密码，认证失败返回错误信息，成功返回加密的用户信息。
 * TODO 这个模块太复杂，考虑拆分为认证 + 查库 + 加密3个，但拆分开之后也要考虑安全问题（如控制器的访问权）。
 * @author gxbhck
 */
public class AuthController {
	
	private static String SERVER_HOSTNAME = "http://ids.chddata.com/";
	private static String CHD_AUTH_URL = "http://ids.chd.edu.cn/authserver/login?service=";
	
	@PostMapping("/login")
	@ResponseBody
	public String login(@ModelAttribute AuthInfo info, String serviceUrl) {
		String authUrl = CHD_AUTH_URL + serviceUrl;
		CHDAuthServiceCrawlerImpl loginer = new CHDAuthServiceCrawlerImpl(authUrl);
		boolean loginFlag = loginer.login(info.getUserName(), info.getPassWord());
		
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
		
		CHDAuthServiceCrawlerImpl loginer = new CHDAuthServiceCrawlerImpl(authUrl);
		boolean loginFlag = loginer.login(user, pwd);
		
		if(!loginFlag) {
			response.setStatus(401);
			response.setHeader("SuccessFlag", "false");
		}
		else {
			UserInfo userInfo = UserQuery.findByID(user);
			response.setStatus(200);
			response.setHeader("SuccessFlag", "true");
			
			Uis uis = Uis.getInstance();
			try {
				uis.setup(response, serviceUrl, "src/main/resources/pkcs8_priv.pem"); // TODO 如何访问工程资源？
				uis.run(userInfo.toMap());
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
				response.setHeader("SuccessFlag", "false");
			}
		}
	}
	
}
