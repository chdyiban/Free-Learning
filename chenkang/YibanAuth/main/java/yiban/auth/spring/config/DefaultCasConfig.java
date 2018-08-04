package yiban.auth.spring.config;

import java.util.*;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 结合传统Filter模式的Bean装配配置
 * @see https://blog.csdn.net/liuchuanhong1/article/details/73176603
 */
@Configuration
@Component
public class DefaultCasConfig {

	private static final String CAS_SERVER_PREFIX = "http://123.56.24.172:8080/cas";//"http://ids.chd.edu.cn/authserver";
	private static final String CAS_SERVER_LOGIN_URL = "http://123.56.24.172:8080/cas/login";
	private static final String CAS_CLIENT_HOSTNAME = "http://192.168.2.11:8080";
	
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistrationBean() {
		FilterRegistrationBean<AuthenticationFilter> authenticationFilter = new FilterRegistrationBean<AuthenticationFilter>();
		authenticationFilter.setFilter(new AuthenticationFilter());
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerLoginUrl", CAS_SERVER_LOGIN_URL);
		initParameters.put("serverName", CAS_CLIENT_HOSTNAME);
		authenticationFilter.setInitParameters(initParameters);
		authenticationFilter.setOrder(2);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");// 设置匹配的url
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
	
	@Bean
	public FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> ValidationFilterRegistrationBean(){
		FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> authenticationFilter = new FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter>();
		authenticationFilter.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerUrlPrefix", CAS_SERVER_PREFIX);
		initParameters.put("serverName", CAS_CLIENT_HOSTNAME);
		authenticationFilter.setInitParameters(initParameters);
		authenticationFilter.setOrder(1);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");// 设置匹配的url
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
	
	@Bean
	public FilterRegistrationBean<HttpServletRequestWrapperFilter> casHttpServletRequestWrapperFilter(){
		FilterRegistrationBean<HttpServletRequestWrapperFilter> authenticationFilter = new FilterRegistrationBean<HttpServletRequestWrapperFilter>();
		authenticationFilter.setFilter(new HttpServletRequestWrapperFilter());
		authenticationFilter.setOrder(3);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");// 设置匹配的url
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
	
	@Bean
	public FilterRegistrationBean<AssertionThreadLocalFilter> casAssertionThreadLocalFilter(){
		FilterRegistrationBean<AssertionThreadLocalFilter> authenticationFilter = new FilterRegistrationBean<AssertionThreadLocalFilter>();
		authenticationFilter.setFilter(new AssertionThreadLocalFilter());
		authenticationFilter.setOrder(4);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");// 设置匹配的url
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
}