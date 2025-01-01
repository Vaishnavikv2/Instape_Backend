
package com.instape.app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@ComponentScan("com.instape.app")
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class InstaPeSupportPortalBackendApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(InstaPeSupportPortalBackendApplication.class, args);
	}

	@GetMapping("/isServiceRunning")
	public String serviceStatus() {
		return "Service is running";
	}	
	
	@Bean
	public ModelMapper createModelMapper() {
		ModelMapper modelMap = new ModelMapper();
		modelMap.getConfiguration().setAmbiguityIgnored(true);
		return modelMap;
	}
	
	@Bean
	public RestTemplate getTemplate() {
		return new RestTemplate();
	}
	
}
