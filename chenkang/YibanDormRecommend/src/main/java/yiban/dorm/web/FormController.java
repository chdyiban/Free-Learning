package yiban.dorm.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yiban.dorm.beans.UserInfo;

@Controller
public class FormController {
	
	@RequestMapping("/submit")
	@ResponseBody
	public UserInfo receive(UserInfo user) {
		return user;
	}
	
}
