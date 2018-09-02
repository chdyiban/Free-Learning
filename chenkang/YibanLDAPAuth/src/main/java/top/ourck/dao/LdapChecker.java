package top.ourck.dao;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.ourck.entity.AccountInfo;

import java.io.Closeable;

/**
 * LDAP用户名密码校验实现类
 */
@Repository
public class LdapChecker implements Closeable {
	
	@Autowired
    private LdapContext ldapContext;
	
	@Autowired
	private String BASEDN;

//    public static void main(String[] args){
//		// 登录用户的用户名和密码
//    	// TODO DEBUG-ONLY
//        String username = "Jack";
//        String password = "980309";
//        
//        LdapChecker checker = new LdapChecker();
//        System.out.println(checker.authenticate(username, password));
//        checker.close();
//    }

    /**
     * 与LDAP服务器通信，根据（唯一的）用户名查询该用户是否存在，以及密码是否匹配
     * TODO 这里应该加什么注解？AccountInfo是作为Entity返回的
     * @param usr 用户名
     * @param pwd 密码
     * @return 用户信息。若校验失败返回null
     */
    public AccountInfo qForAccountInfo(String usr, String pwd) {
    	if(authenticate(usr, pwd)) return new AccountInfo(usr, pwd);
    	else return null;
    }
    
    /**
     * 校验用户名密码的方法
     * @param usr 用户名
     * @param pwd 密码
     * @return 校验是否通过
     */
    private boolean authenticate(String usr, String pwd) {
        boolean validatedFlag = false;
        if(pwd == null || pwd == "")
            return false;
        String userDN = getUserDN("cn", usr);
			if ("".equals(userDN) || userDN == null) {
            return false;
        }
        try {
        	ldapContext.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
        	ldapContext.addToEnvironment(Context.SECURITY_CREDENTIALS, pwd);
        	ldapContext.reconnect(ldapContext.getConnectControls());
            validatedFlag = true;
        } catch (AuthenticationException e) {
            System.out.println(userDN + " is not authenticated");
            System.out.println(e.toString());
            validatedFlag = false;
        }catch (NamingException e) {
            System.out.println(userDN + " is not authenticated");
            validatedFlag = false;
        }
        return validatedFlag;
    }

    /**
     * 关闭LDAP环境上下文 释放资源
     */
    public void close(){
        try {
            if(ldapContext != null) ldapContext.close();
        } catch (NamingException ex) {}
    }

    /**
     * 按照不同的条目类型的唯一键值对查找用户。
     * @param key 要查找的键。可以是如cn，uid，sn，...
     * @param val 要查找的值。
     * @return 查找到的条目DN。若没有该条目，返回“”字符串对象
     */
    private String getUserDN(String key, String val){
        String userDN = "";
        try{
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> en = ldapContext.search("", key + "=" + val, constraints);
            if(en == null){
                System.out.println("Have no NamingEnumeration.");
            }
            if(!en.hasMoreElements()){
                System.out.println("Have no element.");
            }
            while (en != null && en.hasMoreElements()){
                Object obj = en.nextElement(); // TODO why upcasting???
                if(obj instanceof SearchResult){
                    SearchResult si = (SearchResult) obj;
                    userDN += si.getName();
                    userDN += "," + BASEDN;
                }
                else{
                    System.out.println(obj);
                }
                System.out.println();
            }
        }catch(Exception e){
            System.out.println("Exception in search():" + e);
        }

        return userDN;
    }

}
