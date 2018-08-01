package top.ourck.dao;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import top.ourck.entity.AccountInfo;

import java.io.Closeable;
import java.util.Hashtable;

/**
 * LDAP用户名密码校验实现类
 */
public class LdapChecker implements Closeable {
    private static LdapContext ctx = null;
    private static Control[] connCtls = null;

    /****定义LDAP的基本连接信息 在这里写死真的好吗？******/
	private static String URL = "ldap://127.0.0.1:389/";
	// LDAP的根DN
    private static String BASEDN = "dc=ourck,dc=top";
    // LDAP的连接账号（身份认证管理平台添加的应用账号，应用账号格式：uid=?,ou=?,dc=????）
    private static String PRINCIPAL = "cn=admin,dc=ourck,dc=top";
	// LDAP的连接账号的密码（身份认证管理平台添加的应用账号的密码）
    private static String PASSWORD = "voidPwd039";

    public static void main(String[] args){
		// 登录用户的用户名和密码
        String username = "Jack";
        String password = "233333";
        
        LdapChecker checker = new LdapChecker();
        System.out.println(checker.authenticate(username, password));
        checker.close();
    }

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
        if(ctx == null){
            getCtx();
        }
        String userDN = getUserDN("cn", usr);
			if ("".equals(userDN) || userDN == null) {
            return false;
        }
        try {
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, pwd);
            ctx.reconnect(connCtls);
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
     * 根据提供的登录用户名 & 密码获取LDAP上下文。<p>
     * <b>注意：<b> 根据提供的用户名不同，所具有的LDAP数据库权限可能不一样。
     */
    private void getCtx() {
        if (ctx != null ) {
            return;
        }
        Hashtable<String, String> env = new Hashtable<>(); // Old-styled?
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, URL + BASEDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, PRINCIPAL);
        env.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        try {
            // 连接LDAP
            ctx = new InitialLdapContext(env, connCtls);
            System.out.println("********* Connect success! *********"); // TODO DEBUG-ONLY
        } catch(javax.naming.AuthenticationException e){
            System.out.println("Authentication failed: " + e.toString());
        } catch(Exception e){
            System.out.println("Something wrong while authenticating: " + e.toString());
        }
    }

    /**
     * 关闭LDAP环境上下文 释放资源
     */
    public void close(){
        try {
            if(ctx != null) ctx.close();
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
            NamingEnumeration<SearchResult> en = ctx.search("", key + "=" + val, constraints);
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
            System.out.println("Exception in search():"+e);
        }

        return userDN;
    }

}
