package ourck.spring.index;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SolrAssistApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SolrAssistApplication.class, args);
	}
	
	@Bean
	public String doNothing() {
		System.out.println("SolrAssist is on your serve!");
		return "SolrAssist is on your serve!";
	}
	
}
