package yiban.auth.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
	
	@RequestMapping("/index")
	public String index(String ticket) {
		return "Hello world!";
	}
}
