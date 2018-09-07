package top.ourck.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;

import top.ourck.dao.LdapChecker;
import top.ourck.service.UserService;

@Configuration
public class DefaultWiringConfig {

	// Local test LDAP Server:
	private static String URL = "ldap://218.195.56.161:389/";
	private static String BASEDN = "dc=chd,dc=edu,dc=cn";
	private static String PRINCIPAL = "cn=directory manager";
	private static String PASSWORD = "chdwisedu2015";

    @Bean
    public String baseDn() {
    	return BASEDN;
    }
    
    @Bean
    public LdapChecker ldapChecker() {
    	return new LdapChecker();
    }
    
    @Bean
    public UserService userService() {
    	return new UserService();
    }
    
	@Bean
	@SuppressWarnings("restriction")
	public LdapContextSource ldapContextSource() {
    	LdapContextSource lctxt = new LdapContextSource();
    	lctxt.setBase(BASEDN);
    	lctxt.setContextFactory(com.sun.jndi.ldap.LdapCtxFactory.class);
    	lctxt.setUrl(URL);
    	lctxt.setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy());
    	lctxt.setUserDn(PRINCIPAL);
    	lctxt.setPassword(PASSWORD);
    	return lctxt;
    }
    
    @Bean
    @Autowired
    public LdapTemplate ldapTemplate(LdapContextSource lctxt) {
    	return new LdapTemplate(lctxt);
    }
}
