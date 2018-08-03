package yiban.auth.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yiban.auth.beans.UserInfo;
import yiban.auth.service.UserDataSource;

@RestController
@RequestMapping("/query")
public class AccountQueryController {

	@GetMapping(value = "/userID", produces = MediaType.APPLICATION_JSON_VALUE) // TODO [!] DON'T EXPOSE TO WEB!
	public UserInfo getByID(String ID) {
		return new UserDataSource().findByID("2016900667");
	}
	
}
