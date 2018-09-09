package top.ourck.dao;

import java.util.*;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import top.ourck.entity.AccountInfo;

/**
 * LDAP用户名密码校验实现类
 */
@Repository
public class LdapChecker {
	
	private static final String DN_KEY = "uid";
	private static final String PERSON_CLASS = "(objectclass=person)"; 
	
	@Autowired
	private LdapTemplate ldapTemplate;
	
    /**
     * 校验用户名密码的方法。若认证通过，返回该用户信息。
     * @param usr 待认证用户名
     * @param pwd 待认证用户密码
     * @return 用户信息。失败则返回null
     */
    public AccountInfo authenticate(String usr, String pwd) {
    	boolean flag = false;
    	List<AccountInfo> list = ldapTemplate.search("", DN_KEY + '=' + usr, new AccountCtxMapper());
    	if(list.size() != 1) {
    		System.err.println("[!] " + DN_KEY + " = " + usr + " has " + list.size() + " value(s)!");
    		flag = false;
    	}
    	
    	AccountInfo account = list.get(0);
    	flag = ldapTemplate.authenticate(account.getUserName(), PERSON_CLASS, pwd);
    	if(flag)		return account;
    	else			return null;
    }
    
}

class AccountCtxMapper implements ContextMapper<AccountInfo> {

	@Override
	public AccountInfo mapFromContext(Object ctx) throws NamingException {
		AccountInfo account = new AccountInfo();
		DirContextAdapter dir = (DirContextAdapter) ctx;
		account.setUserName(dir.getDn().toString());
		account.setPwd(null);
		return account;
	}
	
}