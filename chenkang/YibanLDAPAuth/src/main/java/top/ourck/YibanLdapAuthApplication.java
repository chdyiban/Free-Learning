package top.ourck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "top.ourck.web")
public class YibanLdapAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(YibanLdapAuthApplication.class, args);
	}
}
