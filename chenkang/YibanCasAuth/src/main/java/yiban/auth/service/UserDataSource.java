package yiban.auth.service;

import org.springframework.context.annotation.ComponentScan;

import yiban.auth.beans.UserInfo;
import yiban.auth.dao.UserRepository;

@ComponentScan(basePackages = "yiban.auth.beans")
public class UserDataSource {
	
	public UserInfo findByID(String ID) {
		/*
		 * UserRepository userRepository = ctx.getBean(UserDao.class); 
		 * TODO ǧ����ͨ��newʵ������дƪ���¼�������
		 * https://blog.csdn.net/diu_brother/article/details/47834769
		 */
		UserRepository userRepository = new UserRepository(); 
		UserInfo user = userRepository.findByStuID(ID); // TODO findByTeacherID
		return user;
	}
}
