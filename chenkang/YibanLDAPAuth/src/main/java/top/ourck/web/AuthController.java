package top.ourck.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.ourck.entity.AccountInfo;
import top.ourck.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@PostMapping("/login")
	public String login(@ModelAttribute AccountInfo info) { // TODO 考虑对PWD加密...不过似乎学校的Portal也没这么干就算了吧
		UserService service = new UserService();
		boolean authFlag = service.getAuth(info.getUserName(), info.getPwd());
		
		if(!authFlag) 		return "Login failed!";
		else 				return "Welcome, " + info.getUserName();
	}
	
	@GetMapping(value = "/RESTLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) // TODO DEBUG-ONLY
	public String login(String userName, String pwd) {
		UserService service = new UserService();
		boolean authFlag = service.getAuth(userName, pwd);
		
		if(!authFlag) 		return "Login failed!";
		else 				return "Welcome, " + userName + "!";
	}
}
