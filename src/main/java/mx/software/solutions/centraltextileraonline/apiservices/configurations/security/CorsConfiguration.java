package mx.software.solutions.centraltextileraonline.apiservices.configurations.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

	@Value("${configuration.cors-allowed-origins}")
	private String[] allowedOrigins;

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(this.allowedOrigins)
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("X-Auth-Token", "Content-Type")
				.exposedHeaders("X-Auth-Token");
	}

}
