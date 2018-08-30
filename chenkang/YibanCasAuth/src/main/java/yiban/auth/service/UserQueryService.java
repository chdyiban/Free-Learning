package yiban.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import yiban.auth.beans.UserInfo;
import yiban.auth.dao.UserRepository;

@ComponentScan(basePackages = "yiban.auth.beans")
public class UserQueryService {
	
	@Autowired
	private UserRepository userRepository;
	
	public UserInfo findByID(String ID) {
		/*
		 * UserRepository userRepository = ctx.getBean(UserDao.class); 
		 * TODO 千万不能通过new实例化！写篇文章记下来。
		 * https://blog.csdn.net/diu_brother/article/details/47834769
		 */
		UserInfo user = userRepository.findByID(ID);
		return user;
	}
}
