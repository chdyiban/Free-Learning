package top.ourck.spring.config;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import top.ourck.dao.LdapChecker;
import top.ourck.service.UserService;

@Configuration
public class DefaultWiringConfig {

    // TODO 定义LDAP的基本连接信息 在这里写死真的好吗？
	private static String URL = "ldap://127.0.0.1:389/";
	// LDAP的根DN
    private static String BASEDN = "dc=ourck,dc=top";
    // LDAP的连接账号（身份认证管理平台添加的应用账号，应用账号格式：uid=?,ou=?,dc=????）
    private static String PRINCIPAL = "cn=admin,dc=ourck,dc=top";
	// LDAP的连接账号的密码（身份认证管理平台添加的应用账号的密码）
    private static String PASSWORD = "voidPwd039";

    /**
     * 获得用于取得上下文的Controls本身
     * @return Control[]对象
     */
    @Bean(name="BASEDN")
    public String baseDN() {
    	return BASEDN;
    }

	/**
	 * 根据提供的登录用户名 & 密码获取LDAP上下文。<p>
	 * <b>注意：</b>根据提供的用户名不同，所具有的LDAP数据库权限可能不一样。
	 */
    @Bean
    public LdapContext ldapContext() {
    	LdapContext ctx = null;
    	
        Hashtable<String, String> env = new Hashtable<>(); // Old-styled?
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, URL + BASEDN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, PRINCIPAL);
        env.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        try {
            // 连接LDAP
            ctx = new InitialLdapContext(env, null); // TODO ctls?
            System.out.println("********* Connect success! *********"); // TODO DEBUG-ONLY
        } catch(javax.naming.AuthenticationException e){
            System.out.println("Authentication failed: " + e.toString());
        } catch(Exception e){
            System.out.println("Something wrong while authenticating: " + e.toString());
        }
        
        return ctx;
    }
    
    @Bean
    public LdapChecker ldapChecker() {
    	return new LdapChecker();
    }
    
    @Bean
    public UserService userService() {
    	return new UserService();
    }
}
