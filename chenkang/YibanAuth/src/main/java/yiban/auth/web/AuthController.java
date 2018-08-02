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
