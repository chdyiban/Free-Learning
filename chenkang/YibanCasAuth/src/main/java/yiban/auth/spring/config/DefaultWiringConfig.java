package yiban.auth.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yiban.auth.service.CHDCwlAuthService;
import yiban.auth.service.UserQueryService;
import yiban.auth.service.YibanProcessingService;

@Configuration
public class DefaultWiringConfig {

	private static final String SERVICE_URL = "http://www.yiban.cn/" ;
	
	@Bean
	public UserQueryService userQueryService() {
		return new UserQueryService();
	}
	
	@Bean
	public YibanProcessingService yibanProcessingService() {
		return new YibanProcessingService();
	}
	
	@Bean
	public CHDCwlAuthService cwlAuthService() {
		return new CHDCwlAuthService(SERVICE_URL);
	}
}
