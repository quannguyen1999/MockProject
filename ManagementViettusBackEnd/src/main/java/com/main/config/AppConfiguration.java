package com.main.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

//configuration path message and path image
@Configuration
public class AppConfiguration {
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = 
				new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(
				"classpath:/message/error_message"
				);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
