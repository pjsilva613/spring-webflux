package com.project.reactor.app.configuration;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebPropertiesConfig {

	@Bean
	public WebProperties.Resources resources() {
		return new WebProperties.Resources();
	}
}
