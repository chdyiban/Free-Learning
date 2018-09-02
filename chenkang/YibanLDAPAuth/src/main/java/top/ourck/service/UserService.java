package top.ourck.service;

import org.springframework.beans.factory.annotation.Autowired;

import top.ourck.dao.LdapChecker;
import top.ourck.entity.AccountInfo;

/**
 * 用户服务实现类。<p>
 * 提供用户的登录服务。<p>
 * TODO Service的周期？By session / by what？
 * @author ourck
 */
public class UserService {
	
	@Autowired
	LdapChecker ldapChecker;
	
	/**
	 * 根据提供的用户名和密码进行校验。
	 * @param usr 用户名
	 * @param pwd 密码
	 * @return 校验成功标志
	 */
	public boolean getAuth(String usr, String pwd) {
		AccountInfo account = ldapChecker.qForAccountInfo(usr, pwd); //别把这玩意返回前台！这里边有密码
		ldapChecker.close();
		
		if(account != null) return true;
		else return false;
	}
	
}
