package yiban.dorm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yiban.dorm.beans.UserInfo;
import yiban.dorm.service.UserInfoMgr;

@Controller
public class FormController {
	
	@Autowired
	private UserInfoMgr userInfoMgr;
	
	@RequestMapping("/submit")
	@ResponseBody
	public UserInfo receive(UserInfo user) {
		userInfoMgr.calculateScores(user);
		userInfoMgr.addUser(user);
		return user;
	}
	
}
