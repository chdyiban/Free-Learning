package top.ourck.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.ourck.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/RESTLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String RESTLogin(String userName, String pwd) {
		boolean flag = userService.getAuth(userName, pwd);
		
		if(flag) 		return "Welcome, " + userName + "!";
		else			return "Login failed!";
	}
	
}
