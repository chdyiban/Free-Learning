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
 * 结合Filter的Bean装配配置
 * @see https://blog.csdn.net/liuchuanhong1/article/details/73176603
 */
@Configuration
@Component
public class DefaultCASConfig {

	private static final String CAS_SERVER_PREFIX = "http://123.56.24.172:8080/cas";//"http://ids.chd.edu.cn/authserver";
	private static final String CAS_SERVER_LOGIN_URL = "http://123.56.24.172:8080/cas/login";
	private static final String CAS_CLIENT_HOSTNAME = "http://192.168.2.11:8080";
	
	/**
	 * 指定本CAS客户端 要启用CAS认证的资源的 url模式
	 */
	private static final String CAS_URL_PATTERN = "/auth/cas/*";
	
	
	/**
	 * 用于拦截用户对这台服务器（CAS客户端）上的请求，并重定向至CAS认证中心进行验证。<p>
	 * 除非该用户已登录（在这种情况下应持有TGC cookie）
	 * @return AuthenticationFilter Bean
	 */
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistrationBean() {
		FilterRegistrationBean<AuthenticationFilter> authenticationFilter = 
				new FilterRegistrationBean<AuthenticationFilter>();
		authenticationFilter.setFilter(new AuthenticationFilter());
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerLoginUrl", CAS_SERVER_LOGIN_URL);
		initParameters.put("serverName", CAS_CLIENT_HOSTNAME);
		authenticationFilter.setInitParameters(initParameters);
		authenticationFilter.setOrder(2);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add(CAS_URL_PATTERN); // 设置匹配的url
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
	
	/**
	 * 用于从CAS认证中心验证CAS票据的过滤器。同时支持服务票据 / 代理票据。
	 * @see org.jasig.cas.client.validation.Cas20ProxyTicketValidator
	 * @return Cas20ProxyReceivingTicketValidationFilter Bean
	 */
	@Bean
	public FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> ValidationFilterRegistrationBean(){
//		Cas20ProxyTicketValidator validator = new Cas20ProxyTicketValidator(CAS_CLIENT_HOSTNAME); // TODO 这句话应该没用吧？
		FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter> authenticationFilter = 
				new FilterRegistrationBean<Cas20ProxyReceivingTicketValidationFilter>();
		authenticationFilter.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerUrlPrefix", CAS_SERVER_PREFIX);
		initParameters.put("serverName", CAS_CLIENT_HOSTNAME);
		authenticationFilter.setInitParameters(initParameters);
		authenticationFilter.setOrder(1);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add(CAS_URL_PATTERN);
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
	
	/**
	 * 包装HttpServletRequest，使得能通过HttpServletRequest.getRemoteUser()获得认证通过的用户名。
	 * @return HttpServletRequestWrapperFilter Bean
	 */
	@Bean
	public FilterRegistrationBean<HttpServletRequestWrapperFilter> casHttpServletRequestWrapperFilter(){
		FilterRegistrationBean<HttpServletRequestWrapperFilter> authenticationFilter = 
				new FilterRegistrationBean<HttpServletRequestWrapperFilter>();
		authenticationFilter.setFilter(new HttpServletRequestWrapperFilter());
		authenticationFilter.setOrder(3);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add(CAS_URL_PATTERN);
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
	
	/**
	 * https://blog.csdn.net/yuwenruli/article/details/6602493
	 * 将Assertion绑定到ThreadLocal，使得不同线程之间能共享这个Assertion对象。
	 * @return AssertionThreadLocalFilter Bean
	 */
	@Bean
	public FilterRegistrationBean<AssertionThreadLocalFilter> casAssertionThreadLocalFilter(){
		FilterRegistrationBean<AssertionThreadLocalFilter> authenticationFilter = 
				new FilterRegistrationBean<AssertionThreadLocalFilter>();
		authenticationFilter.setFilter(new AssertionThreadLocalFilter());
		authenticationFilter.setOrder(4);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add(CAS_URL_PATTERN);
		authenticationFilter.setUrlPatterns(urlPatterns);
		return authenticationFilter;
	}
}