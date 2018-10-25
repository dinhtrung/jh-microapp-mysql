package com.ft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.ft.components.HeaderRequestInterceptor;

/**
 * Properties specific to App.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@Configuration
public class RestTemplateConfiguration {

	/**
	 * Rest Template to send to Kannel
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate result = new RestTemplate();
		result.getInterceptors().add(new HeaderRequestInterceptor(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE));
		return result;
	}
}
